pipeline {
    agent any

    environment {
        DB_URL = credentials('DB_URL')

    }
        
    tools {
        maven "MAVEN_HOME"
    }

    stages {
        stage('Initialize') {
            steps {
                echo "PATH = ${M2_HOME}/bin:${PATH}"
                echo "M2_HOME = /opt/maven"
            }
        }

        stage('Run Unit Test Cases') {
            steps {
                script {
                    withEnv([
                        "DB_URL=${DB_URL}",
                        "DB_USERNAME=${DB_USERNAME}",
                        "DB_PASSWORD=${DB_PASSWORD}"
                    ]){
                        def workspacePath = env.WORKSPACE
                        dir(workspacePath) {
                            sh 'mvn clean install'
                            sh 'mvn test'
                        }
                    }
                }
            }
            post {
                always {
                    junit(
                        allowEmptyResults: true,
                        testResults: '*/test-reports/*.xml'
                    )
                }
            }
        }

        stage('Generate Code Coverage Report') {
            steps {
                script {
                    // Generate code-coverage report using Jacoco
                    sh 'mvn jacoco:report'
                }
            }
            post {
                always {
                    // Upload code-coverage report as an artifact
                    archiveArtifacts artifacts: 'target/site/jacoco/index.html', onlyIfSuccessful: true
                }
            }
        }

        stage('Build and SonarQube Scan') {
            steps {
                script {
                    sh "${MAVEN_HOME}/bin/mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=${SONAR_PROJECT_KEY} -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=${SONAR_TOKEN}"
                }
            }
        }

        stage('Build project and package jar') {
            steps {
                script {
                    // This command builds the Maven project and packages it into a JAR
                    sh 'mvn package'

                    // Optionally, you can include additional Maven goals or parameters as needed
                    // For example: sh 'mvn clean install'
                }
            }

            post {
                success {
                    // This block is executed if the build is successful
                    echo 'Build successful!'
                    // You can add additional steps or notifications here
                }

                failure {
                    // This block is executed if the build fails
                    echo 'Build failed!'
                    // You can add additional steps or notifications here
                }
            }
        }
        
        stage('AWS Configuration') {
            steps {
                // Optional: Explicitly run aws configure with environment variables
                sh "aws configure set aws_access_key_id ${AWS_ACCESS_KEY_ID}"
                sh "aws configure set aws_secret_access_key ${AWS_SECRET_ACCESS_KEY}"
                sh "aws configure set default.region ${AWS_REGION}"
            }
        }

        stage('Build Image') {
            steps {
                script {
                    // Retrieve the commit SHA from the Jenkins environment
                    def commitSha = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
        
                    // Build the image with the commit SHA as the tag
                    sh "docker build -t humanresource:${commitSha} ."  // Replace with your Dockerfile location
                }
            }
        }

        stage('ECR Login') {
            steps {
                script {
                    // Use the retrieved URL for ECR login
                    sh "aws ecr get-login-password | docker login --username AWS --password-stdin ${ecrRegistryUrl}"
                }
            }
        }

        stage('Push Image to ECR') {
            steps {
                script {
                    // Retrieve the commit SHA from the Jenkins environment
                    def commitSha = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
        
                    // Tag the image with the ECR repository name
                    sh "docker tag humanresource:${commitSha} ${ecrRegistryUrl}:${commitSha}"  // Replace with your ECR repository name
        
                    // Push the image to ECR
                    sh "docker push ${ecrRegistryUrl}:${commitSha}"
                }
            }
        }
    }
    

}


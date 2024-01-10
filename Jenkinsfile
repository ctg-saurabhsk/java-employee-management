pipeline {
    agent any

    environment {
        DB_URL = credentials('DB_URL')
        GCP_SERVICE_ACCOUNT_KEY = credentials('gcp-service-account-key')
        GCR_PROJECT_ID = 'sylvan-fusion-410508'
        GCR_REGISTRY = 'gcr.io'
        GCR_REPO_NAME = credentials('gcr-repo-name')

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
                        "DB_URL=${DB_URL}"
                    
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

        // stage('Build and SonarQube Scan') {
        //     steps {
        //         script {
        //             sh "${MAVEN_HOME}/bin/mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=${SONAR_PROJECT_KEY} -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=${SONAR_TOKEN}"
        //         }
        //     }
        // }

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

           
  
        
     

}
    
 }


 


      




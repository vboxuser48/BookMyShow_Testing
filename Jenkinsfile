pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo "Code started in docker"
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                script {
                    // Convert Windows workspace path (C:\...) -> Linux (/c/...) for Docker
                    def hostPath = env.WORKSPACE
                    if (!isUnix()) {
                        hostPath = hostPath.replaceAll('\\\\','/')
                        if (hostPath =~ /^[A-Za-z]:/) {
                            def drive = hostPath[0].toLowerCase()
                            hostPath = "/${drive}${hostPath.substring(2)}"
                        }
                    }

                    echo "Using hostPath for docker: ${hostPath}"

                    // Run Maven inside container
                    bat """
                      docker run --rm -v ${hostPath}:/workspace -w /workspace maven:3.9.6-eclipse-temurin-17 mvn -B clean test
                    """
                }
            }
        }

        stage('Report') {
            steps {
                junit 'target/surefire-reports/*.xml'
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Report'
                ])
            }
        }
    }
}

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
                    // Convert Windows path (C:\...) -> Linux path (/c/...) for Docker mount
                    def hostPath = env.WORKSPACE
                    if (!isUnix()) {
                        hostPath = hostPath.replaceAll('\\\\','/')
                        if (hostPath =~ /^[A-Za-z]:/) {
                            def drive = hostPath[0].toLowerCase()
                            hostPath = "/${drive}${hostPath.substring(2)}"
                        }
                    }
                    echo "Using hostPath for docker: ${hostPath}"

                    // Run Maven tests inside Maven+Chrome container
                    bat """
                      docker run --rm -v ${hostPath}:/workspace -w /workspace markhobson/maven-chrome mvn -B clean test
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

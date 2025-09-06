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
                    // Prepare host path for Docker mounts:
                    // - On Unix agents use env.WORKSPACE directly
                    // - On Windows agents convert "C:\path\to\ws" -> "/c/path/to/ws" so Docker Desktop (Linux containers) accepts it
                    def hostPath = env.WORKSPACE
                    if (!isUnix()) {
                        hostPath = hostPath.replaceAll('\\\\','/')
                        if (hostPath =~ /^[A-Za-z]:/) {
                            def drive = hostPath[0].toLowerCase()
                            hostPath = "/${drive}${hostPath.substring(2)}"
                        }
                    }

                    // Run Maven inside container with Linux-style workspace mapping
                    docker.image('maven:3.9.6-eclipse-temurin-17')
                          .inside("-v ${hostPath}:/workspace -w /workspace") {
                        sh 'mvn clean test'
                        echo "Code executed in docker"
                    }
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

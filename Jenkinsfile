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
                      docker run --rm -u 0:0 -v ${hostPath}:/workspace -w /workspace markhobson/maven-chrome ^
                      sh -c "mkdir -p /workspace/test-output && chmod -R 777 /workspace/test-output && \
                      rm -rf /home/seluser/.config/google-chrome /tmp/.com.google.Chrome* /tmp/.org.chromium* || true && \
                      mvn -B -DforkCount=1 -DreuseForks=false -Dparallel=none clean test"
                    """
                }
            }
        }

        stage('Report') {
            steps {
                // JUnit test results
                junit 'target/surefire-reports/*.xml'

                // Publish Extent Report (from target/ and test-output/)
                publishHTML([
                    allowMissing: true,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Report (target)'
                ])

                publishHTML([
                    allowMissing: true,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'test-output',
                    reportFiles: 'Report.html',
                    reportName: 'Extent Report (test-output)'
                ])

                // Archive artifacts for download
                archiveArtifacts artifacts: 'target/**/*.html, test-output/**/*.html', fingerprint: true
            }
        }
    }
}

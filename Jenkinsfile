pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo "Checkout from SCM"
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean test'
            }
        }

        stage('Report') {
            steps {
                // JUnit XML results
                junit 'target/surefire-reports/*.xml'

                // Publish Extent reports
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'test-output',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Report'
                ])
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'test-output',
                    reportFiles: 'Report.html',
                    reportName: 'TestNG Report'
                ])
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'test-output/*.html, target/surefire-reports/*.xml', allowEmptyArchive: true
        }
    }
}

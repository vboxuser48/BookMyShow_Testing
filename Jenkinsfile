pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps {
        echo "Checkout from SCM"
        checkout scm
      }
    }

    stage('Build & Test (native)') {
      steps {
        script {
          // Run mvn directly on the Jenkins agent (Windows)
          // Use -B for batch mode, keep test flags as before to avoid parallel forks if desired
          bat 'mvn -B -DforkCount=1 -DreuseForks=false -Dparallel=none clean test'
        }
      }
    }

    stage('Report') {
      steps {
        junit 'target/surefire-reports/*.xml'

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

        archiveArtifacts artifacts: 'target/**/*.html, test-output/**/*.html', fingerprint: true
      }
    }
  }
}

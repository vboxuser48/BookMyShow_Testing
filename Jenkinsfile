pipeline {
  agent any

  environment {
    // make sure this matches your project structure if different
    REPORT_DIR = "test-output"    // where your Extent/Report listener writes files
    MVN_CMD = "mvn -B -DforkCount=1 -DreuseForks=false -Dparallel=none clean test"
  }

  stages {
    stage('Checkout') {
      steps {
        echo "Checkout from SCM"
        checkout scm
      }
    }

    stage('Prep report dir & perms') {
      steps {
        echo "Ensure report dir exists and grant write permissions"
        // create directory if it doesn't exist and try to make it writable
        bat """
          if not exist "%WORKSPACE%\\${env.REPORT_DIR}" (
            mkdir "%WORKSPACE%\\${env.REPORT_DIR}"
          )
          REM Try granting SYSTEM and Everyone full control (harmless if already set)
          icacls "%WORKSPACE%\\${env.REPORT_DIR}" /grant "NT AUTHORITY\\SYSTEM":(OI)(CI)F /T || echo "icacls SYSTEM grant failed"
          icacls "%WORKSPACE%\\${env.REPORT_DIR}" /grant Everyone:(OI)(CI)F /T || echo "icacls Everyone grant failed"
        """
      }
    }

    stage('Build & Test (native)') {
      steps {
        script {
          // run maven on Windows agent (native)
          bat "${env.MVN_CMD}"
        }
      }
    }

    stage('Publish Test Results & HTML') {
      steps {
        script {
          // JUnit XML (surefire) publish - adjust path if needed
          junit 'target/surefire-reports/*.xml'

          // publish HTML report produced by your Extent/ReportListener
          // adjust reportDir/reportFiles to match what the test writes (ExtentReport.html / Report.html)
          publishHTML([
            allowMissing: false,
            alwaysLinkToLastBuild: true,
            keepAll: true,
            reportDir: "${env.REPORT_DIR}",
            reportFiles: 'ExtentReport.html',
            reportName: 'Extent Report (ExtentReport.html)'
          ])
          // if your listener writes Report.html too, publish it as a second report:
          publishHTML([
            allowMissing: true,
            alwaysLinkToLastBuild: true,
            keepAll: true,
            reportDir: "${env.REPORT_DIR}",
            reportFiles: 'Report.html',
            reportName: 'Report (Report.html)'
          ])
        }
      }
    }
  }

  post {
    always {
      echo "Archiving reports and cleaning up workspace"
      archiveArtifacts artifacts: "${env.REPORT_DIR}/*.html, target/surefire-reports/*.xml", allowEmptyArchive: true
    }
  }
}

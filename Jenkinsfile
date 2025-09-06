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
          // prepare host path: convert Windows "C:\..." -> "/c/..." for Docker Desktop Linux backend
          def hostPath = env.WORKSPACE
          if (!isUnix()) {
            hostPath = hostPath.replaceAll('\\\\','/')
            if (hostPath =~ /^[A-Za-z]:/) {
              def drive = hostPath[0].toLowerCase()
              hostPath = "/${drive}${hostPath.substring(2)}"
            }
          }

          // mount workspace into container but DON'T pass -w (avoid plugin adding conflicting -w)
          def dockerArgs = "-v ${hostPath}:/workspace"

          docker.image('maven:3.9.6-eclipse-temurin-17').inside(dockerArgs) {
            // change into mounted workspace inside the container, then run maven
            sh 'cd /workspace && mvn clean test'
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

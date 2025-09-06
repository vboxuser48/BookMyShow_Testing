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
          // convert Windows "C:\path\to\ws" -> "/c/path/to/ws" for Docker Desktop
          def hostPath = env.WORKSPACE
          if (!isUnix()) {
            hostPath = hostPath.replaceAll('\\\\','/')
            if (hostPath =~ /^[A-Za-z]:/) {
              def drive = hostPath[0].toLowerCase()
              hostPath = "/${drive}${hostPath.substring(2)}"
            }
          }

          echo "Converted hostPath for docker: ${hostPath}"

          // Temporarily override WORKSPACE so the docker plugin will inject a Linux-style -w
          withEnv(["WORKSPACE=${hostPath}"]) {
            // mount workspace into container (plugin will also add -w using WORKSPACE)
            def dockerArgs = "-v ${hostPath}:/workspace"
            docker.image('maven:3.9.6-eclipse-temurin-17').inside(dockerArgs) {
              // run inside container; use /workspace which is the mounted dir
              sh 'cd /workspace && mvn clean test'
              echo "Code executed in docker"
            }
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

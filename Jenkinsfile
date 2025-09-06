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

                    // Run Maven inside container with Linux-style workspace mapping

                    docker.image('maven:3.9.6-eclipse-temurin-17').inside('-v %cd%:/workspace -w /workspace') {

                        sh 'mvn clean test'

                        echo "Code executed in docker"

                    }

                }

            }

        }

        stage('Report') {

            steps {

                junit '/target/surefire-reports/*.xml'

                publishHTML([allowMissing: false,

                    alwaysLinkToLastBuild: true,

                    keepAll: true,

                    reportDir: 'target',

                    reportFiles: 'ExtentReport.html',

                    reportName: 'Extent Report'])

            }

        }

    }

}
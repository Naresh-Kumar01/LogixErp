pipeline {
    agent any

    parameters {
        choice(name: 'SUITE', choices: ['testng.xml', 'master.xml', 'signin_test_suite.xml'], description: 'TestNG suite')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Browser')
        choice(name: 'ENV', choices: ['qa', 'uat', 'dev'], description: 'Target environment')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Headless execution')
    }

    environment {
        MAVEN_OPTS = '-Xmx2048m'
        ALLURE_RESULTS = 'target/allure-results'
    }

    options {
        timestamps()
        timeout(time: 120, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '15'))
    }

    triggers {
        cron('H 2 * * 1-5')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                dir('LogixErp-main') {
                    sh 'mvn -B -DskipTests clean compile test-compile'
                }
            }
        }

        stage('UI + API Tests') {
            steps {
                dir('LogixErp-main') {
                    sh """
                        mvn -B test \\
                          -DsuiteFile=${params.SUITE} \\
                          -Denv=${params.ENV} \\
                          -Dbrowser=${params.BROWSER} \\
                          -Dheadless=${params.HEADLESS} \\
                          -Dparallel=tests \\
                          -DthreadCount=3
                    """
                }
            }
        }

        stage('Allure Report') {
            steps {
                dir('LogixErp-main') {
                    sh 'mvn -B allure:report || true'
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'LogixErp-main/reports/**,LogixErp-main/screenshots/**,LogixErp-main/target/allure-results/**', allowEmptyArchive: true
                    junit allowEmptyResults: true, testResults: 'LogixErp-main/target/surefire-reports/*.xml'
                }
            }
        }
    }

    post {
        failure {
            echo 'Pipeline failed - check Extent and Allure artifacts.'
        }
    }
}

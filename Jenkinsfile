pipeline {
    agent any
    
    // Trigger pipeline on GitHub webhook events
    triggers {
        githubPush()
    }
    
    // Define tools configured in Jenkins Global Tool Configuration
    tools {
        maven 'Maven'
        jdk 'JDK-11'
    }
    
    // Build Parameters
    parameters {
        string(name: 'BROWSER', defaultValue: 'chrome', description: 'Browser to run tests on (chrome/firefox/edge)')
        string(name: 'TEST_SUITE', defaultValue: 'master.xml', description: 'TestNG suite XML file to execute')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser in headless mode')
    }
    
    // Environment variables
    environment {
        // Ensure tools are in PATH
        PATH = "${MAVEN_HOME}/bin:${JAVA_HOME}/bin:${PATH}"
        
        // Browser configuration
        CHROME_BIN = '/usr/bin/google-chrome'
        DISPLAY = ':99'
    }
    
    // Pipeline options
    options {
        buildDiscarder(logRotator(numToKeepStr: '10')) // Keep last 10 builds
        timeout(time: 60, unit: 'MINUTES')             // Timeout after 1 hour
        timestamps()                                   // Add timestamps to logs
        disableConcurrentBuilds()                      // Prevent concurrent builds
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '🔄 Checking out source code from GitHub...'
                echo '🚀 Project: Logixgrid'
                checkout scm
                
                // Display branch and commit info for debugging
                script {
                    try {
                        def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
                        def gitBranch = sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim()
                        echo "📋 Branch: ${gitBranch}"
                        echo "📋 Commit: ${gitCommit}"
                    } catch (Exception e) {
                        echo "Could not retrieve git info: ${e.message}"
                    }
                }
            }
        }
        
        stage('Environment Setup') {
            steps {
                echo '⚙️ Setting up environment...'
                sh '''
                    echo "Java Version:"
                    java -version
                    echo "Maven Version:"
                    mvn -version
                '''
                echo "📋 Configuration: Browser=${params.BROWSER}, Suite=${params.TEST_SUITE}, Headless=${params.HEADLESS}"
            }
        }
        
        stage('Install Dependencies & Build') {
            steps {
                echo '📦 Installing dependencies and compiling...'
                // Clean, compile, and install dependencies (skip tests here)
                sh 'mvn clean compile test-compile -DskipTests'
                
                // Create required directories for reports if they don't exist
                sh 'mkdir -p reports screenshots logs test-output'
            }
        }
        
        stage('Run Tests') {
            steps {
                echo "🧪 Executing automated tests using ${params.TEST_SUITE}..."
                
                // Construct Maven command with parameters
                script {
                    def mavenCmd = "mvn test -Dsurefire.suiteXmlFiles=${params.TEST_SUITE} -Dbrowser=${params.BROWSER}"
                    if (params.HEADLESS) {
                        mavenCmd += " -Dheadless=true"
                    }
                    
                    try {
                        sh mavenCmd
                    } catch (Exception e) {
                        currentBuild.result = 'UNSTABLE'
                        echo "Tests failed: ${e.message}"
                        // We don't throw exception here to ensure post-actions run (report archiving)
                        // But Maven usually exits with non-zero, so 'sh' will fail. 
                        // To ensure we mark it failure but continue to post, we rely on pipeline behavior.
                        // Actually, 'sh' failure will abort the stage and go to 'post'.
                        error "Tests failed"
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo '📊 Archiving test reports...'
            
            // publish JUnit test results
            junit 'target/surefire-reports/*.xml'
            
            // Archive other reports (ExtentReports, screenshots, logs)
            archiveArtifacts artifacts: 'target/surefire-reports/**, reports/**, screenshots/**, logs/**, test-output/**', allowEmptyArchive: true
        }
        
        success {
            echo '✅ Build and Tests Passed Successfully!'
            // notifySlack('SUCCESS') // Uncomment if Slack plugin is configured
        }
        
        failure {
            echo '❌ Build or Tests Failed!'
            // notifySlack('FAILURE') // Uncomment if Slack plugin is configured
            
            // Email notification (requires Email Extension Plugin)
            /*
            emailext subject: "Build Failed: ${currentBuild.fullDisplayName}",
                body: "Build failed. Check console output at ${env.BUILD_URL}",
                to: 'team@example.com'
            */
        }
        
        unstable {
            echo '⚠️ Build is Unstable (Some tests failed)!'
        }
    }
}

// Helper function for Slack notifications (Optional)
def notifySlack(String buildStatus) {
    // slackSend (color: buildStatus == 'SUCCESS' ? '#36a64f' : '#FF0000', message: "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
}

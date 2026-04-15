# Jenkins CI/CD Setup Guide for TestNG Selenium Framework

## 🚀 Quick Start (Local Setup)

1.  **Start Jenkins**:
    Run `d:\Logixerp\start_jenkins.bat`.
    
2.  **Unlock Jenkins**:
    - Open [http://localhost:8080](http://localhost:8080)
    - Enter the Initial Admin Password: `c5d8e57fa2fb4ecf9e63f3330fd5414d`
      (You can also find this password in `C:\Users\My\.jenkins\secrets\initialAdminPassword`)

3.  **Install Plugins**:
    - Choose "Install suggested plugins".
    - Wait for installation to complete.

4.  **Create Admin User**:
    - Create your admin account or continue as admin.

5.  **Create the Job**:
    - Click **New Item**.
    - Enter name: `Logixerp-Pipeline`.
    - Select **Pipeline** and click **OK**.
    - Scroll to **Pipeline** section.
    - Definition: **Pipeline script from SCM**.
    - SCM: **Git**.
    - Repository URL: `D:\Logixerp` (for local testing) or your GitHub URL.
    - Script Path: `Jenkinsfile`.
    - Click **Save**.
    - Click **Build Now** to run the pipeline.

---

## 🚀 Jenkins Pipeline Features

### ✅ **What This Pipeline Does:**
- **Automatic Triggers**: Runs on GitHub push/PR events to the main branch
- **Environment Setup**: Configures Java, Maven, and browser environment
- **Configurable Execution**: Supports running different test suites and browsers via build parameters
- **Test Reports**: Generates JUnit, HTML, and ExtentReports
- **Artifact Archiving**: Saves test reports, screenshots, and logs
- **Notifications**: Supports Slack and email notifications (optional)
- **Failure Handling**: Marks build as failed if tests fail

## 🛠️ Jenkins Setup Requirements

### 1. **Jenkins Plugins Required:**
```
- Git Plugin
- GitHub Plugin
- Maven Integration Plugin
- JUnit Plugin
- Email Extension Plugin (for email notifications)
- Slack Notification Plugin (optional)
- Pipeline Plugin
- Build Timeout Plugin
- Timestamper Plugin
```

### 2. **Global Tool Configuration:**
In Jenkins → Manage Jenkins → Global Tool Configuration:

**Maven:**
- Name: `Maven`
- Version: `3.8.x` or later
- Install automatically: ✅

**JDK:**
- Name: `JDK-11`
- Version: `OpenJDK 11` or `Oracle JDK 11`
- Install automatically: ✅

### 3. **System Configuration:**
In Jenkins → Manage Jenkins → Configure System:

**GitHub:**
- Add GitHub Server
- API URL: `https://api.github.com`
- Credentials: Add GitHub personal access token

**Environment Variables:**
```
CHROME_BIN=/usr/bin/google-chrome
DISPLAY=:99
```

## 📋 Jenkins Job Setup

### 1. **Create New Pipeline Job:**
1. New Item → Pipeline
2. Name: `TestNG-Selenium-Pipeline`
3. Configure as below:

### 2. **General Configuration:**
- ✅ GitHub project: `https://github.com/Naresh-Kumar01/LogixErp`
- ✅ Build Triggers → GitHub hook trigger for GITScm polling

### 3. **Pipeline Configuration:**
- Definition: `Pipeline script from SCM`
- SCM: `Git`
- Repository URL: `https://github.com/Naresh-Kumar01/LogixErp.git`
- Branch: `*/main`
- Script Path: `Jenkinsfile`

### 4. **GitHub Webhook Setup:**
1. Go to your GitHub repository
2. Settings → Webhooks → Add webhook
3. Payload URL: `http://your-jenkins-url/github-webhook/`
4. Content type: `application/json`
5. Events: `Push` and `Pull requests`

## 🔧 Pipeline Customization

### **Build Parameters:**
The pipeline supports the following parameters (defaults can be changed in `Jenkinsfile`):
- `BROWSER`: Browser to run tests on (default: `chrome`)
- `TEST_SUITE`: TestNG suite XML file to execute (default: `master.xml`)
- `HEADLESS`: Run browser in headless mode (default: `true`)

### **Test Report Types:**
1. **JUnit XML**: For Jenkins test result trends
2. **Maven Surefire Reports**: Standard Maven reports
3. **ExtentReports**: Rich HTML reports with screenshots (if configured in framework)

## 📊 Notifications Setup

### **Slack Integration (Optional):**
1. Install Slack Notification Plugin
2. Configure Slack in Jenkins
3. Uncomment `notifySlack` calls in `Jenkinsfile`

### **Email Notifications (Optional):**
1. Configure SMTP in Jenkins (Manage Jenkins → Configure System → E-mail Notification)
2. Uncomment `emailext` block in `Jenkinsfile`

## 🐳 Docker-based Jenkins (Alternative)

If using Docker Jenkins, create this docker-compose.yml:

```yaml
version: '3.8'
services:
  jenkins:
    image: jenkins/jenkins:lts
    ports:
      - "8080:8080"
      - "50000:50000"
    volumes:
      - jenkins_home:/var/jenkins_home
      - /var/run/docker.sock:/var/run/docker.sock
    environment:
      - JAVA_OPTS=-Djenkins.install.runSetupWizard=false

volumes:
  jenkins_home:
```

## 🔍 Troubleshooting

### **Common Issues:**

1. **Chrome/ChromeDriver Issues:**
   - Install Chrome: `apt-get install google-chrome-stable`
   - Ensure `CHROME_BIN` path is correct

2. **Display Issues (Headless):**
   - Xvfb is configured in pipeline (commented out by default, enable if needed)
   - Set DISPLAY=:99 environment variable

3. **Maven Issues:**
   - Ensure Maven is properly configured in Global Tools with name `Maven`

4. **Test Failures:**
   - Check `target/surefire-reports` and `screenshots` folders
   - Review Console Output for stack traces

## 📈 Pipeline Stages Explained

1. **Checkout**: Gets latest code from GitHub
2. **Environment Setup**: Configures Java, Maven, browsers
3. **Install Dependencies & Build**: Runs `mvn clean compile`
4. **Run Tests**: Executes TestNG suite (`master.xml` by default)
5. **Post Actions**: Archives artifacts (reports, screenshots) and sends notifications

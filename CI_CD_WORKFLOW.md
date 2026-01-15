# 📘 Logixerp CI/CD & Workflow Guide

This guide covers the project configuration, local testing, Git workflow, and Jenkins CI/CD integration for **Logixerp**.

---

## 1. ⚙️ Project Configuration (`pom.xml`)

The `pom.xml` file manages dependencies and build configuration.

### **Key Dependencies**
Used to download required libraries:
- **Selenium**: Web browser automation.
- **TestNG**: Testing framework.
- **ExtentReports**: HTML reporting.

### **Key Plugins**
- **Maven Compiler Plugin**: Compiles Java code (Target: Java 1.8).
- **Maven Surefire Plugin**: Executes tests defined in `master.xml`.

---

## 2. 🛠️ Local Setup (Windows)

### **Install Maven**
1. Download Maven from [maven.apache.org](https://maven.apache.org/download.cgi).
2. Extract to a folder (e.g., `C:\Program Files\apache-maven-3.9.6`).
3. Add `bin` folder to **System Variables** → `PATH`.
4. Verify in CMD:
   ```cmd
   mvn -version
   ```

### **Running Tests Locally**
You can run tests easily using the provided batch script:

1. **Double-click** `run_tests.bat` in the project root.
   - *Note: This script automatically tries to find Maven if it's installed via Jenkins.*

Or run manually via Command Prompt:
```cmd
cd d:\Logixerp
mvn clean test -Dsurefire.suiteXmlFiles=master.xml
```

---

## 3. 🐙 Git & GitHub Workflow

### **One-Time Setup**
```bash
# Initialize Repo
git init

# Configure User
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# Link Remote
git remote add origin https://github.com/Naresh-Kumar01/LogixErp.git
```

### **Daily Workflow (Round 2)**
When you have made changes:

1. **Check Status**:
   ```bash
   git status
   ```

2. **Stage Changes**:
   ```bash
   git add .
   ```

3. **Commit**:
   ```bash
   git commit -m "Description of changes"
   ```

4. **Push to GitHub**:
   ```bash
   git push origin main
   ```
   *(This will automatically trigger the Jenkins Pipeline)*

---

## 4. 🚀 Jenkins CI/CD Pipeline

The Jenkins pipeline is configured in `Jenkinsfile` and handles the automation.

### **Pipeline Stages**
1. **Checkout**: Pulls code from GitHub.
2. **Environment Setup**: Configures Java/Maven.
3. **Build**: Compiles code (`mvn clean compile`).
4. **Test**: Runs `master.xml` suite.
5. **Post Actions**: Generates JUnit/Extent reports and archives artifacts.

### **Accessing Jenkins**
- **URL**: [http://localhost:8080](http://localhost:8080)
- **Start Server**: Run `start_jenkins.bat`
- **Job Name**: `logixgrid`

### **Triggering Builds**
- **Automatic**: Push to `main` branch.
- **Manual**: Go to Job -> **Build Now**.

---

## 5. 📄 Cheat Sheet

| Task | Command |
|------|---------|
| **Check Maven** | `mvn -version` |
| **Run Tests** | `mvn clean test` |
| **Run Specific Suite** | `mvn clean test -Dsurefire.suiteXmlFiles=testng.xml` |
| **Git Status** | `git status` |
| **Git Add All** | `git add .` |
| **Git Commit** | `git commit -m "msg"` |
| **Git Push** | `git push origin main` |
| **Start Jenkins** | `.\start_jenkins.bat` |


How to Run Jenkins
==================

1. Run `install_jenkins.bat`
   - This script will check for JDK 17 and Jenkins WAR.
   - If missing, it will download and set them up in the `tools` directory.
   - Note: If you see "file used by another process" errors, please restart your IDE/Terminal and try again, or manually ensure `tools\jdk17.zip` is not locked.

2. Run `start_jenkins.bat`
   - This script will start Jenkins using the downloaded JDK 17.
   - It will run on port 8080.
   - Access Jenkins at: http://localhost:8080

3. Initial Setup
   - When Jenkins starts, it will print an "Initial Admin Password" in the console.
   - Copy this password and paste it into the Jenkins web setup wizard.
   - Install suggested plugins.

Files created:
- install_jenkins.bat: Setup script.
- start_jenkins.bat: Startup script.
- tools/: Directory where JDK and Jenkins are installed.

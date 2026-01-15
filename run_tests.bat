@echo off
echo ==========================================
echo 🚀 Running Logixerp Tests...
echo ==========================================

REM Check if Maven is in PATH
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo ❌ Maven is not found in your PATH.
    echo Please install Maven as per the documentation or add it to your PATH.
    echo.
    echo Attempting to use Jenkins-installed Maven if available...
    
    REM Try to find Maven in Jenkins tools (best guess path)
    if exist "C:\Users\My\.jenkins\tools\hudson.tasks.Maven_MavenInstallation\Maven\bin\mvn.cmd" (
        set "PATH=C:\Users\My\.jenkins\tools\hudson.tasks.Maven_MavenInstallation\Maven\bin;%PATH%"
        echo ✅ Found Jenkins Maven. Using it.
    ) else (
        echo ❌ Could not find Jenkins Maven either.
        echo Please install Maven manually.
        pause
        exit /b 1
    )
)

echo 🧹 Cleaning and Testing...
call mvn clean test -Dsurefire.suiteXmlFiles=master.xml

if %errorlevel% neq 0 (
    echo ❌ Tests Failed!
) else (
    echo ✅ Tests Passed!
)

pause

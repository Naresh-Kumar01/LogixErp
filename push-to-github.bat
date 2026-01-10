@echo off
echo ========================================
echo    PUSHING PROJECT TO GITHUB
echo ========================================

echo Step 1: Adding all files to git...
git add .

echo Step 2: Committing changes...
git commit -m "TestNG Automation Framework - Fixed classpath issues and cleaned project"

echo Step 3: Pushing to GitHub...
git push origin main

echo.
echo ========================================
echo    PUSH COMPLETE!
echo ========================================
echo Your project has been pushed to GitHub.
echo.
pause
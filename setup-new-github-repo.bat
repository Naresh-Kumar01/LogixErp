@echo off
echo ========================================
echo    SETUP NEW GITHUB REPOSITORY
echo ========================================

echo Step 1: Initialize git repository...
git init

echo Step 2: Add all files...
git add .

echo Step 3: Initial commit...
git commit -m "Initial commit - TestNG Automation Framework"

echo Step 4: Add remote repository...
echo IMPORTANT: Replace YOUR_USERNAME and YOUR_REPO_NAME with actual values
echo Example: git remote add origin https://github.com/yourusername/your-repo-name.git
echo.
set /p remote_url="Enter your GitHub repository URL: "
git remote add origin %remote_url%

echo Step 5: Push to GitHub...
git branch -M main
git push -u origin main

echo.
echo ========================================
echo    SETUP COMPLETE!
echo ========================================
echo Your project has been pushed to GitHub.
echo.
pause
# Script to download Maven if needed and run tests
$ProgressPreference = 'SilentlyContinue'

Write-Host "=========================================="
Write-Host "Running Logixerp Tests with Video Recording"
Write-Host "=========================================="

# Check if Maven is available
$mavenFound = $false
try {
    $null = Get-Command mvn -ErrorAction Stop
    $mavenFound = $true
    Write-Host "Maven found in PATH."
} catch {
    Write-Host "Maven not found in PATH."
}

# If Maven not found, try to use local installation
if (-not $mavenFound) {
    $mavenDir = Get-ChildItem "tools" -Filter "apache-maven-*" -Directory -ErrorAction SilentlyContinue | Select-Object -First 1
    
    if ($mavenDir) {
        $mavenPath = Join-Path $mavenDir.FullName "bin\mvn.cmd"
        if (Test-Path $mavenPath) {
            $env:Path = "$($mavenDir.FullName)\bin;$env:Path"
            $mavenFound = $true
            Write-Host "Found Maven in tools directory: $($mavenDir.Name)"
        }
    }
}

# If still not found, download it
if (-not $mavenFound) {
    Write-Host "Maven not found. Downloading and setting up..."
    & ".\download_maven.ps1"
    
    # Try again after download
    $mavenDir = Get-ChildItem "tools" -Filter "apache-maven-*" -Directory -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($mavenDir) {
        $env:Path = "$($mavenDir.FullName)\bin;$env:Path"
        $mavenFound = $true
    }
}

# Verify Maven is available
if (-not $mavenFound) {
    Write-Host "Could not set up Maven. Please install it manually."
    Write-Host "Download from: https://maven.apache.org/download.cgi"
    exit 1
}

# Set JAVA_HOME to use JDK from tools directory if available
$jdkBaseDir = Get-ChildItem "tools" -Filter "jdk*" -Directory -ErrorAction SilentlyContinue | Select-Object -First 1
if ($jdkBaseDir) {
    # Check if there's a nested jdk directory (like jdk17\jdk-17.0.17+10\)
    $jdkDir = Get-ChildItem $jdkBaseDir.FullName -Filter "jdk-*" -Directory -ErrorAction SilentlyContinue | Select-Object -First 1
    if (-not $jdkDir) {
        $jdkDir = $jdkBaseDir
    }
    
    if (Test-Path "$($jdkDir.FullName)\bin\javac.exe") {
        $env:JAVA_HOME = $jdkDir.FullName
        $env:Path = "$($jdkDir.FullName)\bin;$env:Path"
        Write-Host "Using JDK from: $($jdkDir.FullName)"
        Write-Host "Java version:"
        & java -version 2>&1
    } else {
        Write-Host "Warning: JDK compiler not found. Using system Java."
    }
} else {
    Write-Host "Warning: JDK not found in tools directory. Using system Java."
}

# Verify Maven works
Write-Host ""
Write-Host "Verifying Maven installation..."
try {
    & mvn -version
    Write-Host ""
} catch {
    Write-Host "Maven verification failed."
    exit 1
}

# Run tests
Write-Host "=========================================="
Write-Host "Cleaning and Running Tests..."
Write-Host "=========================================="
Write-Host ""

& mvn clean test "-Dsurefire.suiteXmlFiles=master.xml"

$exitCode = $LASTEXITCODE

Write-Host ""
Write-Host "=========================================="
if ($exitCode -eq 0) {
    Write-Host "Tests Completed Successfully!"
} else {
    Write-Host "Tests Failed with exit code: $exitCode"
}
Write-Host "=========================================="
Write-Host ""
Write-Host "Video recordings are saved in: videos\"
Write-Host "Test reports are in: test-output\"
Write-Host ""

exit $exitCode

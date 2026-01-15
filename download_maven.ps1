$ProgressPreference = 'SilentlyContinue'
Write-Host "=========================================="
Write-Host "Downloading and Setting Up Maven..."
Write-Host "=========================================="

# Create tools directory if it doesn't exist
if (-not (Test-Path "tools")) {
    New-Item -ItemType Directory -Path "tools" | Out-Null
}

# Maven version and download URL
$mavenVersion = "3.9.6"
$mavenUrl = "https://archive.apache.org/dist/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
$mavenZip = "tools\apache-maven-$mavenVersion-bin.zip"
$mavenDir = "tools\apache-maven-$mavenVersion"

# Check if Maven already exists
if (Test-Path "$mavenDir\bin\mvn.cmd") {
    Write-Host "Maven $mavenVersion already exists in tools directory."
    Write-Host "Adding to PATH for this session..."
    $mavenBinPath = (Resolve-Path $mavenDir).Path + "\bin"
    $env:Path = "$mavenBinPath;$env:Path"
    Write-Host "Maven is now available. Run: mvn -version"
    exit 0
}

# Download Maven
Write-Host "Downloading Maven $mavenVersion..."
try {
    Invoke-WebRequest -Uri $mavenUrl -OutFile $mavenZip -ErrorAction Stop
    Write-Host "Download complete."
} catch {
    Write-Host "Failed to download Maven: $_"
    exit 1
}

# Extract Maven
Write-Host "Extracting Maven..."
try {
    Expand-Archive -Path $mavenZip -DestinationPath "tools" -Force
    Remove-Item $mavenZip -Force
    Write-Host "Extraction complete."
} catch {
    Write-Host "Failed to extract Maven: $_"
    exit 1
}

# Add to PATH for this session
$mavenBinPath = (Resolve-Path $mavenDir).Path + "\bin"
$env:Path = "$mavenBinPath;$env:Path"

Write-Host ""
Write-Host "Maven setup complete!"
Write-Host "Maven has been added to PATH for this session."
Write-Host ""
Write-Host "To make it permanent, add this to your System Environment Variables:"
Write-Host "  $mavenBinPath"
Write-Host ""
Write-Host "Verifying installation..."
& "$mavenDir\bin\mvn.cmd" -version

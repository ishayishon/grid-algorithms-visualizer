param([switch]$BuildOnly, [switch]$CheckGui)
$ErrorActionPreference = 'Stop'
$taskCandidates = @()
if ($env:JAVA_HOME) { $taskCandidates += $env:JAVA_HOME }
$taskJavac = Get-Command javac.exe -ErrorAction SilentlyContinue
if ($taskJavac) { $taskCandidates += Split-Path (Split-Path $taskJavac.Source -Parent) -Parent }
$taskJdks = Join-Path $env:USERPROFILE '.jdks'
if (Test-Path -LiteralPath $taskJdks) {
    $taskCandidates += (Get-ChildItem -LiteralPath $taskJdks -Directory | Sort-Object Name -Descending).FullName
}
$taskJdk = $null
foreach ($taskCandidate in ($taskCandidates | Select-Object -Unique)) {
    $taskCompiler = Join-Path $taskCandidate 'bin\javac.exe'
    if (-not (Test-Path -LiteralPath $taskCompiler)) { continue }
    $taskVersion = (& $taskCompiler -version 2>&1 | Out-String)
    if ($taskVersion -match 'javac\s+(\d+)' -and [int]$Matches[1] -ge 21) {
        $taskJdk = $taskCandidate
        break
    }
}
if (-not $taskJdk) { throw 'A JDK version 21 or newer is required. Install one and set JAVA_HOME, or add javac to PATH.' }
$taskBuild = Join-Path $PSScriptRoot 'build'
$taskClasses = Join-Path $taskBuild 'app-classes'
New-Item -ItemType Directory -Path $taskClasses -Force | Out-Null
$taskSources = (Get-ChildItem -LiteralPath (Join-Path $PSScriptRoot 'src\main\java') -Filter '*.java').FullName
& (Join-Path $taskJdk 'bin\javac.exe') --release 21 -encoding UTF-8 -d $taskClasses $taskSources
if ($LASTEXITCODE -ne 0) { throw 'Compilation failed.' }
$taskJar = Join-Path $taskBuild 'grid-algorithms-visualizer.jar'
& (Join-Path $taskJdk 'bin\jar.exe') --create --file $taskJar --main-class Ex3 -C $taskClasses .
if ($LASTEXITCODE -ne 0) { throw 'JAR packaging failed.' }
Write-Output ('Built: ' + $taskJar)
if ($CheckGui) {
    $taskTools = Join-Path $taskBuild 'tool-classes'
    New-Item -ItemType Directory -Path $taskTools -Force | Out-Null
    & (Join-Path $taskJdk 'bin\javac.exe') --release 21 -encoding UTF-8 -cp $taskClasses -d $taskTools (Join-Path $PSScriptRoot 'tools\SwingSmokeCheck.java')
    if ($LASTEXITCODE -ne 0) { throw 'GUI check compilation failed.' }
    & (Join-Path $taskJdk 'bin\java.exe') -cp ($taskClasses + ';' + $taskTools) SwingSmokeCheck (Join-Path $PSScriptRoot 'docs\verification\screenshots')
    if ($LASTEXITCODE -ne 0) { throw 'GUI verification failed.' }
} elseif (-not $BuildOnly) {
    & (Join-Path $taskJdk 'bin\java.exe') -jar $taskJar
    if ($LASTEXITCODE -ne 0) { throw 'Application exited with an error.' }
}

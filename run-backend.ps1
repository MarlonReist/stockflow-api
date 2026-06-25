$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$env:JAVA_HOME = Join-Path $root ".tools\jdk-25"
$env:Path = "$env:JAVA_HOME\bin;$(Join-Path $root '.tools\apache-maven-3.9.12\bin');$env:Path"

Set-Location $root
& .\.tools\apache-maven-3.9.12\bin\mvn.cmd "-Dmaven.repo.local=.m2\repository" spring-boot:run

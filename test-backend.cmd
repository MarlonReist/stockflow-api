@echo off
setlocal

set "ROOT=%~dp0"
set "JAVA_HOME=%ROOT%.tools\jdk-25"
set "PATH=%JAVA_HOME%\bin;%ROOT%.tools\apache-maven-3.9.12\bin;%PATH%"

cd /d "%ROOT%"
"%ROOT%.tools\apache-maven-3.9.12\bin\mvn.cmd" "-Dmaven.repo.local=.m2\repository" test

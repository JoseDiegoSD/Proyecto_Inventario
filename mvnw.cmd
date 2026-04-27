@REM Maven Wrapper for Windows
@REM ----------------------------
@echo off
setlocal

set JAVA_HOME=C:\Program Files\Java\jdk-11
set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"

if not exist "%JAVA_EXE%" (
    echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
    exit /b 1
)

set "WRAPPER_JAR=%~dp0.mvn\wrapper\maven-wrapper.jar"
set "WRAPPER_PROPERTIES=%~dp0.mvn\wrapper\maven-wrapper.properties"

if not exist "%WRAPPER_JAR%" (
    echo ERROR: Maven wrapper jar not found: %WRAPPER_JAR%
    exit /b 1
)

"%JAVA_EXE%" -cp "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*

endlocal

@echo off
setlocal EnableDelayedExpansion
title Hotel Management System

echo.
echo  =====================================================
echo    Hotel Management System  ^|  Servlet + Tomcat + MySQL
echo  =====================================================
echo.

set ROOT_DIR=%~dp0
if "%ROOT_DIR:~-1%"=="\" set ROOT_DIR=%ROOT_DIR:~0,-1%

set TOMCAT_VER=10.1.26
set TOMCAT_DIR=%ROOT_DIR%\apache-tomcat-%TOMCAT_VER%
set TOMCAT_URL=https://archive.apache.org/dist/tomcat/tomcat-10/v%TOMCAT_VER%/bin/apache-tomcat-%TOMCAT_VER%-windows-x64.zip

set MYSQL_JAR=mysql-connector-j-9.0.0.jar
set MYSQL_JAR_URL=https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.0.0/mysql-connector-j-9.0.0.jar
set LIB_DIR=%ROOT_DIR%\lib

set SRC_DIR=%ROOT_DIR%\src
set WEBAPP_DIR=%ROOT_DIR%\webapp
set BUILD_DIR=%ROOT_DIR%\build

for /d %%D in ("%ROOT_DIR%\jdk-17*") do set "JAVA_HOME=%%~D"
if not defined JAVA_HOME (
    if exist "C:\Program Files\Java\jdk-24" set "JAVA_HOME=C:\Program Files\Java\jdk-24"
    if exist "C:\Program Files\Java\jdk-26" set "JAVA_HOME=C:\Program Files\Java\jdk-26"
)

if defined JAVA_HOME (
    set "PATH=%JAVA_HOME%\bin;%PATH%"
    echo  Using JAVA_HOME: %JAVA_HOME%
) else (
    echo  ERROR: No JDK found. Please install a JDK or set JAVA_HOME.
    pause & exit /b 1
)

if not exist "%TOMCAT_DIR%\lib\servlet-api.jar" (
    echo [1/5] Downloading Apache Tomcat %TOMCAT_VER% ...
    if exist "%TOMCAT_DIR%" rmdir /s /q "%TOMCAT_DIR%"
    curl -L --progress-bar "%TOMCAT_URL%" -o "%ROOT_DIR%\tomcat.zip"
    if !errorlevel! neq 0 (
        echo  ERROR: Tomcat download failed. Check internet connection.
        pause & exit /b 1
    )
    echo       Extracting Tomcat ...
    tar -xf "%ROOT_DIR%\tomcat.zip" -C "%ROOT_DIR%"
    del "%ROOT_DIR%\tomcat.zip"
    echo       Done.
) else (
    echo [1/5] Tomcat already present.
)

if not exist "%LIB_DIR%\%MYSQL_JAR%" (
    echo [2/5] Downloading MySQL JDBC Driver ...
    mkdir "%LIB_DIR%" 2>nul
    curl -L --progress-bar "%MYSQL_JAR_URL%" -o "%LIB_DIR%\%MYSQL_JAR%"
    if !errorlevel! neq 0 (
        echo  ERROR: MySQL JDBC driver download failed.
        pause & exit /b 1
    )
    echo       Done.
) else (
    echo [2/5] MySQL JDBC driver already present.
)

set SERVLET_JAR=%TOMCAT_DIR%\lib\servlet-api.jar
set CLASSPATH=%SERVLET_JAR%;%LIB_DIR%\%MYSQL_JAR%

echo [3/5] Compiling Java source files...
if exist "%BUILD_DIR%" (
    set "_CLEAN=0"
    for /L %%i in (1,1,3) do (
        if !_CLEAN! equ 0 (
            rmdir /s /q "%BUILD_DIR%" 2>nul && set "_CLEAN=1"
            if !_CLEAN! equ 0 (
                echo       Build dir locked, retrying in 2s ^(attempt %%i/3^)...
                timeout /t 2 /nobreak >nul
            )
        )
    )
    if !_CLEAN! equ 0 (
        echo       WARNING: Could not fully delete build dir. Cleaning classes only...
        if exist "%BUILD_DIR%\WEB-INF\classes" rmdir /s /q "%BUILD_DIR%\WEB-INF\classes" 2>nul
    )
)
mkdir "%BUILD_DIR%\WEB-INF\classes" 2>nul
mkdir "%BUILD_DIR%\WEB-INF\lib" 2>nul

javac -encoding UTF-8 -cp "%CLASSPATH%" -d "%BUILD_DIR%\WEB-INF\classes" ^
    "%SRC_DIR%\com\hotel\model\Room.java"            ^
    "%SRC_DIR%\com\hotel\util\DBManager.java"        ^
    "%SRC_DIR%\com\hotel\servlet\HotelServlet.java"

if !errorlevel! neq 0 (
    echo  ERROR: Compilation failed. See errors above.
    pause & exit /b 1
)
echo       Compilation successful.

echo [4/5] Packaging web application...
copy /Y "%LIB_DIR%\%MYSQL_JAR%" "%BUILD_DIR%\WEB-INF\lib\" >nul
xcopy /E /I /Q /Y "%WEBAPP_DIR%\*" "%BUILD_DIR%\" >nul
echo       Done.

echo [5/5] Deploying to Tomcat...
if exist "%TOMCAT_DIR%\webapps\ROOT" rmdir /s /q "%TOMCAT_DIR%\webapps\ROOT"
mkdir "%TOMCAT_DIR%\webapps\ROOT"
xcopy /E /I /Q /Y "%BUILD_DIR%\*" "%TOMCAT_DIR%\webapps\ROOT\" >nul

net start MYSQL80 2>nul

echo.
echo  =====================================================
echo    Starting Server on http://localhost:8080
echo    Opening browser automatically...
echo    Press Ctrl+C in this window to stop.
echo  =====================================================
echo.

start "" cmd /c "timeout /t 2 /nobreak >nul & start http://localhost:8080"

set CATALINA_HOME=%TOMCAT_DIR%
set CATALINA_BASE=%TOMCAT_DIR%
call "%TOMCAT_DIR%\bin\catalina.bat" run

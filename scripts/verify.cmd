@echo off
setlocal

java -version
call mvn -version
if errorlevel 1 exit /b 1

call mvn clean verify
if errorlevel 1 (
  echo Verification failed.
  exit /b 1
)

if not exist target\surefire-reports (
  echo ERROR: Surefire report directory not found.
  exit /b 1
)

findstr /S /C:"tests=\"" target\surefire-reports\TEST-*.xml >nul
if errorlevel 1 (
  echo ERROR: No executed-test report was found.
  exit /b 1
)

echo Verification passed. Inspect target\surefire-reports for test counts.
endlocal

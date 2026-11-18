@echo off
REM Compila y ejecuta el Sokoban (requiere javac y java en el PATH)
cd /d "%~dp0"
if exist out rmdir /s /q out
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -d out @sources.txt
if errorlevel 1 (
  del sources.txt
  pause
  exit /b 1
)
del sources.txt
java -cp "out;src" sokoban.Main

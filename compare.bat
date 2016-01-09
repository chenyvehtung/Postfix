javac -d bin src\Compare.java
@echo off
pause
cd bin
@echo on
java Compare
@echo off
pause
move result.txt ..\
cd ..
@echo on
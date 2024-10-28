@echo off

REM proto文件生成的java类，最终存在的项目代码目录
set "JAVA_OUTPUT_DIR=D:\WorkCode\GameServer\GameServer\src\main\java"
REM proto文件生成的java类，包名(注意与文件内部包名统一)
set "JAVA_PROTO_PACKAGE=com\games\proto"

REM 定义protoc可执行文件路径
set "PROTOC_PATH=.\protoc-3.19.6-win64\bin\protoc.exe"
REM 定义所有原始proto文件目录
set "PROTO_DEFINE_DIR=.\..\define"
REM proto文件生成的java类，临时存在的目录
set "JAVA_GENERATE_DIR=.\..\generate"

REM ===============================
REM Script start
REM ===============================

REM step1：删除临时目录，然后再创建新的
if exist "%JAVA_GENERATE_DIR%" (
    rd /s /q "%JAVA_GENERATE_DIR%"
)
mkdir "%JAVA_GENERATE_DIR%"

REM step2：删除最终目录，然后再创建新的
if exist "%JAVA_OUTPUT_DIR%\%JAVA_PROTO_PACKAGE%" (
    rd /s /q "%JAVA_OUTPUT_DIR%\%JAVA_PROTO_PACKAGE%"
)
mkdir "%JAVA_OUTPUT_DIR%\%JAVA_PROTO_PACKAGE%"

REM step3：生成文件，并拷贝
%PROTOC_PATH% --proto_path=%PROTO_DEFINE_DIR% --java_out=%JAVA_GENERATE_DIR% %PROTO_DEFINE_DIR%\*.proto

REM step4：拷贝生成的文件到目标目录
copy "%JAVA_GENERATE_DIR%\%JAVA_PROTO_PACKAGE%\*.java" "%JAVA_OUTPUT_DIR%\%JAVA_PROTO_PACKAGE%"

REM step5：删除临时生成目录
rd /s /q "%JAVA_GENERATE_DIR%"

pause
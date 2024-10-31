@echo off

REM ##########################################################################
REM 1、该脚本用到了Python，请保证机器上可以执行Python命令
REM ##########################################################################

REM proto文件生成的java类，包名(注意与proto定义文件内部包名统一)
set "JAVA_PROTO_PACKAGE=com.games.proto"
REM 定义所有原始proto文件目录
set "PROTO_DEFINE_DIR=.\..\define"

REM 调用Python脚本并传递参数
python generate_proto_ids.py ^
  --proto_define_dir %PROTO_DEFINE_DIR% ^
  --package_name %JAVA_PROTO_PACKAGE%

pause
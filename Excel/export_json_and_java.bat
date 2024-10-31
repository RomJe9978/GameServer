@echo off

REM ###############################################################################
REM ##
REM ## 1、该脚本用到了Python，请保证机器上可以执行Python命令
REM ##
REM ###############################################################################

REM 定义Json数据输出的目录
SET JSON_OUTPUR_DIR=D:\\WorkCode\GameServer\GameServer\src\main\resources\xls

REM 定义Java文件输出的目录
SET JAVA_OUTPUT_DIR=D:\\WorkCode\GameServer\GameServer\src\main\java\com\games\xls

REM 调用Python脚本，导出json文件
python excel_export_json.py --json_output_dir %JSON_OUTPUR_DIR%

REM 调用Python脚本，自动生成java类文件
python excel_export_java.py --java_output_dir %JAVA_OUTPUT_DIR%

pause
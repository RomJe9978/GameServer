import pandas as pd
import json
import os
from glob import glob
import argparse

################################# 脚本说明 ###################################################
##
## 1、安装Excel相关的库(pandas和openpyxl)
##   windows环境下在命令行依次执行，“pip install pandas”和“pip install openpyxl”即可
##
## 2、约定前四行为表头定义，一到四行依次含义为：“变量命名”，“可见性标识”，“数据类型”，“备注说明”
##   从第5行开始是真正的数据定义，数据配置可以是空单元格，会自动转换成默认值“DEFAULT_VALUES”
##
#############################################################################################


################################## 全局定义，可自行修改 ########################################

INPUT_DIR = ''  # 指定所有Excel所在的文件目录,如果是空字符串则默认是当前脚本所在目录
OUTPUT_DIR = '.\\json' # 指定所有JSON文件输出目录,如果是空字符串则默认是当前脚本所在目录
VISIBILITY_FILTER = 'S'  # 定义过滤条件中的可见性字符“C，S，CS”，只有包含这个可见性的才会被输出

TRUE_VALUES = ['true', '1', 'yes', 'y']  #定义全局的布尔字符串集合，这些值都代表“TRUE”，无视大小写
DELIMITER = ',' # 数组类型的数据各个元素之间的分隔符统一

# 定义全局的默认值字典
DEFAULT_VALUES = {
    'int': 0,
    'long': 0,
    'string': '',
    'bool': False,
    'int[]': [],
    'long[]': [],
    'string[]': [],
    'bool[]': []
}
##############################################################################################

# 根据Excel中的值和类型进行转换。空单元格使用默认值
def convert_value(value, data_type):
    if pd.isnull(value):
        return DEFAULT_VALUES.get(data_type, None)

    try:
        if data_type == 'int' or data_type == 'long':
            return int(value)
        elif data_type == 'string':
            return str(value)
        elif data_type == 'bool':
            if isinstance(value, str):
                return value.lower() in TRUE_VALUES
            return bool(value)
        elif data_type == 'int[]' or data_type == 'long[]':
            return list(map(int, value.split(DELIMITER)))
        elif data_type == 'string[]':
            return value.split(DELIMITER)
        elif data_type == 'bool[]':
            return list(map(lambda x: x.lower() in TRUE_VALUES, value.split(DELIMITER)))
        else:
            return value
    except Exception as e:
        print(f"数据转换错误！值: {value}, 类型: {data_type}, 错误: {e}")
        return value

# 将Excel文件生成对应的JSON格式数据，并输出到指定文件夹
def excel_to_json(excel_file, output_dir):
    df = pd.read_excel(excel_file, header=None)
    headers = df.iloc[0].tolist()
    visibility = df.iloc[1].tolist()
    data_types = df.iloc[2].tolist()
    comments = df.iloc[3].tolist()
    
    # 过滤掉不包含指定可见性配置的列
    indices_to_keep = [i for i, v in enumerate(visibility) if VISIBILITY_FILTER in str(v)]
    headers = [headers[i] for i in indices_to_keep]
    data_types = [data_types[i] for i in indices_to_keep]
    comments = [comments[i] for i in indices_to_keep]
    
    data = df.iloc[4:, indices_to_keep].copy()
    data.columns = headers

    result = []
    for index, row in data.iterrows():
        entry = {}
        for header, value, data_type, comment in zip(headers, row, data_types, comments):
            converted_value = convert_value(value, data_type)
            entry[header] = converted_value
        result.append(entry)

    # 获取文件名（不带路径）
    file_name_with_ext = os.path.basename(excel_file)
    # 去掉扩展名
    file_name = os.path.splitext(file_name_with_ext)[0]

    # 输出到同名的.json文件中
    json_file = os.path.join(output_dir, f"{file_name}.json")
    with open(json_file, 'w', encoding='utf-8') as f:
        json.dump(result, f, ensure_ascii=False, indent=4)
    print(f"数据已成功导出到 {json_file}")

# 处理指定文件夹下的所有Excel文件
def process_all_excels(input_dir, output_dir):
    os.makedirs(output_dir, exist_ok=True)
    excel_files = glob(os.path.join(input_dir, "*.xlsx"))
    for excel_file in excel_files:
        excel_to_json(excel_file, output_dir)

# 1、如果target_dir是空字符串，则直接返回base_dir的绝对路径
# 2、如果target_dir是相对路径，则返回在base_dir的基础上加上相对路径后的绝对路径
# 3、如果target_dir是绝对路径，则直接返回target_dir
def process_dir(base_dir, target_dir):
    if target_dir:
        if os.path.isabs(target_dir):
            return os.path.normpath(target_dir)
        else:
            return os.path.normpath(os.path.join(base_dir, target_dir))
    else:
        return os.path.normpath(base_dir)

# 清空输出目录中的所有内容
def clear_output_dir(output_dir):
    if os.path.exists(output_dir):
        for filename in os.listdir(output_dir):
            file_path = os.path.join(output_dir, filename)
            try:
                if os.path.isfile(file_path) or os.path.islink(file_path):
                    os.unlink(file_path)
                elif os.path.isdir(file_path):
                    shutil.rmtree(file_path)
            except Exception as e:
                print(f'删除文件 {file_path} 失败. Reason: {e}')
        
def main():
    # 获取当前脚本所在的目录
    current_dir = os.path.dirname(os.path.abspath(__file__))

    # 处理输入目录字符串，确保无论是相对路径还是绝对路径都能正确处理
    input_dir = process_dir(current_dir, INPUT_DIR)
    output_dir = process_dir(current_dir, OUTPUT_DIR)
    
    # 允许外部进行参数指定（不指定就采用当前文件内的默认值）
    parser = argparse.ArgumentParser(description="Generate json data from excel file.")
    parser.add_argument("--excel_dir", default=input_dir, help="Path to the directory containing excel files")
    parser.add_argument("--json_output_dir", default=output_dir, help="Path to output generated json data")
    args = parser.parse_args()
    
    # 先将旧的全部删除
    clear_output_dir(args.json_output_dir)

    # 处理目录下的所有Excel文件
    process_all_excels(args.excel_dir, args.json_output_dir)

if __name__ == "__main__":
    main()
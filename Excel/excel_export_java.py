import os
import pandas as pd
import argparse

################################# 脚本说明 ###################################################
##
## 1、安装Excel相关的库(pandas和openpyxl)
##   windows环境下在命令行依次执行，“pip install pandas”和“pip install openpyxl”即可
##
## 2、约定前四行为表头定义，一到四行依次含义为：“变量命名”，“可见性标识”，“数据类型”，“备注说明”
##   从第5行开始是真正的数据定义，数据配置可以是空单元格，会自动转换成默认值“DEFAULT_VALUES”
##   所有Excel表约定主键列的命名为“id”，“int”类型，不可重复，最后以该id作为行数据索引
##   
## 3、最后每张Excel关系表会生成对应的一下一组类，假如Excel文件名为“YYY.xlsx”
##   (1) "XlsYYY.java": Excel表结构映射的数据结构（统一继承一个基类，增强维护性）
##   (2) "XlsYYYManager.java": Excel表内所有实际数据的管理者（统一继承一个基类，增强维护性）
##   (3) "XlsYYYAssembler.java": Excel表内所有原始数据的“二次加工组装处理类”（统一实现一个接口，增强维护性）
##   以上的所有“Xls”的前缀和“Manager”“Assembler”的后缀均可自行指定
##
## 4、自动生成的Java类，此时依赖了一些外部包和注解等，如下：
##   (1) "com.alibaba/fastjson/1.2.83": 最终将Excel数据转换成内存数据是通过Json进行的
##   (2) "org.projectlombok/lombok/1.18.28": 使用到了相关的注解
##
#############################################################################################

################################## 全局定义，可自行修改 ########################################

INPUT_DIR = ''  # 指定所有Excel所在的文件目录,如果是空字符串则默认是当前脚本所在目录
OUTPUT_DIR = './/java' # 指定所有JSON文件输出目录,如果是空字符串则默认是当前脚本所在目录
VISIBILITY_FILTER = 'S'  # 定义过滤条件中的可见性字符“C，S，CS”，只有包含这个可见性的才会被输出

JAVA_BEAN_SUB_DIR = 'bean'  # 所有JavaBean统一存在的子目录
JAVA_PACKAGE_NAME = f'com.games.xls.{JAVA_BEAN_SUB_DIR}'  # JavaBean所统一生成的包名
CLASS_NAME_PREFIX = 'Xls'   # 根据需要调整JavaBean类名前缀
BASE_CLASS_NAME = 'com.games.framework.component.xlskit.AbstractXlsBean'  # 设定JavaBean类指定的基类名称

MANAGER_SUB_DIR = 'manager'  # 所有Manager统一存在的子目录
MANAGER_PACKAGE_NAME = f'com.games.xls.{MANAGER_SUB_DIR}'  # Manager所统一生成的包名
MANAGER_CLASS_SUFFIX = 'Manager'  # 根据需要调整Manager类名后缀
MANAGER_BASE_CLASS = 'com.games.framework.component.xlskit.AbstractXlsManager'  # 设定Manager基类

ASSEMBLER_SUB_DIR = 'assembler' # 所有Assembler统一存在的子目录
ASSEMBLER_PACKAGE_NAME = f'com.games.xls.{ASSEMBLER_SUB_DIR}' # Assembler所统一生成的包名
ASSEMBLER_CLASS_SUFFIX = 'Assembler' # Assembler所统一生成的包名
ASSENBLER_BASE_CLASS = 'com.games.framework.component.xlskit.IXlsAssembler'  # 设定Assembler接口

# 定义全局变量和映射关系
DATA_TYPE_MAPPING = {
    'int': 'int',
    'long': 'long',
    'string': 'String',
    'bool': 'boolean',
    'int[]': 'int[]',
    'long[]': 'long[]',
    'string[]': 'String[]',
    'bool[]': 'boolean[]'
}
##############################################################################################

# 读取Excel文件的元数据
def read_excel_metadata(excel_file):
    df = pd.read_excel(excel_file, header=None)
    headers = df.iloc[0].tolist()
    visibility = df.iloc[1].tolist()
    data_types = df.iloc[2].tolist()
    comments = df.iloc[3].tolist()
    
    # 过滤依据VISIBILITY_FILTER
    indices_to_keep = [i for i, v in enumerate(visibility) if VISIBILITY_FILTER in str(v)]
    
    headers = [headers[i] for i in indices_to_keep]
    data_types = [data_types[i] for i in indices_to_keep]
    comments = [comments[i] for i in indices_to_keep]
    return headers, data_types, comments

# 自动生成JavaBean代码
def generate_java_bean_class(file_name, headers, data_types, comments):
    base_class_name = ''.join(word.capitalize() for word in file_name.split('_'))
    class_name = f"{CLASS_NAME_PREFIX}{base_class_name}"
    
    java_code = ''
    if JAVA_PACKAGE_NAME:
        java_code += f"package {JAVA_PACKAGE_NAME};\n\n"

    # 导入必要的类
    java_code += "import java.util.List;\n"
    java_code += "import java.util.Map;\n"
    java_code += "import java.util.ArrayList;\n"
    java_code += "import java.util.HashMap;\n\n"
    java_code += f"import {BASE_CLASS_NAME};\n"
    java_code += "import com.alibaba.fastjson.JSONObject;\n"
    java_code += "import lombok.Getter;\n\n"

    # 类声明和继承
    java_code += f"/**\n * 该类自动生成，不可编辑，每次生成会覆盖\n */\n"
    java_code += f"@Getter\n"
    super_simple_name = BASE_CLASS_NAME.split('.')[-1]
    java_code += f"public final class {class_name} extends {super_simple_name} {{\n\n"

    # 添加类成员变量
    for header, data_type, comment in zip(headers, data_types, comments):
        java_type = DATA_TYPE_MAPPING.get(data_type, 'Object')
        java_code += f"    /**\n     * {comment}\n     */\n"
        java_code += f"    private final {java_type} {header};\n\n"
    
    java_code += "\n"

    # 生成有参数的构造方法
    constructor_params = ', '.join(f"{DATA_TYPE_MAPPING.get(data_type, 'Object')} {header}" for header, data_type in zip(headers, data_types))
    java_code += f"    private {class_name}({constructor_params}) {{\n"
    for header in headers:
        java_code += f"        this.{header} = {header};\n"
    java_code += "    }\n\n"
    
    # 生成 public static of(JSONObject jsonObject) 方法
    java_code += f"    public static {class_name} of(JSONObject jsonObject) {{\n"
    java_code += f"        return new {class_name}(\n"
    json_params = []
    for header, data_type in zip(headers, data_types):
        if data_type == 'int':
            json_params.append(f'jsonObject.getIntValue("{header}")')
        elif data_type == 'long':
            json_params.append(f'jsonObject.getLongValue("{header}")')
        elif data_type == 'bool':
            json_params.append(f'jsonObject.getBooleanValue("{header}")')
        elif data_type == 'string':
            json_params.append(f'jsonObject.getString("{header}")')
        elif data_type == 'int[]':
            json_params.append(f'jsonObject.getObject("{header}", int[].class)')
        elif data_type == 'long[]':
            json_params.append(f'jsonObject.getObject("{header}", long[].class)')
        elif data_type == 'bool[]':
            json_params.append(f'jsonObject.getObject("{header}", boolean[].class)')
        elif data_type == 'string[]':
            json_params.append(f'jsonObject.getObject("{header}", String[].class)')
        else:
            json_params.append(f'jsonObject.getObject("{header}", Object.class)')
    
    java_code += f"                {',\n                '.join(json_params)}\n"
    java_code += "        );\n"
    java_code += "    }\n"
    java_code += "}\n"
    return java_code

# 生成对应的管理类方法
def generate_manager_class(file_name):
    base_class_name = ''.join(word.capitalize() for word in file_name.split('_'))
    manager_class_name = f"{CLASS_NAME_PREFIX}{base_class_name}{MANAGER_CLASS_SUFFIX}"
    bean_class_name = f"{CLASS_NAME_PREFIX}{base_class_name}"
    
    java_code = ''
    if JAVA_PACKAGE_NAME:
        java_code += f"package {MANAGER_PACKAGE_NAME};\n\n"

    # 导入必要的类
    java_code += "import com.alibaba.fastjson.JSONArray;\n"
    java_code += "import com.alibaba.fastjson.JSONObject;\n\n"
    java_code += "import java.util.ArrayList;\n"
    java_code += "import java.util.Collections;\n"
    java_code += "import java.util.List;\n"
    java_code += "import java.util.Objects;\n\n"
    java_code += f"import {MANAGER_BASE_CLASS};\n"
    java_code += f"import {JAVA_PACKAGE_NAME}.{bean_class_name};\n\n"

    # 类声明和单例模式实现
    java_code += f"/**\n * 该类自动生成，不可编辑，每次生成会覆盖\n */\n"
    super_simple_name = MANAGER_BASE_CLASS.split('.')[-1]
    java_code += f"public final class {manager_class_name} extends {super_simple_name}<{bean_class_name}> {{\n\n"
    
    # 单例模式的静态变量
    java_code += f"    private static final {manager_class_name} INSTANCE = new {manager_class_name}();\n\n"
    
    # 私有构造函数禁止外部实例化
    java_code += f"    private {manager_class_name}() {{\n"
    java_code += f"    }}\n\n"
    
    # 获取单例实例的方法
    java_code += f"    public static {manager_class_name} getInstance() {{\n"
    java_code += f"        return INSTANCE;\n"
    java_code += f"    }}\n\n"
    
    # 生成 xlsName 方法
    java_code += f"    @Override\n"
    java_code += f"    public String xlsName() {{\n"
    java_code += f"        return \"{base_class_name}\";\n"
    java_code += f"    }}\n\n"
    
    # 生成 parseFrom 方法
    java_code += f"    @Override\n"
    java_code += f"    public List<{bean_class_name}> parseFrom(String jsonText) {{\n"
    java_code += f"        JSONArray jsonArray = JSONArray.parseArray(jsonText);\n"
    java_code += f"        if (Objects.isNull(jsonArray) || jsonArray.isEmpty()) {{\n"
    java_code += f"            return Collections.emptyList();\n"
    java_code += f"        }}\n\n"
    java_code += f"        List<{bean_class_name}> resultList = new ArrayList<>(jsonArray.size());\n"
    java_code += f"        for (int i = 0, iSize = jsonArray.size(); i < iSize; i++) {{\n"
    java_code += f"            JSONObject single = jsonArray.getJSONObject(i);\n"
    java_code += f"            {bean_class_name} newInstance = {bean_class_name}.of(single);\n"
    java_code += f"            resultList.add(newInstance);\n"
    java_code += f"        }}\n"
    java_code += f"        return resultList;\n"
    java_code += f"    }}\n\n"

    java_code += "}\n"
    return java_code

# 生成对应的二次组装类方法
def generate_assembler_class(file_name):
    base_class_name = ''.join(word.capitalize() for word in file_name.split('_'))
    assembler_class_name = f"{CLASS_NAME_PREFIX}{base_class_name}{ASSEMBLER_CLASS_SUFFIX}"
    bean_class_name = f"{CLASS_NAME_PREFIX}{base_class_name}"
    
    java_code = ''
    if JAVA_PACKAGE_NAME:
        java_code += f"package {ASSEMBLER_PACKAGE_NAME};\n\n"

    # 导入必要的类
    java_code += "import java.util.List;\n"
    java_code += "import java.util.Objects;\n\n"
    java_code += f"import {ASSENBLER_BASE_CLASS};\n\n"

    # 分割字符串并获取最后一个部分
    java_code += f"/**\n * 该类自动生成，但只会生成一次，可以编写逻辑\n */\n"
    interface_simple_name = ASSENBLER_BASE_CLASS.split('.')[-1]
    java_code += f"public final class {assembler_class_name} implements {interface_simple_name} {{\n\n"
    
    # 单例模式的静态变量
    java_code += f"    private static final {assembler_class_name} INSTANCE = new {assembler_class_name}();\n\n"
    
    # 私有构造函数禁止外部实例化
    java_code += f"    private {assembler_class_name}() {{\n"
    java_code += f"    }}\n\n"
    
    # 获取单例实例的方法
    java_code += f"    public static {assembler_class_name} getInstance() {{\n"
    java_code += f"        return INSTANCE;\n"
    java_code += f"    }}\n\n"
    
    # 生成 xlsName 方法
    java_code += f"    @Override\n"
    java_code += f"    public String xlsName() {{\n"
    java_code += f"        return \"{base_class_name}\";\n"
    java_code += f"    }}\n\n"
    
    # 生成 assemble 方法
    java_code += f"    @Override\n"
    java_code += f"    public boolean assemble() {{\n"
    java_code += f"        return false;\n"
    java_code += f"    }}\n\n"
    
    # 生成 afterAssemble 方法
    java_code += f"    @Override\n"
    java_code += f"    public boolean afterAssemble() {{\n"
    java_code += f"        return false;\n"
    java_code += f"    }}\n\n"
    
    # 生成 check 方法
    java_code += f"    @Override\n"
    java_code += f"    public boolean check() {{\n"
    java_code += f"        return false;\n"
    java_code += f"    }}\n\n"

    java_code += "}\n"
    return java_code
    
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

# 保存Java代码到文件
def save_java_file(output_dir, file_name, java_code):
    os.makedirs(output_dir, exist_ok=True)
    java_file_path = os.path.join(output_dir, f"{file_name}.java")
    with open(java_file_path, 'w', encoding='utf-8') as file:
        file.write(java_code)
    print(f"Java类已成功生成并保存到 {java_file_path}")

def main():
    # 获取当前脚本所在的目录
    current_dir = os.path.dirname(os.path.abspath(__file__))

    # 处理输入目录字符串，确保无论是相对路径还是绝对路径都能正确处理
    input_dir = process_dir(current_dir, INPUT_DIR)
    output_dir = process_dir(current_dir, OUTPUT_DIR)

    # 允许外部进行参数指定（不指定就采用当前文件内的默认值）
    parser = argparse.ArgumentParser(description="Generate java class from excel file.")
    parser.add_argument("--excel_dir", default=input_dir, help="Path to the directory containing excel files")
    parser.add_argument("--java_output_dir", default=output_dir, help="Path to output generated java file")
    args = parser.parse_args()
    
    # 组合目录
    bean_dir_path = os.path.join(args.java_output_dir, JAVA_BEAN_SUB_DIR)
    manager_dir_path = os.path.join(args.java_output_dir, MANAGER_SUB_DIR)
    assembler_dir_path = os.path.join(args.java_output_dir, ASSEMBLER_SUB_DIR)
    
    # 清除目录下已有的所有文件，保证每次都是全新的（Assembler生成的是模板，不要清除）
    clear_output_dir(bean_dir_path)
    clear_output_dir(manager_dir_path)
    
    # 处理目录下的所有Excel文件
    for excel_file in os.listdir(args.excel_dir):
        if excel_file.endswith('.xlsx'):
            file_path = os.path.join(args.excel_dir, excel_file)
            file_name = os.path.splitext(excel_file)[0]
            
            # 读取Excel的元数据
            headers, data_types, comments = read_excel_metadata(file_path)
            
            # 生成并保存JavaBean类代码
            java_bean_code = generate_java_bean_class(file_name, headers, data_types, comments)
            java_bean_file_name = f"{CLASS_NAME_PREFIX}{file_name.capitalize()}"
            save_java_file(bean_dir_path, java_bean_file_name, java_bean_code)
            
            # 生成并保存Manager类代码
            manager_code = generate_manager_class(file_name)
            manager_file_name = f"{java_bean_file_name}{MANAGER_CLASS_SUFFIX}"
            save_java_file(manager_dir_path, manager_file_name, manager_code)
            
            # Assembler生成的是模板，只需要生成一次，如果目标文件存在，则跳过生成
            assembler_file_name = f"{java_bean_file_name}{ASSEMBLER_CLASS_SUFFIX}"
            assembler_java_file_path = os.path.join(assembler_dir_path, f"{assembler_file_name}.java")
            if os.path.exists(assembler_java_file_path):
                print(f"Java class {assembler_file_name} already exists, skipping...")
            else:
                assembler_code = generate_assembler_class(file_name)
                save_java_file(assembler_dir_path, assembler_file_name, assembler_code)

if __name__ == "__main__":
    main()

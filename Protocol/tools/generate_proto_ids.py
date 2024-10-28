import os
import re
import argparse

# 为定义的Proto消息，自动分配一个标识的Id，说明如下：
#
# 1、自动分配消息Id，并自动生成一个新Proto文件，内部使用枚举的方式，建立“消息”与“消息Id”的对应关系
#    这种“新建Proto”文件的方式是为了，保证前后端的消息Id一致，并且可以利用所有ProtoBuffer的特性，比如跨语言
#
# 2、消息Id的分配是根据“消息命名”而来，脚本会将消息命名转换成“全大写+下划线”的常量命名方式，然后分配消息ID
#    约定，所有消息的命名均统一采用“完全驼峰法”命名。脚本会转换，例如：CsLoginRequest会变成“CS_LOGIN_REQUEST”
#    注意，脚本转换的命名只是用在了消息Id对应的自动生成的枚举中，所有原始定义不会被改变，也不应被改变
#
# 3、已有消息的消息号只要被分配了消息Id，便不再会改变，除非修改消息命名
#    保证所有消息在整个项目开发进程中，其消息号Id的不变性，避免因为重新分配而导致的可能存在的问题
#
# 4、如果删除了一个消息定义，此时消息Id会保留，保证该消息Id不会再被分配给任何其他消息，即使已经不存在了
#    这点是出于版本兼容问题考虑。并且理论上，好的规约应该保证消息不应该被删除，废弃不使用即可

"""提取给定proto文件中的所有message名称"""
def extract_message_names(proto_file):
    message_names = []
    with open(proto_file, "r", encoding="utf-8") as file:
        lines = file.readlines()
        for line in lines:
            match = re.match(r'\s*message\s+(\w+)\s*\{', line)
            if match:
                message_names.append(match.group(1))
    return message_names

"""搜寻指定文件夹下的所有.proto文件"""
def find_proto_files(directory):
    proto_files = []
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith(".proto"):
                proto_files.append(os.path.join(root, file))
    return proto_files

"""将驼峰命名转换成“大写＋下划线”的方式"""
def camel_to_snake(name):
    s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
    return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).upper()

"""解析现有的Proto文件，提取已经存在的枚举值和对应的id"""
def parse_existing_enum(file_path, enum_name):
    existing_enum = {}

    # 如果文件不存在，返回空字典
    if not os.path.exists(file_path):
        return existing_enum

    with open(file_path, "r", encoding="utf-8") as file:
        lines = file.readlines()
        enum_started = False
        for line in lines:
            if f"enum {enum_name}" in line:
                enum_started = True
                continue
            if enum_started and "}" in line:
                break
            if enum_started:
                match = re.match(r'\s*(\w+)\s*=\s*(\d+);', line)
                if match:
                    existing_enum[match.group(1)] = int(match.group(2))
    return existing_enum

"""生成包含所有协议的新版Proto文件"""
def generate_java_class(proto_files, existing_enum, output_file, package_name, class_name, enum_name, starting_id):
    protocol_map = existing_enum.copy()
    current_id = max(existing_enum.values(), default=starting_id - 1) + 1

    for proto_file in proto_files:
        message_names = extract_message_names(proto_file)
        for name in message_names:
            snake_case_name = camel_to_snake(name)
            if snake_case_name not in protocol_map:
                protocol_map[snake_case_name] = current_id
                current_id += 1

    with open(output_file, "w", encoding="utf-8") as file:
        file.write(f"syntax = \"proto3\";\n\n")
        file.write(f"option java_package=\"{package_name}\";\n")
        file.write(f"option java_outer_classname=\"{class_name}\";\n\n")
        file.write(f"enum {enum_name} {{\n")
        if "None" not in protocol_map:
            file.write("    None = 0;\n")
        for name, id_ in sorted(protocol_map.items(), key=lambda item: item[1]):
            file.write(f"    {name} = {id_};\n")
        file.write("}\n")

def main():
    parser = argparse.ArgumentParser(description="Generate ID enums for Proto files.")
    parser.add_argument("--proto_define_dir", required=True, help="Path to the directory containing proto files")
    parser.add_argument("--package_name", required=True, help="Build package name")
    parser.add_argument("--class_name", default="PacketIds", help="Build class name")
    parser.add_argument("--enum_name", default="Ids", help="Name of the enum ID")
    parser.add_argument("--starting_id", type=int, default=1000, help="Starting ID number")
    args = parser.parse_args()

    # 创建输出目录（如果不存在）
    os.makedirs(args.proto_define_dir, exist_ok=True)

    # 包含所有id的生成的proto文件的文件名称
    proto_file_name = f"{args.class_name}.proto"
    proto_file_path = os.path.join(args.proto_define_dir, proto_file_name)
    print(f"Proto File Path: {proto_file_path}")

    # 读取现有的proto文件（如果存在），获取已有的枚举映射，为了保证旧协议号不被重新分配
    existing_enum = parse_existing_enum(proto_file_path, args.enum_name)
    print("Existing Enum:")
    for key, value in existing_enum.items():
        print(f"{key}: {value}")

    all_proto_files = find_proto_files(args.proto_define_dir)
    print("All Proto Files:")
    for proto_file in all_proto_files:
        print(proto_file)

    # 生成最新的即可
    generate_java_class(all_proto_files, existing_enum, proto_file_path,
                        args.package_name, args.class_name, args.enum_name, args.starting_id)

if __name__ == "__main__":
    main()

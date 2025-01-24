import subprocess
import pickle
from flask import Flask, request

app = Flask(__name__)


# 主页，提供不同操作的链接和表单
@app.route('/', methods=['GET'])
def index():
    return '''
    <html>
    <body>
        <h1>命令执行</h1>
        <form action="/execute_command" method="post">
            <label for="command">命令:</label>
            <input type="text" id="command" name="command">
            <input type="submit" value="执行">
        </form>
        <h1>反序列化</h1>
        <form action="/deserialize" method="post">
            <label for="serialized_data">序列化数据:</label>
            <input type="text" id="serialized_data" name="serialized_data">
            <input type="submit" value="反序列化">
        </form>
        <h1>SQL 注入</h1>
        <form action="/sql_injection" method="post">
            <label for="username">用户名:</label>
            <input type="text" id="username" name="username">
            <input type="submit" value="查询">
        </form>
    </body>
    </html>
    '''


# 命令执行接口，存在命令注入漏洞
@app.route('/execute_command', methods=['POST'])
def execute_command():
    command = request.form.get('command')
    try:
        # 直接使用用户输入的命令执行系统命令，未做任何过滤
        result = subprocess.check_output(command, shell=True)
        return result.decode('utf-8')
    except Exception as e:
        return str(e)


# 反序列化接口，存在反序列化漏洞
@app.route('/deserialize', methods=['POST'])
def deserialize():
    serialized_data = request.form.get('serialized_data')
    try:
        # 直接对用户提供的序列化数据进行反序列化，未做验证
        data = pickle.loads(eval(serialized_data))
        return str(data)
    except Exception as e:
        return str(e)


# SQL 注入接口，存在 SQL 注入漏洞
@app.route('/sql_injection', methods=['POST'])
def sql_injection():
    import sqlite3
    username = request.form.get('username')
    try:
        conn = sqlite3.connect('example.db')
        cursor = conn.cursor()
        # 使用字符串拼接构建 SQL 查询语句，未对用户输入进行过滤
        query = f"SELECT * FROM users WHERE username = '{username}'"
        cursor.execute(query)
        results = cursor.fetchall()
        conn.close()
        return str(results)
    except Exception as e:
        return str(e)


if __name__ == '__main__':
    app.run(debug=True)

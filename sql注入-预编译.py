from django.http import HttpResponse
import MySQLdb

def secure_sql_call(request):
    # 假设这里获取了用户输入的数据，在实际场景中可能是从表单、URL参数等地方获取
    user_input = request.GET.get('input_param', '')

    try:
        conn = MySQLdb.connect(
            host="your_host",
            user="your_user",
            passwd="your_password",
            db="your_database"
        )
        cursor = conn.cursor()

        # 使用预编译语句，将SQL语句模板和参数分开传递
        sql = "SELECT * FROM some_table WHERE some_column = %s"
        cursor.execute(sql, (user_input,))

        results = cursor.fetchall()

        # 这里简单返回查询结果作为示例，实际可能需要更合理的处理和展示
        return HttpResponse("Results: {}".format(results))

    except Exception as e:
        return HttpResponse("Error: {}".format(str(e)))

    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()
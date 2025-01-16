from django.http import HttpResponse
import MySQLdb
def vulnerable_sql_call(request):
    # 假设这里获取了用户输入的数据，在实际场景中可能是从表单、URL参数等地方获取
    user_input = request.GET.get('input_param', '')

    sql = "SELECT * FROM some_table WHERE some_column = '{}'".format(user_input)

    try:
        conn = MySQLdb.connect(
            host="10.10.10.10",
            user="admin_user",
            passwd="12345678",
            db="mysql"
        )
        cursor = conn.cursor()
        cursor.execute(sql)
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
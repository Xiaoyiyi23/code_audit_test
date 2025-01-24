import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

// 该 Servlet 处理用户登录、注册和文件上传等操作，但存在多个安全漏洞
@WebServlet("/RiskyServlet")
public class RiskyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    // 处理 GET 请求，用于显示登录和注册页面
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>Login</h1>");
        response.getWriter().println("<form action='RiskyServlet' method='post'>");
        response.getWriter().println("Username: <input type='text' name='username'><br>");
        response.getWriter().println("Password: <input type='password' name='password'><br>");
        response.getWriter().println("<input type='submit' value='Login'>");
        response.getWriter().println("</form>");
        response.getWriter().println("<h1>Register</h1>");
        response.getWriter().println("<form action='RiskyServlet' method='post'>");
        response.getWriter().println("Username: <input type='text' name='newUsername'><br>");
        response.getWriter().println("Password: <input type='password' name='newPassword'><br>");
        response.getWriter().println("<input type='submit' value='Register'>");
        response.getWriter().println("</form>");
        response.getWriter().println("<h1>Upload File</h1>");
        response.getWriter().println("<form action='RiskyServlet' method='post' enctype='multipart/form-data'>");
        response.getWriter().println("<input type='file' name='file'><br>");
        response.getWriter().println("<input type='submit' value='Upload'>");
        response.getWriter().println("</form>");
        response.getWriter().println("</body></html>");
    }

    // 处理 POST 请求，包括登录、注册和文件上传逻辑
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String newUsername = request.getParameter("newUsername");
        String newPassword = request.getParameter("newPassword");
        Part filePart = request.getPart("file");

        if (username != null && password != null) {
            // SQL 注入漏洞：使用拼接的 SQL 语句，未对用户输入进行过滤
            String sql = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    // XSS 漏洞：直接将用户输入输出到页面，未进行转义
                    response.getWriter().println("<html><body>Welcome, " + username + "!</body></html>");
                } else {
                    response.getWriter().println("<html><body>Login failed.</body></html>");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (newUsername != null && newPassword != null) {
            // SQL 注入漏洞：同样使用拼接的 SQL 语句，未对用户输入进行过滤
            String sql = "INSERT INTO users (username, password) VALUES ('" + newUsername + "', '" + newPassword + "')";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql);
                // 密码明文存储漏洞：将用户密码以明文形式存储在数据库中
                response.getWriter().println("<html><body>Registration successful.</body></html>");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (filePart != null) {
            // 未经验证的文件上传漏洞：未对上传的文件进行类型和大小验证
            String fileName = filePart.getSubmittedFileName();
            String filePath = "uploads/" + fileName;
            try (InputStream inputStream = filePart.getInputStream();
                 OutputStream outputStream = new FileOutputStream(new File(filePath))) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                response.getWriter().println("<html><body>File uploaded successfully.</body></html>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

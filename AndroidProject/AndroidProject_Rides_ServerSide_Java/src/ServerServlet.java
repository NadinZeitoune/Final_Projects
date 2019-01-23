import java.io.IOException;

public class ServerServlet extends javax.servlet.http.HttpServlet {

    public static final int EXIST = 100;
    public static final int SUCCESS = 200;

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String action = request.getParameter("action");
        String userAsString;
        User user;

        switch (action){
            case "signUp":
                userAsString = request.getParameter("body");
                user = new User(userAsString);
                response.getWriter().write(String.valueOf(signUp(user)));
                break;
            case "login":
                userAsString = request.getParameter("body");
                user = new User(userAsString);
                response.getWriter().write("");
                break;

        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    private User login(User user){
        // Check username and password.

        return null;
    }

    private int signUp(User user){
        // Check if username exist.
        // If exist- return EXIST = 100
        if (SqlServer.searchUserNameForSignUp(user.getUserName()) != 0)
            return EXIST;

        // Else - create new user + return SUCCESS = 200
        SqlServer.insertUser(user);
        return SUCCESS;
    }
}

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ServerServlet extends javax.servlet.http.HttpServlet {

    public static final int EXIST = 100;
    public static final int SUCCESS = 200;
    public static final int ERROR = 404;
    public static final String DELIMITER = "!";

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String action = request.getParameter("action");
        String userAsString, rideAsString, detailsAsString;
        User user;
        Ride ride;
        JSONObject searchDetails;


        // Choose action.
        switch (action) {
            case "signUp":
                userAsString = request.getParameter("bodyUser");
                user = new User(userAsString);
                response.getWriter().write(String.valueOf(signUp(user)));
                break;

            case "login":
                userAsString = request.getParameter("bodyUser");

                user = new User(userAsString);
                User userResponse = login(user);
                if (userResponse == null) {
                    response.getWriter().write(ERROR);
                    break;
                }
                response.getWriter().write(userResponse.toString());
                break;

            case "addRide":
                rideAsString = request.getParameter("bodyRide");
                ride = new Ride(rideAsString);

                response.getWriter().write(String.valueOf(addRide(ride, request.getParameter("bodyUsername"))));
                break;
            case "search":
                detailsAsString = request.getParameter("bodySearch");
                try {
                    if (detailsAsString.equals("null"))
                        throw new JSONException("");
                    searchDetails = new JSONObject(detailsAsString);
                } catch (JSONException e) {
                    searchDetails = null;
                }

                response.getWriter().write(searchRides(searchDetails));
                break;
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    private String searchRides(JSONObject details) {
        // Get JSONObject and send it to the sql server + get back result set.
        ResultSet result = SqlServer.searchRides(details);

        // Transfer result set to stringBuilder. = new Ride(), DELIMITER, new Ride(), DELIMITER
        StringBuilder stringBuilder = new StringBuilder();

        try {
            while (result.next()){
                Ride ride = new Ride(result.getInt(1), result.getString(2), result.getString(3),
                        result.getString(4), result.getString(5), result.getInt(6),
                        result.getString(7), result.getString(8), result.getString(9),
                        result.getString(10), result.getString(11), result.getString(12));
                stringBuilder.append(ride.toString());
                stringBuilder.append(DELIMITER);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Delete the last and unnecessary DELIMITER.
        stringBuilder.deleteCharAt(stringBuilder.length());

        // Send back rides.
        return stringBuilder.toString();
    }

    private int addRide(Ride ride, String driver) {
        if (SqlServer.insertRide(ride, driver) != 1)
            return ERROR;
        return SUCCESS;
    }

    private User login(User user) {
        // Check username and password.
        return SqlServer.searchUsernameAndPasswordForLogin(user);
    }

    private int signUp(User user) {
        // Check if username exist.
        // If exist- return EXIST = 100
        if (SqlServer.searchUserNameForSignUp(user.getUserName()) != 0)
            return EXIST;

        // Else - create new user + return SUCCESS = 200
        SqlServer.insertUser(user);
        return SUCCESS;
    }
}

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class ServerServlet extends javax.servlet.http.HttpServlet {

    public static final int EXIST = 100;
    public static final int SUCCESS = 200;
    public static final int ERROR = 404;

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        String userAsString, rideAsString, detailsAsString;
        User user;
        Ride ride;
        JSONObject searchDetails = null;

        InputStream inputStream = request.getInputStream();
        byte[] buffer = new byte[1024];
        int actuallyRead;
        StringBuilder stringBuilder = new StringBuilder();
        while ((actuallyRead = inputStream.read(buffer)) != -1){
            stringBuilder.append(new String(buffer, 0, actuallyRead));
        }
        String action = request.getParameter("action");
        String[] parts = stringBuilder.toString().split("&");

        // Choose action.
        switch (action) {
            case "signUp":
                userAsString = extractBody("bodyUser", parts);
                user = new User(userAsString);
                response.getWriter().write(String.valueOf(signUp(user)));
                break;

            case "login":
                userAsString = extractBody("bodyUser", parts);
                user = new User(userAsString);
                User userResponse = login(user);
                if (userResponse == null) {
                    response.getWriter().write(ERROR);
                    break;
                }
                response.getWriter().write(userResponse.toString());
                break;

            case "addRide":
                rideAsString = extractBody("bodyRide", parts);
                ride = new Ride(rideAsString);

                response.getWriter().write(String.valueOf(addRide(ride, extractBody("bodyUsername", parts))));
                break;
            case "search":
                detailsAsString = extractBody("bodySearch", parts);

                try {
                    if (detailsAsString == null)
                        throw new JSONException("");
                    searchDetails = new JSONObject(detailsAsString);
                } catch (JSONException e) {
                    searchDetails = null;
                }finally {
                    response.getWriter().write(searchRides(searchDetails));
                    break;
                }
            case "searchDriver":
                userAsString = extractBody("bodyUsername", parts);
                response.getWriter().write(searchDriver(userAsString));
                break;
            case "searchPassenger":
                userAsString = extractBody("bodyUsername", parts);
                response.getWriter().write(searchPassenger(userAsString));
                break;
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    private String searchPassenger(String passenger){
        return SqlServer.searchPassenger(passenger);
    }

    private String searchDriver(String driver){
        return SqlServer.searchDriver(driver);
    }

    private String searchRides(JSONObject details) {
        // Get JSONObject and send it to the sql server + get back result set.
        return SqlServer.searchRides(details);
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

    private String extractBody(String whatToLook, String[] inside){

        for (String s : inside) {
            if (s.contains(whatToLook)) {
                String[] parts = s.split("=");
                if (parts.length != 2)
                    break;
                return parts[1];
            }
        }
        return null;
    }
}

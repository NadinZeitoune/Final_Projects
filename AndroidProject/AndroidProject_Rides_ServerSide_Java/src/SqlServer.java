import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;

public class SqlServer {

    public static final String DELIMITER = "!";

    public static int insertUser(User user) {
        // Get user details.
        String userName = user.getUserName();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String phoneNumber = String.valueOf(user.getPhoneNumber());
        int rowsAffected = 0;

        // Connect to mySql table and insert the new user.
        try (Connection conn = getConn()) {
            try (PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO users(username, password, first_name, last_name, phone_number) VALUES (?,?,?,?,?)")) {
                statement.setString(1, userName);
                statement.setString(2, password);
                statement.setString(3, firstName);
                statement.setString(4, lastName);
                statement.setString(5, phoneNumber);
                rowsAffected = statement.executeUpdate();
            } catch (MySQLIntegrityConstraintViolationException e) {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public static int searchUserNameForSignUp(String userName) {
        // Connect to mySql table.
        try (Connection conn = getConn()) {
            // Check if there is already username like param.
            try (PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM users WHERE username = ?")) {
                statement.setString(1, userName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    // If there is- send 1.
                    while (resultSet.next()) {
                        return 1;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static User searchUsernameAndPasswordForLogin(User user) {
        // Connect to mySql table.
        try (Connection conn = getConn()) {
            // Check if BOTH username and password are correct.
            try (PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?")) {
                statement.setString(1, user.getUserName());
                statement.setString(2, user.getPassword());

                try (ResultSet resultSet = statement.executeQuery()) {
                    // If they are - send full user details.
                    User newUser = getUserFromSet(resultSet);
                    if (newUser != null) return newUser;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int insertRide(Ride ride, String driver) {
        // Get ride details.
        String origin = ride.getOrigin();
        String destination = ride.getDestination();
        String departure = ride.getDeparture();
        String arrival = ride.getArrival();
        int passengersNum = ride.getNumOfPassengers();
        int rowsAffected = 0;

        // Connect to mySql table and insert the new ride.
        try (Connection conn = getConn()) {
            try (PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO ride_db.rides(origin, destination, departure, arrival, passenger_num, driver) VALUES (?,?,?,?,?,?)")) {

                statement.setString(1, origin);
                statement.setString(2, destination);
                statement.setString(3, departure);
                statement.setString(4, arrival);
                statement.setInt(5, passengersNum);
                statement.setString(6, driver);

                rowsAffected = statement.executeUpdate();
                return rowsAffected;
            } catch (MySQLIntegrityConstraintViolationException e) {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowsAffected;
    }

    public static String searchRides(JSONObject details) {
        // Connect to mySql table.
        try (Connection conn = getConn()) {
            // Select the rides according to the details OR null(everything).
            String sql;
            sql = "SELECT * FROM ride_db.rides WHERE ride_number LIKE ? AND departure LIKE ?" +
                    " AND arrival LIKE ? AND origin LIKE ? AND destination LIKE ?;";
            try (PreparedStatement statement = conn.prepareStatement(sql)){
                if (details == null){
                    statement.setString(1, "%%");
                    statement.setString(2, "%%");
                    statement.setString(3, "%%");
                    statement.setString(4, "%%");
                    statement.setString(5, "%%");
                }else {
                    statement.setString(1, "%"+details.getString("ride_id")+"%");
                    statement.setString(2, "%"+details.getString("departure")+"%");
                    statement.setString(3, "%"+details.getString("arrival")+"%");
                    statement.setString(4, "%"+details.getString("origin")+"%");
                    statement.setString(5, "%"+details.getString("destination")+"%");
                }

                try (ResultSet resultSet = statement.executeQuery()) {
                    // send back the resultSet as Ride[].
                    return resultSetToRideArr(resultSet).toString();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String searchDriver(String driver){
        // Connect to mySql table.
        try (Connection conn = getConn()){
            try (PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM ride_db.rides WHERE driver = ?")){
                statement.setString(1, driver);

                try (ResultSet resultSet = statement.executeQuery()){
                    // send back the resultSet as Ride[].
                    return resultSetToRideArr(resultSet).toString();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String searchPassenger(String passenger){
        // Connect to mySql table.
        try (Connection conn = getConn()){
            try (PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM ride_db.rides WHERE passenger1 = ? OR passenger2 = ? OR passenger3 = ?" +
                            " OR passenger4 = ? OR passenger5 = ?")){
                statement.setString(1, passenger);
                statement.setString(2, passenger);
                statement.setString(3, passenger);
                statement.setString(4, passenger);
                statement.setString(5, passenger);

                try (ResultSet resultSet = statement.executeQuery()){
                    // send back the resultSet as Ride[].
                    return resultSetToRideArr(resultSet).toString();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int joinRide(String rideNumber, String username){

        // Connect to mySql table.
        try (Connection conn = getConn()){
            // Get the passengers.
            try (PreparedStatement statement = conn.prepareStatement(
                    "SELECT passenger_num, passenger1, passenger2, passenger3, passenger4, passenger5 FROM ride_db.rides " +
                            "WHERE ride_number = ?")){
                statement.setString(1, rideNumber);
                try (ResultSet resultSet = statement.executeQuery()){
                    while (resultSet.next()){

                        // Check where is the free seat(passenger)
                        int i = 2;
                        do {

                            if(resultSet.getString(i) == null){
                                // Insert the new passenger to the spot located earlier.
                                try (PreparedStatement statement1 = conn.prepareStatement(
                                        "UPDATE ride_db.rides SET passenger"+(i-1)+" = ? WHERE ride_number = ?")){
                                    statement1.setString(1, username);
                                    statement1.setString(2, rideNumber);

                                    return statement1.executeUpdate();
                                }
                            }

                            i++;
                        }while (i <= resultSet.getInt(1) + 1);
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ServerServlet.ERROR;
    }

    public static int leaveRide(String rideNumber, String userName){
        // Connect to mySql table.
        try (Connection conn = getConn()){
            try (PreparedStatement statement = conn.prepareStatement(
                    "SELECT passenger_num, passenger1, passenger2, passenger3, passenger4, passenger5 FROM ride_db.rides " +
                            "WHERE ride_number = ?")){
                statement.setString(1, rideNumber);

                try (ResultSet resultSet = statement.executeQuery()){
                    while (resultSet.next()){

                        // Check where is the passenger.
                        int i = 2;
                        do {

                            if(resultSet.getString(i).equals(userName)){
                                // Bubble the remaining passengers  to be as left as they can.
                                StringBuilder sql = new StringBuilder("UPDATE ride_db.rides SET passenger"+(i-1)+" = NULL ");
                                int j = 1;
                                while ((j + i <= resultSet.getInt(1) + 1) && (resultSet.getString(i + j) != null)){
                                    sql.append(", passenger" + (i - j) + " = '" + resultSet.getString(i + j) + "'"
                                            + ", passenger" + (i - j + 1) + " = NULL");

                                    j++;
                                }

                                sql.append(" WHERE ride_number = '" + rideNumber + "'");

                                try (PreparedStatement statement1 = conn.prepareStatement(sql.toString())){
                                    return statement1.executeUpdate();
                                }
                            }

                            i++;
                        }while (i <= resultSet.getInt(1) + 1);
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return ServerServlet.ERROR;
    }

    public static User searchUser(String userName){
        // Connect to mySql table.
        try (Connection conn = getConn()) {
            // Check get the user details.
            try (PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM users WHERE username = ?")) {
                statement.setString(1, userName);

                try (ResultSet resultSet = statement.executeQuery()) {
                    User newUser = getUserFromSet(resultSet);
                    if (newUser != null) return newUser;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static User getUserFromSet(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            // Create user to return
            User newUser = new User(resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
            return newUser;
        }
        return null;
    }

    private static Connection getConn() throws SQLException {
        // Get connection to mySql schema.
        String connectionString = "jdbc:mysql://localhost:3306/ride_db?useSSL=false";
        String user = "nadin";
        String password = "NadinSql81196";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(connectionString, user, password);
    }

    private static StringBuilder resultSetToRideArr(ResultSet resultSet) {
        // Transfer result set to stringBuilder. = new Ride(), DELIMITER, new Ride(), DELIMITER
        StringBuilder stringBuilder = new StringBuilder();

        try {
            while (resultSet.next()) {
                Ride ride = new Ride(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6),
                        resultSet.getString(7), resultSet.getString(8), resultSet.getString(9),
                        resultSet.getString(10), resultSet.getString(11), resultSet.getString(12));
                stringBuilder.append(ride.toString());
                stringBuilder.append(DELIMITER);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Delete the last and unnecessary DELIMITER.
        if (stringBuilder.length() > 0)
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return stringBuilder;
    }
}

import java.io.Serializable;
import java.security.InvalidParameterException;

public class User implements Serializable {
    public static final String DELIMITER = "#";

    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private int phoneNumber;

    public User(String userName, String password, String firstName, String lastName, int phoneNumber) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public User(String userAsString){
        if (userAsString == null)
            throw new InvalidParameterException();

        String[] parts = userAsString.split(DELIMITER);
        if (parts.length != 5)
            throw new InvalidParameterException();

        this.userName = parts[0];
        this.password = parts[1];
        this.firstName = parts[2];
        this.lastName = parts[3];
        this.phoneNumber = Integer.valueOf(parts[4]);
    }

    @Override
    public String toString() {
        StringBuilder userAsString = new StringBuilder();

        // ToString user profile- user details.
        userAsString.append(userName).append(DELIMITER);
        userAsString.append(password).append(DELIMITER);
        userAsString.append(firstName).append(DELIMITER);
        userAsString.append(lastName).append(DELIMITER);
        userAsString.append(phoneNumber).append(DELIMITER);

        return userAsString.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof User){
            User user = (User) obj;
            return (this.userName == user.userName);
        }

        return false;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

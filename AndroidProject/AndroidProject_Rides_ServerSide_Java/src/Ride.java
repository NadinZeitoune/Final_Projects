
import java.security.InvalidParameterException;

public class Ride {
    public static final String DELIMITER = "#";
    private int rideNumber;
    private String origin;
    private String destination;
    private String departure;
    private String arrival;
    //private int freeSeatsNum;
    private int numOfPassengers;

    private String driver;
    private String passenger1;
    private String passenger2;
    private String passenger3;
    private String passenger4;
    private String passenger5;

    public Ride(int rideNumber, String origin, String destination,
                String departure, String arrival, int numOfPassengers,
                String driver, String passenger1, String passenger2,
                String passenger3, String passenger4, String passenger5) {
        this.rideNumber = rideNumber;
        this.origin = origin;
        this.destination = destination;
        this.departure = departure;
        this.arrival = arrival;
        this.numOfPassengers = numOfPassengers;
        this.driver = driver;
        this.passenger1 = passenger1;
        this.passenger2 = passenger2;
        this.passenger3 = passenger3;
        this.passenger4 = passenger4;
        this.passenger5 = passenger5;
    }

    public Ride(String origin, String destination, String departure, String arrival, int numOfPassengers) {
        this.origin = origin;
        this.destination = destination;
        this.departure = departure;
        this.arrival = arrival;
        this.numOfPassengers = numOfPassengers;
        //freeSeatsNum = numOfPassengers;
    }

    public Ride(String rideAsString){
        if (rideAsString == null)
            throw new InvalidParameterException();
        String[] parts = rideAsString.split(DELIMITER);

        if (parts.length != 6)
            throw new InvalidParameterException();

        // Ride details.
        this.rideNumber = Integer.valueOf(parts[0]);
        this.origin = parts[1];
        this.destination = parts[2];
        this.departure = parts[3];
        this.arrival = parts[4];
        this.numOfPassengers = Integer.valueOf(parts[5]);
        //this.freeSeatsNum = Integer.valueOf(parts[6]);
    }

    @Override
    public String toString() {
        StringBuilder rideAsString = new StringBuilder();

        // Ride details.
        rideAsString.append(rideNumber).append(DELIMITER);
        rideAsString.append(origin).append(DELIMITER);
        rideAsString.append(destination).append(DELIMITER);
        rideAsString.append(departure).append(DELIMITER);
        rideAsString.append(arrival).append(DELIMITER);
        rideAsString.append(numOfPassengers).append(DELIMITER);
        //rideAsString.append(freeSeatsNum).append(DELIMITER);

        return rideAsString.toString();
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public int getNumOfPassengers() {
        return numOfPassengers;
    }

    public void setNumOfPassengers(int numOfPassengers) {
        this.numOfPassengers = numOfPassengers;
    }
}

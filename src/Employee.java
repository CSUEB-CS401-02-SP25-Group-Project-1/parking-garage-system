public class Employee {
    private String id;
    private String username;
    private String password;
    private Garage garage;

    // Constructor
    public Employee(Garage garage) {
        this.garage = garage;
    }

    // Getter methods
    public String getID() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

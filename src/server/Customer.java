package ParkingLot;
import java.io.IOException;

public class Customer extends User{
	
	public Customer() {
		this.garage = null;
		this.userType = UserType.Customer;
	}
	
	public Customer(Garage garage) {
		this.garage = garage;
		this.userType = UserType.Customer;
	}
	

	public String viewReport() throws IOException {		//garage and report
		Report report = garage.viewReport();
		if(report != null && this.garage != null)
			return "\n\nAvailable Parking Spaces: " + garage.isFull() + "Garage Report Summary: \n" + report.toString();
		return "ERROR in Customer.viewReport ";
	}
}

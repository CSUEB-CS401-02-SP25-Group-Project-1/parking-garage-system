package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import mock.User;
import server.UserType;

public class UserTest 
{

	@Test
	public void testnewUser()
	{
		User user = new User();
		Assert.assertNull(user.getGarage());
		Assert.assertEquals(UserType.Undefined, user.getType());
	}
	
	@Test
	public void testGarageUser()
	{
		Garage garage = new Garage();
		User user = new User(garage);
		Assert.assertEquals(garage, user.getGarage());
		Assert.assertEquals(UserType.Undefined, user.getType());
	}
	
	@Test
	public void testSetGarage()
	{
		User user = new User();
		Garage garage = new Garage();
		user.setGarage(garage);
		Assert.assertEquals(garage, user.getGarage());
	}
	
	@Test
	public void testGetType()
	{
		User user = new User();
		Assert.assertEquals(UserType.Undefined, user.getType());
	}
	
	@Test
	public void testPrintReciept()
	{
		Garage garage = new Garage();
		User user = new User(garage);
		Ticket ticket = new Ticket();
		ticket.setPaid(true);
		garage.addTicket(ticket);
		user.printReceipt(ticket);
	}
}

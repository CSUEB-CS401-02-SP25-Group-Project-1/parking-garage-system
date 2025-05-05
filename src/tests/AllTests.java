package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({
    EarningTest.class,
    EmployeeTest.class,
    GarageTest.class,
    GateTest.class,
    ImageMessageTest.class,
    MessageTest.class,
    ReceiptTest.class,
    ReportTest.class,
    SecurityCameraTest.class,
    TicketTest.class,
    UserTest.class,
})

public class AllTests{
}

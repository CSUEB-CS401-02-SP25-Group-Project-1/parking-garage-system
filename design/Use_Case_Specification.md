# Use Case Specification
## Use Case 1: Obtain Parking Ticket
### Use Case ID: UC-01
### Use Case Name: Obtain Parking Ticket
### Relevant Requirements:

- 3.1.1.3: The system shall assign a unique ticket number to every vehicle entering a garage.

- 3.1.2.3.1: Customers shall receive a ticket with a unique number upon entering a garage.

- 3.1.3.2: The system shall update the vehicle count in real time as cars enter and exit the garage.

- 3.1.4.1: The system shall generate a unique ticket number for each vehicle entering the garage.

- 3.1.4.2: The system shall store the entry timestamp when a ticket is created.

### Primary Actor: 
- Customer

### Pre-conditions:
- The garage has available parking spaces.

- The entry gate, ticket printer, and network are fully functional.

### Post-conditions:
- The customer receives a valid parking ticket.

- The new ticket is added to the garage's active ticket list and the vehicle count is incremented.

- The entry gate opens.

### Basic Flow or Main Scenario:

1. The customer approaches the entry gate.

2. The system checks the garage's current vehicle count and capacity.

3. If space is available:
	1. the system generates a unique ticket with an entry timestamp.
	2. The ticket is added to the garage's active ticket list.

4. The system prints the ticket and opens the entry gate.

5. The customer takes the ticket and enters the garage.

6. The customer drives into the garage and the gate closes behind them.

7. The system update the vehicle count.

### Extensions or Alternate Flows:
- Alternate Flow 1: Garage Full
  - If the garage is full, the system displays a "No Available Spaces" message, leaves the entry gate closed and will direct the customer to another parking lot.

### Exceptions:
- Printer Failure: If the ticket printer fails, the system notifies maintenance staff and displays an error message to the customer.

- Network Failure: If the system cannot communicate with the central server, it displays an error message and prevents ticket generation.

### Related Use Cases:
- UC-03: Pay Parking Fee (Customer must have a valid ticket to pay upon exit.)

## Use Case 2: View Available Spaces

### Use Case ID: UC-02
### Use Case Name: View Available Spaces
### Relevant Requirements:

- 3.1.2.3.2: Customers shall be able to view the number of available spaces in a garage.

### Primary Actor: 
- Customer
### Pre-conditions: 
- None.

### Post-conditions: 
- The customer is informed of the garage's current availability.
  
### Basic Flow or Main Scenario:
1. The customer selects "View Available Spaces" on the interface.

2. The system retrieves the garage's current vehicle count and capacity.

3. The system calculates and displays the number of available spaces.

4. The customer views the information.

### Extensions or Alternate Flows: 
- None.
   
### Exceptions:
- Network Failure: If the system cannot communicate with the central server, it displays an error message.

### Related Use Cases:
- UC-01: Obtain Parking Ticket (Customer may request a ticket after viewing available spaces.)

## Use Case 3: Pay Parking Fee
### Use Case ID: UC-03
### Use Case Name: Pay Parking Fee
### Relevant Requirements:

- 3.1.1.5: The system shall generate a printed receipt when a customer completes their payment.

- 3.1.1.6: The system shall calculate each customer's parking fee based on the duration of their stay and the garage's fixed hourly rate.

- 3.1.2.3.3: Customers shall pay their calculated parking fee before exiting the garage.

- 3.1.2.3.4: The system shall verify the customer's ticket before processing payment.

- 3.1.2.3.6: The system shall automatically open the exit gate after the customer has completed payment.

- 3.1.4.3: The system shall update the exit timestamp when the customer leaves the garage.

- 3.1.4.4: The system shall calculate the parking fee based on the duration between the entry and exit timestamps and the garage's hourly rate.

- 3.1.4.6: The system shall validate a ticket before processing payment.

- 3.1.4.7: The system shall generate and print a receipt upon successful payment.

- 3.1.4.9: The system shall notify the customer if a ticket is invalid or has already been used.

### Primary Actor: 
- Customer

### Pre-conditions:
- The customer has a valid parking ticket.

- The ticket's entry timestamp is recorded and fee calculation numbers have been set.

### Post-conditions:
- The customer's payment is successfully processed.

- A receipt is generated and printed.

- The exit gate opens, and the vehicle count is decremented by one.

- Payment logs are updated.

### Basic Flow or Main Scenario:
1. The customer inserts their ticket into the payment terminal.

2. The system retrieves the entry timestamp associated with the ticket

3. The system calculate the parking fee using the garage's hourly rate.

4. The system displays the fee to the customer.

5. The customer confirms the fee and completes payment.

6. On successful payment:
	1. The system generates and prints a receipt with ticket ID, garage name, entry time, exit time, and fee charged.
	2. The exit timestamp is recorded.
	3. The system removes the paid ticket from the garage's active ticket list and decrements the vehicle count.

7. The system opens the exit gate.

8. The customer takes the receipt and exits the garage.

### Extensions or Alternate Flows:
- Alternate Flow 1: Payment Failure

  - If payment fails, the system displays an error message and prompts the customer to retry.

- Alternate Flow 2: Invalid Ticket

  - If the ticket is invalid, the system displays an error message and directs the customer to seek assistance.

### Exceptions:
- Printer Failure: If the receipt printer fails, the system notifies the customer and logs the issue.

- Gate Malfunction: If the exit gate fails to open, the system alerts the customer and notifies maintenance staff.

### Related Use Cases:
- UC-01: Obtain Parking Ticket (Customer must have a valid ticket to pay.)

- UC-05: Override Customer Fee (Employee may override the fee if there is an issue.)

## Use Case 4: Employee Login
### Use Case ID: UC-04
### Use Case Name: Employee Login
### Relevant Requirements:
- 3.1.2.4.1: Employees shall log in using a username and password.

### Primary Actor: Employee
### Pre-conditions:
- The employee has valid login credentials.

### Post-conditions:
- The employee gains access to the employee dashboard.

### Basic Flow or Main Scenario:
1. The employee enters their username and password.

2. The system verifies the credentials.

3. If valid, the system grants access to the employee dashboard.

### Extensions or Alternate Flows:
- Alternate Flow 1: Invalid Credentials

  - If the credentials are invalid, the system displays an error message and prompts the employee to retry.

### Exceptions:
- Network Failure: If the system cannot communicate with the central server, it displays an error message.

### Related Use Cases:
- UC-05: Override Customer Fee (Employee must log in to access this feature.)

- UC-07: View Garage Usage Report (Employee must log in to access this feature.)

## Use Case 5: Override Customer Fee
### Use Case ID: UC-05
### Use Case Name: Override Customer Fee
### Relevant Requirements:
- 3.1.2.4.2: Employees shall be able to override a customer's parking fee when necessary.

- 3.1.4.5: The system shall allow employees to generate new tickets manually for customers.

### Primary Actor: 
- Employee
### Pre-conditions:
- The employee is logged in.

- The customer has a valid ticket.

### Post-conditions:
- The customer's fee is updated to the manually set value.

### Basic Flow or Main Scenario:
1. The employee selects "Override Fee" on the dashboard.

2. The employee enters the customer's ticket number.

3. The system retrieves the current fee.

4. The employee enters the new fee amount.

5. The system updates the fee and logs the override action.

### Extensions or Alternate Flows: 
- None.
### Exceptions:
- Invalid Ticket: If the ticket is invalid, the system displays an error message.

### Related Use Cases:
- UC-03: Pay Parking Fee (Employee may override the fee during payment processing.)

## Use Case 6: Generate Parking Ticket
### Use Case ID: UC-06
### Use Case Name: Generate Parking Ticket
### Relevant Requirements:
- 3.1.2.4.3: Employees shall be able to generate a new parking ticket for customers when needed.

- 3.1.4.1: The system shall generate a unique ticket number for each vehicle entering the garage.

- 3.1.4.2: The system shall store the entry timestamp when a ticket is created.

- 3.1.4.5: The system shall allow employees to generate new tickets manually for customers.

### Primary Actor: 
- Employee
### Pre-conditions:
- The employee is logged in.

- The garage has available parking spaces.

### Post-conditions:
- The customer receives a valid parking ticket.

- The garage's vehicle count is incremented by one.

### Basic Flow or Main Scenario:
1. The employee selects "Generate Ticket" on the dashboard.

2. The system generates a unique ticket with an entry timestamp.

3. The system prints the ticket.

4. The employee hands the ticket to the customer.

5. The system updates the garage's vehicle count.

### Extensions or Alternate Flows: 
- None.
### Exceptions:
- Printer Failure: If the ticket printer fails, the system notifies the employee and logs the issue.

- Network Failure: If the system cannot communicate with the central server, it displays an error message.

### Related Use Cases:
- UC-01: Obtain Parking Ticket (Similar functionality for self-parking customers.)

## Use Case 7: View Garage Usage Report
### Use Case ID: UC-07
### Use Case Name: View Garage Usage Report
### Relevant Requirements:
- 3.1.2.4.6: Employees shall be able to access and view their garage's usage reports.

- 3.1.3.4: The system shall generate garage usage reports for employees on a daily, weekly, monthly, or yearly basis.

### Primary Actor: 
- Employee
### Pre-conditions:
- The employee is logged in.

### Post-conditions:
- The employee views the requested usage report.

### Basic Flow or Main Scenario:
1. The employee selects "View Usage Report" on the dashboard.

2. The employee selects the report type (daily, weekly, monthly, yearly).

3. The system retrieves the relevant data (total revenue, cars parked, peak hours).

4. The system displays the report to the employee.

### Extensions or Alternate Flows: 
- None.
### Exceptions:
- Network Failure: If the system cannot communicate with the central server, it displays an error message.

### Related Use Cases:
- UC-04: Employee Login (Employee must log in to access this feature.)

## Use Case 8: Modify Garage Fee Rate
### Use Case ID: UC-08
### Use Case Name: Modify Garage Fee Rate
### Relevant Requirements:
- 3.1.2.4.7: Employees shall be able to modify their garage's parking fee rate.

### Primary Actor: 
- Employee
### Pre-conditions:
- The employee is logged in.

### Post-conditions:
- The garage's parking fee rate is updated.

### Basic Flow or Main Scenario:
1. The employee selects "Modify Fee Rate" on the dashboard.

2. The employee enters the new fee rate.

3. The system updates the garage's fee rate.

4. The system logs the change.

### Extensions or Alternate Flows: 
- None.
### Exceptions:
- Invalid Input: If the employee enters an invalid fee rate, the system displays an error message.

### Related Use Cases:
- UC-04: Employee Login (Employee must log in to access this feature.)

## Use Case 9: Track Vehicle Entry/Exit
### Use Case ID: UC-09
### Use Case Name: Track Vehicle Entry/Exit
### Relevant Requirements:
- 3.1.1.4: The system shall log each vehicle's entry and exit timestamps for tracking and fee calculation.

- 3.1.3.2: The system shall update the vehicle count in real time as cars enter and exit the garage.

- 3.1.4.3: The system shall update the exit timestamp when the customer leaves the garage.

- 3.1.4.8: The system shall track and store all issued, paid, and expired tickets for auditing and reporting purposes.

### Primary Actor: 
- System
### Pre-conditions:
- The garage is operational.

### Post-conditions:
- The vehicle count and timestamps are updated.

### Basic Flow or Main Scenario:
1. A vehicle enters or exits the garage.

2. The system updates the garage's vehicle count.

3. The system logs the entry/exit timestamp.

### Extensions or Alternate Flows: 
- None.
### Exceptions:
- None.

### Related Use Cases:
- UC-01: Obtain Parking Ticket (Entry tracking is tied to ticket generation.)

- UC-03: Pay Parking Fee (Exit tracking is tied to payment processing.)

## Use Case 10: Handle Errors
### Use Case ID: UC-10
### Use Case Name: Handle Errors
### Relevant Requirements:
- 3.1.1.2: The system shall detect and notify the relevant user (customer or employee) when an error occurs such as an invalid ticket or network failure.

### Primary Actor: 
- System
### Pre-conditions:
- An error condition occurs (e.g., invalid ticket, network failure).

### Post-conditions:
- The relevant user is notified of the error with a clear message.
- The error is logged with details for troubleshooting
- The system may offer options to retry the errored operation.

### Basic Flow or Main Scenario:
1. The system detects an error during an operation (e.g., invalid ticket, network failure).

2. The system identifies the affected user (customer or employee).

3. The system displays an error message on the user's GUI indicating the type of error and next steps to take.

4. The system logs the error with details including error type, timestamp, and affected part of the system.

5. For errors that are recoverable, the system may automatically retry or prompt the user to try again.

### Extensions or Alternate Flows: 
- If the error persists after multiple retries or is critical, the system directs the user to contact support.
### Exceptions: 
- None.
### Related Use Cases:
- UC-03: Pay Parking Fee (Errors may occur during payment processing.)

- UC-05: Override Customer Fee (Errors may occur during fee override.)

## Use Case 11: Ensure System Security
### Use Case ID: UC-11
### Use Case Name: Ensure System Security
### Relevant Requirements:
- 4.1.1: When an employee creates a unique password, it must include special characters like '/', '*', etc., so it doesn't become a weak password.

### Primary Actor: 
- System
### Pre-conditions:
- The employee is creating or updating their password.

### Post-conditions:
- The password meets security requirements.

### Basic Flow or Main Scenario:
1. The employee enters a new password.

2. The system checks if the password includes special characters and meets complexity requirements.

3. If valid, the system updates the password.

4. If invalid, the system prompts the employee to create a stronger password.

### Extensions or Alternate Flows: 
- None.
### Exceptions: 
- None.
### Related Use Cases:
- UC-04: Employee Login (Password security is tied to employee login.)

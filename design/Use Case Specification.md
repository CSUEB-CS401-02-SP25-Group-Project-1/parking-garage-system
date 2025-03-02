# Use Case 1: Obtain Parking Ticket

**Use Case ID**: UC-01

**Use Case Name**: Obtain Parking Ticket

**Relevant Requirements**:

- 3.1.1.3: The system shall assign a unique ticket number to every vehicle entering a garage.

- 3.1.2.3.1: Customers shall receive a ticket with a unique number upon entering a garage.

- 3.1.3.2: The system shall update the vehicle count in real time as cars enter and exit the garage.

**Primary Actor**: Customer

**Pre-conditions**:
- The garage has available parking spaces.

- The entry gate system is operational.

**Post-conditions**:
- The customer receives a valid parking ticket.

- The garage's vehicle count is incremented by one.

**Basic Flow or Main Scenario**:

1. The customer approaches the entry gate.

2. The system checks the garage's current vehicle count and capacity.

3. If space is available, the system generates a unique ticket with an entry timestamp.

4. The system prints the ticket and opens the entry gate.

5. The customer takes the ticket and enters the garage.

6. The system updates the garage's vehicle count in real time.

**Extensions or Alternate Flows**:
- Alternate Flow 1: Garage Full
  - If the garage is full, the system displays a "No Available Spaces" message.

  - The entry gate remains closed, and the customer is directed to another garage.

**Exceptions**:
- Printer Failure: If the ticket printer fails, the system notifies maintenance staff and displays an error message to the customer.

- Network Failure: If the system cannot communicate with the central server, it displays an error message and prevents ticket generation.

**##Related Use Cases**:
- UC-003: Pay Parking Fee (Customer must have a valid ticket to pay upon exit.)

Use Case 2: View Available Spaces

Use Case ID: UC-02
Use Case Name: View Available Spaces
Relevant Requirements:
3.1.2.3.2: Customers shall be able to view the number of available spaces in a garage.

Primary Actor: Customer
Pre-conditions: None.
Post-conditions: The customer is informed of the garage's current availability.
Basic Flow or Main Scenario:
The customer selects "View Available Spaces" on the interface.

The system retrieves the garage's current vehicle count and capacity.

The system calculates and displays the number of available spaces.

The customer views the information.

Extensions or Alternate Flows: None.
Exceptions:
Network Failure: If the system cannot communicate with the central server, it displays an error message.

Related Use Cases:
UC-001: Obtain Parking Ticket (Customer may request a ticket after viewing available spaces.)

Use Case 3: Pay Parking Fee
Use Case ID: UC-003
Use Case Name: Pay Parking Fee
Relevant Requirements:
3.1.1.5: The system shall generate a printed receipt when a customer completes their payment.

3.1.1.6: The system shall calculate each customer's parking fee based on the duration of their stay and the garage's fixed hourly rate.

3.1.2.3.3: Customers shall pay their calculated parking fee before exiting the garage.

3.1.2.3.4: The system shall verify the customer's ticket before processing payment.

3.1.2.3.6: The system shall automatically open the exit gate after the customer has completed payment.

Primary Actor: Customer
Pre-conditions:
The customer has a valid parking ticket.

The customer is ready to exit the garage.

Post-conditions:
The customer's payment is successfully processed.

A receipt is generated and printed.

The exit gate opens, and the customer leaves the garage.

The garage's vehicle count is decremented by one.

Basic Flow or Main Scenario:
The customer inserts their ticket into the payment terminal.

The system retrieves the entry timestamp and calculates the fee.

The system displays the fee to the customer.

The customer completes the payment.

The system generates and prints a receipt.

The system opens the exit gate.

The customer takes the receipt and exits the garage.

The system updates the garage's vehicle count.

Extensions or Alternate Flows:
Alternate Flow 1: Payment Failure

If payment fails, the system displays an error message and prompts the customer to retry.

Alternate Flow 2: Invalid Ticket

If the ticket is invalid, the system displays an error message and directs the customer to seek assistance.

Exceptions:
Printer Failure: If the receipt printer fails, the system notifies the customer and logs the issue.

Gate Malfunction: If the exit gate fails to open, the system alerts the customer and notifies maintenance staff.

Related Use Cases:
UC-001: Obtain Parking Ticket (Customer must have a valid ticket to pay.)

UC-005: Override Customer Fee (Employee may override the fee if there is an issue.)

Use Case 4: Employee Login
Use Case ID: UC-004
Use Case Name: Employee Login
Relevant Requirements:
3.1.2.4.1: Employees shall log in using a username and password.

Primary Actor: Employee
Pre-conditions:
The employee has valid login credentials.

Post-conditions:
The employee gains access to the employee dashboard.

Basic Flow or Main Scenario:
The employee enters their username and password.

The system verifies the credentials.

If valid, the system grants access to the employee dashboard.

Extensions or Alternate Flows:
Alternate Flow 1: Invalid Credentials

If the credentials are invalid, the system displays an error message and prompts the employee to retry.

Exceptions:
Network Failure: If the system cannot communicate with the central server, it displays an error message.

Related Use Cases:
UC-005: Override Customer Fee (Employee must log in to access this feature.)

UC-007: View Garage Usage Report (Employee must log in to access this feature.)

Use Case 5: Override Customer Fee
Use Case ID: UC-005
Use Case Name: Override Customer Fee
Relevant Requirements:
3.1.2.4.2: Employees shall be able to override a customer's parking fee when necessary.

Primary Actor: Employee
Pre-conditions:
The employee is logged in.

The customer has a valid ticket.

Post-conditions:
The customer's fee is updated to the manually set value.

Basic Flow or Main Scenario:
The employee selects "Override Fee" on the dashboard.

The employee enters the customer's ticket number.

The system retrieves the current fee.

The employee enters the new fee amount.

The system updates the fee and logs the override action.

Extensions or Alternate Flows: None.
Exceptions:
Invalid Ticket: If the ticket is invalid, the system displays an error message.

Related Use Cases:
UC-003: Pay Parking Fee (Employee may override the fee during payment processing.)


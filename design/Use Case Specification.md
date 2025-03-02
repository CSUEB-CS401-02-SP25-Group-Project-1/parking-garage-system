Use Case 1: Obtain Parking Ticket

Use Case ID: UC-001

Use Case Name: Obtain Parking Ticket

Relevant Requirements:
3.1.1.3: The system shall assign a unique ticket number to every vehicle entering a garage.

3.1.2.3.1: Customers shall receive a ticket with a unique number upon entering a garage.

3.1.3.2: The system shall update the vehicle count in real time as cars enter and exit the garage.

Primary Actor: Customer

Pre-conditions:
The garage has available parking spaces.

The entry gate system is operational.

Post-conditions:
The customer receives a valid parking ticket.

The garage's vehicle count is incremented by one.

Basic Flow or Main Scenario:
The customer approaches the entry gate.

The system checks the garage's current vehicle count and capacity.

If space is available, the system generates a unique ticket with an entry timestamp.
The system prints the ticket and opens the entry gate.

The customer takes the ticket and enters the garage.

The system updates the garage's vehicle count in real time.

Extensions or Alternate Flows:
Alternate Flow 1: Garage Full

If the garage is full, the system displays a "No Available Spaces" message.

The entry gate remains closed, and the customer is directed to another garage.

Exceptions:
Printer Failure: If the ticket printer fails, the system notifies maintenance staff and displays an error message to the customer.

Network Failure: If the system cannot communicate with the central server, it displays an error message and prevents ticket generation.


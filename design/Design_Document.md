# Parking Garage System: Design Document
**Contributors:** Brandon P., Isaiah W., Andrew L., Prinay S., Paul M., and Riley G.
## Table of Contents
- [**Overview**](#overview)
- [**Context**](#context)
- [**Goals & Non-goals**](#goals--non-goals)
    - [**Goals**](#goals)
    - [**Non-goals**](#non-goals)
- [**Implementation Details**](#implementation-details)
    - [**User Class**](#user-class)
    - [**UserType Enum**](#usertype-enum)
    - [**Customer Class**](#customer-class)
    - [**Employee Class**](#employee-class)
    - [**Ticket Class**](#ticket-class)
    - [**TicketStatus Enum**](#ticketstatus-enum)
    - [**Garage Class**](#garage-class)
    - [**Reciept Class**](#reciept-class)
    - [**Report Class**](#report-class)
    - [**Message Class**](#message-class)
    - [**MessageType Enum**](#messagetype-enum)
    - [**ClientType Enum**](#clienttype-enum)
    - [**Server Class**](#server-class)
    - [**CustomerGUI Class**](#customergui-class)
    - [**EmployeeGUI Class**](#employeegui-class)
        - [**Login Screen**](#login-screen)
        - [**Dashboard Screen**](#dashboard-screen)
- [**Design Diagrams**](#design-diagrams)
    - [**UML Class Diagram**](#uml-class-diagram)
    - [**Sequence Diagram**](#sequence-diagram)
    - [**Use Case Diagram**](#use-case-diagram)
- [**Milestones / Timeline**](#milestones--timeline)
## Overview
This design document aims to eliminate any ambiguity surrounding the implementation of the Parking Garage System (PGS). It reiterates the original problem description, outlines the system's goals and non-goals based on the requirements and scope defined in the [Software Requirements Specification (SRS) document](SRS.md), and provides a clear breakdown of how each class and module should be implemented in Java. To support this, the document includes detailed design diagrams for visual reference and a project timeline with milestones to ensure a smooth and organized implementation cycle.
## Context
We are developing a graphical parking garage management system in Java that enables employees to monitor garage metrics, such as the current vehicle count, generate usage reports, and print tickets and receipts for customers. Customers also have the option to self-park via a separate graphical user interface (GUI) integrated into the system.

The system allows employees to configure fixed rate fees (by the hour) for individual garages and manually override a customer's final fee for when they are exiting the garage. Each customer's ticket is represented by the vehicle they park in the garage. Employees access their interface by logging in with a valid username and password.

The system handles network communication between the user (customers and employees alike) and their garage via TCP/IP through a threaded server capable of handling multiple client connections simultaneously.

## Goals & Non-goals
### Goals
Upon successful implementation of the Parking Garage System (PGS), the following goals will be achieved:
- **Employees** will be able to:
    - Log in to their GUI dashboard using their credentials (username and password).
    - View real-time statistics and activity logs for their assigned garage.
    - Generate usage reports detailing metrics such as revenue earned, vehicle count, and peak usage times.
    - Manage garage settings including adjusting the garage's hourly rate fee.
    - Print parking tickets and payment receipts for customers.
Manually override a customer's final parking fee when necessary.
- **Customers** will be able to:
    - Self-park using a user-friendly graphical interface.
    - View the current availability of parking spaces in their selected garage.
    - Receive a unique ticket upon entry and make a payment upon exit.
    - Obtain a printed receipt after completing their payment.
- **All users** will be able to interact with the system in a clear and efficient manner:
    - The system will display relevant error messages to guide users when issues occur.
    - It will handle errors gracefully to minimize user disruption.
    - It will support concurrent interactions through multithreading, allowing multiple users to use the system simultaneously without delay.
### Non-goals
- The system will not interact with or manage any external hardware or sensors such as air conditioning or surveillance systems.
- The system will not use encryption to secure transmitted messages over the network as encryption is explicitly prohibited by the project constraints.
- The system will not hash or encrypt user credentials; all credentials will be stored in plaintext.
- The system will not use standard data serialization formats like JSON or XML. Instead, a custom, human-readable format will be implemented for saving data to file.
- The system will not use any third-party databases, libraries, or frameworks. Only Java's built-in standard libraries will be used and all core functionality must be implemented from scratch by us (the development team).
## Implementation Details
### User Class
- Serves as the base class for both `Customer` and `Employee`, providing common attributes and methods used in both of those classes
- Defines common attributes such as the user's associated `Garage` and their `userType`
- All attributes and methods in this class are marked as `protected` as they are meant to be used in the child classes
- Upon instantiation, the `User` class sets its `userType` to `UserType.Undefined`; this value is later updated by the subclass (`Customer` or `Employee`)
- The `userType` is used by the `Server` to determine which kind of user is currently interacting with the system
- Has a method to assign a garage to the user
- Has a method to get the associated garage
- Has a method to get the user type
### UserType Enum
- `Customer`
- `Employee`
- `Undefined`: Default value before the user role is specified
### Customer Class
- Not to be confused with the `CustomerGUI`, this class is server side and does not handle input directly; it instead processes authenticated actions sent over the network via `Message` packets
- The `Customer` class does not handle outgoing messages directly; instead, the `Server` calls its methods and returns the result to the `CustomerGUI` as a `Message` packet over the network
- Inherits from the `User` class and, thus, becomes associated with a specific `Garage` upon initialization
- Customers do not have a unique user ID; the system uses ticket-based identification since actions like valet parking or shared ticket use are possible
- The `Customer` class is designed to facilitate and validate customer actions, such as parking, paying, and checking space availability
- Upon receiving a valid ticket ID, the system associates the ticket with the `Customer` object, allowing further actions like payment
- Has a method to request a new ticket, which returns the ticket's string ID if the garage has space available
- Provides a method to check the number of available spaces in the customer's assigned garage (returns an integer)
- Handles the payment process for the customer when exiting the garage, validating the ticket and updating its status and fee if appropriate
- Includes a method to generate a `Receipt` once a ticket is paid, which is returned to the `CustomerGUI` as a `Message`
### Employee Class
- Represents an employee's actions on the server side, separate from the `EmployeeGUI` which sends commands over the network via `Message` packets
- Similar to the `Customer` class, the `Employee` class does not handle outgoing messages directly; instead, the `Server` calls its methods and returns the result to the `EmployeeGUI` as a `Message` packet over the network
- Authenticates employee logins based on their plaintext credentials (`username` and `password`)
- Once an employee successfully logs in, the `isOnline` flag gets set to `true`, preventing multiple logins of the same user account at the same time
- After an employee logs out, `isOnline` gets set to `false`, reallowing logins from that user account
- Each employee has a unique string ID (e.g., "EM0", "EM1") generated on initialization
- Inherits from the `User` class and, thus, becomes associated with a specific `Garage` upon initialization
- Provides a method to override a ticket's fee, based on a given ticket ID and new fee amount
- Includes a method to generate a new ticket for a customer; returns the ticket ID if successful (fails if the garage is full)
- Provides a method to generate and return a `Receipt` for a paid ticket, which is formatted into a `Message` for the `EmployeeGUI`
- Supports generating a usage report (`Report`) for the employee's assigned garage, including:
    - Total revenue earned across time intervals (hour/day/week/month/year)
    - Peak revenue hours
    - Currently parked vehicles (active tickets)
- Has a method to modify the associated garage's hourly rate
### Ticket Class
- Represents both the customer's physical parking ticket and their currently-parked vehicle in the garage
- Is associated with a garage upon creation
- A ticket is created when a customer successfully parks their vehicle, either through self-parking or when an employee provides a ticket
- Tickets are not created if their associated garage is full
- Upon creation, `entryTime` is set to the current time
- When the customer is checking out, `exitTime` is set to the current time
- The system calculates the parking fee by multiplying the garage's fixed hourly rate by the total parking duration (`exitTime - entryTime`)
- If an employee manually overrides the fee, the `isOverridden` flag is set to `true`, which prevents the system from recalculating the fee automatically afterward
- Each ticket has a status (from the `TicketStatus` enum) that reflects its current phase in the parking lifecycle
- Once a ticket reaches the `Paid` status, it becomes invalid (unable to be reused or modified)
- Each ticket has a unique string ID (e.g., "TI0", "TI1"), generated from a system-wide counter (`count`)
- Tickets can be searched by ID within a garage's record, useful for:
    - Returning customers attempting to leave
    - Employees needing to look up and manage specific tickets
### TicketStatus Enum
- `Parking`: The vehicle has entered and is parked
- `Leaving`: The vehicle is about to leave, payment is being processed
- `Paid`: The payment has been completed
### Garage Class
- Represents a physical parking garage in the system
- Aggregates all tickets associated with the garage (vehicles currently parked)
- Maintains a list of active tickets, which allows the system to:
    - Track current occupancy
    - Look up tickets by their string ID (e.g., for returning customers or employees)
- A new ticket cannot be created if the garage is full (e.g., the number of active tickets equals the garage's capacity)
- The garage can report its number of available spaces using the formula: `capacity - ticketList.size()`
- Provides a method to check if the garage is currently full, returning a boolean value of the condition `capacity == ticketList.size()`
- Each garage has a unique string ID (e.g., "GA0", "GA1") generated at creation
- Garage instances are initialized with a fixed capacity, hourly rate, and garage name (separate from their ID)
- A garage's name can be renamed after initialization
- Stores a configurable hourly parking rate, which can be updated by an employee
- Tracks total revenue earned by the garage across all paid tickets
- When a new ticket is generated (and space is available), it is added to the garage's ticket list
- When a ticket is paid:
    - It gets removed from the garage's ticket list
    - Its fee gets added to the garage's total revenue
- Garage keeps track of its total revenue earned over the last hour, day, week, month, and year
- Garage stores its peak hour of usage (based on the highest revenue earned during any given hour)
### Reciept Class
- Represents a summary of a completed parking transaction, generated after a ticket is fully paid
- The receipt includes the following attributes:
    - The ticket ID
    - The garage name
    - The entry timestamp
    - The exit timestamp
    - The payment amount (final fee charged to the customer)
- The `Server` creates a `Receipt` object using information from the paid ticket, then formats it into a string using the `toString()` method
- This string is sent to the appropriate GUI (`CustomerGUI` or `EmployeeGUI`) via a `Message` object over the network
- The GUI parses the message payload back into a `Receipt` object (or simply displays the formatted string, depending on implementation)
- The `Receipt` class is strictly a data container, it does not perform any calculations or network communication
- The class constructor has all of the class attributes as its arguments to ensure it can be reconstructed reliably from the server's message payload
### Report Class
- Used by both the `Server` and `EmployeeGUI` to provide a real-time summary of garage performance
- Like the `Receipt` class, `Report` is a simply data container that does not perform any calculations or network communication
- Contains the following attributes, representing key operational metrics of the garage:
    - Revenue generated this hour
    - Revenue generated this today
    - Revenue generated this week
    - Revenue generated this month
    - Revenue generated this year
    - Total revenue earned since system start
    - Peak hour (hour with highest revenue)
    - Number of curently parked vehicles
- Includes a `toString()` method used by the `Server` to format the report into a string `Message` payload
- Depending on how the GUI is implemented, the `EmployeeGUI` either parses the string back into a `Report` object or directly displays the formatted string
### Message Class
- Enables communication between the `Server` and GUI clients (`CustomerGUI` and `EmployeeGUI`), used to send status updates, data payloads, and log messages
- Contains a type attribute (from the `MessageType` enum) that indicates the purpose of the message
- Contains attributes for sender `to` and reciever `from` which are specified by a `ClientType` enum for the server and GUI clients to know how to process the message for the original sender
- The message type helps both the `Server` and the GUI determine how to interpret and respond to the message (e.g., display a success confirmation or error prompt)
- Includes a text attribute, which stores the actual human-readable message content or payload string (e.g., a receipt, report, or feedback message)
- Includes a timestamp attribute that records when the message was created, set automatically at initialization
- Common methods include:
    - `getText()`: Returns the message content
    - `getType()`: Returns the message type
    - `getTimestamp()`: Returns the time the message was created
    - `getSender()`: Returns the message sender
    - `getReciever()`: Returns the message recipient
### MessageType Enum
- `Success`: Indicates that the user's action was successfully completed (e.g., payment processed, ticket generated)
- `Fail`: Indicates that the user's action could not be completed (e.g., garage full, invalid ticket ID)
- `Log`: Used for system-level logging (intended for the `Server` and `EmployeeGUI` to display the message's text or write it to a log file)
- `Data`: Represents a structured payload such as a `Receipt`, `Report`, or employee login credentials formatted as a string
- `Request`: Requests a specific action from Server (meant for the GUIs)
### ClientType Enum
- `EmployeeGUI`
- `CustomerGUI`
- `Server`: While not a client by itself, the server is still able to send and recieve messages from other clients
### Server Class
- Acts as the central controller of the system, receiving and processing all commands sent by the `CustomerGUI` and `EmployeeGUI`
- Authenticates user input, delegates requests to the appropriate objects (e.g., `Customer`, `Employee`, `Garage`), and returns an appropriate `Message` in response
- Maintains real-time multithreaded communication with all connected clients using individual threads for each socket connection
- Logs all system activity to a persistent file for auditing and troubleshooting purposes
- Sends real-time system logs to the `EmployeeGUI` through `Log`-type `Messages`, allowing employees to view internal events on their dashboard
- Handles data persistence by saving all essential objects (Garages, Tickets, and Employees) to file in a custom plaintext format
- Uses each of its objects' `toString()` methods for serialization, and loads data in a specific order to preserve ID consistency and object associations upon relaunch
- Is responsible for creating, interpreting, and routing all `Message` objects, including status updates, data responses (e.g., receipts, reports), and error messages
- Ensures thread safety and data integrity when modifying shared resources, such as garage capacity or ticket lists
- Contains in-memory references to all active users, tickets, and garages, and manages them across sessions
### CustomerGUI Class
- Represents the customer-facing graphical interface used during self-parking and checkout
- Provides a simplified, user-friendly interface for performing key actions without employee assistance
- Has a "Request Ticket" button:
    - Sends a `Request` message to the server requesting a new ticket
    - The server replies with either a `Success` message containing the ticket ID or a `Fail` message if the garage is full
    - The GUI then displays the ticked ID on-screen for the customer
- Has a "View Garage Availability" button:
    - Sends a `Request` message to the server requesting the number of available parking spaces
    - The server replies with a `Data` message containing the number of open spots
    - The GUI then displays the number of available parking spaces in that garage
- Has a "Pay Ticket" button:
    - Transitions the GUI to a new screen prompting the user to input their ticket ID
    - Sends a `Request` message to the server requesting to look up the ticket
    - If found, the server replies with a `Data` message containing ticket details (fee, time, etc.); otherwise, a `Fail` message is returned
    - The GUI displays the ticket information and prompts the customer to confirm payment
    - Upon confirmation, the GUI sends another `Request` message to the server to process payment
    - If successful, the server returns a `Receipt` (formatted from recieved `Data` message), which is displayed on-screen for the customer
- All server communication is handled using the `Message` class, and responses are parsed and interpreted by the GUI for user display
### EmployeeGUI Class
#### Login Screen
- Upon launching, displays a login screen with input fields for username and password
- When the employee clicks "Submit", the GUI sends credentials to the server for authentication:
    - If the credentials are valid, the server responds with a `Success` message and the GUI transitions to the dashboard screen
    - If invalid, the server returns a `Fail` message and the GUI displays an error prompt
#### Dashboard Screen
- "Override Ticket Fee" button
    - Opens a window to input a ticket ID and a new fee value
    - Sends the override request to the server
    - Displays confirmation or error message based on server response
- "Generate New Ticket" button (same functionality as in `CustomerGUI`)
    - Sends a request to generate a ticket
    - Displays ticket ID if successful; otherwise shows error if garage is full
- "Checkout / Pay Ticket" button (same functionality as in `CustomerGUI`)
    - Allows the employee to assist a customer with checkout
    - Sends a lookup request using a ticket ID, confirms payment, and displays the receipt
- "View Usage Report" button
    - Sends a report request to the server
    - Server returns a report (encapsulated in a `Data` message)
    - GUI displays the usage statistics on screen
- "Modify Garage Hourly Rate" button
    - Prompts for a new hourly rate input
    - Sends the new value to the server
    - Displays confirmation based on server response
- "View Server Logs" button
    - GUI receives real-time log updates via `Log`-type `Messages` sent from server
    - Displays logs in an immutable text area panel
- List of Parked Vehicles
    - Displays a real-time list of ticket IDs for all currently parked vehicles
    - The server broadcasts these periodically or upon change using `Data` messages
- Vehicle Count Display
    - GUI shows a live counter label of parked cars vs. garage capacity (e.g., "27/50")
    - Updated by the server as tickets are added or removed
## Design Diagrams
### UML Class Diagram
<img src="ClassDiagram.svg" alt="UML Class Diagram" width="600"/>

### Sequence Diagram
<img src="SequenceDiagram.svg" alt="Sequence Diagram" width="600"/>

### Use Case Diagram
<img src="UseCaseDiagram.svg" alt="Use Case Diagram" width="600"/>

## Milestones / Timeline
**TODO**

# Parking Garage System: Design Document
**Contributors:** Brandon P., Isaiah W., Andrew L., Prinay S., Paul M., and Riley G.
## Table of Contents
- [**Overview**](#overview)
- [**Context**](#context)
- [**Goals & Non-goals**](#goals--non-goals)
    - [**Goals**](#goals)
    - [**Non-goals**](#non-goals)
- [**Implementation Details**](#implementation-details)
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
#### Description
Represents a user interacting with the system. This class is not intended to be instantiated directly. Instead, the system will create `Customer` or `Employee` objects, which inherit from `User` and make use of its shared attributes and methods.
#### Relationships
- Inherited by: `Customer`, `Employee`
- Associated with: `Garage`
#### Attributes
- `protected UserType userType`: Indicates the user's role (`Customer`, `Employee`, or `Undefined`)
- `protected Garage garage`: The garage the user is currently associated with
#### Methods
- `protected User()`: Constructor that initializes `userType` to `UserType.Undefined`
- `public void setGarage(Garage garage)`: Assigns the user to a specific garage
- `public Garage getGarage()`: Returns the user's garage
- `public UserType getUserType()`: Returns the user's type
### UserType Enum
#### Description
Defines the possible user roles in the system. Used by the `User`, `Customer`, and `Employee` classes.
#### Values
- `Customer`
- `Employee`
- `Undefined`: Default value before the user role is specified
### Ticket Class
#### Description
Represents a customer's parking ticket, used to track their entry and exit timestamps, associated garage, and the final parking fee. Tickets are created upon entry and processed at exit. This class also supports fee calculation, manual fee overrides by employees, and status transitions (e.g., from `Parking` → `Leaving` → `Paid`).
#### Relationships
- Associated with: `Customer`, `Garage`
#### Attributes
- `private static int count`: Tracks the total number of tickets ever created by the system (used to generate unique IDs)
- `private String id`: Unique ticket ID (e.g., "TI0")
- `private Date entryTime`: Time the customer entered the garage
- `private Date exitTime`: Time the customer exited the garage
- `private double fee`: Final parking fee (either automatically calculated or manually overridden)
- `private boolean isOverridden`: Flag indicating whether the fee has been manually overridden
- `private Customer customer`: The customer associated with the ticket
- `private Garage garage`: The garage associated with the ticket
- `private TicketStatus status`: Current status of the ticket (`Parking`, `Leaving`, or `Paid`)
#### Methods
- `public Ticket(Customer customer, Garage garage)`: Initializes a new ticket with a unique ID based on current ticket count, sets `entryTime` to current time, sets `status` to `TicketStatus.Parking`, and associates the ticket with a customer and garage
- `public void setExitTime(Date exitTime)`: Sets the `exitTime` to current time and updates `status` to `TicketStatus.Leaving`
- `public void calculateFee()`: Calculates the fee as (`exitTime` - `entryTime`) in hours * garage's rate, will not recalculate if `fee` has been manually overridden
- `public void overrideFee(double newFee)`: Sets `isOverridden` to `true` and sets `fee` to the manually specified value
- `public void markAsPaid()`: Finalizes the ticket by setting `status` to `TicketStatus.Paid`
- `public double getFee()`: Returns the final fee
- `public String getID()`: Returns the unique ticket ID
- `public Date getEntryTime()`: Returns the timestamp of when the customer entered the garage
- `public Date getExitTime()`: Returns the timestamp of when the customer exited the garage
- `public Customer getCustomer()`: Returns the customer associated with the ticket
- `public Garage getGarage()`: Returns the garage associated with the ticket
- `public TicketStatus getStatus()`: Returns the ticket's current status
- `public void setCount(int count)`: Sets total number of tickets to specified value (useful for the server to load it from file)
- `public int getCount()`: Returns total number of tickets ever generated by the system (useful for the server to save it to file)
- `public boolean isValid()`: Checks if the ticket is valid (e.g., `status` is not `TicketStatus.Paid`)
### TicketStatus Enum
#### Description
Indicates the status of a parking ticket based on the customer's current progress in the garage.
#### Values
- `Parking`: Entry timestamp has been set, customer is parked
- `Leaving`: Exit timestamp has been set, customer is ready to pay
- `Paid`: Customer has already paid
## Design Diagrams
### UML Class Diagram
<img src="ClassDiagram.svg" alt="UML Class Diagram" width="600"/>

### Sequence Diagram
<img src="SequenceDiagram.svg" alt="Sequence Diagram" width="600"/>

### Use Case Diagram
<img src="UseCaseDiagram.svg" alt="Use Case Diagram" width="600"/>

## Milestones / Timeline
**TODO**

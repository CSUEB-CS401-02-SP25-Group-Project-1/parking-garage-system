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
This design document aims to eliminate any ambiguity surrounding the implementation of the Parking Garage System (PGS). It reiterates the original problem description, outlines the system’s goals and non-goals based on the requirements and scope defined by the [Software Requirements Specification (SRS) document](SRS.md), and provides a clear breakdown of how each class and module should be implemented in Java. To support this, the document includes detailed design diagrams for visual reference and a project timeline with milestones to ensure a smooth and organized implementation cycle.
## Context
We are developing a graphical parking garage management system in Java that enables employees to monitor garage metrics, such as the current vehicle count, generate usage reports, and print tickets and receipts for customers. Customers also have the option to self-park via a separate graphical user interface (GUI) integrated into the system.

The system allows employees to configure fixed rate fees (by the hour) for individual garages and manually override a customer’s final fee for when they are exiting the garage. Each customer is represented by the vehicle they park in the garage. Employees access their interface by logging in with a valid username and password.

The system handles network communication between the user (customers and employees alike) and their garage via TCP/IP through a threaded server capable of handling multiple client connections simultaneously.

## Goals & Non-goals
### Goals
Upon successful implementation of the Parking Garage System (PGS), the following goals will be achieved:
- **Employees** will be able to:
    - Log in to their GUI dashboard using their credentials (username and password).
    - View real-time statistics and activity logs for their assigned garage.
    - Generate usage reports detailing metrics such as revenue earned, vehicle count, and peak usage times.
    - Manage garage settings including adjusting the garage’s hourly rate fee.
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
- The system will not use any third-party databases, libraries, or frameworks. Only Java’s built-in standard libraries will be used and all core functionality must be implemented from scratch by us (the development team).
## Implementation Details
**TODO**
## Design Diagrams
### UML Class Diagram
<img src="ClassDiagram.svg" alt="UML Class Diagram" width="600"/>

### Sequence Diagram
<img src="SequenceDiagram.svg" alt="Sequence Diagram" width="600"/>

### Use Case Diagram
<img src="UseCaseDiagram.svg" alt="Use Case Diagram" width="600"/>

## Milestones / Timeline
**TODO**

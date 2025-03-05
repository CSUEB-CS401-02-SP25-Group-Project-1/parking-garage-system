# Software Requirements Specification

## 1\. Purpose

This document outlines the requirements for the Parking Garage System (PGS).

### 1.1. Scope

This document will catalog the user, system, and hardware requirements for the PGS system. It will not, however, document how these requirements will be implemented.

### 1.2. Definitions, Acronyms, Abbreviations

- **PGS**: Parking Garage System
- **TCP/IP**: Transmission Control Protocol/Internet Protocol
- **Employee**: a person working for a company holding a license of the PGS software
- **Customer**: a person who parks in a garage that utilizes the PGS system
- **OO**: Object-Oriented (Programming)
- **JSON**: JavaScript Object Notation **(irrelevant to application)**
- **XML**: Extensible Markup Language **(irrelevant to application)**

### 1.3. References

- [Use Case Specification Document](Use_Case_Specification.md)
- [UML Use Case Diagrams Document](UseCaseDiagram.svg)
- [Class Diagrams](ClassDiagram.svg)
- [Sequence Diagrams](SequenceDiagram.svg)

### 1.4. Overview

The Parking Garage System (PGS) is designed for companies to manage multiple parking garages simultaneously over the network using TCP/IP. The system will support two types of users: customers and employees, and each will have a separate graphical interface to interact with that is tailored to their specific needs. Generally, customers will use the PGS system for self-parking, receiving a ticket upon entry, and paying their final fee upon exit. Employees, however, will be using this system to manage the garage such as generating tickets and receipts to serve customers who do not want to self-park, process their payments manually, and overseeing that garage's activity such as viewing its current availability and overall statistics. The PGS system keeps track of every connected garage in real-time such as their current vehicle count, can process multiple customers' payments across different garages and once, and is able to generate usage reports of a particular garage that are only viewable to that garage's employees.

## 2\. Overall Description

### 2.1. Product Perspective

The PGS system is a standalone and self-contained application. It is not designed to be a dependency or module of a larger system.

### 2.2. Product Architecture

The system will be organized into 3 major modules: the User module, the Garage module, and the Ticket module. Additionally, the User module will have 2 submodules for each kind of user: Customer and Employee.

Note: System architecture should follow standard OO design practices.

### 2.3. Product Functionality/Features

The high-level features of the system are as follows (see section 3 of this document for more detailed requirements that address these features):

- **Multi-Garage Management:** Manage multiple garages across different locations through a centralized system via TCP/IP networking.
- **Employee Sign-in:** Employees log in with a username and password to access exclusive features such as overriding customers' fees and viewing usage reports of a particular garage.
- **Vehicle Tracking:** Tracks cars entering and exiting a garage in real time.
- **Timestamp Logging:** Records entry and exit times in order to calculate the final parking fee and track parking availability in a garage.
- **Reliable & Intuitive Interface:** Graphical interface is easy to understand for both customers and employees alike and is responsive.
- **Automatic Fee Calculation:** Calculates parking fees based on how long a car stays in a garage using its entry and exit timestamps.
- **Manual Fee Override:** Allows employees to manually override the system's calculated fee by setting a fixed value in case of an error or rebate.
- **Ticket & Receipt Generation:** Issues a unique ticket to a customer upon entry and prints a receipt while exiting containing their final fee and time spent in the garage.
- **Garage Usage Reports:** Provides employees with reports on total revenue earned, cars parked, and peak usage times of a particular garage.
- **Error Handling & Notification System:** System is able to detect issues such as invalid tickets or network failures and alerts the relevant party.

### 2.4. Constraints

The PGS system shall not use any additional databases, libraries, frameworks, and other technologies. Use of encryption for network communications is not allowed. Usage of data exchange formats such as JSON and XML for storing system data to file is also forbidden. Program must be written entirely in Java.

### 2.5. Assumptions and Dependencies

It is assumed that the companies using the PGS system will manage more than one garage and consist of multiple employees that will be interacting with this software from different locations at the same time. It is also assumed that there will be multiple customers attempting to self-park in different garages in different locations at the same time. Depending on the size of the company as well as how many customers would be interacting with the system across all garages, it is expected that the amount of people who will be using this program simultaneously would range from hundreds to thousands.

## 3\. Specific Requirements

### 3.1. Functional Requirements

#### 3.1.1. Common Requirements:

- **3.1.1.1.** The system shall provide real-time communication between multiple garages using TCP/IP networking.
- **3.1.1.2.** The system shall detect and notify the relevant user (customer or employee) when an error occurs such as an invalid ticket or network failure.
- **3.1.1.3.** The system shall assign a unique ticket number to every vehicle entering a garage.
- **3.1.1.4.** The system shall log each vehicle's entry and exit timestamps for tracking and fee calculation.
- **3.1.1.5.** The system shall generate a printed receipt when a customer completes their payment.
- **3.1.1.6.** The system shall calculate each customer's parking fee based on the duration of their stay and the garage's fixed hourly rate.

#### 3.1.2. User Module Requirements:

- **3.1.2.1.** The system shall support two types of users: customers and employees.
- **3.1.2.2.** Each user shall be associated with a specific garage.

##### 3.1.2.3. Customer Submodule Requirements:

- **3.1.2.3.1.** Customers shall receive a ticket with a unique number upon entering a garage.
- **3.1.2.3.2.** Customers shall be able to view the number of available spaces in a garage.
- **3.1.2.3.3.** Customers shall pay their calculated parking fee before exiting the garage.
- **3.1.2.3.4.** The system shall verify the customer's ticket before processing payment.
- **3.1.2.3.5.** The system shall provide customers the ability to self-park.
- **3.1.2.3.6.** The system shall automatically open the exit gate after the customer has completed payment.

##### 3.1.2.4. Employee Submodule Requirements:

- **3.1.2.4.1.** Employees shall log in using a username and password.
- **3.1.2.4.2.** Employees shall be able to override a customer's parking fee when necessary.
- **3.1.2.4.3.** Employees shall be able to generate a new parking ticket for customers when needed.
- **3.1.2.4.4.** Employees shall be able to print a receipt for a customer upon payment completion.
- **3.1.2.4.5.** Employees shall be able to view their assigned garage's total vehicle count in real time.
- **3.1.2.4.6.** Employees shall be able to access and view their garage's usage reports.
- **3.1.2.4.7.** Employees shall be able to modify their garage's parking fee rate.

### 3.1.3. Garage Module Requirements:

- **3.1.3.1.** Each garage shall have a unique ID, a total parking capacity, a current vehicle count, and a parking fee rate.
- **3.1.3.2.** The system shall update the vehicle count in real time as cars enter and exit the garage.
- **3.1.3.3.** The system shall track and store data on total revenue, total cars parked, and peak usage hours.
- **3.1.3.4.** The system shall generate garage usage reports for employees on a daily, weekly, monthly, or yearly basis.

### 3.1.4. Ticket Module Requirements:

- **3.1.4.1.** The system shall generate a unique ticket number for each vehicle entering the garage.
- **3.1.4.2.** The system shall store the entry timestamp when a ticket is created.
- **3.1.4.3.** The system shall update the exit timestamp when the customer leaves the garage.
- **3.1.4.4.** The system shall calculate the parking fee based on the duration between the entry and exit timestamps and the garage's hourly rate.
- **3.1.4.5.** The system shall allow employees to generate new tickets manually for customers.
- **3.1.4.6.** The system shall validate a ticket before processing payment.
- **3.1.4.7.** The system shall generate and print a receipt upon successful payment.
- **3.1.4.8.** The system shall track and store all issued, paid, and expired tickets for auditing and reporting purposes.
- **3.1.4.9.** The system shall notify the customer if a ticket is invalid or has already been used.

## 3.2. External Interface Requirements

- **3.2.1.** The system shall provide distinct graphical interfaces for customers and employees, assigning the appropriate interface based on user type.
- **3.2.2.** Each graphical interface shall be responsive and intuitive, requiring minimal user effort to understand.
- **3.2.3.** The customer's interface shall provide a menu with available actions such as obtaining a parking ticket, viewing available spaces, and making payments.
- **3.2.4.** After successful login, the employee's interface shall provide a dashboard with available commands such as viewing garage metrics, processing customer payments, and managing garage settings.
- **3.2.5.** The interface shall display relevant notifications for the user such as when an error occurs.

## 3.3. Internal Interface Requirements

- **3.3.1.** The internal interface shall use TCP/IP for communication and data exchange between each garage and the centralized system.
- **3.3.2.** When an employee requests their garage's usage report, the system shall retrieve and send the relevant data over to the employee's dashboard.
- **3.3.3.** For non-repudiation purposes, the system shall log all parking-related activities, tickets generated, payment processes, and employee logins.

## 4\. Non-Functional Requirements

### 4.1. Security and Privacy Requirements

- **4.1.1** When a employee creates a unique password it must include special characters like '/', '*', etc so it doesn't become a weak password
- **4.1.2** A gate that will allow vehicles to head to the designated parking area. This can only occur when the payment has finally been made. 
- **4.1.3** Surveillance cameras to monitor the parking garage. This is for any suspicious activity that needs to taken action. 

### 4.2. Environmental Requirements

- **4.2.1** Electrical energy to keep the garage’s power to be powered on so obviously everything can run smoothly. 

### 4.3. Performance Requirements

- **4.3.1** The number of parking spaces being available in each level. This will show the capacity to everyone so they'll know how many spaces are available.
- **4.3.2** Once the payment is done processing, the gate will slowly open. This will then lead them to the parking area in the garage. 
- **4.3.3** A vehicle being parked for the amount of hours it can stay in that spot but staying for too long will be charged with a fee and it will increase every hour.

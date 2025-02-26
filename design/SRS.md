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

*TODO*

### 1.4. Overview

The Parking Garage System (PGS) is designed for companies to manage multiple parking garages simultaneously over the network using TCP/IP. The system will support two types of users: customers and employees, and each will have a separate graphical interface to interact with that is tailored to their specific needs. Generally, customers will use the PGS system for self-parking, receiving a ticket upon entry, and paying their final fee upon exit. Employees, however, will be using this system to manage the garage such as generating tickets and receipts to serve customers who do not want to self-park, process their payments manually, and overseeing that garage's activity such as viewing its current availability and overall statistics. The PGS system keeps track of every connected garage in real-time such as their current vehicle count, can process multiple customers' payments across different garages and once, and is able to generate usage reports of a particular garage that are only viewable to that garage's employees.

## 2\. Overall Description

### 2.1. Product Perspective

The PGS system is a standalone and self-contained application. It is not designed to be a dependency or module of a larger system.

### 2.2. Product Architecture

The system will be organized into 2 major modules: the User module and the Garage module. Additionally, the User module will have 2 submodules for each kind of user: Customer and Employee.

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
- **Ticket & Receipt Generation:** Issues a unique ticket to a customer upon entry and prints a receipt while exiting containing their final fee and time spent in garage.
- **Garage Usage Reports:** Provides employees with reports on total revenue earned, cars parked, and peak usage times of a particular garage.
- **Error Handling & Notification System:** System is able to detect issues such as invalid tickets or network failures and alerts the relevant party.

### 2.4. Constraints

The PGS system shall not use any additional databases, libraries, frameworks, and other technologies. Use of encryption for network communications is not allowed. Usage of data exchange formats such as JSON and XML for storing system data to file is also forbidden. Program must be written entirely in Java.

### 2.5. Assumptions and Dependencies

It is assumed that the companies using the PGS system will manage more than one garage and consist of multiple employees that will be interacting with this software from different locations at the same time. It is also assumed that there will be multiple customers attempting to self-park in different garages in different locations at the same time. Depending on the size of the company as well as how many customers would be interacting with the system across all garages, it is expected that the amount of people who will be using this program simulatenously would range from hundreds to thousands.

## 3\. Specific Requirements

### 3.1. Functional Requirements

#### 3.1.1. Common Requirements:

- **3.1.1.1.** The system shall provide TCP/IP networking to allow real-time communication between multiple garages.
- **3.1.1.2.** The system shall support error detection and notify the relevant party (customers or employees) in case of issues (e.g. invalid tickets, network errors).
- **3.1.1.3.** The system shall assign a unique ticket number to every car entering a garage and log their timestamps of when they are entering and leaving that garage.
- **3.1.1.4.** The system shall print a receipt after the customer has paid their final fee so that an employee or terminal can give it to them when they are about to leave the garage.
- **3.1.1.5.** The system shall calculate each customers' parking fees based on time spent (in minutes) using the logged timestamps of their car in a garage and multiplied by that garage's fixed rate.

#### 3.1.2. User Module Requirements:

- **3.1.2.1.** The User module shall contain an enumeration value of the type of user (either customer or employee) and be able to manage child objects (customer or employee alike) in an array such as when the system needs to keep track of every user that is currently using the system.
- **3.1.2.2.** The User module shall contain the user's garage ID (in order for the system to know which garage the customer's car is parked in as well as what employee can access their garage's metrics).

##### 3.1.2.3. Customer Submodule Requirements:

- **3.1.2.3.1.** The Customer submodule shall contain the customer's ticket number and timestamps for garage entry and exit and represent the car being parked.
- **3.1.2.3.2.** The Customer submodule shall provide methods for the customer's actions such as entering and leaving the garage as well as the ability to self-park.

##### 3.1.2.4. Employee Submodule Requirements:

- **3.1.2.4.1.** The Employee submodule shall store the employee's credentials (username and password) in plaintext.
- **3.1.2.4.2.** The Employee submodule shall provide methods for the employee's actions such as viewing usage reports of their garage, overriding a customer's fee, changing their garage's rate, and generating the customer's ticket and receipt.

### 3.1.3. Garage Module Requirements:

- **3.1.3.1.** The Garage module shall store the garage's ID, its total capacity, current count of parked vehicles, and parking fee rate.
- **3.1.3.2.** The Garage module shall keep track of all vehicles parked in-real time and update its parked vehicle count accordingly.
- **3.1.3.3.** The Garage shall be able to provide the system its total revenue collected, total cars parked, and peak hours in a fixed time frame (e.g. weekly, monthly, annually).

## 3.2. External Interface Requirements

- **3.2.1.** The external interface shall manage both the customers' and employee's distinct graphical interfaces and assign one to them based on their user type.
- **3.2.2.** The interface shall be responsive and intuitive enough for users to understand almost immediately and not get confused or wait a significant amount of time while it handles its internal tasks.
- **3.2.3.** The customer's interface shall provide a menu giving them the ability to obtain a ticket upon entry, pay their final fee before exiting the garage and get their receipt, and view the number of available spaces in that garage.
- **3.2.4.** Once the employee signs in to the system using their username and password, the interface shall provide them a dashboard giving them the ability to view their garage's current capacity and view its usage reports, be able to print tickets and receipts for them to hand over to the customer, and override the customer's final fee when necessary.
- **3.2.5.** The interface shall display relevant notifications for the user such as when an error occurs (like, for the customer, when a garage is full or they have an invalid ticket; or, for all users, when a network error occurs).

## 3.3. Internal Interface Requirements

- **3.3.1.** The internal interface shall use TCP/IP for communication and data exchange between each garages and the centralized system (which the user has access to).
- **3.3.2.** When an employee requests to view their garage's usage report, the interface must retrieve those metrics from that garage and send it over to the employee's dashboard across the network.
- **3.3.3.** For non-repudiation purposes, the interface shall log all parking activity (tickets generated, receipts printed, timestamps of when a car enters and exits the garage, employee logins) in a log file for each garage.

## 4\. Non-Functional Requirements

### 4.1. Security and Privacy Requirements

- **4.1.1** When a employee creates a unique password it must include special characters like ‘/’, ‘*’, etc so it doesn’t become a weak password
- **4.1.2** A gate that will allow vehicles to head to the designated parking area. 
- **4.1.3** Surveillance cameras to monitor the parking garage.

### 4.2. Environmental Requirements

- **4.2.1** A garbage/recycling/organic bin in order to toss anything that corresponds to the right bin.
- **4.2.2** Air Control so that it can control the right amount of air needed. 
- **4.2.3** Electrical energy to keep the garage’s power to be powered on.

### 4.3. Performance Requirements

- **4.3.1** The number of parking spaces being available in each level.
- **4.3.2** Once the payment is done processing, the gate will slowly open.
- **4.3.3** A vehicle being parked for the amount of hours it can stay in that spot but staying for too long will be charged with a fee and it will increase every hour.

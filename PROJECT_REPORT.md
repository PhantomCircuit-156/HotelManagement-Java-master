# Mini Project Report
# Hotel Management System (LuxeStay)

**Submitted in partial fulfillment of the requirements for the degree of**  
**Bachelor of Engineering in Computer Science / Information Technology**  
**Academic Year 2026–2027**

---

### Prepared By:
- **Devansh & Team** (Roll No: 01, 02, 03, 04)

### Supervisor:
- **Project Guide / Professor In-Charge**

**Vidya Prasarak Mandal’s College Of Engineering**  
*“Jnanadweepa” Thane College Campus, Chendani Bunder Road, Thane–400601*  
*URL: [www.vpmthane.org](http://www.vpmthane.org)*

---

\newpage

# Section 1: Introduction

## 1.1 Background
The hospitality industry relies heavily on efficient guest handling, prompt room reservations, and transparent billing operations. In traditional or small-to-medium hospitality setups, room allocations, occupancy logs, and billing calculations are often performed manually or managed via disconnected spreadsheet registers. Such approaches frequently introduce record duplications, delayed status updates, billing computation errors, and poor visibility into current room inventory.

A centralized, automated web-based Hotel Management System resolves these operational bottlenecks by providing real-time room tracking, structured booking workflows, automated night-rate billing, and clear occupancy metrics for front-desk personnel and hotel administration.

## 1.2 Problem Statement
Manual hotel desk registers and spreadsheet-based room ledgers face substantial operational challenges:
- **Difficulty in Real-Time Tracking:** Determining whether a specific room (Single, Double, or Suite) is vacant or occupied requires manual inspection of physical logbooks.
- **Billing Errors:** Manual calculation of guest stay charges based on variable nightly rates and stay duration can lead to financial discrepancies during checkout.
- **Lack of Centralized Records:** Guest details and contact numbers are often lost across paper slips and disjointed records.
- **Operational Inefficiencies:** Front desk staff lack an integrated dashboard to view total rooms, current occupancy rates, and pending checkouts at a glance.

This project delivers a streamlined, lightweight Java web application that enables front-desk staff to register rooms, book guest stays with validation, view live room occupancy analytics, and execute checkouts with instant itemized bill receipts.

## 1.3 Objectives
- [x] Provide a centralized relational database schema for room inventory and active guest bookings.
- [x] Restrict system operations and room administration to authenticated hotel staff and managers.
- [x] Enable staff to register new rooms with customizable room numbers, types (Single, Double, Suite), and nightly rates.
- [x] Provide an interactive room booking engine that captures customer details and stay duration while preventing double-booking of occupied rooms.
- [x] Automate checkout and billing calculations (`nights × pricePerNight`) and generate instant itemized receipts.
- [x] Offer a live analytics dashboard featuring key performance indicators (KPIs) and an SVG-rendered room occupancy donut chart.
- [x] Demonstrate the practical application of **Jakarta Servlet 6.0, Apache Tomcat 10.1, JDBC, MySQL 8.x, and Vanilla JavaScript/CSS** in an enterprise-style decoupled web architecture.

## 1.4 Scope
### Included Features:
- **Staff Authentication:** Secure login screen with credential verification and session management.
- **Executive Dashboard:** Real-time KPI summary (Total Rooms, Available Rooms, Booked Rooms, Potential Revenue/Night) and dynamic SVG occupancy chart.
- **Room Inventory Management:** Module to register new rooms with unique room identifiers, room categories, and price validation.
- **Room Directory:** Tabular listing of all rooms displaying room numbers, types, price per night, booking status, and assigned guest.
- **Booking Engine:** Dropdown-based room selection filtering only currently available rooms, guest details capture, and stay duration specification.
- **Checkout & Instant Billing:** Fast checkout module with automated charge computation and modal billing receipt generation.
- **Automated Setup & Deployment:** Windows batch script (`run.bat`) that handles Tomcat deployment, JDBC driver resolution, Java compilation, and server startup.

### Excluded / Future Scope:
- Online self-booking customer portal (system is designed as an internal hotel front-desk suite).
- Payment gateway integration (credit cards, UPI, net banking).
- Multi-branch hotel chain support and role-based access control hierarchies (Manager vs. Front Desk vs. Housekeeping).

## 1.5 Intended Users
- **Front Desk Staff:** Performs daily guest check-ins, allocates available rooms, and processes guest checkouts with billing receipts.
- **Hotel Administrator / Manager:** Monitors overall room occupancy rates, potential nightly revenue, adds new room inventory, and oversees hotel operations.

---

\newpage

# Section 2: Requirements & Feasibility

## 2.1 Functional Requirements

| ID | Requirement | Expected Behaviour |
|:---|:---|:---|
| **FR-01** | Staff Authentication | Validate entered username and password against configured administrative credentials; grant dashboard access upon success. |
| **FR-02** | Executive Dashboard | Fetch real-time room records and compute Total Rooms, Available Rooms, Booked Rooms, and Potential Revenue per night; update the SVG donut chart and recent bookings table. |
| **FR-03** | Room Registration (`/api/add`) | Validate room number uniqueness, room type, and positive numeric price; persist the new room into MySQL with default status `'Available'`. |
| **FR-04** | Room Inventory Retrieval (`/api/rooms`) | Query the relational database and return all room objects in JSON format with status and guest details. |
| **FR-05** | Room Booking Engine (`/api/book`) | Allow selection of only vacant rooms; validate guest name, phone number, and nights; update room status to `'Booked'` and assign guest metadata. |
| **FR-06** | Guest Checkout & Billing (`/api/checkout`) | Validate that the selected room is currently `'Booked'`; calculate `totalBill = pricePerNight × numberOfNights`; reset room status to `'Available'` and return the bill breakdown. |
| **FR-07** | Staff Logout | Invalidate current user session, clear form states, and return staff to the secure authentication screen. |

## 2.2 Non-Functional Requirements
- **Usability:** Single-page dashboard interface styled with modern typography (Inter), glassmorphic card elements, clear color-coded status badges, and collapsible navigation sidebar.
- **Data Integrity:** Strict relational schema with primary key constraints on `room_number`; all database updates use JDBC `PreparedStatement` to ensure parameterized execution and prevent SQL injection.
- **Maintainability:** Clean separation of concerns between Model (`Room.java`), Controller (`HotelServlet.java`), Data Access Utility (`DBManager.java`), and Presentation Layer (`index.html` + `style.css`).
- **Compatibility:** Targets modern Jakarta EE 10 specifications (Jakarta Servlet 6.0) executing seamlessly on Apache Tomcat 10.1.x and Java SE 17+.
- **Performance & Light Footprint:** Zero third-party heavyweight JavaScript frameworks or ORM overhead; native Fetch API and vanilla CSS ensure sub-100ms response times.

## 2.3 Software & Hardware Requirements

| Component | Specification / Requirement | Purpose |
|:---|:---|:---|
| **Operating System** | Windows 10 / 11, Linux, or macOS | Host development and deployment environment. |
| **Java Development Kit (JDK)** | OpenJDK / Oracle JDK 17+ (JDK 17.0.20+ supported) | Compilation and execution of backend servlets. |
| **Web / Servlet Container** | Apache Tomcat 10.1.26 | Jakarta EE compliant servlet and static resource container. |
| **Database Server** | MySQL Community Server 8.0 / 8.4+ | Persistent relational storage for room records and bookings. |
| **JDBC Driver** | MySQL Connector/J 9.0.0 (`mysql-connector-j-9.0.0.jar`) | Database communication bridge between Java Servlets and MySQL. |
| **Client / Web Browser** | Google Chrome, Edge, Firefox, or Safari (Modern ES6+) | Rendering user interface, charts, and handling Fetch AJAX calls. |
| **IDE / Editor** | Visual Studio Code / IntelliJ IDEA / Eclipse | Source code authoring, debugging, and configuration. |

## 2.4 Feasibility Study
- **Technical Feasibility:** The project utilizes established, standard enterprise Java web technologies (Jakarta Servlets) backed by MySQL. Native browser standards (HTML5/CSS3/ES6) eliminate client-side dependencies.
- **Operational Feasibility:** The interface requires zero training for hotel staff; intuitive step-by-step navigation, dropdown room selectors, and automated billing eliminate human calculation errors.
- **Economic Feasibility:** The application is built entirely upon open-source software (OpenJDK, Apache Tomcat, MySQL Community Edition), requiring zero licensing fees.

---

\newpage

# Section 3: System Design

## 3.1 High-Level Architecture
The Hotel Management System follows a classic **3-Tier Client-Server Architecture** designed for decoupling, low latency, and rapid data flow:

```
+--------------------------------------------------------------------------+
|                       Presentation Layer (Browser)                       |
|   HTML5 UI  *  Vanilla CSS3 (Glassmorphism)  *  JavaScript Fetch API     |
|   SVG Occupancy Chart  *  Single-Page Application (SPA) Views            |
+--------------------------------------------------------------------------+
                                     ↕ HTTP POST / GET (JSON REST API)
+--------------------------------------------------------------------------+
|                     Controller Layer — Java Servlets                     |
|           HotelServlet.java (Jakarta Servlet 6.0 / Tomcat 10.1)          |
|    Routes: /api/rooms  |  /api/add  |  /api/book  |  /api/checkout       |
+--------------------------------------------------------------------------+
                                     ↕ JDBC Connections & PreparedStatements
+--------------------------------------------------------------------------+
|                  Data Access & Business Logic Layer                      |
|           DBManager.java (Connection Pool, Auto-Init, DAO Methods)       |
|           Room.java (Domain Entity & JSON / CSV Serialization)           |
+--------------------------------------------------------------------------+
                                     ↕ MySQL Protocol (Port 3306)
+--------------------------------------------------------------------------+
|                          Data Layer — MySQL Database                     |
|                 Database: hotel_db  |  Table: rooms                      |
+--------------------------------------------------------------------------+
```

## 3.2 Major Modules

| Module | Files Involved | Key Responsibilities |
|:---|:---|:---|
| **Authentication Module** | `index.html`, `style.css` | Handles staff login validation, toggle between login card and dashboard views, and session teardown on logout. |
| **Executive Dashboard** | `index.html`, `style.css` | Renders 4 high-level stat cards, dynamically calculates occupancy percentage, draws SVG donut graphic, and renders recent bookings. |
| **Room Management Module** | `index.html`, `HotelServlet.java`, `DBManager.java` | Collects room number, type, and nightly price; validates uniqueness; persists record into database via `INSERT`. |
| **Booking Engine** | `index.html`, `HotelServlet.java`, `DBManager.java` | Filters vacant rooms into dropdown, captures guest identity and duration, performs atomic update to `'Booked'` status. |
| **Checkout & Billing** | `index.html`, `HotelServlet.java`, `DBManager.java` | Identifies occupied room, computes stay charges, resets room status to `'Available'`, and formats itemized bill receipt. |
| **Database Manager (DAO)** | `DBManager.java` | Manages JDBC connection lifecycle, verifies/creates database schema on boot, and executes parameterized SQL statements. |

## 3.3 Request Processing Pattern
1. **Client Action:** The user performs an action on the UI (e.g., clicks *Confirm Booking*).
2. **AJAX Dispatch:** JavaScript formulates an asynchronous HTTP `POST` request payload using `URLSearchParams` / form-urlencoded encoding and dispatches it via `fetch('/api/book')`.
3. **Servlet Handling:** `HotelServlet` intercepts the request via `@WebServlet("/api/*")`, parses request parameters, and verifies constraints.
4. **Data Access Execution:** `HotelServlet` invokes `DBManager.bookRoom(...)` which acquires a JDBC connection and executes a parameterized `UPDATE` statement.
5. **JSON Response:** The servlet writes a structured JSON response (`{"success": "..."}` or `{"error": "..."}`) with HTTP status `200` or `400/500`.
6. **UI Update:** The client-side JavaScript receives the JSON response, shows a toast notification, and automatically triggers `loadDashboard()` to refresh stats and tables.

---

\newpage

# Section 4: Database Design

## 4.1 Database Overview
The application uses MySQL relational database named `hotel_db`. The primary table `rooms` manages room inventory, occupancy status, and current booking assignments in an integrated structure.

```
DATABASE: hotel_db
│
└── rooms
    ├── room_number       VARCHAR(20)    PRIMARY KEY
    ├── type              VARCHAR(20)    NOT NULL DEFAULT 'Single'
    ├── price_per_night   DECIMAL(10,2)  NOT NULL
    ├── status            VARCHAR(20)    NOT NULL DEFAULT 'Available'
    ├── customer_name     VARCHAR(100)   DEFAULT ''
    ├── customer_phone    VARCHAR(20)    DEFAULT ''
    └── number_of_nights  INT            DEFAULT 0
```

## 4.2 Data Dictionary — `rooms` Table

| Field Name | Data Type | Constraints | Default | Purpose & Description |
|:---|:---|:---|:---|:---|
| `room_number` | `VARCHAR(20)` | `PRIMARY KEY`, `NOT NULL` | — | Unique physical room identifier (e.g., '101', '204B'). |
| `type` | `VARCHAR(20)` | `NOT NULL` | `'Single'` | Category of room (`Single`, `Double`, or `Suite`). |
| `price_per_night` | `DECIMAL(10,2)` | `NOT NULL` | — | Nightly rental rate in Indian Rupees (₹). |
| `status` | `VARCHAR(20)` | `NOT NULL` | `'Available'` | Current availability state: `'Available'` or `'Booked'`. |
| `customer_name` | `VARCHAR(100)` | Nullable | `''` | Full legal name of the registered staying guest. |
| `customer_phone` | `VARCHAR(20)` | Nullable | `''` | Contact phone number of the registered guest. |
| `number_of_nights`| `INT` | Nullable | `0` | Total duration of stay booked for bill computation. |

## 4.3 Data Operations (CRUD)
- **Create (Add Room):** Staff submits room creation form; executed via `INSERT INTO rooms (room_number, type, price_per_night, status, customer_name, customer_phone, number_of_nights) VALUES (?,?,?,?,?,?,?)`.
- **Read (Load Rooms & Metrics):** Dashboard reads all records via `SELECT * FROM rooms`; filtered on client and server to segregate available vs. booked rooms.
- **Update (Book Room):** Transitions an available room to booked status: `UPDATE rooms SET status='Booked', customer_name=?, customer_phone=?, number_of_nights=? WHERE room_number=?`.
- **Update / Reset (Checkout):** Releases room and clears guest data: `UPDATE rooms SET status='Available', customer_name='', customer_phone='', number_of_nights=0 WHERE room_number=?`.

---

\newpage

# Section 5: Implementation & Deployment

## 5.1 Project Directory Structure

```
HotelManagement-Java-master/
├── db/
│   └── schema.sql                  # Database creation and table initialization script
├── src/
│   └── com/
│       └── hotel/
│           ├── model/
│           │   └── Room.java       # Domain model entity with JSON/CSV serialization
│           ├── servlet/
│           │   └── HotelServlet.java # Jakarta Servlet handling /api/* REST endpoints
│           └── util/
│               └── DBManager.java  # JDBC connection, auto-init, and DAO methods
├── webapp/
│   ├── WEB-INF/
│   │   └── web.xml                 # Jakarta EE 6 web application descriptor
│   ├── index.html                  # Single-page interface (Auth, Dashboard, Forms, Modals)
│   └── style.css                   # Responsive CSS styling, CSS variables, dark theme
├── lib/
│   └── mysql-connector-j-9.0.0.jar # MySQL JDBC Type 4 driver
├── apache-tomcat-10.1.26/          # Portable Tomcat web container
├── build/                          # Compiled classes and packaged webapp deployment target
├── run.bat                         # Automated 1-click build, compile, and run batch script
└── roadmap.md                      # Comprehensive execution and troubleshooting guide
```

## 5.2 Important Implementation Details
- **Zero External JSON Library Dependency:** The model class `Room.java` implements native `toJSON()` methods, stringifying data directly into standard JSON payloads, removing dependencies on heavy external libraries like Jackson or Gson.
- **Automatic Database Bootstrapping:** `DBManager.java` executes `ensureDatabaseExists()` synchronously on first connection, automatically creating `hotel_db` and the `rooms` table if not already present.
- **Security through Parameterized Statements:** All SQL queries use Java `PreparedStatement` with positional parameters (`?`), eliminating SQL injection risks.
- **Automated Deployment Pipeline:** The `run.bat` script automates environment variable validation, Tomcat verification, JDBC driver download, `javac` compilation targeting Jakarta Servlet API, artifact staging into `build/`, and live deployment into Tomcat's `webapps/ROOT/`.

## 5.3 Setup & Execution Summary
1. Ensure **MySQL Server 8.x** is installed and running on port 3306.
2. Verify or update the database password in [DBManager.java](file:///c:/Users/Devansh/Desktop/HotelManagement-Java-master/HotelManagement-Java-master/src/com/hotel/util/DBManager.java#L12) or set environment variables `DB_USER` and `DB_PASS`.
3. Open a terminal in the project directory and execute:
   ```cmd
   run.bat
   ```
4. The batch script detects JDK 17+, compiles the Java source code, copies web assets, and starts Tomcat on port 8080.
5. Access the application in any web browser at:
   ```
   http://localhost:8080
   ```

---

\newpage

# Section 6: User Workflows & System Interfaces

## 6.1 Workflow 1 — Staff Authentication & Executive Dashboard
The staff member arrives at the landing page and enters administrative credentials (`admin` / `admin123`). Upon successful authentication, the system transitions to the executive dashboard showcasing four high-level KPI cards (Total Rooms, Available Rooms, Booked Rooms, and Potential Revenue), an animated SVG room occupancy donut chart, and a table of active bookings.

```
+-------------------------------------------------------------------------------------------------------+
|  🏨 LuxeStay Management Suite                                                  [Admin - Manager] [⎋]  |
+-------------------------------------------------------------------------------------------------------+
|  [📊 Dashboard]   [Total Rooms: 12]  [Available: 8]  [Booked: 4]  [Potential Revenue: ₹24,500]        |
|  [➕ Add Room ]                                                                                       |
|  [🛏 View Rooms]   +------------------------------------+  +---------------------------------------+  |
|  [📋 Book Room ]   | Recent Bookings                    |  | Room Occupancy Breakdown              |  |
|  [🧾 Checkout  ]   | Room 101 - Rahul Sharma (2 nights) |  |             ((  33%  ))               |  |
|  [📑 Bookings  ]   | Room 204 - Priya Patel  (3 nights) |  |           Booked Occupancy          |  |
|                    +------------------------------------+  +---------------------------------------+  |
+-------------------------------------------------------------------------------------------------------+
```

## 6.2 Workflow 2 — Room Inventory Management (Add & View Rooms)
Staff can navigate to the *Add Room* section to expand hotel capacity. The form requires entering the Room Number (e.g., `301`), selecting the Room Type (`Single`, `Double`, or `Suite`), and setting the nightly rate (e.g., `₹2,500`). Clicking *Add Room* triggers validation and dispatches an asynchronous call to `/api/add`. Once registered, the room appears in the *View Rooms* directory with its real-time status.

```
+-------------------------------------------------------------------------------------------------------+
|  Add a New Room                                                                                       |
|  Room Number: [ 301           ]     Room Type: [ Suite      ▼ ]     Price / Night: [ ₹3500          ] |
|  [ ➕ Add Room ]  --> Toast Notification: "✅ Room 301 added successfully"                           |
+-------------------------------------------------------------------------------------------------------+
|  All Rooms Inventory                                                                                  |
|  +-------------+-------------+------------------+------------------+-------------------------------+  |
|  | Room No     | Type        | Price / Night    | Status           | Current Guest                 |  |
|  +-------------+-------------+------------------+------------------+-------------------------------+  |
|  | 101         | Single      | ₹1,500           | Booked           | Rahul Sharma                  |  |
|  | 102         | Double      | ₹2,500           | Available        | —                             |  |
|  | 301         | Suite       | ₹3,500           | Available        | —                             |  |
|  +-------------+-------------+------------------+------------------+-------------------------------+  |
+-------------------------------------------------------------------------------------------------------+
```

## 6.3 Workflow 3 — Room Booking Operation
When a guest arrives at the hotel front desk, staff navigates to *Book Room*. The system automatically populates the dropdown selector with **only vacant rooms**, eliminating the chance of double-booking. The staff enters the customer's full name, contact phone number, and number of nights. Submitting the form updates the room status to `'Booked'` in MySQL and increments the dashboard occupancy metric.

```
+-------------------------------------------------------------------------------------------------------+
|  Book a Room for Guest                                                                                |
|  Select Available Room: [ Room 102 (Double - ₹2500/night) ▼ ]    Number of Nights: [ 3              ] |
|  Customer Full Name:    [ Amit Kumar                       ]    Phone Number:     [ 9876543210      ] |
|  [ 📋 Confirm Booking ] --> Toast: "✅ Room 102 booked for Amit Kumar"                                |
+-------------------------------------------------------------------------------------------------------+
```

## 6.4 Workflow 4 — Guest Checkout & Instant Bill Generation
Upon departure, staff navigates to *Checkout*. The dropdown list displays all currently occupied rooms. Selecting a room (e.g., `101`) and clicking *Checkout & Generate Bill* invokes `/api/checkout`. The system automatically computes `totalBill = pricePerNight × nights`, resets the room status to `'Available'` for immediate re-booking, and renders an itemized billing receipt modal on screen:

```
+-------------------------------------------------------------------------------------------------------+
|  Checkout Guest                                                                                       |
|  Select Booked Room: [ Room 101 - Booked by Rahul Sharma ▼ ]                                          |
|  [ 🧾 Checkout & Generate Bill ]                                                                      |
+-------------------------------------------------------------------------------------------------------+
|  +-------------------------------------------------------------------------------------------------+  |
|  | 🧾 Bill Receipt                                                                                 |  |
|  | Guest Name:       Rahul Sharma                                                                  |  |
|  | Room Number:      101 (Single)                                                                  |  |
|  | Duration:         2 Nights                                                                      |  |
|  | Rate Per Night:   ₹1,500.00                                                                     |  |
|  | ----------------------------------------------------------------                                |  |
|  | TOTAL BILL:       ₹3,000.00                                                                     |  |
|  +-------------------------------------------------------------------------------------------------+  |
+-------------------------------------------------------------------------------------------------------+
```

---

\newpage

# Section 7: Limitations & Future Enhancements

## 7.1 Current Limitations
- **Demonstration Authentication:** The current system uses front-end/administrative demo credentials (`admin` / `admin123`) without multi-user role segregation (e.g., General Manager vs. Receptionist vs. Housekeeping).
- **Single Flat Relational Table:** Booking information is recorded directly on the `rooms` table row. Past booking records are overwritten during checkout rather than preserved in a historical reservations ledger.
- **Absence of Payment Gateway Integration:** The system calculates billing accurately but relies on external manual cash/card transactions rather than integrated digital payment gateways (Stripe, Razorpay).
- **Local Deployment Architecture:** Current deployment uses local Tomcat and local MySQL configurations rather than containerized cloud environments (Docker/Kubernetes).

## 7.2 Future Enhancements

```
+------------------------------------------+------------------------------------------+
|                 SECURITY                 |                 WORKFLOW                 |
| - BCrypt hashed password storage in DB   | - Dedicated historical `bookings` table  |
| - Role-based authorization filters       | - Date-range reservation calendar        |
| - Session tokens & CSRF token protection | - Integrated payment gateway (Razorpay)  |
+------------------------------------------+------------------------------------------+
|                USABILITY                 |               RELIABILITY                |
| - PDF receipt download and print engine  | - Automated JUnit integration tests      |
| - Filter rooms by price and amenities    | - Connection pool management (HikariCP)  |
| - Dark/Light mode user preference toggle | - Containerized deployment with Docker   |
+------------------------------------------+------------------------------------------+
```

### Strategic Objective:
Evolve the LuxeStay Hotel Management System from a high-efficiency academic demonstration into an enterprise-grade cloud-native property management system (PMS) featuring audit trails, multi-hotel property support, and online guest self-service reservations.

---

\newpage

# Section 8: Conclusion & References

## 8.1 Conclusion
The **Hotel Management System (LuxeStay)** successfully illustrates how modern Jakarta EE web architectures can simplify, accelerate, and organize hotel front-desk operations. By combining **Java Servlets, Apache Tomcat 10.1, JDBC PreparedStatement security, MySQL 8.x**, and a responsive **Single-Page Application frontend**, the system delivers:
1. Instantaneous room inventory tracking and prevention of double-booking.
2. Fast and automated billing computations eliminating cashier calculation errors.
3. Clean executive visibility through interactive KPI counters and SVG occupancy charts.
4. An automated, zero-friction local build and deployment workflow via Windows batch automation.

The system meets all academic project requirements and provides a robust, extensible foundation for enterprise hospitality workflows.

## 8.2 Project Repository & Local Deployment
- **Project Name:** Hotel Management System (LuxeStay Management Suite)
- **Local Access Endpoint:** `http://localhost:8080/`
- **Source Repository:** Local Workspace (`HotelManagement-Java-master`)
- **Key Files:**
  - Servlet Controller: `src/com/hotel/servlet/HotelServlet.java`
  - Database Manager: `src/com/hotel/util/DBManager.java`
  - Domain Model: `src/com/hotel/model/Room.java`
  - Web Interface: `webapp/index.html` & `webapp/style.css`

## 8.3 References
1. **Jakarta Servlet 6.0 Specification & API Documentation:**  
   [https://jakarta.ee/specifications/servlet/6.0/](https://jakarta.ee/specifications/servlet/6.0/)
2. **Apache Tomcat 10.1 Architecture and Configuration Guide:**  
   [https://tomcat.apache.org/tomcat-10.1-doc/](https://tomcat.apache.org/tomcat-10.1-doc/)
3. **Oracle Java Platform, Standard Edition Documentation (JDK 17/21):**  
   [https://docs.oracle.com/en/java/](https://docs.oracle.com/en/java/)
4. **MySQL 8.4 Reference Manual & JDBC Connector/J Developer Guide:**  
   [https://dev.mysql.com/doc/connector-j/en/](https://dev.mysql.com/doc/connector-j/en/)
5. **Mozilla Developer Network (MDN) — Fetch API and Modern Asynchronous Web Guidelines:**  
   [https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)

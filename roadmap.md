# 🏨 Hotel Management System — Run Roadmap

## Project Overview

| Component | Technology |
|---|---|
| **Backend** | Java Servlets (Jakarta EE) |
| **Server** | Apache Tomcat 10.1.26 |
| **Database** | MySQL (JDBC) |
| **Frontend** | Vanilla HTML + CSS |
| **Build** | Batch script ([run.bat](file:///d:/HotelManagement/run.bat)) |

The app exposes a REST-like API at `/api/*` (rooms, book, checkout) served by [HotelServlet.java](file:///d:/HotelManagement/src/com/hotel/servlet/HotelServlet.java), backed by MySQL via [DBManager.java](file:///d:/HotelManagement/src/com/hotel/util/DBManager.java).

---

## Prerequisites

### 1. ☕ Java JDK (17+)
- Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [Adoptium](https://adoptium.net/)
- Set `JAVA_HOME` environment variable to your JDK install path
- Verify:
  ```bash
  java -version
  javac -version
  ```

### 2. 🐬 MySQL Server (8.x recommended)
- Download from [MySQL Community Downloads](https://dev.mysql.com/downloads/mysql/)
- Install and **remember your root password**
- Ensure the MySQL service is **running**
- Verify:
  ```bash
  mysql -u root -p -e "SELECT 1"
  ```

> [!IMPORTANT]
> The project is hardcoded to connect with **user `root`** and **password `Om18@2007`** in [DBManager.java](file:///d:/HotelManagement/src/com/hotel/util/DBManager.java#L19-L20). If your MySQL root password is different, update line 20 before running.

---

## Step-by-Step: Running the Project

### Step 1 — Update the DB Password (if needed)

Open [DBManager.java](file:///d:/HotelManagement/src/com/hotel/util/DBManager.java#L20) and change:

```java
private static final String DB_PASS = "Om18@2007";  // ← put YOUR password
```

### Step 2 — (Optional) Seed the Database Manually

The app auto-creates the database and table on first startup. But if you want sample rooms pre-loaded, run the schema manually:

```bash
mysql -u root -p < d:\HotelManagement\db\schema.sql
```

Then uncomment the seed `INSERT` lines at the bottom of [schema.sql](file:///d:/HotelManagement/db/schema.sql#L28-L31) if desired.

### Step 3 — Run the Batch Script

```bash
d:\HotelManagement\run.bat
```

This single script does **everything** automatically:

| Step | What it does |
|------|-------------|
| **1/5** | Downloads & extracts Apache Tomcat 10.1.26 (first run only, ~12 MB) |
| **2/5** | Downloads MySQL JDBC driver jar (first run only, already present in `lib/`) |
| **3/5** | Compiles all Java source files (`Room.java`, `DBManager.java`, `HotelServlet.java`) |
| **4/5** | Packages the webapp (copies MySQL jar + static files into `build/`) |
| **5/5** | Deploys to Tomcat's `webapps/ROOT` and **starts the server** |

### Step 4 — Open the App

Once you see the Tomcat startup log in the console, open:

```
http://localhost:8080
```

The frontend ([index.html](file:///d:/HotelManagement/webapp/index.html)) loads and talks to the backend API.

---

## API Endpoints

| Method | Endpoint | Action |
|--------|----------|--------|
| `GET` | `/api/rooms` | List all rooms |
| `POST` | `/api/add` | Add a new room (`roomNumber`, `type`, `price`) |
| `POST` | `/api/book` | Book a room (`roomNumber`, `customerName`, `customerPhone`, `nights`) |
| `POST` | `/api/checkout` | Checkout a room (`roomNumber`) → returns bill |

---

## Stopping the Server

Press **Ctrl+C** in the terminal window running `run.bat`.

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| `javac` not found | Install JDK and set `JAVA_HOME` |
| `Compilation failed` | Ensure JDK 17+ (project uses Jakarta Servlet API, not `javax`) |
| `Database error` | Make sure MySQL is running and password in `DBManager.java` matches |
| Port 8080 in use | Kill the other process or change the port in `apache-tomcat-10.1.26/conf/server.xml` |
| Tomcat download fails | Check your internet connection; the script downloads from Apache archives |

---

## TL;DR — 3 Commands

```bash
# 1. Make sure MySQL is running, then:
mysql -u root -p -e "SELECT 1"

# 2. (If needed) Edit DB password in DBManager.java

# 3. Run everything:
d:\HotelManagement\run.bat

# 4. Open → http://localhost:8080
```

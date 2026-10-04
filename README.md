# Equity Report Utility

A Spring Boot REST service that reads the **Equity Trading App** PostgreSQL database
(customers and trades) and serves reports. All endpoints are documented and testable
in **Swagger UI**.

The utility is **read-only**: it never creates, changes or deletes tables or data in the
trading database.

| | |
|---|---|
| Language / runtime | Java 17 |
| Framework | Spring Boot 3.3 (Web, Data JPA, Validation) |
| Database | PostgreSQL 12 or newer |
| API docs | springdoc-openapi 2.6 (Swagger UI) |
| Build | Maven, via the included Maven Wrapper (no Maven install needed) |

---

## 1. Prerequisites

Install these on the computer first:

| Tool | Version | Check with |
|---|---|---|
| **JDK** | 17 or newer | `java -version` |
| **PostgreSQL** | 12 or newer (the scripts use a generated column) | `psql --version` |
| **Git** | any | `git --version` |
| Eclipse IDE *(optional)* | 2023-x or newer, "for Enterprise Java and Web Developers" | |

You do **not** need to install Maven. The project includes the Maven Wrapper (`mvnw` / `mvnw.cmd`),
which downloads the correct Maven version the first time it runs (internet access is needed once).

Make sure PostgreSQL is running and that you know a login (username and password)
that can create tables. A default installer or Docker setup usually uses `postgres` / `postgres`.

---

## 2. Get the code

```bash
git clone -b arijit https://github.com/arijitdeb1502/equity-report-utility.git
cd equity-report-utility
```

> The full project is on the `arijit` branch. `main` currently contains only the SQL scripts.

---

## 3. Create the database tables and sample data

The scripts are in [`sql/`](sql/) and must be run **in order**. By default the application
uses the database named `postgres`, but you can use any database (see step 4).

```bash
psql -U postgres -d postgres -f sql/01_create_customer_table.sql
psql -U postgres -d postgres -f sql/02_create_trade_table.sql
psql -U postgres -d postgres -f sql/03_insert_customer_data.sql   # 100 sample customers
psql -U postgres -d postgres -f sql/04_insert_trade_data.sql      # 1,148 sample trades
```

- Add `-h <host> -p <port>` if PostgreSQL is not on `localhost:5432`.
- To use a separate database instead, create it first (`createdb -U postgres equity_trading`) and
  replace `-d postgres` with `-d equity_trading`.
- **Without `psql`** (e.g. on Windows): open each file in **pgAdmin → Query Tool**, in the same order, and run it.
- All four scripts are safe to run again. Existing tables and rows are skipped.

Check that the data was loaded:

```bash
psql -U postgres -d postgres -c "SELECT (SELECT count(*) FROM customer) AS customers, (SELECT count(*) FROM trade) AS trades;"
```

Expected result: `100 | 1148`.

---

## 4. Configure the database connection

The connection is set in [`src/main/resources/application.properties`](src/main/resources/application.properties):

| Setting | Default | Override with environment variable |
|---|---|---|
| JDBC URL | `jdbc:postgresql://localhost:5432/postgres` | `DB_URL` |
| Username | `postgres` | `DB_USERNAME` |
| Password | `postgres` | `DB_PASSWORD` |

If those defaults match your PostgreSQL, skip to step 5. Otherwise, use **one** of these options:

**Option A: environment variables** (for the current terminal session)

```bash
# macOS / Linux
export DB_URL=jdbc:postgresql://localhost:5432/postgres
export DB_USERNAME=myuser
export DB_PASSWORD=mypassword
```

```powershell
# Windows PowerShell
$env:DB_URL="jdbc:postgresql://localhost:5432/postgres"
$env:DB_USERNAME="myuser"
$env:DB_PASSWORD="mypassword"
```

**Option B: a local config file** (permanent, and it also works when running from Eclipse)

Create `config/application.properties` in the project folder (next to `pom.xml`):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=myuser
spring.datasource.password=mypassword
```

Spring Boot reads this file automatically when the app is started from the project folder, and its
values override the defaults. The `config/` folder is in `.gitignore`, so your password is never committed.

> **macOS with Homebrew PostgreSQL:** there is usually no `postgres` user. The login is your
> macOS username and the password is empty, e.g. `spring.datasource.username=<your mac username>`
> and `spring.datasource.password=` (nothing after `=`).

---

## 5. Build and run the tests

```bash
./mvnw clean test          # macOS / Linux
mvnw.cmd clean test        # Windows
```

The first run downloads Maven and the dependencies, which takes a few minutes. Expected result: `BUILD SUCCESS`.

> The test `EquityReportUtilityApplicationTests` starts the whole application, so **the database
> from steps 3–4 must be reachable** for the tests to pass.

---

## 6. Run the application

**From the terminal:**

```bash
./mvnw spring-boot:run     # macOS / Linux
mvnw.cmd spring-boot:run   # Windows
```

Or build a JAR and run it:

```bash
./mvnw clean package
java -jar target/equity-report-utility-0.0.1-SNAPSHOT.jar
```

The application is ready when the log shows `Started EquityReportUtilityApplication`.
Stop it with `Ctrl+C`.

To use a different port: `./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081`
(or `java -jar target/equity-report-utility-0.0.1-SNAPSHOT.jar --server.port=8081`).

**From Eclipse:**

1. **File → Import → Maven → Existing Maven Projects** and select the project folder
   (or **General → Existing Projects into Workspace**, since the Eclipse project files are included).
2. Right-click the project → **Maven → Update Project** (downloads the dependencies).
3. Open `src/main/java/com/equity/reports/EquityReportUtilityApplication.java` →
   right-click → **Run As → Java Application**.
4. Stop it with the red ■ button in the **Console** view.

---

## 7. Use the API

| What | URL |
|---|---|
| **Swagger UI** (docs + "Try it out") | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |

### Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/api/v1/customers` | All customers, ordered by customer code (PAN and demat numbers are masked) |
| GET | `/api/v1/trades/by-volume` | All trades, highest volume (number of shares) first |
| GET | `/api/v1/reports/top-traders?from=YYYY-MM-DD&to=YYYY-MM-DD&limit=10` | Customers with the most executed/settled trades between two dates (inclusive). Tied customers share a rank. `limit` is optional (1–100, default 10). |

In Swagger UI, open an endpoint, click **Try it out**, then **Execute**. Or use curl:

```bash
curl http://localhost:8080/api/v1/customers
curl http://localhost:8080/api/v1/trades/by-volume
curl "http://localhost:8080/api/v1/reports/top-traders?from=2026-07-01&to=2026-09-30&limit=10"
```

---

## Project structure

```
equity-report-utility/
├── sql/                                   # PostgreSQL scripts (run in order, see step 3)
├── src/main/java/com/equity/reports/
│   ├── EquityReportUtilityApplication.java   # entry point
│   ├── config/        # Swagger / OpenAPI configuration
│   ├── controller/    # REST endpoints
│   ├── service/       # business logic (read-only transactions)
│   ├── repository/    # Spring Data JPA repositories
│   ├── entity/        # JPA mappings of the customer and trade tables (+ enums)
│   ├── dto/           # API response objects
│   └── exception/     # error handling (404 / 400 responses)
├── src/main/resources/application.properties
├── src/test/java/...  # tests
├── mvnw, mvnw.cmd, .mvn/   # Maven Wrapper
└── pom.xml
```

---

## Troubleshooting

| Error | Cause and fix |
|---|---|
| `Port 8080 was already in use` | Another copy of the app (or another program) is running. Stop it (`Ctrl+C` in its terminal, or on macOS/Linux `kill $(lsof -t -iTCP:8080 -sTCP:LISTEN)`), or run on another port (step 6). |
| `FATAL: password authentication failed for user "..."` | Wrong username or password. See step 4. |
| `FATAL: role "postgres" does not exist` | Your PostgreSQL has no `postgres` user (common with Homebrew on macOS). Set your own username (step 4). |
| `Connection to localhost:5432 refused` | PostgreSQL is not running, or is on another host/port. Start it, or set `DB_URL`. |
| `Schema-validation: missing table [customer]` | The tables don't exist in the database the app connects to. Run the scripts from step 3 against that database. |
| `Schema-validation: wrong column type ...` | The table definition differs from what the code expects. Recreate the tables with the scripts in `sql/`. |
| `release version 17 not supported` / `invalid target release: 17` | Maven is using an older JDK. Install JDK 17+ and point `JAVA_HOME` at it. |
| `./mvnw: Permission denied` | Run `chmod +x mvnw` once. |
| Eclipse shows errors on every import | Right-click the project → **Maven → Update Project** (tick *Force Update*). |

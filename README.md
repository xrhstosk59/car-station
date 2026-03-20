# Car Station

JavaFX desktop application for a car service station. The project covers customer, staff, and admin flows on top of a local SQLite database and is now organized as a standard Maven project.

## Tech Stack

- Java 21
- JavaFX 24
- SQLite
- Maven
- JUnit 5

## Features

- Role-based login for Admin, Staff, and Customer accounts
- Customer registration flow backed by SQLite
- Maintenance appointment booking
- Parking reservation flow
- Fuel ordering for customers and fuel replenishment for admins
- Parts ordering for admins and staff
- Profile, notifications, account directory, and vehicle-related screens

## Project Structure

- `src/main/java/car/station`: application source code
- `src/main/resources/car/station`: FXML views, CSS, and assets
- `src/test/java/car/station`: smoke, database, and UI flow tests
- `data/station.db`: local SQLite database used by the app

## Demo Accounts

The bundled database already includes sample users:

- `Director / Diri123!` for the admin flow
- `Empy / Loid456!` for the staff flow
- `Tomy / Cuss789!` for the customer flow

## Requirements

- JDK 21
- Maven 3.9+ recommended

## Run Locally

From the project root:

```bash
mvn javafx:run
```

If you want a clean local Maven cache, this also works:

```bash
mvn -Dmaven.repo.local=/tmp/car-station-m2 javafx:run
```

## Build

```bash
mvn clean package
```

The packaged application jar is created under `target/`.

## Tests

Run the full suite with:

```bash
mvn test
```

The automated checks currently include:

- Login tests for admin, staff, and customer accounts
- Registration success, duplicate-user, and empty-form scenarios
- FXML smoke loading for every view in `src/main/resources/car/station`
- Maintenance appointment persistence
- Customer profile data binding
- Notifications loading without duplicate menu items
- Parking reservation, invalid selection, and no-availability scenarios
- Fuel ordering, invalid selection, and admin over-capacity scenarios
- Account directory visibility rules
- Parts ordering, quantity pricing, and stock guard scenarios
- Core navigation smoke tests with logout for all three roles

The UI flow tests run against a temporary copy of `data/station.db`, so the real project database is not mutated during test execution.

## Verified Status

At the current state of the repository, the following commands pass:

```bash
mvn compile
mvn test
mvn package
```

## Notes

- The project uses JavaFX on the module path via Maven.
- A runtime warning from `javafx-graphics` about `sun.misc.Unsafe` may still appear on newer JDKs. That warning comes from the JavaFX graphics library itself, not from this project's source code.

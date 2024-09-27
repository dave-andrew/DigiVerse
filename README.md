# DigiVerse
DigiVerse is a Google Classroom clone with an integrated auto-scoring system, built using JavaFX. It provides a user-friendly interface for managing online classrooms, assignments, and grading. The application automates the scoring process, streamlining the workload for educators.

## Features
- Classroom Management: Create and manage classrooms with ease.
- Assignment Submission: Students can submit their assignments directly through the platform.
- Auto-Scoring: Automatically score student assignments based on predefined criteria.
- User Interface: Built using JavaFX to ensure a smooth and responsive user experience.
- Admin & User Roles: Support for multiple user roles (teacher and student).

## Prerequisites
Java 11: Ensure that Java 11 is installed and set as your default Java version. You can download Java 11 from the Oracle website.

### Verify your Java version using the command:

```bash
Copy code
java -version
Gradle: This project uses Gradle for dependency management. You don't need to install Gradle manually as it comes with a wrapper included in the project.
```

### Getting Started
1. Clone the repository
Clone this repository to your local machine using Git:

```bash
Copy code
git clone https://github.com/your-username/DigiVerse.git
cd DigiVerse
```

2. Build the project
Build the project and resolve all dependencies using Gradle:

```bash
Copy code
./gradlew build
This will automatically download and install all necessary dependencies required to run DigiVerse.
```
3. Run the application
After building the project, you can execute the application using the Main.java file.

```bash
Copy code
./gradlew run
Alternatively, you can manually run the application using the following command:
```
```bash
Copy code
java -cp build/classes/java/main Main
```
4. Directory Structure
src/main/java - Contains the source code of the application.
src/main/resources - Holds the resources such as FXML files, icons, and stylesheets.
5. Contributing
We welcome contributions to improve DigiVerse! If you'd like to contribute:

Fork the repository.
Create a new branch (git checkout -b feature-branch).
Make your changes.
Commit your changes (git commit -m 'Add new feature').
Push to the branch (git push origin feature-branch).
Create a pull request.

==
Java Code Challenge
==

Welcome! This challenge is designed to test your real-world coding and
troubleshooting skills using a stack similar to ours (Java 21, Payara, JDBC, Docker).

The project is a simple Task Management REST API. It's mostly functional but
contains a few hidden bugs and is missing one key feature. Your goal is to
fix the bugs and implement the new feature.

--------------------------------------------------------------------------------
## Step 1: Project Setup
--------------------------------------------------------------------------------

1.  **Prerequisites:**✅ Ensure you have Docker, Java 21, and Maven installed on your machine.

2.  **Build the Project:** ✅Open a terminal in the project's root directory and run the
    following Maven command to build the application:
    mvn clean install

3.  **Start the Environment:**✅ Use Docker Compose to start the Payara application
    server and the MySQL database.
    docker-compose up

    This will start the application, which will be accessible at:❌
    http://localhost:8199/task-manager/api/

--------------------------------------------------------------------------------
## Step 2: Task 1 - Troubleshoot the "Silent Failure" Bug
--------------------------------------------------------------------------------

**Scenario:**
A feature was implemented to create an audit log every time a task is viewed.
However, our QA team has noticed that the `audit_log` table in the database
is always empty, even after fetching tasks multiple times.

**Symptom:**
When you make a GET request to fetch a specific task, the API returns a `200 OK`
response with the correct task data, but no audit record is created. This
failure is "silent" to the API consumer.

**Your Goal:**
1.  Investigate why the `audit_log` is not being written to the database.
2.  Identify the root cause.
3.  Fix the bug.

**How to Reproduce:**
- Make a GET request to an endpoint that fetches a single task with a long title,
  for example: `http://localhost:8199/task-manager/api/tasks/11`
- Check the `audit_log` table in the `taskdb` database. It will be empty.
- Check the logs of the `task-app` container for error messages (`docker logs task-app`).

--------------------------------------------------------------------------------
## Step 3: Task 2 - Troubleshoot the "Incorrect Data" Bug
--------------------------------------------------------------------------------

**Scenario:**
The endpoint for fetching a single task by its ID has a strange bug.

**Symptom:**
The endpoint works correctly for tasks with small IDs (e.g., 1, 9, 10). However,
when you request a task with a larger ID (e.g., 130), the API returns a
`404 Not Found` error, even though the task exists in the database.

**Your Goal:**
1.  Identify the logic error that causes this behavior.
2.  Fix the bug so that tasks with any valid ID can be retrieved successfully.

**How to Reproduce:**
- Request a task with a low ID: `GET http://localhost:8199/task-manager/api/tasks/9` (This will work).
- Request a task with a high ID: `GET http://localhost:8199/task-manager/api/tasks/130` (This will fail with a 404).

--------------------------------------------------------------------------------
## Step 4: Task 3 - Implement a New Feature
--------------------------------------------------------------------------------

**User Story:**
"As a manager, I want to be able to generate an on-demand summary report of the
current state of all tasks. This report should be stored in the database for
historical tracking."

**Your Goal:**
Implement a new API endpoint that generates and saves a task summary.

**Requirements:**
1.  **New Table:** Ensure the `task summaries` table exists (the `CREATE TABLE`
    statement is already in `db/init.sql`).
    - Columns: `id`, `completed_count`, `pending_count`, `total_tasks`, `generated_at`

2.  **New Endpoint:** Create a `POST` endpoint at `/api/summaries`.

3.  **Logic:** When this endpoint is called, it must:
    a. Query the `tasks` table to calculate the total number of completed and
       pending tasks.
    b. Insert a new row into the `task_summaries` table with these counts.

4.  **Response:** The endpoint should return a `201 Created` status code and a JSON
    body representing the newly created summary record.
	
--------------------------------------------------------------------------------
## Step 5: Performance and Load Testing
--------------------------------------------------------------------------------

This project includes the performance-test.js script and the k6.exe tool to
help you diagnose and verify the impact of the bugs. This is especially useful
for identifying resource leaks.

**How to Run the Test**
1.	Make sure the application is running via `docker compose up`
2.	Open a new terminal in the project root and run the following command `k6 run performance-test.js`

--------------------------------------------------------------------------------
## Final Deliverables
--------------------------------------------------------------------------------

When you are finished, please provide one of the following:
- A link to a Git repository containing your modified code.
- A .zip file of the project with your changes.

Be prepared to discuss the changes you made, the reasons for the bugs, and your
approach to implementing the new feature.

Good luck!
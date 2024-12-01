
Project Title: LOAN-MANAGEMENT

Description:
The project basically calculate and allocate a loan under specific conditions

Prerequisites:

Java Development Kit (JDK): 19
Build Tool: maven
Installation:

Clone the Repository:
Bash
git clone https://github.com/tanercakiroglu/loan-management.git


Build the Project:
Maven:
Bash
cd [project-directory]
mvn clean install

Running the Application:
There is no specific instruction to run the project , it is enough to click run or debug

Usage:
After run the project you can find the collection to trigger the endpoints
Loan-Management.postman_collection.json
You can find the other credentials inside 
src/main/java/com/bank/loan/management/api/security/BasicAuthConfig.java


Additional Notes:
As database h2 is being used.For the console
http://localhost:8081/bank/loan-management/v1/h2/
jdbc url : jdbc:h2:mem:db
username: sa
password: sa
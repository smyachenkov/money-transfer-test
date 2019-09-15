# Requirements

Design and implement a RESTful API (including data model and the backing implementation) for
money transfers between accounts.

Explicit requirements:
1. You can use Java or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. You can use frameworks/libraries if you like (except Spring), but don't forget about
requirement #2 and keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require a
pre-installed container/server).
7. Demonstrate with tests that the API works as expected.

Implicit requirements:
1. The code produced by you is expected to be of high quality.
2. There are no detailed requirements, use common sense.
Please put your work on github or bitbucket.

# How to build and run

It's a maven project and can be build with the `mvn package` command.

To run this application, execute command `java -jar transfer-1.0.jar server config.yml`.  
Note that the configuration file config.yml must be present.

# API Overview

**GET     /account/list**  
Retrieve all accounts.
Response example:
```json
[
    {
            "id": 1,
            "accountNumber": 1,
            "name": "Alice",
            "balance": 100.00
    },
    {
        "id": 2,
        "accountNumber": 2,
        "name": "Bob",
        "balance": 100.00
    }
]
```
              
**GET     /account/{accountNumber}**  
Get account by it's number.
Response example:  
```
{
        "id": 1,
        "accountNumber": 1,
        "name": "Alice",
        "balance": 100.00
    }
```
Or 404 if account with such number is not found.

**GET     /{accountNumber}/history/**  
Retrieve all transactions in which this account have participated.
Response example:  
```json
[
    {
        "from": 1,
        "to": 2,
        "amount": 11.10,
        "date": "2019-09-15T18:30:55.329+03:00"
    },
    {
        "from": 1,
        "to": 2,
        "amount": 20.00,
        "date": "2019-09-15T18:31:01.211+03:00"
    }
]
```
Or 404 if account with such number is not found.

**POST    /account**  
Create new account.  
Body example:  
```json
{
	"name": "Alice",
	"accountNumber": 123,
	"balance": 100.00
}
```

**POST    /transfer**  
Request transfer between 2 accounts.  
Body example:
```json
{
	"from": 1,
	"to": 2,
	"amount": 15.30
}
```
Response:   
```json
{
    "from": 1,
    "fromBalance": 84.70,
    "to": 2,
    "toBalance": 115.30,
    "transferred": 15.30
}
```
Or error status with description, for example:
404 Account 33 is not found  
409 Insufficient funds on sender account  
409 InvalidTransferParams: Can't transfer to same account!
 

# Implementation notes

Frameworks and libraries:

* [Dropwizard](https://www.dropwizard.io/)  
Java framework for RESTful web services development. 

* [jOOQ](https://www.jooq.org/)  
SQL query generator and transaction manager.

* [HikariCP](https://github.com/brettwooldridge/HikariCP)  
Lightweight connection pool.

* [H2](http://www.h2database.com/)  
SQL database. It can be run as embedded and in-memory db.   
For the sake of this test it is used only in in-memory mode.

* [kotlin-logging](https://github.com/MicroUtils/kotlin-logging)  
Lightweight logging framework for Kotlin.

* [Flyway](https://flywaydb.org/)  
Version control for SQL database.

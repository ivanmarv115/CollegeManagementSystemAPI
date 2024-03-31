# College Management System API
This is the API for a College Management System. It is connected to a database and allows to managament of students, professors, courses and degrees, aswell as the users who can interact with this system.
The connection to the database is made using Data JPA, the tables for the entities are created using ORM.

The API has more than 30 endpoints, these endpoints help in doing CRUD operations to all the entities listed above. The endpoints are secured using spring security with JWTs, some can only be accesed by admin users.

We have tests for all the methods in the controller, service and repository layers, which add to around 90 methods.

We also have a Dockerfile to build and run the API as a docker image, and a Jenkinsfile with a pipeline script which automates the compilation, testing, building and deploying.

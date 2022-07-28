# BIKESTORES-ACCEPTANCE-TEST
Boilerplate Microservice with automation testing of bikestores with Java 8 and Spring Boot useful to test the REST request of a microservice, in this case bikestores.

Please note that this is only a boilerplate microservice, there is not a defined business, many configurations and developments are forced to show all the different features of Spring Boot.

## Documentation
In order to deploy it, it is necessary only to create a database on SQL server, launch the scripts of the folder Documentation->SQL-Query 
(in order: create objects, load data, and Procedure Rename), and then create a user with the permission on that database with the username and password to overwrite 
in the file bikestores\src\main\resources\application-dev.properties. 

After that you have only to deploy with Spring Boot Application and the profile dev.

### Additional documentation in the repository
Under the path "bikestores/Documentation/" you can find:
- The folder "SQL-Query" with inside the SQL Queries used;
- The image of the UML of the database "SQL-Server-Sample-Database.png";
- The collection to import on postman with all the REST requests.

### Where can I get the latest release?
You can download source and binaries from [Github page](https://github.com/LucaNocella1993/bikestores.git).


### Release Note
+ 1.0.0
    + Initial commit of the first stable project on the repository
    

### N.B.
In Eclipse for the error "execution not covered by lifecycle configuration",
select Window > Preferences > Maven > Errors/Warnings > execution not covered by lifecycle configuration > Ignore.

### 📫 How to reach me:

Luca Nocella

Java Developer – Naples – Italy

e-mail: [luca.nocella@hotmail.com](luca.nocella@hotmail.com)

linkedin: [https://www.linkedin.com/in/luca-nocella-6488aa153/](https://www.linkedin.com/in/luca-nocella-6488aa153/)

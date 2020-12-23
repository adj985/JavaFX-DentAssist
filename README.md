# JavaFX-DentAssist

A java fx program that helps dentists to manage their business. It manages the appointments, patient's data, income and outcome, storing interventions data into the database. 
It also creates a document for every patient in a specified folder, and append interventions data inside.

The database is created in MySQL Workbench. DDL is provided in this repository (dentassist.sql).

Program was made with NetBeans 8.2 IDE, JDK 8. Mapping and DB connection is provided by Hibernate, so to run this app, you have to provide a username and password in hibernate.cfg, in order to establish a connection with the database. With NetBeans, after cloning the project, that should do it.

For running in IntelliJ Idea, after cloning the project, you should add a library for hibernate 4.3 and MySQL connector.
Go to File/Project Structure. From the side menu choose Libraries, click on + sign to add a new project library, and select From Maven. In the search bar paste 
"org.hibernate:hibernate-core:4.3.0.Final", click ok and the jars should be added. After that go to https://mvnrepository.com/artifact/mysql/mysql-connector-java/8.0.16 and download the jar file. Go to File/Project Structure. From the side menu choose Libraries, click on + sign to add a new project library, and select Java. Locate the downloaded jar file and add it to a project. You can run a program now.

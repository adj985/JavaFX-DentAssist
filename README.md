# JavaFX-DentAssist

A java fx program that helps dentists to manage their business. It manages the appointments, patient's data, income and outcome, storing interventions data into the database. 
It also creates a document for every patient in a specified folder, and append interventions data inside.

Program is made with NetBeans 8.2 IDE, JDK 8. Mapping and DB connection is provided by Hibernate, so to run this app, you have to provide a username and password in hibernate.cfg, in order to establish a connection with the database(after creating one, of course).

For running in Intelij Idea, after cloning the project, you should add a library for hibernate 4.3
Go to File/Project Structure. From the side menu choose Libraries, click on + sign to add a new project library and select From Maven. In the search bar paste this URL
"org.hibernate:hibernate-core:4.3.0.Final" and after the jars are downloaded you can run the program.

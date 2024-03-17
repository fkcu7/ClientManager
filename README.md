# Virtual Assistance Client Manager Java Program

ITCC 11.1 - B Activity 5 by Francis King C. Uyguangco
-Created using MySQL Workbench and NetBeans IDE 16

## Application Features

1. **Client Management** - the admin can add, view, update, and delete clients. 
2. **Service Management** - the admin can add, view, update, and delete services offered.
3. **Invoice Management** - the admin generate new invoices, add a service to the invoice, update the hours of service,
                and delete an invoice.
4. **Analytics**: 
        1. **Total Income** - the admin can view his/her total income for the time period given.
        2. **Popular Serive** - the admin can view his/her most availed services from the time period given.
        3. **Top Client** - the admin can view his/her top client for the time period given.

## Recreating the database from the SQL dump

1. Make sure you have the JDBC jar file or download here https://dev.mysql.com/downloads/connector/j/
2. Access your database connection in mySQL Workbench.
3. Go to File>Open SQL Script and locate the cloned SQL dump file.
4. Click the lightning icon to run the file.
5. Go to the cloned java file and change the user and password for the connection.
6. Check the library for the JDBC driver or the jar file. If it does not exist, add it to the library to successfully 
        connect the java file and the database.

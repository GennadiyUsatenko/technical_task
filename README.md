# Task Condition
Image you have two companies.
Each company has such tables:
- Staff with fields: name and age.
- Employs with fields: employee and department
- Departments with fields: department and district

Your task is:
- design tables.
- write method for creating DB structure
- write a small method for populating DB with test data, I want you to populate DB with million employees.
- write a report method which will take as input such values: age bounds for employees and district for
department. Then inside the method run query which will show how many employees in both companies within the
desired age bounds works in each department but of course you need to filter departments by District.
- on the database level limit the possibility for an employee to work at different departments at the same time.
- think how to speed up your query.

# Overview
Simple Spring Boot Application integrated with an embedded H2 database. Databases are stored in db files into project 
directory and are automatically deleted when the app is success finished. Access to the database by using JDBC.
To speed up the insertion of a million records, I divided the original query into 50 small queries, with 20,000 rows each.
Of course I used several threads to populate both DB with test data. 
The same approach was applied to improve performance a report method.

package interfaces;


public interface SqlTaskBasicStructure {

    // db access info
    String DB_DIRECTORY = "jdbc:h2:file:./";
    String DB_PASSWORD = "";
    String DB_LOGIN = "sa";
    String DB_NAME_FOR_COMPANY_A = "Company_A";
    String DB_NAME_FOR_COMPANY_B = "Company_B";

    // db structure
    String STAFF_DB_STRUCTURE = "CREATE TABLE STAFF("
            + "ID INT NOT NULL AUTO_INCREMENT, "
            + "NAME VARCHAR(128) NOT NULL, "
            + "AGE TINYINT NOT NULL, "
            + "CONSTRAINT PK_STAFF_ID PRIMARY KEY (ID) );";

    String DEPARTMENT_DB_STRUCTURE = "CREATE TABLE DEPARTMENTS("
            + "ID INT NOT NULL AUTO_INCREMENT, "
            + "DEPARTMENT VARCHAR(128) NOT NULL, "
            + "DISTRICT VARCHAR(128) NOT NULL, "
            + "CONSTRAINT PK_DEPARTMENTS_ID PRIMARY KEY (ID) );";

    String EMPLOYEE_DB_STRUCTURE = "CREATE TABLE EMPLOYS("
            + "ID INT NOT NULL AUTO_INCREMENT, "
            + "EMPLOYEE INT NOT NULL, "
            + "DEPARTMENT INT NOT NULL, "
            + "CONSTRAINT PK_EMPLOYS_ID PRIMARY KEY (ID), "
            + "CONSTRAINT FK_EMPLOYS_STAFF FOREIGN KEY (EMPLOYEE) REFERENCES STAFF(ID) "
            + "ON DELETE CASCADE ON UPDATE CASCADE, "
            + "CONSTRAINT FK_EMPLOYS_DEPARTMENTS FOREIGN KEY (DEPARTMENT) REFERENCES DEPARTMENTS(ID) "
            + "ON DELETE CASCADE ON UPDATE CASCADE );";

    String FULL_DB_STRUCTURE = STAFF_DB_STRUCTURE + DEPARTMENT_DB_STRUCTURE + EMPLOYEE_DB_STRUCTURE;

    // base of insert query
    String STAFF_INSERT_EXPR = "INSERT INTO STAFF (NAME, AGE) VALUES ";
    String DEPARTMENT_INSERT_EXPR = "INSERT INTO DEPARTMENTS (DEPARTMENT, DISTRICT) VALUES ";
    String EMPLOYEE_INSERT_EXPR = "INSERT INTO EMPLOYS (EMPLOYEE, DEPARTMENT) VALUES ";

    // base sql for report method
    String SQL_REPORT_QUERY =
            "SELECT " +
                    "   (SELECT D.DEPARTMENT FROM DEPARTMENTS AS D WHERE D.ID = E.DEPARTMENT AND D.ID IN (arg2)) " +
                    "AS Department, COUNT(E.EMPLOYEE) AS Employees " +
                    "FROM EMPLOYS AS E " +
                    "WHERE E.EMPLOYEE IN (arg1) AND E.DEPARTMENT IN (arg2) " +
                    "GROUP BY Department;";

    // base for report arguments: sub-query for Employee and Departments db
    String SQL_REPORT_ARGUMENT_EM_QUERY = "SELECT GROUP_CONCAT(S.ID) AS RS FROM STAFF AS S WHERE S.ID >= argFrom AND S.ID <= argTo AND S.AGE <= argAge;";
    String SQL_REPORT_ARGUMENT_DP_QUERY = "SELECT GROUP_CONCAT(D.ID) AS RS FROM DEPARTMENTS AS D WHERE D.DISTRICT = 'arg';";

}

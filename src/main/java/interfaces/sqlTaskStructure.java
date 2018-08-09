package interfaces;

import org.h2.tools.DeleteDbFiles;
import service.CommonParallelProcess;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface sqlTaskStructure {

    // db access info
    String dbDirectory = "jdbc:h2:file:./";
    String dbPassword = "";
    String dbLogin = "sa";
    String dbNameForCompanyA = "Company_A";
    String dbNameForCompanyB = "Company_B";

    // db structure
    String staffDBStructure = "CREATE TABLE STAFF("
            + "ID INT NOT NULL AUTO_INCREMENT, "
            + "NAME VARCHAR(128) NOT NULL, "
            + "AGE TINYINT NOT NULL, "
            + "CONSTRAINT PK_STAFF_ID PRIMARY KEY (ID) );";

    String departmentDBStructure = "CREATE TABLE DEPARTMENTS("
            + "ID INT NOT NULL AUTO_INCREMENT, "
            + "DEPARTMENT VARCHAR(128) NOT NULL, "
            + "DISTRICT VARCHAR(128) NOT NULL, "
            + "CONSTRAINT PK_DEPARTMENTS_ID PRIMARY KEY (ID) );";

    String employeeDBStructure = "CREATE TABLE EMPLOYS("
            + "ID INT NOT NULL AUTO_INCREMENT, "
            + "EMPLOYEE INT NOT NULL, "
            + "DEPARTMENT INT NOT NULL, "
            + "CONSTRAINT PK_EMPLOYS_ID PRIMARY KEY (ID), "
            + "CONSTRAINT FK_EMPLOYS_STAFF FOREIGN KEY (EMPLOYEE) REFERENCES STAFF(ID) "
            + "ON DELETE CASCADE ON UPDATE CASCADE, "
            + "CONSTRAINT FK_EMPLOYS_DEPARTMENTS FOREIGN KEY (DEPARTMENT) REFERENCES DEPARTMENTS(ID) "
            + "ON DELETE CASCADE ON UPDATE CASCADE );";

    String fullDBStructure = staffDBStructure + departmentDBStructure + employeeDBStructure;

    // base of insert query
    String staffInsertExpr = "INSERT INTO STAFF (NAME, AGE) VALUES ";
    String departmentInsertExpr = "INSERT INTO DEPARTMENTS (DEPARTMENT, DISTRICT) VALUES ";
    String employeeInsertExpr = "INSERT INTO EMPLOYS (EMPLOYEE, DEPARTMENT) VALUES ";

    // base sql for report method
    String sqlReportQuery =
            "SELECT " +
            "   (SELECT D.DEPARTMENT FROM DEPARTMENTS AS D WHERE D.ID = E.DEPARTMENT AND D.ID IN (arg2)) " +
            "AS Department, COUNT(E.EMPLOYEE) AS Employees " +
            "FROM EMPLOYS AS E " +
            "WHERE E.EMPLOYEE IN (arg1) AND E.DEPARTMENT IN (arg2) " +
            "GROUP BY Department;";

    // base for report arguments: sub-query for Employee and Departments db
    String sqlReportArgumentEmQuery = "SELECT GROUP_CONCAT(S.ID) AS RS FROM STAFF AS S WHERE S.ID >= argFrom AND S.ID <= argTo AND S.AGE <= argAge;";
    String sqlReportArgumentDpQuery = "SELECT GROUP_CONCAT(D.ID) AS RS FROM DEPARTMENTS AS D WHERE D.DISTRICT = 'arg';";

    default void executeQuery(String query) throws SQLException {
        Statement statementA = getConnectionForCompanyA().createStatement();
        Statement statementB = getConnectionForCompanyB().createStatement();

        statementA.execute(query);
        statementB.execute(query);

        statementA.close();
        statementB.close();
    }

    //execute query in different threads
    default void parallelExecuteQuery(String query, String task) throws SQLException, InterruptedException {
        Statement statementA = getConnectionForCompanyA().createStatement();
        Statement statementB = getConnectionForCompanyB().createStatement();

        Thread threadA = new Thread( new CommonParallelProcess(statementA, query, task + "A") );
        Thread threadB = new Thread( new CommonParallelProcess(statementB, query, task + "B") );

        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();
    }

    default void createDBStructure() throws SQLException{
        executeQuery(fullDBStructure);
    }

    default void deleteDBFiles() throws SQLException {
        closeConnection();
        DeleteDbFiles.execute(System.getProperty("user.dir"), dbNameForCompanyA, false);
        DeleteDbFiles.execute(System.getProperty("user.dir"), dbNameForCompanyB, false);
    }

    void closeConnection() throws SQLException ;

    void populateDBWithTestData() throws SQLException, InterruptedException;

    void report(Integer ageBounds, String district) throws SQLException, InterruptedException;

    Connection getConnectionForCompanyA();

    Connection getConnectionForCompanyB();
}

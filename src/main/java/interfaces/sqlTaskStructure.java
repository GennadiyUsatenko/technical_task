package interfaces;

import org.h2.tools.DeleteDbFiles;
import service.CommonParallelProcess;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface sqlTaskStructure extends SqlTaskBasicStructure {

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
        executeQuery(FULL_DB_STRUCTURE);
    }

    default void deleteDBFiles() throws SQLException {
        closeConnection();
        DeleteDbFiles.execute(System.getProperty("user.dir"), DB_NAME_FOR_COMPANY_A, false);
        DeleteDbFiles.execute(System.getProperty("user.dir"), DB_NAME_FOR_COMPANY_B, false);
    }

    void closeConnection() throws SQLException ;

    void populateDBWithTestData() throws SQLException, InterruptedException;

    void report(Integer ageBounds, String district) throws SQLException, InterruptedException;

    Connection getConnectionForCompanyA();

    Connection getConnectionForCompanyB();

}

package service;

import interfaces.sqlTaskStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class SqlTaskService implements sqlTaskStructure {

    @Autowired
    GenerateTestDataService generateTestDataService;

    // connection to work with Company_A db
    private static Connection connectionA = null;
    // connection to work with Company_B db
    private static Connection connectionB = null;

    public SqlTaskService(){
        try {
            connectionA = DriverManager.getConnection(dbDirectory + dbNameForCompanyA, dbLogin, dbPassword);
            connectionB = DriverManager.getConnection(dbDirectory + dbNameForCompanyB, dbLogin, dbPassword);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createStructure() throws SQLException {
        createDBStructure();
    }

    @Override
    public void closeConnection() throws SQLException {
        connectionA.close();
        connectionB.close();
    }

    @Override
    public Connection getConnectionForCompanyA(){
        return connectionA;
    }

    @Override
    public Connection getConnectionForCompanyB(){
        return connectionB;
    }

    @Override
    public void populateDBWithTestData() throws SQLException, InterruptedException{
        // generate five departments
        StringBuilder sqlInsertQuery = new StringBuilder(
                generateTestDataService.generateTestDataForDepartmentDB(departmentInsertExpr)
        );
        // generate million employees by 20 000 for one time
        while (!generateTestDataService.isStaffTestDataReady){
            sqlInsertQuery.append(
                    generateTestDataService.generateTestDataForStaffAndEmployeeDB(staffInsertExpr, employeeInsertExpr)
            );
            parallelExecuteQuery( sqlInsertQuery.toString(), "execute" );
            sqlInsertQuery.setLength(0);
        }
    }

    @Override
    public void report(Integer ageBounds, String district) throws SQLException, InterruptedException {

        Long to;
        // get employees within the desired age bounds by 20 000 for one time
        for (Long from = 1l; from < generateTestDataService.limitOfDiapason; from += generateTestDataService.endOfNewInterval) {
            to = from + generateTestDataService.endOfNewInterval;
            parallelExecuteQuery(
                    sqlReportArgumentEmQuery
                            .replace("argFrom", from.toString())
                            .replace("argTo", to.toString())
                            .replace("argAge", ageBounds.toString()),
                    "executeQuery"
            );
        }

        ResultListenerService.changeResultFocus();
        // filter departments by District
        parallelExecuteQuery( sqlReportArgumentDpQuery.replace("arg", district), "executeQuery" );

        // report query with (sub-query for Employee and Departments db)
        parallelExecuteQuery(
                sqlReportQuery
                        .replace("arg1", ResultListenerService.resultEmForReportArgumentA.toString())
                        .replaceAll("arg2", ResultListenerService.resultDpForReportArgumentA.toString()),
                "mapExecuteQuery"
        );

        // join report results from both Companies into final result
        ResultListenerService.addMapResultForReportMethodA(ResultListenerService.mapResultForReportMethodB);
        // show sql result output
        System.out.println("Department | Employees");
        ResultListenerService.mapResultForReportMethodA.forEach(
                (k,v) -> {
                    System.out.println(k + " | " + v.toString());
                }
        );
    }
}

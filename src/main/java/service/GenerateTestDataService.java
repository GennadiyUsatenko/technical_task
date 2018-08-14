package service;

import model.Department;
import model.Employee;
import model.Staff;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GenerateTestDataService {

    // how many departments we have in both companies
    private final int COUNT_OF_DEPARTMENT_ITEMS = 5;
    public final long LIMIT_OF_DIAPASON = 1_000_000;
    // start of new interval when we divided by into parts a millions diapason(sql query for million records)
    private long startOfNewInterval = 1;
    // size of optimal interval for app
    public long endOfNewInterval = 20000;
    // means when its true then test data for Staff db table is ready
    public boolean isStaffTestDataReady = false;

    public String generateTestDataForDepartmentDB(String sqlInsertExpr) {
        StringBuilder testData = new StringBuilder(sqlInsertExpr);
        Department department = null;
        char endChar = ',';

        for (int i = 1; i <= COUNT_OF_DEPARTMENT_ITEMS; i++) {
            if(i == COUNT_OF_DEPARTMENT_ITEMS) endChar = ';';

            department = new Department("Department" + i, "District" + (i % 2));

            testData.append(department.toString() + endChar);
        }

        return testData.toString();
    }

    public String generateTestDataForStaffAndEmployeeDB(String staffSqlInsertExpr, String employeeSqlInsertExpr){
        StringBuilder staffTestData = new StringBuilder(staffSqlInsertExpr);
        StringBuilder employeeTestData = new StringBuilder(employeeSqlInsertExpr);
        Staff staff = null;
        Employee employee = null;
        char endChar = ',';

        for (long i = startOfNewInterval; i <= endOfNewInterval; i++) {
            if(i == endOfNewInterval) endChar = ';';

            staff = new Staff("E" + i, getRandomInt(18, 100));
            employee = new Employee(i, getRandomLong(1, COUNT_OF_DEPARTMENT_ITEMS));

            staffTestData.append(staff.toString() + endChar);
            employeeTestData.append(employee.toString() + endChar);
        }

        if( endOfNewInterval == LIMIT_OF_DIAPASON){
            isStaffTestDataReady = true;
        }else{
            long difference = endOfNewInterval - startOfNewInterval + 1;
            startOfNewInterval += difference;
            endOfNewInterval += difference;
        }

        return staffTestData.toString() + employeeTestData.toString();
    }

    private static int getRandomInt(int from, int to){
        return new Random().nextInt(++to - from) + from;
    }

    private static long getRandomLong(int from, int to){
        return new Random().nextInt(++to - from) + from;
    }

}

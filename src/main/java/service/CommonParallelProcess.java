package service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class CommonParallelProcess implements Runnable {

    private final boolean IS_USE_LOGGER = false;
    private Map<String, Long> map = null;
    private ResultSet resultSet = null;
    private Statement statement = null;
    private String query = null;
    //use 3 type of thread tasks for both Companies
    private String task = null;

    public CommonParallelProcess(Statement statement, String query, String task){
        this.statement = statement;
        this.query = query;
        this.task = task;
    }

    @Override
    public void run(){

        if(IS_USE_LOGGER)System.out.println("Thread is running...");

        try {
            switch (task){
                //just execute query
                case "executeA":
                case "executeB":
                    statement.execute(query);
                    break;
                // execute query and get single result
                case "executeQueryA":
                    resultSet = statement.executeQuery(query);

                    if(resultSet.next()){
                        ResultListenerService.addResultForReportArgumentA(resultSet.getString("RS"));
                    }

                    resultSet.close();
                    break;
                case "executeQueryB":
                    resultSet = statement.executeQuery(query);

                    if(resultSet.next()){
                        ResultListenerService.addResultForReportArgumentB(resultSet.getString("RS"));
                    }

                    resultSet.close();
                    break;
                //execute query and get result map from result set
                case "mapExecuteQueryA":
                    resultSet = statement.executeQuery(query);
                    map = new ConcurrentHashMap<>();

                    while (resultSet.next()){
                        map.put( resultSet.getString("Department"), resultSet.getLong("Employees") );
                    }

                    ResultListenerService.addMapResultForReportMethodA(map);
                    resultSet.close();
                    break;
                case "mapExecuteQueryB":
                    resultSet = statement.executeQuery(query);
                    map = new ConcurrentHashMap<>();

                    while (resultSet.next()){
                        map.put( resultSet.getString("Department"), resultSet.getLong("Employees") );
                    }

                    ResultListenerService.addMapResultForReportMethodB( map );
                    resultSet.close();
                    break;
            }
            statement.close();

            if(IS_USE_LOGGER)System.out.println("Thread is stopped...");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}

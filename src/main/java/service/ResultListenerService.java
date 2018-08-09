package service;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResultListenerService {

    // store the result of report method for A and B company
    public static Map<String, Long> mapResultForReportMethodA = new ConcurrentHashMap<>();
    public static Map<String, Long> mapResultForReportMethodB = new ConcurrentHashMap<>();
    // store the report sql arguments (sub-query for Employee and Departments db) for A and B company
    public static StringBuffer resultEmForReportArgumentA = new StringBuffer();
    public static StringBuffer resultDpForReportArgumentA = new StringBuffer();
    public static StringBuffer resultEmForReportArgumentB = new StringBuffer();
    public static StringBuffer resultDpForReportArgumentB = new StringBuffer();
    // comma separator
    public static String prefixA = "";
    public static String prefixB = "";
    // flag to use Employee or Departments report arguments
    public static boolean isUseEmResult = true;

    // change the report argument focus
    public static void changeResultFocus(){
        ResultListenerService.isUseEmResult = false;
        ResultListenerService.prefixA = "";
        ResultListenerService.prefixB = "";
    }

    // use to store result of report method for A and B company
    public static synchronized void addMapResultForReportMethodA(Map<String, Long> result){
        if(result.size() != 0){
            result.keySet().forEach( key -> {
                    long value = result.get(key);
                    value += mapResultForReportMethodA.get(key) == null ? 0 : mapResultForReportMethodA.get(key);
                    mapResultForReportMethodA.put(key, value);
                }
            );
        }
    }

    public static synchronized void addMapResultForReportMethodB(Map<String, Long> result){
        if(result.size() != 0){
            result.keySet().forEach( key -> {
                    long value = result.get(key);
                    value += mapResultForReportMethodB.get(key) == null ? 0 : mapResultForReportMethodB.get(key);
                    mapResultForReportMethodB.put(key, value);
                }
            );
        }
    }

    // use to store results of report arguments methods for A and B company
    public static synchronized void addResultForReportArgumentA(String result){
        if(result != null){
            if(isUseEmResult) resultEmForReportArgumentA.append(prefixA + result);
            else resultDpForReportArgumentA.append(prefixA + result);

            prefixA = ",";
        }
    }

    public static synchronized void addResultForReportArgumentB(String result){
        if(result != null){
            if(isUseEmResult) resultEmForReportArgumentB.append(prefixB + result);
            else resultDpForReportArgumentB.append(prefixB + result);

            prefixB = ",";
        }
    }
}

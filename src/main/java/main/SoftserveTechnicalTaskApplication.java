package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import service.SqlTaskService;

import java.sql.SQLException;
import java.util.Date;

@SpringBootApplication(scanBasePackages = { "main","service" })
public class SoftserveTechnicalTaskApplication {

	public static void main(String[] args) {
		ApplicationContext app = SpringApplication.run(SoftserveTechnicalTaskApplication.class, args);//init the context
		SqlTaskService sqlTaskService = app.getBean(SqlTaskService.class);

        try {

            // task number 1 and 2 - design tables and creating DB structure
            sqlTaskService.createStructure();

            // task number 3 - populating DB with test data, runs about 100 seconds
            sqlTaskService.populateDBWithTestData();

            // task number 4 - report method
            sqlTaskService.report(18, "District1");

            // delete db files from user dir
            sqlTaskService.deleteDBFiles();

        }catch (Exception e){
            try {
                sqlTaskService.deleteDBFiles();
            }catch (SQLException s){ s.printStackTrace(); }
            e.printStackTrace();
        }
	}
}

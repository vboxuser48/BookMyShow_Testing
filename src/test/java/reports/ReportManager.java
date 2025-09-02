package reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ReportManager {
    private static ExtentReports extent;

    // Singleton pattern - one report instance for all tests
    public static ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
            ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);

            reporter.config().setReportName("BookMyShow Automation Report");
            reporter.config().setDocumentTitle("Automation Test Results");

            extent = new ExtentReports();
            extent.attachReporter(reporter);

            // Add dynamic system info
            extent.setSystemInfo("Tester", "Abhay Chauhan");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Application", "BookMyShow");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
        }
        return extent;
    }
}

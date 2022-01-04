import com.aventstack.extentreports.ExtentReports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.*;
import org.testng.xml.ISuiteParser;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExtentReporterNG implements IReporter {
    private ExtentReports extentReports;
    ExtentSparkReporter reporter;

    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> iSuites, String outputDir) {
        String path = String.format("src/main/resources/report/ReportExecution-%s.html", new Date());
        reporter = new ExtentSparkReporter(new File(path));
        extentReports = new ExtentReports();
        extentReports.attachReporter(reporter);
        for(ISuite suite : iSuites) {
            Map<String, ISuiteResult> results = suite.getResults();

            for(ISuiteResult result : results.values()) {
                ITestContext context = result.getTestContext();
                buildTestNodes(context.getPassedTests(), Status.PASS);
                buildTestNodes(context.getFailedTests(), Status.FAIL);
                buildTestNodes(context.getSkippedTests(), Status.SKIP);
            }
        }
        extentReports.flush();
    }

    private void buildTestNodes(IResultMap tests, Status status) {
        ExtentTest test;

        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                test = extentReports.createTest(result.getMethod().getMethodName());

                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);

                String message = "Test " + status.toString().toLowerCase() + "ed";

                if (result.getThrowable() != null)
                    message = result.getThrowable().getMessage();

                test.log(status, message);
            }
        }
    }
}

import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;

public class Listeners implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        String failedTc = result.getName();
        try {
            Base.takeScreenshot(failedTc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

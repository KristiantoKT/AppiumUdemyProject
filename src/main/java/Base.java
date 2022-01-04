import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

public class Base {
    public static AndroidDriver<AndroidElement> driver;
    private static AppiumDriverLocalService service;

    public void startServer() {
        service = AppiumDriverLocalService.buildDefaultService();
        service.start();
    }

    public void stopServer() {
        service.stop();
    }

    public void startEmulator() throws IOException, InterruptedException {
        Runtime.getRuntime().exec("src/main/resources/startemulator.sh");
        Thread.sleep(15000);
    }

    public void stopEmulator() throws IOException {
        Runtime.getRuntime().exec("src/main/resources/stopemulator.sh");
    }

    public static void takeScreenshot(String testName) throws IOException {
        File screenshot =  ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String targetPath = String.format("src/main/resources/screenshot/ss-%s-%s.png", testName, new Date());
        FileUtils.copyFile(screenshot, new File(targetPath));
    }

    public AndroidDriver<AndroidElement> generateDriver(String appName) throws IOException {
        File appDir = new File("src/main/resources");

        Properties prop = new Properties();
        File propFile = new File("src/main/java/global.properties");
        FileInputStream propFileInpStream = new FileInputStream(propFile.getAbsolutePath());
        prop.load(propFileInpStream);
        File apk = new File(appDir, prop.getProperty(appName));

        URL appiumServer = new URL("http://localhost:4723/wd/hub");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, prop.getProperty("DEVICE_NAME"));
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        capabilities.setCapability(MobileCapabilityType.APP, apk.getAbsolutePath());

        driver = new AndroidDriver<>(appiumServer, capabilities);
        return driver;
    }
}

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
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

    public AndroidDriver<AndroidElement> generateDriver(String appName) throws IOException, ParseException {
//        File appDir = new File("src/main/resources");
//
//        Properties prop = new Properties();
//        File propFile = new File("src/main/java/global.properties");
//        FileInputStream propFileInpStream = new FileInputStream(propFile.getAbsolutePath());
//        prop.load(propFileInpStream);
//        File apk = new File(appDir, prop.getProperty(appName));
//
//        URL appiumServer = new URL("http://localhost:4723/wd/hub");
//
//        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
//        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, prop.getProperty("DEVICE_NAME"));
//        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
//        capabilities.setCapability(MobileCapabilityType.APP, apk.getAbsolutePath());
//
//        driver = new AndroidDriver<>(appiumServer, capabilities);
        JSONParser parser = new JSONParser();
        JSONObject config = (JSONObject) parser.parse(new FileReader("src/main/resources/first.conf.json"));

        DesiredCapabilities capabilities = new DesiredCapabilities();

        JSONArray envs = (JSONArray) config.get("environments");
        Map<String, String> envCapabilities = (Map<String, String>) envs.get(0);
        Iterator it = envCapabilities.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
        }

        Map<String, String> commonCapabilities = (Map<String, String>) config.get("capabilities");
        it = commonCapabilities.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(capabilities.getCapability(pair.getKey().toString()) == null){
                capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
            }
        }

        String username = System.getenv("BROWSERSTACK_USERNAME");
        if(username == null) {
            username = (String) config.get("username");
        }

        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        if(accessKey == null) {
            accessKey = (String) config.get("access_key");
        }

        String app = System.getenv("BROWSERSTACK_APP_ID");
        if(app != null && !app.isEmpty()) {
            capabilities.setCapability("app", app);
        }

        driver = new AndroidDriver(new URL("http://"+username+":"+accessKey+"@"+config.get("server")+"/wd/hub"), capabilities);
        return driver;
    }
}

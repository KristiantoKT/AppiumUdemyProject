import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class HomePageObject {
    private AndroidDriver driver;

    public HomePageObject(AndroidDriver driver) {
        // use AppiumFieldDecorator to provide compatibility with Android and iOS platform
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        this.driver = driver;
    }

    @AndroidFindBy(xpath = "//*[contains(@resource-id, 'generalstore:id/spinnerCountry')]")
    public AndroidElement spinnerCountry;

    @AndroidFindBy(xpath = "//*[contains(@resource-id, 'generalstore:id/nameField')]")
    public AndroidElement nameInputField;

    @AndroidFindBy(xpath = "//*[contains(@resource-id, 'generalstore:id/radioMale')]")
    public AndroidElement maleRadioBtn;

    @AndroidFindBy(xpath = "//*[contains(@resource-id, 'generalstore:id/radioFemale')]")
    public AndroidElement femaleRadioBtn;

    @AndroidFindBy(xpath = "//*[contains(@resource-id, 'generalstore:id/btnLetsShop')]")
    public AndroidElement letsShopBtn;

    public void scrollCountryDynamic(String countryName) {
        String uiAutomator = String.format("new UiScrollable(new UiSelector()).scrollIntoView(text(\"%s\"))",
                countryName);
        driver.findElementByAndroidUIAutomator(uiAutomator);
    }

    public AndroidElement selectCountryDynamic(String countryName) {
        String countryXpath = String.format("//*[contains(@resource-id, 'text1') and @text='%s']", countryName);
        AndroidElement countryElement = (AndroidElement) driver.findElementByXPath(countryXpath);
        return countryElement;
    }
}

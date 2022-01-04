import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class ProductListObject {
    private AndroidDriver driver;

    public ProductListObject(AndroidDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        this.driver = driver;
    }

    public void scrollToProductCard(String productName) {
        String uiAutomator = String.format("new UiScrollable(new UiSelector().resourceId(\"com.androidsample.generalstore:id/rvProductList\")).scrollIntoView(new UiSelector().textMatches(\"%s\").instance(0))", productName);
        driver.findElementByAndroidUIAutomator(uiAutomator);
    }

    public AndroidElement atcProductBtnDynamic(int productIdx) {
        List<AndroidElement> atcBtns = driver.findElementsByXPath("//*[contains(@resource-id, 'generalstore:id/productAddCart')]");
        AndroidElement atcBtn = atcBtns.get(productIdx);
        return atcBtn;
    }

    public void scrollAndAtcProduct(String productName, int productIdx) {
        scrollToProductCard(productName);
        atcProductBtnDynamic(productIdx).click();
    }

    @AndroidFindBy(xpath = "//*[contains(@resource-id, 'generalstore:id/appbar_btn_cart')]")
    public AndroidElement atcButton;
}

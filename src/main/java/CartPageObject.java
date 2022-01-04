import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.*;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class CartPageObject {
    AndroidDriver driver;

    public CartPageObject(AndroidDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        this.driver = driver;
    }

    @AndroidFindBy(xpath = "//*[contains(@resource-id, 'generalstore:id/productName')]")
    public List<AndroidElement> productNamesInCart;

    @AndroidFindBy(xpath = "//*[contains(@resource-id, 'generalstore:id/productPrice')]")
    public List<AndroidElement> productPriceInCart;

    public Double countTotalAmountInCart(List<AndroidElement> productPrices) {
        Double totalAmtProductList = 0.0;
        for(AndroidElement element : productPrices) {
            String temp = element.getText().substring(1);
            totalAmtProductList += Double.parseDouble(temp);
        }
        return totalAmtProductList;
    }

    @AndroidFindBy(xpath = "//*[contains(@resource-id, 'generalstore:id/totalAmountLbl')]")
    public AndroidElement totalAmountInCart;

    @AndroidFindBy(className = "android.widget.CheckBox")
    public AndroidElement emailCheckbox;

    @AndroidFindBy(xpath = "//*[contains(@resource-id, 'generalstore:id/btnProceed')]")
    public AndroidElement proceedBtn;

    @AndroidFindBy(xpath = "//*[contains(@resource-id, 'generalstore:id/termsButton')]")
    public AndroidElement tncBtn;

    @AndroidFindBy(xpath = "//*[contains(@resource-id, 'generalstore:id/alertTitle')]")
    public AndroidElement tncTitle;

    @AndroidFindBy(xpath = "//*[@resource-id = 'android:id/button1']")
    public AndroidElement closePopupBtn;

}

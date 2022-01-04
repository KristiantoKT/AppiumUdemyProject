import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.appium.java_client.touch.LongPressOptions.longPressOptions;
import static io.appium.java_client.touch.TapOptions.tapOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;

public class GeneralStoreTest {
    private Base base;
    private AndroidDriver<AndroidElement> driver;
    private TouchAction t;
    private PageObject po;

    @BeforeClass
    public void setUp() throws IOException, InterruptedException {
        base = new Base();
        base.startServer();
        base.startEmulator();
        driver = base.generateDriver("GENERAL_STORE_APP");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        t = new TouchAction(driver);
        po = new PageObject(driver);
    }

    @AfterClass
    public void tearDown() throws IOException {
        base.stopEmulator();
        base.stopServer();
    }

    @Test
    public void testFillInFormInHomePage() {
        // Choose country
        String countryName = "Argentina";
        t.tap(tapOptions().withElement(element(po.homePageObject.spinnerCountry))).perform();
        po.homePageObject.scrollCountryDynamic(countryName);
        t.tap(tapOptions().withElement(element(po.homePageObject.selectCountryDynamic(countryName)))).perform();

        // Choose Female as Gender
        po.homePageObject.femaleRadioBtn.click();

        // Fill in Name field
        po.homePageObject.nameInputField.sendKeys("Hello");
        driver.hideKeyboard();

        // Tap Let's Shop button
        po.homePageObject.letsShopBtn.click();
    }

    @Test(dependsOnMethods={"testFillInFormInHomePage"}, dataProvider = "ProductDataTest")
    public void testScrollAndAddProductToCart(String productName, int index) {
        // Scroll to product cart and tap ATC
        po.productListObject.scrollAndAtcProduct(productName, index);

        // Assertion
        Assert.assertEquals(po.productListObject.atcProductBtnDynamic(index).getText(), "ADDED TO CART");
    }

    @Test(dependsOnMethods={"testScrollAndAddProductToCart"})
    public void testTapCartButton() throws InterruptedException {
        // Product List
        String productName1 = "Air Jordan 4 Retro";
        String productName2 = "Air Jordan 1 Mid SE";

        // Tap ATC button and wait for 2 seconds
        po.productListObject.atcButton.click();
        Thread.sleep(2000);

        // Get product names in cart
        String actualProductName1 = po.cartPageObject.productNamesInCart.get(0).getText();
        String actualProductName2 = po.cartPageObject.productNamesInCart.get(1).getText();

        // Assertion
        Assert.assertEquals(actualProductName1, productName1);
        Assert.assertEquals(actualProductName2, productName2);
    }

    @Test(dependsOnMethods = "testTapCartButton")
    public void calculateTotalAmountInCart() {
        List<AndroidElement> productPrices = po.cartPageObject.productPriceInCart;
        Double totalAmtProductList = po.cartPageObject.countTotalAmountInCart(productPrices);
        String totalAmtInCartStr = po.cartPageObject.totalAmountInCart.getText().substring(2);
        Double totalAmtInCartConverted = Double.parseDouble(totalAmtInCartStr);
        Assert.assertEquals(totalAmtInCartConverted, totalAmtProductList);
    }

    @Test(dependsOnMethods = "calculateTotalAmountInCart")
    public void proceedCheckout() throws InterruptedException {
        // Tick email checkbox
        t.tap(tapOptions().withElement(element(po.cartPageObject.emailCheckbox))).perform();

        // Long press TNC button to see TNC
        t.longPress(longPressOptions().withElement(element(po.cartPageObject.tncBtn)).withDuration(Duration.ofSeconds(2))).release().perform();
        Assert.assertEquals(po.cartPageObject.tncTitle.getText(), "Terms Of Conditions");
        po.cartPageObject.closePopupBtn.click();

        // Tap proceed button
        Thread.sleep(2000);
        po.cartPageObject.proceedBtn.click();
    }

    @DataProvider(name = "ProductDataTest")
    public Object[][] productDataTest() {
        Object[][] testData = new Object[][]{
                {"Air Jordan 4 Retro", 0},
                {"Air Jordan 1 Mid SE", 1}
        };
        return testData;
    }
}

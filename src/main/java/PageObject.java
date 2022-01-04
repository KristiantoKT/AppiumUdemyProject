import io.appium.java_client.android.AndroidDriver;

public class PageObject {
    private AndroidDriver driver;
    public HomePageObject homePageObject;
    public ProductListObject productListObject;
    public CartPageObject cartPageObject;

    public PageObject(AndroidDriver driver) {
        this.driver = driver;
        this.homePageObject = new HomePageObject(this.driver);
        this.productListObject = new ProductListObject(this.driver);
        this.cartPageObject = new CartPageObject(this.driver);
    }
}

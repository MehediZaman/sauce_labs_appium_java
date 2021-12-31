package tests;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URL;

import static tests.Config.region; // added

public class Mobile_Android_EMU_Test {
//    private static final String APP = "E://_test_automation_//_sauce_labs//quickstart-android//Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";
//    private static final String APPIUM = "http://localhost:4723/wd/hub";
    private static final String APP = "Android.SauceLabs.Mobile.Sample.app.2.7.1.apk"; // changed from local
    URL url; //added, deleted APPIUM

    private String sessionId;
    private AndroidDriver driver;

    String usernameID = "test-Username";
    String passwordID = "test-Password";
    String submitButtonID = "test-LOGIN";
    By ProductTitle = By.xpath("//android.widget.TextView[@text='PRODUCTS']");

    @BeforeMethod
    public void setUp() throws Exception {
        System.out.println("Sauce Android EMU App Test - Parallel - BeforeMethod hook");
        String username = System.getenv("SAUCE_USERNAME"); // added
        String accesskey = System.getenv("SAUCE_ACCESS_KEY"); //added
//        String username = System.getenv("oauth-sqarobi2-6b6ba"); // added
//        String accesskey = System.getenv("c8f7c498-e024-46e0-ac3c-e9c2b6d9b56f"); //added
        String sauceUrl;
        if (region.equalsIgnoreCase("eu")) {
            sauceUrl = "@ondemand.eu-central-1.saucelabs.com:443";
        } else {
            sauceUrl = "@ondemand.us-west-1.saucelabs.com:443";
        }
        String SAUCE_REMOTE_URL = "https://" + username + ":" + accesskey + sauceUrl + "/wd/hub"; // if else statement added
        url = new URL(SAUCE_REMOTE_URL); //added
        System.out.println(url);
        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setCapability("deviceName", "Android Emulator"); //This will change to type of device e.g. Pixel 4 on Saucelabs
        capabilities.setCapability("deviceName", "Android GoogleAPI Emulator"); //Changed to GoogleAPI Emu
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion","10.0" ); //add platformVersion
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("appWaitActivity", "com.swaglabsmobileapp.MainActivity");
//        capabilities.setCapability("app", APP);
        capabilities.setCapability("app", "storage:filename=" + APP); //changed
        driver = new AndroidDriver(url, capabilities); // changed, removed APPIUM and replaced with url

//        driver = new AndroidDriver(new URL(APPIUM), capabilities);

    }

    @AfterMethod
    public void tearDown() {
        System.out.println("Sauce Android Mobile EMU Test - AfterMethod hook");// added

        if (driver != null) {
            driver.quit();
        }

    }

    @Test
    public void loginToSwagLabsTestValid() {
        System.out.println("Sauce - Start loginToSwagLabsTestValid test");

        login("standard_user", "secret_sauce");

        // Verification
        Assert.assertTrue(isOnProductsPage());
    }

    @Test
    public void loginTestValidProblem() {
        System.out.println("Sauce - Start loginTestValidProblem test");

        login("problem_user", "secret_sauce");

        // Verification - we on Product page
        Assert.assertTrue(isOnProductsPage());
    }

    public void login(String user, String pass){

        WebDriverWait wait = new WebDriverWait(driver, 5);
        final WebElement usernameEdit = wait.until(ExpectedConditions.visibilityOfElementLocated(new MobileBy.ByAccessibilityId(usernameID)));

        usernameEdit.click();
        usernameEdit.sendKeys(user);

        WebElement passwordEdit = driver.findElementByAccessibilityId(passwordID);
        passwordEdit.click();
        passwordEdit.sendKeys(pass);

        WebElement submitButton = driver.findElementByAccessibilityId(submitButtonID);
        submitButton.click();
    }

    public boolean isOnProductsPage() {
        //Create an instance of a Appium explicit wait so that we can dynamically wait for an element
        WebDriverWait wait = new WebDriverWait(driver, 5);

        //wait for the product field to be visible and store that element into a variable
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(ProductTitle));
        } catch (TimeoutException e){
            System.out.println("*** Timed out waiting for product page to load.");
            return false;
        }
        return true;
    }
}

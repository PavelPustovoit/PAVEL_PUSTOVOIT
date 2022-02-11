package SelenTask;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class LoginAuto {


    public static WebDriver login(WebDriver driver, List<String> args) {

        driver.get(args.get(0));

        WebElement username=driver.findElement(By.id("txtUsername"));
        WebElement password=driver.findElement(By.id("txtPassword"));
        WebElement login=driver.findElement(By.id("btnLogin"));

        username.sendKeys(args.get(1));
        password.sendKeys(args.get(2));
        login.click();

        String expectedUrl = args.get(3);
        String actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(expectedUrl,actualUrl);

        return driver;
    }

    public static void close(WebDriver driver){
        driver.close();
    }
}
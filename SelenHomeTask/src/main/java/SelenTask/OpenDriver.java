package SelenTask;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class OpenDriver {

    @Test
    public static WebDriver open(){

        WebDriver driver=new ChromeDriver();
        driver.manage().window().maximize();

        return driver;
    }
}

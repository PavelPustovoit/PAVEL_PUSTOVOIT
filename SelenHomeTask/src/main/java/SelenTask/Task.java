package SelenTask;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class Task {
    public static void main(String args[]) throws InterruptedException {

        //System.setProperty("webdriver.chrome.driver", "pathToDriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        String link = "https://opensource-demo.orangehrmlive.com";
        String username = "Admin";
        String password = "admin123";
        String exp_link = "https://opensource-demo.orangehrmlive.com/index.php/dashboard";
        List<String> log_args = Arrays.asList(link, username, password, exp_link);
        List<String> fill_args = Arrays.asList("TaskName", "06:00", "18:00", "12.00");
        LoginAuto.login(driver, log_args);

        AddNewRecord.toForm(driver);

        AddNewRecord.fillForm(driver, fill_args);

        int check = CheckChanges.check(driver, fill_args);

        if (check != -1){
            DeleteShift.delete(driver, check);
        }

        check = CheckChanges.check(driver, fill_args);

        if (check == -1){
            LoginAuto.close(driver);
        }

    }
}

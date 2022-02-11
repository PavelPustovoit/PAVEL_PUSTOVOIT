package SelenTask;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class AddNewRecord {

    @Test
    public static void toForm(WebDriver driver){
        WebElement admin = driver.findElement(By.id("menu_admin_viewAdminModule"));
        admin.click();
        String expectedUrl= "https://opensource-demo.orangehrmlive.com/index.php/admin/viewSystemUsers";
        String actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(expectedUrl,actualUrl);

        WebElement job = driver.findElement(By.id("menu_admin_Job"));
        job.click();

        WebElement workShift = driver.findElement(By.id("menu_admin_workShift"));
        workShift.click();
        expectedUrl= "https://opensource-demo.orangehrmlive.com/index.php/admin/workShift";
        actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(expectedUrl,actualUrl);
    }

    @Test
    public static void fillForm(WebDriver driver, List<String> args) throws InterruptedException {
        WebElement add = driver.findElement(By.id("btnAdd"));
        add.click();

        WebElement name = driver.findElement(By.id("workShift_name"));
        name.sendKeys(args.get(0));

        WebElement hours_from = driver.findElement(By.id("workShift_workHours_from"));
        hours_from.sendKeys(args.get(1));

        WebElement hours_to = driver.findElement(By.id("workShift_workHours_to"));
        hours_to.sendKeys(args.get(2));

        WebElement assignEmp = driver.findElement(By.id("btnAssignEmployee"));

        WebElement employee = driver.findElement(By.id("workShift_availableEmp"));
        employee.sendKeys("54");

        assignEmp.click();
        employee.sendKeys("62");
        assignEmp.click();

        WebElement save = driver.findElement(By.id("btnSave"));
        save.click();

    }
}

package SelenTask;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;


public class DeleteShift {
    @Test
    public static void delete(WebDriver driver, int checkId){

        WebElement checkBox = driver.findElement(By.xpath("//*[@id=\"resultTable\"]/tbody/tr["+checkId+"]/td[1]/input"));
        checkBox.click();

        WebElement delete = driver.findElement(By.id("btnDelete"));
        delete.click();

        WebElement confirm = driver.findElement(By.id("dialogDeleteBtn"));
        confirm.click();

    }
}

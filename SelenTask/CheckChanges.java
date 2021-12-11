package SelenTask;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;


public class CheckChanges {
    @Test
    public static Integer check(WebDriver driver, List<String> args){

        int rowCount = driver.findElements(By.xpath("//*[@id=\"resultTable\"]/tbody/tr")).size();

        if (rowCount > 0) {

            for (int i = 1; i <= rowCount; i++) {
                String cellValue = driver.findElement(By.xpath(".//*[@id=\"resultTable\"]/tbody/tr[" + i + "]/td[2]/a")).getText();

                if (cellValue.equalsIgnoreCase("TaskName")) {
                    String nameShift = driver.findElement(By.xpath("//*[@id=\"resultTable\"]/tbody/tr[" + i + "]/td[2]")).getText();
                    String from = driver.findElement(By.xpath("//*[@id=\"resultTable\"]/tbody/tr[" + i + "]/td[3]")).getText();
                    String to = driver.findElement(By.xpath("//*[@id=\"resultTable\"]/tbody/tr[" + i + "]/td[4]")).getText();
                    String hoursPerDay = driver.findElement(By.xpath("//*[@id=\"resultTable\"]/tbody/tr[" + i + "]/td[5]")).getText();

                    String all = nameShift + from + to + hoursPerDay;
                    String strArgs = args.stream().map(Object::toString).collect(Collectors.joining(""));
                    if (all.equals(strArgs)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
}

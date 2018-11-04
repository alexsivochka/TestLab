package admin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Login {
    public static void main(String[] args) throws InterruptedException {

        WebDriver driver = getDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60,TimeUnit.SECONDS);

        try {
            // Авторизуемся
            driver.navigate().to("http://prestashop-automation.qatestlab.com.ua/admin147ajyvk0/");
            driver.findElement(By.id("email")).sendKeys("webinar.test@gmail.com");
            driver.findElement(By.id("passwd")).sendKeys("Xcg7299bnSmMuRLp9ITw");
            driver.findElement(By.name("submitLogin")).click();

            // Получим список всех элементов меню
            List<WebElement> elements = driver.findElements(By.xpath("//ul[contains(@class,'menu')]//a/span/ancestor::li"));

            // В цикле поочередно выбираем элементы и проводим проверки
            for (int i = 1; i <=elements.size(); i++) {
                // Получаем текущий элемент
                String xpath = String.format("(//ul[contains(@class,'menu')]//a/span/ancestor::li)[%d]",i);
                WebElement element = driver.findElement(By.xpath(xpath));

                //Выводим значение элемента, с которым необхлдимо взаимодействовать, в консоль
                String menuTitle = element.getText();
                System.out.println("Should click menu item: ".concat(menuTitle));

                // Нажимаем на текущий элемент меню
                element.click();

                // Проверим, что элемент был действительно выбран
                WebElement activeElement = driver.findElement(By.xpath(xpath));
                String activeTitle = activeElement.getAttribute("class");
                Assert.assertTrue(activeTitle.contains("active"));

                // Получим title с вкладки браузера
                String pageTitle = driver.getTitle();

                // Обновляем страницу
                driver.navigate().refresh();

                // Вариант проверки путем считывания аттрибута класса активного элемента
                WebElement afterRefresh = driver.findElement(By.xpath(xpath));
                String afterRefreshTitle = afterRefresh.getAttribute("class");
                Assert.assertTrue(afterRefreshTitle.contains("active"));

                // Вариант проверки путем считывания значения title с вкладки браузера
                String refreshedTitle = driver.getTitle();
                Assert.assertEquals(pageTitle, refreshedTitle, "Titles are different!!!");

                System.out.println("--------------------------------------");
            }

            // Выходим из Админ панели
            driver.findElement(By.id("employee_infos")).click();
            driver.findElement(By.id("header_logout")).click();
        }finally {
            driver.quit();
        }
    }


    public static WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");
        return new ChromeDriver();
    }
}

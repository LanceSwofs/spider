import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangxiangyi on 2018/4/13/013.
 */
public class SpiderTest {

    @Test
    public void test() throws IOException {
        String chromeDriverPath = "res\\chromedriver.exe";
//        String chromeDriverPath = "res\\chromedriver";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
//        System.setProperty("http.proxyHost", "127.0.0.1");
//        System.setProperty("http.proxyPort", "80");
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.managed_default_content_settings.images", 2);
        options.setExperimentalOption("prefs", prefs);
        //设置headless模式启动chrome
//        options.addArguments("--headless");
//        options.addArguments("--disable-gpu");
//        options.addArguments("--start-maximized");
        options.addArguments("--start-fullscreen");
        ChromeDriver driver = new ChromeDriver(options);
        System.out.println("↑↑↑chromedriver version↑↑↑");
        String baseUrl = "https://www.tianyancha.com/company/2448279431";
//        chromeDriver.manage().window().maximize();
        driver.manage().timeouts().setScriptTimeout(5L, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
//        int width = driver.executeScript("return Math.max(document.body.scrollWidth, document.ssbody.offsetWi, document.documentElement.clientWidth, document.documentElement.scrollWidth, document.documentElement.offsetWidth);");
//        int height = driver.executeScript("return Math.max(document.body.scrollHeight, document.body.offsetHe, document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight);");
        System.out.println("=============START=============");
        driver.get(baseUrl);
        //屏幕截图
//        driver.findElement(By.cssSelector("#tab-item-4")).click();
        WebElement webElement = driver.findElement(By.cssSelector("#_container_baseInfo > table:nth-child(1) > thead > tr > td:nth-child(1)"));
        int y = webElement.getLocation().y;
        driver.executeScript("window.scrollTo(0, "+ y +")");
        File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//        String text = webElement.getText();
//        System.out.println("=============爬取得文本为：=============" + text);
        File newFile = new File("res\\screenshot" + File.separator + file.getName());
        System.out.println(Files.copy(file.toPath(), newFile.toPath()));
        String canonicalPath = file.getCanonicalPath();
        System.out.println(canonicalPath);
        driver.quit();
    }
}

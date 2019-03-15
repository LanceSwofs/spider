import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangxiangyi on 2018/4/13/013.
 */
public class SpiderUtil {

    private static final String CHROME_DRIVER_PATH = "\\".equals(System.getProperty("file.separator"))
        ? "res" + File.separator + "chromedriver.exe"
        : "res" + File.separator + "chromedriver";
    private static final String USER_AGENT = "\\".equals(System.getProperty("file.separator"))
        ? "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36"
        : "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36";
    private static final String SCREENSHOT_DIR = "res" + File.separator + "screenshot";
    private static final String IMG_FORMAT = "png";

    /**
     * 页面元素截屏保存
     * @param url 页面地址
     * @param dirName 保存的目录名称
     * @param selectorList 选择器列表
     * @param positioningSelector 定位元素
     * @param high 定位元素距离页面上沿高度
     */
    public static void elementScreenshot(String url, String dirName, List<Map<String, Object>> selectorList,
                                  String positioningSelector, int high) {
        ChromeDriver driver = initDriver();
        driver.get(url);
        System.out.println(driver.getPageSource());
        //滚动页面屏幕截图
        WebElement webElement = driver.findElement(By.cssSelector(positioningSelector));
        int scrollDistance = webElement.getLocation().y - high;
        driver.executeScript("window.scrollTo(0, "+ scrollDistance +")");
        File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        selectorList.forEach(selectorMap -> {
            String imgPath = saveEleImg(driver, selectorMap.get("css_selector").toString(), scrollDistance, screenshot
                    , dirName, selectorMap.get("key").toString());
        });
        driver.quit();
    }

    /**
     * 初始化driver
     * @return
     */
    private static ChromeDriver initDriver() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.managed_default_content_settings.images", 2);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--proxy-server=http://" + "119.123.173.127:9797");
        //设置headless模式
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
//        options.addArguments("--no-sandbox");
//        options.addArguments("--allow-insecure-localhost");
//        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--window-size=1600,900");
//        options.addArguments("--start-maximized");
//        options.addArguments("--remote-debugging-port=9222");
        options.addArguments("--user-agent='" + USER_AGENT + "'");
        //设置全屏模式
        options.addArguments("--start-fullscreen");
        ChromeDriver driver = new ChromeDriver(options);
        System.out.println("↑↑↑chromedriver version↑↑↑");
        driver.manage().timeouts().setScriptTimeout(5L, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
        System.out.println("=============START=============");
        return driver;
    }

    /**
     * 保存单个页面元素图片
     * @param driver 驱动
     * @param cssSelector 元素选择器
     * @param scrollDistance 页面滚动距离
     * @param screenshot 整页截图
     * @param dirName 保存目录名称
     * @param imageName 保存图片名称
     * @return 图片保存路径,失败返回null
     * @throws IOException
     */
    private static String saveEleImg(ChromeDriver driver, String cssSelector, int scrollDistance, File screenshot
            , String dirName, String imageName) {
        WebElement ele = driver.findElement(By.cssSelector(cssSelector));
        // Get the location of element on the page
        Point point = ele.getLocation();
        // Get width and height of the element
        int eleWidth = ele.getSize().getWidth();
        int eleHeight = ele.getSize().getHeight();
        try {
            // Crop the entire page screenshot to get only element screenshot
            BufferedImage fullImg = ImageIO.read(screenshot);
            BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY()- scrollDistance,
                    eleWidth, eleHeight);
            File imgDir = new File(SCREENSHOT_DIR + File.separator + dirName);
            if (!imgDir.exists()){
                System.out.println("mkdir?" + imgDir.mkdir());
            }
            File file = new File(imgDir.getPath() + File.separator + imageName + "." + IMG_FORMAT);
            // Copy the element screenshot to disk
            ImageIO.write(eleScreenshot, IMG_FORMAT, file);
            System.out.println("create new image file ?" + file.createNewFile());
            return file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

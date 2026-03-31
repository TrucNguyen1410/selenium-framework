package framework.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverFactory {
    public static WebDriver createDriver(String browser) {
        String gridUrl = System.getProperty("grid.url");
        boolean isCI = System.getenv("CI") != null; // Nhận diện GitHub Actions

        // Chạy trên Selenium Grid (Bài 4)
        if (gridUrl != null && !gridUrl.isBlank()) {
            return createRemoteDriver(browser, gridUrl);
        }
        
        // Chạy Local hoặc CI (Bài 1)
        return switch (browser.toLowerCase()) {
            case "firefox" -> createFirefoxDriver(isCI);
            default -> createChromeDriver(isCI);
        };
    }

    private static WebDriver createRemoteDriver(String browser, String gridUrl) {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName(browser.toLowerCase());

        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
            caps.merge(options);
        }
        try {
            URL gridEndpoint = new URL(gridUrl + "/wd/hub");
            RemoteWebDriver driver = new RemoteWebDriver(gridEndpoint, caps);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            System.out.println("[Grid] Đã tạo session trên Grid thành công!");
            return driver;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Grid URL không hợp lệ: " + gridUrl);
        }
    }

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
            System.out.println("[Driver] Chạy Chrome HEADLESS (CI mode)");
        } else {
            options.addArguments("--start-maximized");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }
}
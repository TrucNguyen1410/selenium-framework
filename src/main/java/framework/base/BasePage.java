package framework.base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import framework.config.ConfigReader;
import java.time.Duration;

// Bài 1: Lớp nền tảng chứa các hàm thao tác chung (Explicit Waits)
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        int waitTime = ConfigReader.getInstance().getExplicitWait();
        // Cài đặt thời gian chờ mặc định (Explicit Wait)
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
        PageFactory.initElements(driver, this);
    }

    // Hàm chờ element có thể click được rồi mới click
    protected void waitAndClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    // Hàm chờ element hiển thị, xóa dữ liệu cũ rồi mới nhập text
    protected void waitAndType(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    // Hàm chờ element hiển thị rồi lấy text (đã cắt khoảng trắng 2 đầu)
    protected String getText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText().trim();
    }

    // Hàm kiểm tra element có hiển thị hay không (bắt lỗi StaleElement)
    protected boolean isElementVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
            return false;
        }
    }

    // Hàm cuộn trang tới vị trí của element bằng Javascript
    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // Hàm chờ toàn bộ trang tải xong
    protected void waitForPageLoad() {
        wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
    }

    // Hàm lấy giá trị thuộc tính (attribute) của element
    protected String getAttribute(WebElement element, String attr) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getAttribute(attr);
    }
}
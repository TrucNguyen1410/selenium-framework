package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

// Bài 2: Page Object cho trang giỏ hàng
public class CartPage extends BasePage {

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".cart_button")
    private List<WebElement> removeButtons;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getItemCount() {
        return cartItems.size(); // Xử lý trả về 0 nếu không có item
    }

    public CartPage removeFirstItem() {
        if (!removeButtons.isEmpty()) {
            waitAndClick(removeButtons.get(0));
        }
        return this;
    }
    
    public void goToCheckout() {
        waitAndClick(checkoutButton);
    }
}
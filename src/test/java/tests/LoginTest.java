package tests;

import framework.base.BaseTest;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.ExcelReader;
import framework.utils.JsonReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class LoginTest extends BaseTest {

    // DataProvider thuần
    @DataProvider(name = "loginData")
    public Object[][] provideLoginData() {
        return new Object[][]{
            {"standard_user", "secret_sauce", "SUCCESS", "Đăng nhập hợp lệ"}
        };
    }

    @Test(dataProvider = "loginData")
    public void testLoginBasic(String username, String password, String expected, String desc) {
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login(username, password);
        Assert.assertTrue(inventoryPage.isLoaded(), "Trang inventory chưa load cho user: " + desc);
    }

    // DataProvider từ JSON (Bài 4)
    @DataProvider(name = "jsonUsers")
    public Object[][] getUsersFromJson() throws IOException {
        List<JsonReader.UserData> users = JsonReader.readUsers("src/test/resources/testdata/users.json");
        return users.stream()
                .map(u -> new Object[]{u.username, u.password, u.expectSuccess})
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "jsonUsers")
    public void testLoginFromJson(String username, String password, boolean expectSuccess) {
        LoginPage loginPage = new LoginPage(getDriver());
        if (expectSuccess) {
            InventoryPage inventoryPage = loginPage.login(username, password);
            Assert.assertTrue(inventoryPage.isLoaded());
        } else {
            loginPage.loginExpectingFailure(username, password);
            Assert.assertTrue(loginPage.isErrorDisplayed());
        }
    }
    
    // Bài 6: Demo Flaky Test
    private static int callCount = 0;
    @Test(description = "Test mô phỏng flaky fail 1 lần đầu, pass lần thứ 2")
    public void testFlakyScenario() {
        callCount++;
        System.out.println("[FlakyTest] Đang chạy lần thứ: " + callCount);
        if (callCount <= 1) {
            Assert.fail("Mô phỏng lỗi mạng tạm thời lần " + callCount);
        }
        Assert.assertTrue(true, "Test pass ở lần thứ " + callCount);
    }
}
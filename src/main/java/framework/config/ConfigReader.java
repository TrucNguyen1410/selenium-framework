package framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

// Cập nhật cho Lab 11: Hỗ trợ bảo mật Credential (GitHub Secrets)
public class ConfigReader {
    private static final Properties props = new Properties();
    private static ConfigReader instance;

    private ConfigReader() {
        // Lấy biến môi trường "env", mặc định là "dev"
        String env = System.getProperty("env", "dev");
        String file = "src/test/resources/config-" + env + ".properties";
        try (FileInputStream fis = new FileInputStream(file)) {
            props.load(fis);
            System.out.println("[ConfigReader] Đang dùng môi trường: " + env);
        } catch (IOException e) {
            throw new RuntimeException("Không tìm thấy config: " + file);
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String getBaseUrl() { 
        return props.getProperty("base.url", "https://www.saucedemo.com"); 
    }
    
    public String getBrowser() { 
        return props.getProperty("browser", "chrome"); 
    }
    
    public int getExplicitWait() { 
        return Integer.parseInt(props.getProperty("explicit.wait", "15")); 
    }
    
    public int getRetryCount() { 
        return Integer.parseInt(props.getProperty("retry.count", "1")); 
    }
    
    public String getScreenshotPath() { 
        return props.getProperty("screenshot.path", "target/screenshots/"); 
    }

    // Bài 3 (Lab 11): Đọc Username bảo mật
    public String getAppUsername() {
        // Ưu tiên đọc từ biến môi trường (khi chạy trên GitHub Actions CI/CD)
        String user = System.getenv("APP_USERNAME");
        if (user == null || user.isBlank()) {
            // Fallback: đọc từ file config-dev.properties (khi chạy Local trên máy)
            user = props.getProperty("app.username");
        }
        return user;
    }

    // Bài 3 (Lab 11): Đọc Password bảo mật
    public String getAppPassword() {
        // Ưu tiên đọc từ biến môi trường (khi chạy trên GitHub Actions CI/CD)
        String pass = System.getenv("APP_PASSWORD");
        if (pass == null || pass.isBlank()) {
            // Fallback: đọc từ file config-dev.properties (khi chạy Local trên máy)
            pass = props.getProperty("app.password");
        }
        return pass;
    }
}
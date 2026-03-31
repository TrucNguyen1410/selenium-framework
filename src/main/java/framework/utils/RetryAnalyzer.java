package framework.utils;

import framework.config.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

// Bài 6: Retry Analyzer tự động chạy lại test bị fail
public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        int maxRetry = ConfigReader.getInstance().getRetryCount();
        if (retryCount < maxRetry) {
            retryCount++;
            System.out.println("[Retry] Đang chạy lại lần " + retryCount + " cho test: " + result.getName());
            return true;
        }
        return false;
    }

    // Listener đi kèm để tự động Apply RetryAnalyzer cho mọi Test method
    public static class RetryListener implements IAnnotationTransformer {
        @Override
        public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
        }
    }
}
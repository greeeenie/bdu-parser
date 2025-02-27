package org.example.Service;

import lombok.RequiredArgsConstructor;
import org.example.Config.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageFetcher {
    private final WebDriverManager webDriverManager;

    public String fetchPage(String url, WebDriverManager webDriverManager) {
        WebDriver driver = webDriverManager.createWebDriver();
        try {
            driver.get(url);
            return driver.getPageSource();
        } finally {
            driver.quit();
        }
    }
}

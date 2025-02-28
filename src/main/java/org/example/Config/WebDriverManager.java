package org.example.Config;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebDriverManager {
    public WebDriver createWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--ignore-ssl-errors");
        options.addArguments("--headless");

        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            System.setProperty("webdriver.chrome.driver", "selenium\\chromedriver.exe");
        } else if (os.contains("Mac")) {
            System.setProperty("webdriver.chrome.driver", "selenium\\chromedriver");
        }
        return new ChromeDriver(options);
    }
}

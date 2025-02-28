package org.example.Config;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.nio.file.Files;

@EnableConfigurationProperties(AppConfig.AppProperties.class)
@Configuration
public class AppConfig {

    @Getter
    @Setter
    @ConfigurationProperties("app")
    public static class AppProperties {

        private final String report;

        @SneakyThrows
        public AppProperties(File report) {
            this.report = String.join("", Files.readAllLines(report.toPath()));
        }
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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

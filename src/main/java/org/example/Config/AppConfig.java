package org.example.Config;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
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
}

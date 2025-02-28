package org.example.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class PageFetcher {

    private final WebDriver webDriver;

    public Document getDocument(String url) {
        log.info("Send request: {}", url);
        webDriver.get(url);
        String html = webDriver.getPageSource();
        if (html == null) return null;
        return Jsoup.parse(html);
    }
}

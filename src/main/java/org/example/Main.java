package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org. jsoup. select. Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

@Component
public class Main {

    public static String extractCwes(String bduId) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--ignore-ssl-errors");
        System.setProperty("webdriver.chrome.driver", "selenium\\chromedriver.exe");
        WebDriver driver = new ChromeDriver(options);

        StringBuilder fstecUrl = new StringBuilder("https://bdu.fstec.ru/vul/");
        fstecUrl.append(bduId);
        driver.get(String.valueOf(fstecUrl));
        String fstecHtml =  driver.getPageSource();
        driver.quit();

        Document doc = Jsoup.parse(fstecHtml);
        Element table = doc.select("table").get(0);
        Elements rows = table.select("tr");
        Element row = rows.get(7);
        Elements cols = row.select("td");
        Element col = cols.get(1).selectFirst("a");
        String href = col.attr("href");

        return href;
    }

    public static ArrayList<String> extractBduIds() {
        File input = new File("report.html");
        try {
            ArrayList<String> bduIds = new ArrayList<String>();
            Document doc = Jsoup.parse(input);
            Elements bdus = doc.getElementsByClass("bdu");
            for (Element bdu : bdus) {
                bduIds.add(bdu.html().substring(4, 14));
            }
            return bduIds;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        ArrayList<String> bduIds = extractBduIds();
        System.out.printf("Найдено %d уникальных BDU идентификаторов", bduIds.size());
        System.out.println("");
//        for (String bduId : bduIds) {
//            extractCwes(bduId);
//        }
        System.out.println(extractCwes(bduIds.get(0)));





    }
}

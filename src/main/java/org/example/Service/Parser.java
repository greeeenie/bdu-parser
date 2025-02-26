package org.example.Service;

import lombok.RequiredArgsConstructor;
import org.example.Model.CweData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Parser {
    private final PageFetcher pageFetcher;

    public List<String> extractBduIds() {
        File input = new File("report.html");
        try {
            List<String> bduIds = new ArrayList<String>();
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

    public List<CweData> extractCwes(String bduId) {

        StringBuilder fstecUrl = new StringBuilder("https://bdu.fstec.ru/vul/");
        fstecUrl.append(bduId);
        String fstecHtml =  pageFetcher.fetchPage(String.valueOf(fstecUrl));

        Document doc = Jsoup.parse(fstecHtml);
        Element cwe = doc.select("table").get(0).selectFirst("a");

        List<CweData> cweData = new ArrayList<>();
        cweData.add(new CweData(cwe.text(), cwe.attr("href")));

        return cweData;
    }
}

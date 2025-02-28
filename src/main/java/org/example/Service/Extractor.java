package org.example.Service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class Extractor {

    public int extractBdusCount(Document doc) {
        return doc.getElementsByClass("bdu").size();
    }

    public String extractBduName(Document doc, int index) {
        return doc.getElementsByClass("bdu")
                .get(index)
                .text();
    }

    public String extractBduId(Document doc, int index) {
        return doc.getElementsByClass("bdu")
                .get(index)
                .text()
                .substring(4, 14);
    }

    public String extractBduDesc(Document doc, int index) {
        return doc.getElementsByClass("desc")
                .get(index)
                .text();
    }

    public String extractCweName(Document doc) {
        return doc.select("table")
                .get(0)
                .selectFirst("a")
                .text();
    }

    public String extractCweId(Document doc) {
        return doc.selectXpath("//a[contains(text(), 'CWE')]")
                .first()
                .text();
    }

    public String extractCweUri(Document doc) {
        return doc.selectXpath("//a[contains(text(), 'CWE')]")
                .first()
                .attr("href");
    }

    public List<String> extractCapecIds(Document doc) {
        Element table = doc.getElementById("Related_Attack_Patterns");
        if (table != null) {
            return table.getElementsByTag("a")
                    .stream()
                    .map(Element::text)
                    .toList();
        }
        return List.of();
    }

    public String extractCapecUri(Document doc, int index) {
        Element table = doc.getElementById("Related_Attack_Patterns");
        if (table != null) {
            return table.getElementsByTag("a")
                    .get(index + 1)
                    .attr("href");
        }
        return null;
    }

    public String extractCapecName(Document doc, int index) {
        Element table = doc.getElementsByClass("noprint").get(3);
        int colonIndex = table.getElementsByTag("h2")
                .text()
                .lastIndexOf(":");
        if (table != null) {
            return table.getElementsByTag("h2")
                    .text()
                    .substring(colonIndex + 2);
        }
        return null;
    }

    public String extractCapecLikelihood(Document doc) {
        Element element = doc.getElementById("Likelihood_Of_Attack");
        return element != null
                ? element
                    .selectFirst("p")
                    .text()
                : "No chance";
    }
}

package org.example.Service;

import lombok.RequiredArgsConstructor;
import org.example.Config.AppConfig;
import org.example.Config.WebDriverManager;
import org.example.Model.Bdu;
import org.example.Model.Capec;
import org.example.Model.Cwe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class Parser {

    private final WebDriverManager webDriverManager;
    private final PageFetcher pageFetcher;
    private final Extractor extractor;
    private final UriBuilder uriBuilder;
    private final AppConfig.AppProperties appProperties;

    public List<Bdu> parseReport() {
        Document doc = Jsoup.parse(appProperties.getReport());
        return IntStream.range(0, extractor.extractBdusCount(doc))
                .mapToObj(index -> {
                            String bduId = extractor.extractBduId(doc, index);
                            return Bdu.builder()
                                    .id(bduId)
                                    .name(extractor.extractBduName(doc, index))
                                    .desc(extractor.extractBduDesc(doc, index))
                                    .cwe(parseCwe(bduId))
                                    .build();
                        }
                )
                .peek(System.out::println)
                .toList();
    }

    private Cwe parseCwe(String bduId) {
        Document bduDoc = getDocument(uriBuilder.bdu(bduId));
        String cweId = extractor.extractCweId(bduDoc);
        Document cweDoc = getDocument(uriBuilder.cwe(cweId));
        List<String> capecIds = extractor.extractCapecIds(cweDoc);
        return Cwe.builder()
                .id(cweId)
                .name(extractor.extractCweName(bduDoc))
                .uri(extractor.extractCweUri(bduDoc))
                .capecs(parseCapecs(capecIds, cweDoc))
                .build();
    }

    private List<Capec> parseCapecs(List<String> capecIds, Document cweDoc) {
        return IntStream.range(0, capecIds.size())
                .mapToObj(index -> {
                    String capecId = capecIds.get(index);
                    String capecUri = extractor.extractCapecUri(cweDoc, index);
                    if (capecUri == null) return null;
                    Document capecDoc = getDocument(capecUri);
                    return Capec.builder()
                            .id(capecId)
                            .uri(capecUri)
                            .likelihood(extractor.extractCapecLikelihood(capecDoc))
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private Document getDocument(String uri) {
        String html = pageFetcher.fetchPage(uri, webDriverManager);
        return Jsoup.parse(html);
    }
}

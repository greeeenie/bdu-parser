package org.example.Service;

import lombok.extern.slf4j.Slf4j;
import org.example.Config.AppConfig;
import org.example.Model.Bdu;
import org.example.Model.Capec;
import org.example.Model.Cwe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
@Slf4j
public class Parser {

    private static final Integer THREADS_COUNT = 3;

    private final Extractor extractor;
    private final UriBuilder uriBuilder;
    private final AppConfig.AppProperties appProperties;
    private final Map<Integer, PageFetcher> fetchersMap;

    public Parser(
            Extractor extractor,
            UriBuilder uriBuilder,
            AppConfig.AppProperties appProperties,
            ObjectProvider<PageFetcher> pageFetchersProvider
    ) {
        this.extractor = extractor;
        this.uriBuilder = uriBuilder;
        this.appProperties = appProperties;
        this.fetchersMap = IntStream.range(0, THREADS_COUNT)
                .mapToObj(index -> Map.entry(index, pageFetchersProvider.getObject()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private final ExecutorService bduExecutor = new ThreadPoolExecutor(
            THREADS_COUNT,
            THREADS_COUNT,
            1000,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000)
    );

    public List<Bdu> parseReport() {
        Document doc = Jsoup.parse(appProperties.getReport());
        int bdusCount = extractor.extractBdusCount(doc);
        return IntStream.range(0, bdusCount)
                .mapToObj(index -> {
                            String bduId = extractor.extractBduId(doc, index);
                            PageFetcher pageFetcher = fetchersMap.get(index % THREADS_COUNT);
                            return bduExecutor.submit(() -> Bdu.builder()
                                    .id(bduId)
                                    .name(extractor.extractBduName(doc, index))
                                    .desc(extractor.extractBduDesc(doc, index))
                                    .cwe(parseCwe(bduId, pageFetcher))
                                    .build()
                            );
                        }
                )
                .toList()
                .stream()
                .flatMap(futureGetOrSkipOnError())
                .toList();
    }

    private Cwe parseCwe(String bduId, PageFetcher pageFetcher) {
        String bduUri = uriBuilder.bdu(bduId);
        Document bduDoc = pageFetcher.getDocument(bduUri);
        log.info("Received bdu: {}", bduUri);
        String cweId = extractor.extractCweId(bduDoc);
        String cweUri = uriBuilder.cwe(cweId);
        Document cweDoc = pageFetcher.getDocument(cweUri);
        log.info("Received cwe: {}", cweUri);
        List<String> capecIds = extractor.extractCapecIds(cweDoc);
        return Cwe.builder()
                .id(cweId)
                .name(extractor.extractCweName(bduDoc))
                .uri(extractor.extractCweUri(bduDoc))
                .capecs(parseCapecs(capecIds, cweDoc, pageFetcher))
                .build();
    }

    private List<Capec> parseCapecs(List<String> capecIds, Document cweDoc, PageFetcher pageFetcher) {
        return IntStream.range(0, capecIds.size())
                .mapToObj(index -> {
                            String capecId = capecIds.get(index);
                            String capecUri = extractor.extractCapecUri(cweDoc, index);
                            if (capecUri == null) return null;
                            Document capecDoc = pageFetcher.getDocument(capecUri);
                            log.info("Received capec: {}", capecUri);
                            String capecName = extractor.extractCapecName(capecDoc, index);
                            return Capec.builder()
                                    .id(capecId)
                                    .name(capecName)
                                    .uri(capecUri)
                                    .likelihood(extractor.extractCapecLikelihood(capecDoc))
                                    .build();
                        })
                .toList();
    }

    private static <T> Function<Future<T>, Stream<T>> futureGetOrSkipOnError() {
        return future -> {
            try {
                return Stream.of(future.get());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Stream.empty();
            }
        };
    }
}

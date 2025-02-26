package org.example.Runner;

import lombok.AllArgsConstructor;

import org.example.Model.BduData;
import org.example.Service.Parser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ParserRunner implements CommandLineRunner {
    private final Parser parser;

    @Override
    public void run(String... args) throws Exception {
        List<String> bduData = parser.extractBduIds();
        System.out.printf("Найдено %d уникальных BDU идентификаторов", bduData.size());
        System.out.println("");
        for (String bdu : bduData) {
            System.out.println(parser.extractCwes(bdu));
        }
    }
}

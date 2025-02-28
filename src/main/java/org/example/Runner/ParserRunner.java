package org.example.Runner;

import lombok.AllArgsConstructor;

import org.example.Service.Parser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ParserRunner implements CommandLineRunner {

    private final Parser parser;

    @Override
    public void run(String... args) {
        parser.parseReport();
    }
}

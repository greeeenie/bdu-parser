package org.example.Service;

import org.springframework.stereotype.Component;

@Component
public class UriBuilder {

    public String bdu(String bduId) {
        return "https://bdu.fstec.ru/vul/" + bduId;
    }

    public String cwe(String cweId) {
        return "https://cwe.mitre.org/data/definitions/" + cweId +  ".html";
    }
}

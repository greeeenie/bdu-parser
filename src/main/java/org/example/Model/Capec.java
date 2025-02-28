package org.example.Model;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
public class Capec {

    private String id;
    private String name;
    private String likelihood;
    private String uri;
    private Cwe cwe;

    @Override
    public String toString() {
        return "Capec{" + '\n' +
                "id='" + id + '\'' + '\n' +
                "name='" + name + '\'' + '\n' +
                "likelihood='" + likelihood + '\'' + '\n' +
                "uri='" + uri + '\'' + '\n' +
                '}' + '\n';
    }
}

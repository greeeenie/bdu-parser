package org.example.Model;


import lombok.*;

@Builder
@Getter
@AllArgsConstructor
public class Capec {

    private final String id;
    private final String name;
    @Setter
    private String likelihood;
    private final String uri;

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

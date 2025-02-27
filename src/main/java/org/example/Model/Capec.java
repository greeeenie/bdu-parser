package org.example.Model;


import lombok.*;

@Builder
@Getter
@AllArgsConstructor
public class Capec {

    private final String id;
    @Setter
    private String likelihood;
    private final String uri;

    @Override
    public String toString() {
        return "Capec{" + '\n' +
                "id='" + id + '\'' + '\n' +
                "likelihood='" + likelihood + '\'' + '\n' +
                "uri='" + uri + '\'' + '\n' +
                '}' + '\n';
    }
}

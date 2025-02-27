package org.example.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class Cwe {

    private String id;
    private String name;
    private String uri;
    private List<Capec> capecs;

    @Override
    public String toString() {
        return "Cwe{" + '\n' +
                "id='" + id + '\'' + '\n' +
                "name='" + name + '\'' + '\n' +
                "uri='" + uri + '\'' + '\n' +
                "capecs=" + capecs + '\n' +
                '}' + '\n';
    }
}

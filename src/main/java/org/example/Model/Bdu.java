package org.example.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
public class Bdu {

    private String id;
    private String name;
    private String desc;
    private Cwe cwe;

    @Override
    public String toString() {
        return "Bdu{" + '\n' +
                "id='" + id + '\'' + '\n' +
                "name='" + name + '\'' + '\n' +
                "desc='" + desc + '\'' + '\n' +
                "cwe=" + cwe + '\n' +
                '}' + '\n';
    }
}

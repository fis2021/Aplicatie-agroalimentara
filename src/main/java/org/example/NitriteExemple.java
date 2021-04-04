package org.example;

public class NitriteExemple {

    Nitrite db = Nitrite.builder()
            .compressed()
            .filePath("/tmp/test.db")
            .openOrCreate("user", "password");
}

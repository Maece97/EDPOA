package ch.unisg.preprocessing.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class StringTest {
    private String s1;
    private String s2;


    public StringTest(String s, String er) {
        this.s1 = s;
        this.s2 = er;
    }
}

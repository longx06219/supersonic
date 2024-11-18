package com.tencent.supersonic.headless.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchemaElementMatch implements Serializable {
    private SchemaElement element;
    private double offset;
    private double similarity;
    private String detectWord;
    private String word;
    private Long frequency;
    private boolean isInherited;

    public boolean isFullMatched() {
        return 1.0 == similarity;
    }
}

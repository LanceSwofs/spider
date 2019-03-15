package service.third.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordsResult {
    private Probability probability;
    private String words;
}

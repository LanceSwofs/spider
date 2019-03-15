package service.third.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommonResult {
    private String log_id;
    private List<WordsResult> words_result;
    private int words_result_num;
    private double direction;
}
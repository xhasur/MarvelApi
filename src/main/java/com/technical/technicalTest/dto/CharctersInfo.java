package com.technical.technicalTest.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class CharctersInfo {
    @Getter
    @Setter
    private String count;

    @Getter
    @Setter
    private String total;

    @Getter
    @Setter
    private List<MarvelCharacter>  results;

}




package com.technical.technicalTest.service;

import com.technical.technicalTest.dto.MarvelCharacter;

import java.util.List;

public interface MarvelService {

     List<String> getCharactersId();

     List<String>  getCharactersIdWithLimits(String limit , String offset);

     List<MarvelCharacter> getCharacterById(Long characterId);
}


package com.technical.technicalTest.service;

import com.technical.technicalTest.dto.MarvelCharacter;

import java.util.List;

public interface MarvelService {

     Integer  getTotal();

     List<String>  getCharactersIdWithLimits(String limit , String offset);

     MarvelCharacter getCharacterById(Long characterId);

}


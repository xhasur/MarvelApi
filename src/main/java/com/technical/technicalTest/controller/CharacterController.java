package com.technical.technicalTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technical.technicalTest.dto.MarvelCharacter;
import com.technical.technicalTest.service.MarvelService;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    private final MarvelService marvelService;

    public CharacterController(MarvelService marvelService) {
        this.marvelService = marvelService;
    }


    @GetMapping("")
    public List<String> getCharactersIds(){
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> ids = new ArrayList<>();
        try {
             ids = objectMapper.readValue(new File("marvel-ids.json"), List.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ids;
    }

    @GetMapping("/{characterId}")
    public List<MarvelCharacter> getCharactersIds(@PathVariable Long characterId, @RequestParam(name = "language" ,  required = false) String language ){
        List<MarvelCharacter> character = marvelService.getCharacterById(characterId);
        MarvelCharacter marvelCharacter = character.get(0);
        if(language != null){
            marvelCharacter.setDescription("");
        }
        return character;

    }


}

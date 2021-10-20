package com.technical.technicalTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technical.technicalTest.dto.MarvelCharacter;
import com.technical.technicalTest.service.MarvelService;
import com.technical.technicalTest.service.TranslateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    private final MarvelService marvelService;
    private final TranslateService translateService;
    private final Logger log = LoggerFactory.getLogger(CharacterController.class);

    public CharacterController(MarvelService marvelService, TranslateService translateService) {
        this.marvelService = marvelService;
        this.translateService = translateService;
    }


    @GetMapping("")
    @ApiOperation("Search a list of characterIds")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Product not found"),
    })
    public ResponseEntity<List<String>> getCharactersIds() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> ids = new ArrayList<>();
        try {
            ids = objectMapper.readValue(new File("marvel-ids.json"), List.class);

        } catch (IOException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @GetMapping("/{characterId}")
    @ApiOperation("Get a character by the id code")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Character not found"),
    })
    public ResponseEntity<MarvelCharacter> getCharactersIds(@ApiParam(value = "The id of the character", required = true, example = "1017100")
                                                            @PathVariable Long characterId,
                                                            @RequestParam(name = "language", required = false) String language) {
        MarvelCharacter character = marvelService.getCharacterById(characterId);
        if (character == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (language != null) {
            String oldDescription = character.getDescription();
            if(!oldDescription.isEmpty()){
                character.setDescription(translateService.getTranslate(oldDescription, language));
            }
        }
        return new ResponseEntity<>(character, HttpStatus.OK);

    }


}

package com.technical.technicalTest.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technical.technicalTest.service.MarvelService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class LoadCharacters
        implements ApplicationListener<ApplicationReadyEvent> {

    private final MarvelService marvelService;


    public LoadCharacters(MarvelService marvelService) {
        this.marvelService = marvelService;
    }


    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            List<String> idsLoaded = new ArrayList<>();
            String offset= String.valueOf(0);
            idsLoaded   = marvelService.getCharactersIdWithLimits("100" , offset);
            ids.addAll(idsLoaded);
        }
        try {
            objectMapper.writeValue(new File("marvel-ids.json"), ids);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

}
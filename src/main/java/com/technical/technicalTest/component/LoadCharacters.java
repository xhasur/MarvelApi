package com.technical.technicalTest.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technical.technicalTest.service.MarvelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(LoadCharacters.class);

    private final MarvelService marvelService;
    private final String LIMIT = "100";


    public LoadCharacters(MarvelService marvelService) {
        this.marvelService = marvelService;
    }


    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        ObjectMapper objectMapper = new ObjectMapper();
        int total = marvelService.getTotal();
        int totalLoops = total / 100;
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < totalLoops; i++) {
            List<String> idsLoaded;
            String offset= String.valueOf(i);
            idsLoaded   = marvelService.getCharactersIdWithLimits(LIMIT , offset);
            ids.addAll(idsLoaded);
        }
        try {

            log.debug("Adding Ids");
            objectMapper.writeValue(new File("marvel-ids.json"), ids);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

}
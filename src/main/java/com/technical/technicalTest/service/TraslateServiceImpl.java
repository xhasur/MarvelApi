package com.technical.technicalTest.service;

import com.technical.technicalTest.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TraslateServiceImpl implements TraslateService{

    final static Logger logger = LoggerFactory.getLogger(TraslateServiceImpl.class);

    @Value("${traslate.api.key}")
    private String apiKey;

    @Value("${traslate.base.url}")
    private String baseUrl;

    @Autowired
    private RestTemplate template;


    @Override
    public String getTraslate(String text , String lang) {
        logger.info("Traslating...");
        StringBuilder url = new StringBuilder(baseUrl);
        url.append(Constants.TRANSLATION_URL);
        url.append(Constants.KEY).append(Constants.EQUAL_SIGN).append(apiKey).append(Constants.AMPERSAND_SIGN);
        url.append(Constants.TEXT_TO_TRANSLATE).append(Constants.EQUAL_SIGN).append(text).append(Constants.AMPERSAND_SIGN);
        url.append(Constants.TARGET_LANGUAGE).append(Constants.EQUAL_SIGN).append(lang);
        ResponseEntity<String> translation = template.getForEntity(url.toString(), String.class );
        return translation.getBody();

    }
}

package com.technical.technicalTest.service;

import com.technical.technicalTest.dto.MarvelCharacter;
import com.technical.technicalTest.dto.ResponseMarvel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarvelServiceImpl implements MarvelService {

    private final Logger log = LoggerFactory.getLogger(MarvelServiceImpl.class);

    private final String publicKey;

    private final String privateKey;

    private final String marvelUrl;

    private final RestTemplate template;

    public MarvelServiceImpl(@Value("${value.publicKey}") String publicKey,
                             @Value("${value.privateKey}") String privateKey,
                             @Value("${value.url}") String marvelUrl,
                             RestTemplate template) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.marvelUrl = marvelUrl;
        this.template = template;
    }


    public Integer getTotal() {
        final String url = marvelUrl + "characters?ts=" + this.getUrlHash();
        ResponseMarvel serviceResponse = null;
        try {
            serviceResponse = this.getData(url);
            if (serviceResponse.getData() == null && serviceResponse.getData().getResults().isEmpty()) {
                return Integer.parseInt("0");
            }
            return Integer.parseInt(serviceResponse.getData().getTotal());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public List<String> getCharactersIdWithLimits(String limit, String offset) {
        ResponseMarvel serviceResponse = null;
        final String url = marvelUrl + "characters?ts=" + this.getUrlHash() + "&limit=" + limit + "&offset=" + offset;
        try {
            serviceResponse = this.getData(url);
            if (serviceResponse.getData() == null && serviceResponse.getData().getResults().isEmpty()) {
                return new ArrayList<>();
            }
            return serviceResponse.getData().getResults().stream().map(marvelCharacter -> marvelCharacter.getId()).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());

        }
        return null;
    }

    public MarvelCharacter getCharacterById(Long characterId) {
        ResponseMarvel serviceResponse = null;
        final String url = marvelUrl + "characters/" + characterId + "?ts=" + Calendar.getInstance().getTimeInMillis() + "&apikey=" + publicKey + "&hash=" + getHash();
        try {
            serviceResponse = this.getData(url);
            if (serviceResponse.getData() == null && serviceResponse.getData().getResults().isEmpty()) {
                return new MarvelCharacter();
            }
            return serviceResponse.getData().getResults().get(0);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    private ResponseMarvel getData(String url) throws Exception {
        return template.getForEntity(url, ResponseMarvel.class).getBody();
    }

    private String getUrlHash() {
        return Calendar.getInstance().getTimeInMillis() + "&apikey=" + publicKey + "&hash=" + getHash();
    }

    private String getHash() {
        Calendar calendar = Calendar.getInstance();
        final String stringToHash = calendar.getTimeInMillis() + privateKey + publicKey;
        return md5Java(stringToHash);
    }

    private static String md5Java(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException ex) {
        } catch (NoSuchAlgorithmException ex) {
        }
        return digest;
    }
}

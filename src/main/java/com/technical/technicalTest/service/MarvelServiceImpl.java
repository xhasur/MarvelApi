package com.technical.technicalTest.service;

import com.technical.technicalTest.dto.MarvelCharacter;
import com.technical.technicalTest.dto.ResponseMarvel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
        final String url = marvelUrl + "characters?ts=" + Calendar.getInstance().getTimeInMillis() + "&apikey=" + publicKey + "&hash=" + getHash();
        final ResponseMarvel serviceResponse = this.getData(url);
        if (serviceResponse.getData() == null && serviceResponse.getData().getResults().isEmpty()) {
            return Integer.parseInt("0");
        }
        return Integer.parseInt(serviceResponse.getData().getTotal());
    }


    public List<String> getCharactersIdWithLimits(String limit, String offset) {
        log.debug("Searching with limits and offset");
        final String url = marvelUrl + "characters?ts=" + Calendar.getInstance().getTimeInMillis() + "&apikey=" + publicKey + "&hash=" + getHash();
        final String urlWithLimits = url + "&limit=" + limit + "&offset=" + offset;
        final ResponseMarvel serviceResponse = this.getData(urlWithLimits);
        if (serviceResponse.getData() == null && serviceResponse.getData().getResults().isEmpty()) {
            return new ArrayList<>();
        }
        return serviceResponse.getData().getResults().stream().map(marvelCharacter -> marvelCharacter.getId()).collect(Collectors.toList());
    }


    public MarvelCharacter getCharacterById(Long characterId) {
        final String url = marvelUrl + "characters/" + characterId + "?ts=" + Calendar.getInstance().getTimeInMillis() + "&apikey=" + publicKey + "&hash=" + getHash();
        final ResponseMarvel serviceResponse = this.getData(url);
        if (serviceResponse.getData() == null && serviceResponse.getData().getResults().isEmpty()) {
            return new MarvelCharacter();
        }
        return serviceResponse.getData().getResults().get(0);
    }


    public List<String> getCharactersIdFromMarvelApi() {
        final String url = marvelUrl + "characters?ts=" + Calendar.getInstance().getTimeInMillis() + "&apikey=" + publicKey + "&hash=" + getHash();
        final ResponseMarvel serviceResponse = this.getData(url);
        if (serviceResponse.getData() == null && serviceResponse.getData().getResults().isEmpty()) {
            return new ArrayList<>();
        }
        return serviceResponse.getData().getResults().stream().map(marvelCharacter -> marvelCharacter.getId()).collect(Collectors.toList());
    }


    private ResponseMarvel getData(String url) {
        final ResponseEntity<ResponseMarvel> responseEntity = template.getForEntity(
                url, ResponseMarvel.class);
        return responseEntity.getBody();
    }

    /**
     * This method obtain the hash that is used in the marvel Api
     *
     * @return
     */
    private String getHash() {
        Calendar calendar = Calendar.getInstance();
        final String stringToHash = calendar.getTimeInMillis() + privateKey + publicKey;
        return md5Java(stringToHash);
    }

    /**
     * This methdd obtain the Md5 code that is necessary in the Marvel Apo
     *
     * @param message
     * @return
     */
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

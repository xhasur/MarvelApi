package com.technical.technicalTest.service;

import com.technical.technicalTest.dto.MarvelCharacter;
import com.technical.technicalTest.dto.ResponseMarvel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${value.publicKey}")
    private String publicKey;

    @Value("${value.privateKey}")
    private String privateKey;

    @Value("${value.url}")
    private String marvelUrl;

    @Autowired
    private RestTemplate template;

    /**
     * This method contains the total of characters
     * @return
     */
    public Integer getTotal() {
        final String url = marvelUrl + "characters?ts=" + Calendar.getInstance().getTimeInMillis() + "&apikey=" + publicKey + "&hash=" + getHash();
        ResponseMarvel serviceResponse = this.getData(url);
        if (serviceResponse.getData() == null && serviceResponse.getData().getResults().isEmpty()) {
            return  Integer.parseInt("0");
        }
        return Integer.parseInt(serviceResponse.getData().getTotal());
    }

    /**
     * This method get the character using the limits in the requeste
     * @param limit
     * @param offset
     * @return
     */
    public List<String> getCharactersIdWithLimits(String limit , String offset) {
        log.debug("Searching with limits and offset");
        final String url = marvelUrl + "characters?ts=" + Calendar.getInstance().getTimeInMillis() + "&apikey=" + publicKey + "&hash=" + getHash();
        final String urlWithLimits = url + "&limit="+limit+"&offset="+offset;
        ResponseMarvel serviceResponse = this.getData(urlWithLimits);
        if (serviceResponse.getData() == null && serviceResponse.getData().getResults().isEmpty()) {
            return new ArrayList<>();
        }
        return serviceResponse.getData().getResults().stream().map(marvelCharacter -> marvelCharacter.getId()).collect(Collectors.toList());
    }

    /**
     * This method obtain the character by id
     * @param characterId
     * @return
     */
    public MarvelCharacter getCharacterById(Long characterId) {
        final String url = marvelUrl + "characters/" + characterId + "?ts=" + Calendar.getInstance().getTimeInMillis() + "&apikey=" + publicKey + "&hash=" + getHash();
        ResponseMarvel serviceResponse = this.getData(url);
        if (serviceResponse.getData() == null && serviceResponse.getData().getResults().isEmpty()) {
            return new MarvelCharacter();
        }
        return serviceResponse.getData().getResults().get(0);
    }


    /**
     * This method can be used to call the Marvel endpoint dorectly
     * @return
     */
    public List<String> getCharactersId() {
        final String url = marvelUrl + "characters?ts=" + Calendar.getInstance().getTimeInMillis() + "&apikey=" + publicKey + "&hash=" + getHash();
        ResponseMarvel serviceResponse = this.getData(url);
        if (serviceResponse.getData() == null && serviceResponse.getData().getResults().isEmpty()) {
            return new ArrayList<>();
        }
        return serviceResponse.getData().getResults().stream().map(marvelCharacter -> marvelCharacter.getId()).collect(Collectors.toList());
    }


    /**
     * This method , is the method that call the endpoint of marvel
     * @param url
     * @return
     */
    private ResponseMarvel getData(String url) {
        final ResponseEntity<ResponseMarvel> responseEntity = template.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<ResponseMarvel>() {
                });
        return responseEntity.getBody();
    }

    /**
     * This method obtain the hash that is used in the marvel Api
     * @return
     */
    private String getHash() {
        Calendar calendar = Calendar.getInstance();
        final String stringToHash = calendar.getTimeInMillis() + privateKey + publicKey;
        return md5Java(stringToHash);
    }

    /**
     * This methdd obtain the Md5 code that is necessary in the Marvel Apo
     * @param message
     * @return
     */
    public static String md5Java(String message) {
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

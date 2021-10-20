package com.technical.technicalTest.service;

import com.technical.technicalTest.dto.CharctersInfo;
import com.technical.technicalTest.dto.MarvelCharacter;
import com.technical.technicalTest.dto.ResponseMarvel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;




@RunWith( MockitoJUnitRunner.class )
public class MarvelServiceTest {


    @Mock
    private MarvelService marvelService ;

    @Mock
    private RestTemplate template;


    @Test
    public void getTotal_should_be_return_0()
    {
        ResponseMarvel marvelResponse =  Mockito.mock(ResponseMarvel.class);
        Mockito.when( template.exchange(
                Mockito.any(),
                Mockito.any(HttpMethod.class),
                Mockito.any( HttpEntity.class ),
                (Class<Object>) Mockito.any())).thenReturn( new ResponseEntity( marvelResponse, HttpStatus.OK ) );

        int response = marvelService.getTotal();

        Assert.assertEquals( 0, response );
    }

    @Test
    public void getTotal_should_be_return_a_correct_value()
    {
        List<MarvelCharacter>  results = new ArrayList<>();
        MarvelCharacter character1 = new MarvelCharacter();
        character1.setDescription("Wonder-Woman");
        MarvelCharacter character2 = new MarvelCharacter();
        character2.setDescription("Spiderman");
        results.add(character1);
        results.add(character2);
        CharctersInfo characterInfo = new CharctersInfo();
        characterInfo.setResults(results);
        characterInfo.setTotal(String.valueOf(results.size()));
        ResponseMarvel marvelResponse =  new ResponseMarvel();
        marvelResponse.setData(characterInfo);

        Mockito.when( template.exchange(
                Mockito.any(),
                Mockito.any(HttpMethod.class),
                Mockito.any( HttpEntity.class ),
                (Class<Object>) Mockito.any())).thenReturn( new ResponseEntity( marvelResponse, HttpStatus.OK ) );

        int response = marvelService.getTotal();

        Assert.assertEquals( 2, response );
    }


    @Test
    public void getCharactersIdWithLimits_should_be_return_a_number_correct_of_ids()
    {
        List<MarvelCharacter>  results = new ArrayList<>();
        MarvelCharacter character1 = new MarvelCharacter();
        character1.setDescription("Wonder-Woman");
        results.add(character1);
        CharctersInfo characterInfo = new CharctersInfo();
        characterInfo.setResults(results);
        characterInfo.setTotal(String.valueOf(results.size()));
        ResponseMarvel marvelResponse =  new ResponseMarvel();
        marvelResponse.setData(characterInfo);

        Mockito.when( template.exchange(
                Mockito.any( URI.class ),
                Mockito.any(HttpMethod.class),
                Mockito.any( HttpEntity.class ),
                Mockito.any(ParameterizedTypeReference.class))).thenReturn( new ResponseEntity<>( marvelResponse, HttpStatus.OK ) );

        List<String> idsResponse = marvelService.getCharactersIdWithLimits("100", "0");

        Assert.assertEquals( 1, idsResponse.size() );
    }

    @Test
    public void getCharacterById_shoud_return_a_character() {

        MarvelCharacter character = new MarvelCharacter();
        character.setDescription("Wonder-Woman");
        character.setId("100L");

        Mockito.when( template.exchange(
                Mockito.any( URI.class ),
                Mockito.any(HttpMethod.class),
                Mockito.any( HttpEntity.class ),
                (Class<Object>) Mockito.any())).thenReturn( new ResponseEntity<>( character, HttpStatus.OK ) );

        MarvelCharacter marvelCharacter = marvelService.getCharacterById(100L);

        Assert.assertEquals( 100L, marvelCharacter.getId() );
    }

}
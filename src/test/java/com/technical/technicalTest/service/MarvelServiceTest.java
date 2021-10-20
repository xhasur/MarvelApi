package com.technical.technicalTest.service;

import com.technical.technicalTest.dto.CharctersInfo;
import com.technical.technicalTest.dto.MarvelCharacter;
import com.technical.technicalTest.dto.ResponseMarvel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;


@RunWith( MockitoJUnitRunner.class )
public class MarvelServiceTest {


    private MarvelService marvelService ;

    @Mock
    private RestTemplate template;

    @Before
    public  void before() throws Exception {
        marvelService = new MarvelServiceImpl("", "", "", template);
    }

    @Test
    public void getTotal_should_be_return_0() {
        CharctersInfo data = new CharctersInfo();
        data.setTotal("0");
        ResponseMarvel marvelResponse = new ResponseMarvel();
        marvelResponse.setData(data);
        Mockito.when(template.getForEntity(anyString(), eq(ResponseMarvel.class))).thenReturn( new ResponseEntity( marvelResponse, HttpStatus.OK ) );

        int response = marvelService.getTotal();

        Assert.assertEquals( 0, response );
    }

    @Test
    public void getTotal_should_be_return_a_correct_value()
    {
        ResponseMarvel marvelResponse = this.createMarvelCharacters();
        Mockito.when(template.getForEntity(anyString(), eq(ResponseMarvel.class))).thenReturn( new ResponseEntity( marvelResponse, HttpStatus.OK ) );

        int response = marvelService.getTotal();

        Assert.assertEquals( 2, response );
    }

    @Test(expected = Exception.class)
    public void getTotal_should_be_return_exception()
    {
        Mockito.when(template.getForEntity(anyString(), eq(ResponseMarvel.class))).thenThrow( new Exception( ) );

        int response = marvelService.getTotal();

        Assert.assertEquals( 2, response );
    }

    @Test(expected = NullPointerException.class)
    public void getTotal_should_be_return_0_if_the_call_is_failing()
    {
        Mockito.when(template.getForEntity(anyString(), eq(ResponseMarvel.class))).thenReturn( null );

        int response = marvelService.getTotal();

        Assert.assertEquals( 2, response );
    }


    @Test
    public void getCharactersIdWithLimits_should_be_return_a_number_correct_of_ids()
    {
        ResponseMarvel marvelResponse =   this.createMarvelCharacter();
        Mockito.when(template.getForEntity(anyString(), eq(ResponseMarvel.class))).thenReturn( new ResponseEntity( marvelResponse, HttpStatus.OK ) );

        List<String> idsResponse = marvelService.getCharactersIdWithLimits("100", "0");

        Assert.assertEquals( 1, idsResponse.size() );
    }

    @Test
    public void getCharacterById_should_return_a_character() {
        ResponseMarvel marvelResponse =   this.createMarvelCharacter();
        Mockito.when(template.getForEntity(anyString(), eq(ResponseMarvel.class))).thenReturn( new ResponseEntity( marvelResponse, HttpStatus.OK ) );

        MarvelCharacter marvelCharacter = marvelService.getCharacterById(100L);

        Assert.assertEquals( "Wonder-Woman", marvelCharacter.getDescription().toString() );
    }

    private ResponseMarvel createMarvelCharacters() {
        List<MarvelCharacter>  results = new ArrayList<>();
        MarvelCharacter character1 = new MarvelCharacter();
        character1.setDescription("Wonder-Woman");
        MarvelCharacter character2 = new MarvelCharacter();
        character2.setDescription("SpiderMan");
        results.add(character1);
        results.add(character2);
        CharctersInfo characterInfo = new CharctersInfo();
        characterInfo.setResults(results);
        characterInfo.setTotal(String.valueOf(results.size()));
        ResponseMarvel marvelResponse =  new ResponseMarvel();
        marvelResponse.setData(characterInfo);
        return marvelResponse;
    }

    private ResponseMarvel createMarvelCharacter(){
        List<MarvelCharacter>  results = new ArrayList<>();
        MarvelCharacter character1 = new MarvelCharacter();
        character1.setDescription("Wonder-Woman");
        results.add(character1);
        CharctersInfo characterInfo = new CharctersInfo();
        characterInfo.setResults(results);
        characterInfo.setTotal(String.valueOf(results.size()));
        ResponseMarvel marvelResponse =  new ResponseMarvel();
        marvelResponse.setData(characterInfo);
        return marvelResponse;
    }

}
package com.technical.technicalTest.controller;

import com.technical.technicalTest.dto.MarvelCharacter;
import com.technical.technicalTest.service.MarvelService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith( SpringJUnit4ClassRunner.class )
public class CharacterControllerTest {


    private MockMvc mockMvc;

    @InjectMocks
    private CharacterController characterController;

    @Mock
    private MarvelService marvelService;


    @Before
    public void init()
    {
        mockMvc = MockMvcBuilders
                .standaloneSetup( characterController )
                .build();
    }

    @Test
    public void getProvidersShouldBeCalled()
            throws Exception
    {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get( "/characters/1000" );
        when( marvelService.getCharacterById( 1000L) ).thenReturn( null );
        mockMvc.perform( requestBuilder ).andReturn();
        verify( marvelService ).getCharacterById( 1000L );
    }

    @Test
    public void getCharactersIds_should_be_return_ids()
            throws Exception
    {
        ResponseEntity<List<String>> response = characterController.getCharactersIds();
        Assert.assertEquals( response.getBody().size(), 1500 );
    }


    @Test
    public void getProvidersShouldBe_show_not_found()
            throws Exception
    {
        when( marvelService.getCharacterById( 1000L) ).thenReturn( null );
        ResponseEntity<MarvelCharacter> character = characterController.getCharactersIds( 1000L, "");
        Assert.assertEquals( character, new ResponseEntity<>(HttpStatus.NOT_FOUND) );
    }

    @Test
    public void getProvidersShouldBe_return_a_character()
            throws Exception
    {
        MarvelCharacter marvel = new MarvelCharacter();
        marvel.setDescription("iron man");
        when( marvelService.getCharacterById( 1000L) ).thenReturn( marvel );
        ResponseEntity<MarvelCharacter> character = characterController.getCharactersIds( 1000L, null);
        Assert.assertEquals( character, new ResponseEntity<>(marvel, HttpStatus.OK) );
    }


}
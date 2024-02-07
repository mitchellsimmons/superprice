package com.scrumdogs.superprice.postcode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostcodeServiceTest {
    private PostcodeService service;
    private final PostcodeRepository postcodeRepository = mock(PostcodeRepository.class);

    @BeforeEach
    void setup() {this.service = new PostcodeServiceImpl(postcodeRepository); }

    @Test
    void should_returnAllPostcodes_when_available() {
        when(postcodeRepository.findAll()).thenReturn(
                List.of(
                        new Postcode(3000L),
                        new Postcode(3001L),
                        new Postcode(3002L),
                        new Postcode(3003L)
                ));
        assertEquals(4, service.getAllPostcodes().size());
    }

    @Test
    void should_returnEmpty_when_notAvailable() {
        when(postcodeRepository.findAll()).thenReturn(new ArrayList<>());
        assertEquals(0, service.getAllPostcodes().size());
    }
}

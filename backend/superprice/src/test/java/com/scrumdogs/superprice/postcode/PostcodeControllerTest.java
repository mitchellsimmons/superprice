package com.scrumdogs.superprice.postcode;

import com.scrumdogs.superprice.postcode.controllers.PostcodeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostcodeControllerTest {

    PostcodeController controller;
    PostcodeService service;

    @BeforeEach
    void setup() {
        this.service = mock(PostcodeService.class);
        this.controller = new PostcodeController(this.service);
    }

    @Test
    void should_returnAllPostcodes_when_availableInService() {
        when(this.service.getAllPostcodes()).thenReturn(
                List.of(
                        new Postcode(3000L),
                        new Postcode(3001L),
                        new Postcode(3002L),
                        new Postcode(3003L)
                )
        );
        assertEquals(4, this.controller.all().size());
    }
    @Test
    void should_returnEmpty_when_notAvailable() {
        when(this.service.getAllPostcodes()).thenReturn(new ArrayList<>());
        assertEquals(0, this.controller.all().size());
    }
}

package com.scrumdogs.superprice.postcode.controllers;

import com.scrumdogs.superprice.postcode.PostcodeService;
import com.scrumdogs.superprice.postcode.Postcode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@RequiredArgsConstructor // This also performs @Autowiring if only one constructor
@RestController
public class PostcodeController {
    final private PostcodeService postcodeService;

    @GetMapping("v1/postcodes")
    public Collection<Postcode> all() {
        return postcodeService.getAllPostcodes();
    }

}

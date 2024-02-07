package com.scrumdogs.superprice.postcode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collection;

@RequiredArgsConstructor // Performs @Autowiring if only one constructor
@Service
public class PostcodeServiceImpl implements PostcodeService {
    private final PostcodeRepository repository;

    @Override
    public Collection<Postcode> getAllPostcodes() {
        return repository.findAll();
    }
}

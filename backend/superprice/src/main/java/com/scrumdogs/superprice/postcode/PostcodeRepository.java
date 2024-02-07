package com.scrumdogs.superprice.postcode;

import java.util.List;

public interface PostcodeRepository {
    List<Postcode> findAll();
}

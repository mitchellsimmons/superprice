package com.scrumdogs.superprice.postcode;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PostcodeRepositoryTest {
    @Autowired
    private Flyway flyway;

    @Autowired
    private PostcodeRepository repository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void setup() {
        flyway.migrate();
        repository = new PostcodeRepositoryImpl(dataSource);
    }
    @AfterEach
    public void teardown() { flyway.clean(); }

    @Test
    public void whenFindAll_thenReturnAllPostcodes() {
        List<Postcode> expected = Arrays.asList(
                new Postcode(3000L),
                new Postcode(3001L),
                new Postcode(3002L),
                new Postcode(3003L),
                new Postcode(3004L),
                new Postcode(3005L),
                new Postcode(3006L),
                new Postcode(3007L),
                new Postcode(3008L),
                new Postcode(3009L),
                new Postcode(3010L)
            );
        List<Postcode> all = repository.findAll();
        assertEquals(100, all.size());
        assertEquals(expected, all.subList(0,11));
    }

}

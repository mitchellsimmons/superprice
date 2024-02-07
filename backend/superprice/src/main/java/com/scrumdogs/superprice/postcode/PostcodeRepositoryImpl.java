package com.scrumdogs.superprice.postcode;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor// This also performs @Autowiring if only one constructor
@Repository
public class PostcodeRepositoryImpl implements PostcodeRepository {
    private final DataSource dataSource;

    @Override
    public List<Postcode> findAll() {
        Connection connection;
        try {
            connection = dataSource.getConnection();
            PreparedStatement stm = connection.prepareStatement("SELECT DISTINCT Postcode FROM Store ;");
            List<Postcode> postcodes = new ArrayList<>();
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Postcode p = new Postcode(rs.getLong(1));
                postcodes.add(p);
            }

            connection.close();
            return postcodes;

        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error in findAll", e);
        }
    }
}

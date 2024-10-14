package com.whoimi.config;

import com.whoimi.utils.DataSourcePropertiesUtil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ConfigurerJasyptTest {
    @Autowired
    ConfigurableEnvironment environment;
    @Autowired
    DataSourcePropertiesUtil test;
    @Test
    public void testEnvironmentProperties() throws SQLException {
        assertEquals("kinkim", environment.getProperty("spring.datasource.username"));

        test.test();
    }
}

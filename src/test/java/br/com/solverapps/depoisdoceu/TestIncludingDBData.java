package br.com.solverapps.depoisdoceu;

import org.assertj.db.type.Source;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestIncludingDBData {

    @Value("${spring.datasource.url}")
    String databaseUrl;
    @Value("${spring.datasource.username}")
    String databaseUser;
    @Value("${spring.datasource.password}")
    String databasePassword;
    Source source;

    Map<String, Table> tables = new HashMap<>();

    public void observeTables(List<String> tableNames){
        source = new Source(databaseUrl, databaseUser, databasePassword);
        tables = new HashMap<>();
        tableNames.forEach(
                (tableName)->tables.put(tableName, new Table(source, tableName))
        );
    }

    public org.assertj.db.api.TableAssert assertThatTable(String tableName){
        return org.assertj.db.api.Assertions.assertThat(tables.get(tableName));
    }

}
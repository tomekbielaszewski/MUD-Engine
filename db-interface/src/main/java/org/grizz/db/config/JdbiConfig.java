package org.grizz.db.config;

import org.grizz.db.model.repository.PlayerCommandRepository;
import org.grizz.db.model.repository.ProcessedPlayerResponseRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class JdbiConfig {
    private static final String DB_INTERFACE_JDBI_NAME = "dbInterfaceJdbi";

    @Bean
    @ConfigurationProperties(prefix = "mud.engine.db-interface.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = DB_INTERFACE_JDBI_NAME)
    public Jdbi jdbi(DataSource ds, List<JdbiPlugin> jdbiPlugins, List<RowMapper<?>> rowMappers) {
        TransactionAwareDataSourceProxy proxy = new TransactionAwareDataSourceProxy(ds);
        Jdbi jdbi = Jdbi.create(proxy);
        jdbiPlugins.forEach(jdbi::installPlugin);
        rowMappers.forEach(jdbi::registerRowMapper);
        return jdbi;
    }

    @Bean
    @Qualifier(DB_INTERFACE_JDBI_NAME)
    public PlayerCommandRepository playerCommandRepository(Jdbi dbInterfaceJdbi) {
        return dbInterfaceJdbi.onDemand(PlayerCommandRepository.class);
    }

    @Bean
    @Qualifier(DB_INTERFACE_JDBI_NAME)
    public ProcessedPlayerResponseRepository processedPlayerResponseRepository(Jdbi dbInterfaceJdbi) {
        return dbInterfaceJdbi.onDemand(ProcessedPlayerResponseRepository.class);
    }
}

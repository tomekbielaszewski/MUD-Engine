package org.grizz.game.config;

import org.grizz.game.model.repository.ItemListRepository;
import org.grizz.game.model.repository.LocationItemsRepository;
import org.grizz.game.model.repository.PlayerRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class JdbiConfig {

    @Bean
    public Jdbi jdbi(DataSource ds, List<JdbiPlugin> jdbiPlugins, List<RowMapper<?>> rowMappers) {
        TransactionAwareDataSourceProxy proxy = new TransactionAwareDataSourceProxy(ds);
        Jdbi jdbi = Jdbi.create(proxy);
        jdbiPlugins.forEach(jdbi::installPlugin);
        rowMappers.forEach(jdbi::registerRowMapper);
        return jdbi;
    }

    @Bean
    public ItemListRepository itemListRepository(Jdbi jdbi) {
        return jdbi.onDemand(ItemListRepository.class);
    }

    @Bean
    public LocationItemsRepository locationItemsRepository(Jdbi jdbi) {
        return jdbi.onDemand(LocationItemsRepository.class);
    }

    @Bean
    public PlayerRepository playerRepository(Jdbi jdbi) {
        return jdbi.onDemand(PlayerRepository.class);
    }
}
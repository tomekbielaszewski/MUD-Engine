package org.grizz.db;

import org.grizz.db.config.MainConfig;
import org.grizz.game.model.LocationItems;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.LocationItemsRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class StarterWithCrappyCode {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainConfig.class);
        LocationItemsRepository locationItemsRepository = context.getBean(LocationItemsRepository.class);
        ItemRepo itemRepo = context.getBean(ItemRepo.class);


        LocationItems locationItems = locationItemsRepository.findByLocationId("2");
        locationItems.getStaticItems().add(
                itemRepo.get("301")
        );
        locationItemsRepository.save(locationItems);

        context.close();
    }
}

package org.grizz.game.config;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.model.converters.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import java.util.List;

@Slf4j
@Configuration
public class MongoConfig {
    @Bean
    public CustomConversions customConversions(ItemReadConverter itemReadConverter, ItemWriteConverter itemWriteConverter,
                                               LocationItemsReadConverter locationItemsReadConverter,
                                               LocationItemsWriteConverter locationItemsWriteConverter,
                                               EquipmentReadConverter equipmentReadConverter,
                                               EquipmentWriteConverter equipmentWriteConverter) throws Exception {
        List<Converter<?, ?>> converterList = Lists.newArrayList();

        converterList.add(itemReadConverter);
        converterList.add(itemWriteConverter);
        converterList.add(locationItemsReadConverter);
        converterList.add(locationItemsWriteConverter);
        converterList.add(equipmentReadConverter);
        converterList.add(equipmentWriteConverter);

        return new CustomConversions(converterList);
    }
}

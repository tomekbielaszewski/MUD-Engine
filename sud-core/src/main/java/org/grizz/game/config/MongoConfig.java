package org.grizz.game.config;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.config.converters.ItemReadConverter;
import org.grizz.game.config.converters.ItemWriteConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import java.util.List;

/**
 * Created by Grizz on 2015-10-12.
 */
@Slf4j
@Configuration
public class MongoConfig {
    @Bean
    public CustomConversions customConversions(ItemReadConverter itemReadConverter, ItemWriteConverter itemWriteConverter) throws Exception {
        List<Converter<?, ?>> converterList = Lists.newArrayList();

        converterList.add(itemReadConverter);
        converterList.add(itemWriteConverter);

        return new CustomConversions(converterList);
    }
}

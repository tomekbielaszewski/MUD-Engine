package old.org.grizz.game.config.converters;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import old.org.grizz.game.model.impl.ItemStack;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

/**
 * Created by Grizz on 2015-10-12.
 */
@Service
public class ItemStackWriteConverter implements Converter<ItemStack, DBObject> {
    @Override
    public DBObject convert(ItemStack source) {
        DBObject dbo = new BasicDBObject();
        dbo.put("id", source.getId());
        dbo.put("name", source.getName());
        dbo.put("amount", source.getAmount());
        return dbo;
    }
}

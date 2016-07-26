package old.org.grizz.game.config.converters;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import old.org.grizz.game.model.impl.EquipmentEntity;
import old.org.grizz.game.model.impl.ItemStack;
import old.org.grizz.game.model.items.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Grizz on 2015-11-27.
 */
@Service
public class EquipmentWriteConverter implements Converter<EquipmentEntity, DBObject> {
    @Autowired
    private ItemListToItemStackConverter itemListToItemStackConverter;
    @Autowired
    private ItemStackWriteConverter itemStackConverter;

    @Override
    public DBObject convert(EquipmentEntity source) {
        DBObject dbo = new BasicDBObject();

        dbo.put("head", getItemId(source.getHeadItem()));
        dbo.put("torso", getItemId(source.getTorsoItem()));
        dbo.put("hands", getItemId(source.getHandsItem()));
        dbo.put("legs", getItemId(source.getLegsItem()));
        dbo.put("feet", getItemId(source.getFeetItem()));
        dbo.put("melee", getItemId(source.getMeleeWeapon()));
        dbo.put("range", getItemId(source.getRangeWeapon()));
        dbo.put("backpack", convert(source.getBackpack()));

        return dbo;
    }

    private List<DBObject> convert(List<Item> itemList) {
        List<ItemStack> itemStacks = itemListToItemStackConverter.convert(itemList);
        List<DBObject> dbObjects = itemStacks.stream()
                .map(itemStack -> itemStackConverter.convert(itemStack))
                .collect(Collectors.toList());

        return dbObjects;
    }

    private String getItemId(Item item) {
        return item == null ? null : item.getId();
    }
}

package org.grizz.game.model.converters;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.Weapon;
import org.grizz.game.model.repository.ItemRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentReadConverterTest {
    private static final String HEAD_ITEM = "head item";
    private static final String TORSO_ITEM = "torso item";
    private static final String HANDS_ITEM = "hands item";
    private static final String LEGS_ITEM = "legs item";
    private static final String FEET_ITEM = "feet item";
    private static final String MELEE_ITEM = "melee item";
    private static final String RANGE_ITEM = "range item";

    @Mock
    private ItemRepo itemRepo;
    @Mock
    private DBItemPackToItemListConverter itemListConverter;

    @InjectMocks
    private EquipmentReadConverter converter = new EquipmentReadConverter();

    @Test
    public void convertsActiveEquipment() throws Exception {
        DBObject dbo = new BasicDBObject();
        dbo.put("head", HEAD_ITEM);
        dbo.put("torso", TORSO_ITEM);
        dbo.put("hands", HANDS_ITEM);
        dbo.put("legs", LEGS_ITEM);
        dbo.put("feet", FEET_ITEM);
        dbo.put("melee", MELEE_ITEM);
        dbo.put("range", RANGE_ITEM);
        dbo.put("backpack", Lists.newArrayList());
        when(itemRepo.get(HEAD_ITEM)).thenReturn(dummyArmor(HEAD_ITEM));
        when(itemRepo.get(TORSO_ITEM)).thenReturn(dummyArmor(TORSO_ITEM));
        when(itemRepo.get(HANDS_ITEM)).thenReturn(dummyArmor(HANDS_ITEM));
        when(itemRepo.get(LEGS_ITEM)).thenReturn(dummyArmor(LEGS_ITEM));
        when(itemRepo.get(FEET_ITEM)).thenReturn(dummyArmor(FEET_ITEM));
        when(itemRepo.get(MELEE_ITEM)).thenReturn(dummyWeapon(MELEE_ITEM));
        when(itemRepo.get(RANGE_ITEM)).thenReturn(dummyWeapon(RANGE_ITEM));

        Equipment converted = converter.convert(dbo);

        assertThat(converted.getHeadItem(), is(dummyArmor(HEAD_ITEM)));
        assertThat(converted.getTorsoItem(), is(dummyArmor(TORSO_ITEM)));
        assertThat(converted.getHandsItem(), is(dummyArmor(HANDS_ITEM)));
        assertThat(converted.getLegsItem(), is(dummyArmor(LEGS_ITEM)));
        assertThat(converted.getFeetItem(), is(dummyArmor(FEET_ITEM)));
        assertThat(converted.getMeleeWeapon(), is(dummyWeapon(MELEE_ITEM)));
        assertThat(converted.getRangeWeapon(), is(dummyWeapon(RANGE_ITEM)));
        assertThat(converted.getBackpack(), is(empty()));
    }

    @Test
    public void doesNotAttachItemsWhenNoItemInDB() throws Exception {
        DBObject dbo = new BasicDBObject();
        dbo.put("backpack", Lists.newArrayList());

        Equipment converted = converter.convert(dbo);

        assertThat(converted.getHeadItem(), is(nullValue()));
        assertThat(converted.getTorsoItem(), is(nullValue()));
        assertThat(converted.getHandsItem(), is(nullValue()));
        assertThat(converted.getLegsItem(), is(nullValue()));
        assertThat(converted.getFeetItem(), is(nullValue()));
        assertThat(converted.getMeleeWeapon(), is(nullValue()));
        assertThat(converted.getRangeWeapon(), is(nullValue()));
        assertThat(converted.getBackpack(), is(empty()));
    }

    @Test
    public void callsDBItemPackConverterToGetBackpackContents() throws Exception {
        List<DBObject> backpack = Lists.newArrayList(
                new BasicDBObject("parameter", "value"),
                new BasicDBObject("parameter2", "value2")
        );
        DBObject dbo = new BasicDBObject();
        dbo.put("backpack", backpack);

        converter.convert(dbo);

        verify(itemListConverter).convert(backpack);
    }

    private Item dummyArmor(String id) {
        return Armor.builder()
                .id(id)
                .build();
    }

    private Item dummyWeapon(String id) {
        return Weapon.builder()
                .id(id)
                .build();
    }
}
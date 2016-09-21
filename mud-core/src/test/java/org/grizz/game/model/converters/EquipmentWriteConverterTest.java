package org.grizz.game.model.converters;

import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.ItemStack;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.Weapon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentWriteConverterTest {
    private static final String HEAD_ITEM = "head item";
    private static final String TORSO_ITEM = "torso item";
    private static final String HANDS_ITEM = "hands item";
    private static final String LEGS_ITEM = "legs item";
    private static final String FEET_ITEM = "feet item";
    private static final String MELEE_ITEM = "melee item";
    private static final String RANGE_ITEM = "range item";
    private static final String SOME_ARMOR = "some armor item";
    private static final String SOME_WEAPON = "some weapon item";

    @Mock
    private ItemListToItemStackConverter itemListToItemStackConverter;
    @Mock
    private ItemStackWriteConverter itemStackConverter;

    @InjectMocks
    private EquipmentWriteConverter converter = new EquipmentWriteConverter();

    @Test
    public void convertsActiveEquipment() throws Exception {
        Equipment equipment = Equipment.builder()
                .headItem(dummyArmor(HEAD_ITEM))
                .torsoItem(dummyArmor(TORSO_ITEM))
                .handsItem(dummyArmor(HANDS_ITEM))
                .legsItem(dummyArmor(LEGS_ITEM))
                .feetItem(dummyArmor(FEET_ITEM))
                .meleeWeapon(dummyWeapon(MELEE_ITEM))
                .rangeWeapon(dummyWeapon(RANGE_ITEM))
                .build();

        DBObject converted = converter.convert(equipment);

        assertThat(converted.get("head"), is(HEAD_ITEM));
        assertThat(converted.get("torso"), is(TORSO_ITEM));
        assertThat(converted.get("hands"), is(HANDS_ITEM));
        assertThat(converted.get("legs"), is(LEGS_ITEM));
        assertThat(converted.get("feet"), is(FEET_ITEM));
        assertThat(converted.get("melee"), is(MELEE_ITEM));
        assertThat(converted.get("range"), is(RANGE_ITEM));
    }

    @Test
    public void convertsBackpack() throws Exception {
        List<Item> backpack = Lists.newArrayList(dummyArmor(SOME_ARMOR), dummyWeapon(SOME_WEAPON));
        ItemStack armorStack = ItemStack.builder()
                .amount(1)
                .id(SOME_ARMOR)
                .build();
        ItemStack weaponStack = ItemStack.builder()
                .amount(1)
                .id(SOME_WEAPON)
                .build();
        ArrayList<ItemStack> itemStackOfBackpack = Lists.newArrayList(armorStack, weaponStack);
        Equipment equipment = Equipment.builder()
                .backpack(backpack)
                .build();
        when(itemListToItemStackConverter.convert(backpack)).thenReturn(itemStackOfBackpack);

        converter.convert(equipment);

        verify(itemListToItemStackConverter).convert(backpack);
        verify(itemStackConverter).convert(armorStack);
        verify(itemStackConverter).convert(weaponStack);

        verifyNoMoreInteractions(itemListToItemStackConverter);
        verifyNoMoreInteractions(itemStackConverter);
    }

    private Armor dummyArmor(String id) {
        return Armor.builder()
                .id(id)
                .build();
    }

    private Weapon dummyWeapon(String id) {
        return Weapon.builder()
                .id(id)
                .build();
    }
}
package org.grizz.game.model.mappers;

import com.google.common.collect.Lists;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.Weapon;
import org.grizz.game.model.mapper.EquipmentMapper;
import org.grizz.game.model.repository.ItemListRepository;
import org.grizz.game.model.repository.ItemRepo;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.ResultSet;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentMapperTest {
    private static final String HEAD_ITEM = "head item";
    private static final String TORSO_ITEM = "torso item";
    private static final String HANDS_ITEM = "hands item";
    private static final String LEGS_ITEM = "legs item";
    private static final String FEET_ITEM = "feet item";
    private static final String MELEE_ITEM = "melee item";
    private static final String RANGE_ITEM = "range item";
    private static final String BACKPACK_ID = "backpack id";

    @Mock
    private ItemRepo itemRepo;
    @Mock
    private ItemListRepository itemListRepository;

    @InjectMocks
    private EquipmentMapper mapper = new EquipmentMapper();

    @Test
    public void convertsActiveEquipment() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getString("head")).thenReturn(HEAD_ITEM);
        when(resultSet.getString("torso")).thenReturn(TORSO_ITEM);
        when(resultSet.getString("hands")).thenReturn(HANDS_ITEM);
        when(resultSet.getString("legs")).thenReturn(LEGS_ITEM);
        when(resultSet.getString("feet")).thenReturn(FEET_ITEM);
        when(resultSet.getString("melee")).thenReturn(MELEE_ITEM);
        when(resultSet.getString("range")).thenReturn(RANGE_ITEM);
        when(resultSet.getString("backpack")).thenReturn(BACKPACK_ID);

        when(itemRepo.get(HEAD_ITEM)).thenReturn(dummyArmor(HEAD_ITEM));
        when(itemRepo.get(TORSO_ITEM)).thenReturn(dummyArmor(TORSO_ITEM));
        when(itemRepo.get(HANDS_ITEM)).thenReturn(dummyArmor(HANDS_ITEM));
        when(itemRepo.get(LEGS_ITEM)).thenReturn(dummyArmor(LEGS_ITEM));
        when(itemRepo.get(FEET_ITEM)).thenReturn(dummyArmor(FEET_ITEM));
        when(itemRepo.get(MELEE_ITEM)).thenReturn(dummyWeapon(MELEE_ITEM));
        when(itemRepo.get(RANGE_ITEM)).thenReturn(dummyWeapon(RANGE_ITEM));
        when(itemListRepository.get(BACKPACK_ID)).thenReturn(Lists.newArrayList());

        Equipment equipment = mapper.map(resultSet, null);

        assertThat(equipment.getHeadItem(), is(dummyArmor(HEAD_ITEM)));
        assertThat(equipment.getTorsoItem(), is(dummyArmor(TORSO_ITEM)));
        assertThat(equipment.getHandsItem(), is(dummyArmor(HANDS_ITEM)));
        assertThat(equipment.getLegsItem(), is(dummyArmor(LEGS_ITEM)));
        assertThat(equipment.getFeetItem(), is(dummyArmor(FEET_ITEM)));
        assertThat(equipment.getMeleeWeapon(), is(dummyWeapon(MELEE_ITEM)));
        assertThat(equipment.getRangeWeapon(), is(dummyWeapon(RANGE_ITEM)));
        assertThat(equipment.getBackpack(), is(empty()));
    }

    @Test
    public void doesNotAttachItemsWhenNoItemInDB() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);

        Equipment converted = mapper.map(resultSet, null);

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
    public void callsItemListRepoToGetBackpackContents() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString("backpack")).thenReturn(BACKPACK_ID);
        when(itemListRepository.get(BACKPACK_ID)).thenReturn(Lists.newArrayList(dummyWeapon("someWeapon")));

        Equipment equipment = mapper.map(resultSet, null);

        verify(itemListRepository).get(BACKPACK_ID);
        assertThat(equipment.getBackpack(), hasSize(1));
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

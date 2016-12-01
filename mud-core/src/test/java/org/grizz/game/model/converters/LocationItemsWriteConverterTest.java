package org.grizz.game.model.converters;

import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import org.grizz.game.model.ItemStack;
import org.grizz.game.model.LocationItems;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
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
public class LocationItemsWriteConverterTest {
    private static final String ID = "id";
    private static final String LOCATION_ID = "location id";
    private static final String STATIC_ITEM_ID = "static item id";
    private static final String MOBILE_ITEM_ID = "mobile item id";

    @Mock
    private ItemListToItemStackConverter itemListToItemStackConverter;
    @Mock
    private ItemStackWriteConverter itemStackConverter;

    @InjectMocks
    private LocationItemsWriteConverter converter = new LocationItemsWriteConverter();

    @Test
    public void convertsBasicLocationInfo() throws Exception {
        LocationItems locationItems = LocationItems.builder()
                .locationId(LOCATION_ID)
                .build();

        DBObject converted = converter.convert(locationItems);

        assertThat(converted.get("locationId"), is(LOCATION_ID));
    }

    @Test
    public void convertsStaticItemsOnLocation() throws Exception {
        List<Item> staticItems = Lists.newArrayList(dummyItem(STATIC_ITEM_ID), dummyItem(STATIC_ITEM_ID));
        ItemStack staticItemStack = ItemStack.builder().amount(2).id(STATIC_ITEM_ID).build();
        ArrayList<ItemStack> itemStacks = Lists.newArrayList(staticItemStack);
        LocationItems locationItems = LocationItems.builder()
                .staticItems(staticItems)
                .mobileItems(Lists.newArrayList())
                .build();
        when(itemListToItemStackConverter.convert(staticItems)).thenReturn(itemStacks);

        converter.convert(locationItems);

        verify(itemListToItemStackConverter).convert(Lists.newArrayList());
        verify(itemListToItemStackConverter).convert(staticItems);
        verify(itemStackConverter).convert(staticItemStack);

        verifyNoMoreInteractions(itemListToItemStackConverter);
        verifyNoMoreInteractions(itemStackConverter);
    }

    @Test
    public void convertsMobileItemsOnLocation() throws Exception {
        List<Item> mobileItems = Lists.newArrayList(dummyItem(MOBILE_ITEM_ID), dummyItem(MOBILE_ITEM_ID));
        ItemStack mobileItemStack = ItemStack.builder().amount(2).id(MOBILE_ITEM_ID).build();
        ArrayList<ItemStack> itemStacks = Lists.newArrayList(mobileItemStack);
        LocationItems locationItems = LocationItems.builder()
                .mobileItems(mobileItems)
                .staticItems(Lists.newArrayList())
                .build();
        when(itemListToItemStackConverter.convert(mobileItems)).thenReturn(itemStacks);

        converter.convert(locationItems);

        verify(itemListToItemStackConverter).convert(mobileItems);
        verify(itemListToItemStackConverter).convert(Lists.newArrayList());
        verify(itemStackConverter).convert(mobileItemStack);

        verifyNoMoreInteractions(itemListToItemStackConverter);
        verifyNoMoreInteractions(itemStackConverter);
    }

    private Item dummyItem(String id) {
        return Armor.builder().id(id).build();
    }
}
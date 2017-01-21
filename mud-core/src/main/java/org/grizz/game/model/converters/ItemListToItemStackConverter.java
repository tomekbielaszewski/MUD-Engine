package org.grizz.game.model.converters;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.grizz.game.model.ItemStack;
import org.grizz.game.model.items.Item;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemListToItemStackConverter implements Converter<List<Item>, List<ItemStack>> {

    @Override
    public List<ItemStack> convert(List<Item> source) {
        Multiset<Item> items = HashMultiset.create();
        source.forEach(items::add);
        List<ItemStack> itemStacks = items.elementSet().stream()
                .map(item -> ItemStack.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .amount(items.count(item))
                        .build())
                .collect(Collectors.toList());
        return itemStacks;
    }
}

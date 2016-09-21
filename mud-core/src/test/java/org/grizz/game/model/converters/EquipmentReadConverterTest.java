package org.grizz.game.model.converters;

import org.grizz.game.model.repository.ItemRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentReadConverterTest {

    @Mock
    private ItemRepo itemRepo;

    @InjectMocks
    private EquipmentReadConverter converter = new EquipmentReadConverter();

    @Test
    public void testConvert() throws Exception {
        throw new NotImplementedException();
    }
}
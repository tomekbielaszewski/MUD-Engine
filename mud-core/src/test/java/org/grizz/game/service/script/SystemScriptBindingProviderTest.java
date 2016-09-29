package org.grizz.game.service.script;

import org.grizz.game.service.script.test.a.TestA;
import org.grizz.game.service.script.test.b.TestB;
import org.grizz.game.service.script.test.b.c.TestC_InsideB;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SystemScriptBindingProviderTest {
    private static final String FIRST_PACKAGE = TestA.class.getPackage().getName();
    private static final String THIRD_PACKAGE = TestC_InsideB.class.getPackage().getName();
    private static final String ALLOWED_PACKAGES = FIRST_PACKAGE+";"+THIRD_PACKAGE;
    private static final TestA TEST_A_BEAN = new TestA();
    private static final TestB TEST_B_BEAN = new TestB();
    private static final TestC_InsideB TEST_C_BEAN = new TestC_InsideB();

    @Mock
    private ApplicationContext context;

    @InjectMocks
    private SystemScriptBindingProvider provider = new SystemScriptBindingProvider();

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(provider, "scriptEngineServicePackagesConfiguration", ALLOWED_PACKAGES);
        when(context.getBeanDefinitionNames()).thenReturn(new String[]{"testA","testB","testC_insideB"});
        when(context.getBean("testA")).thenReturn(TEST_A_BEAN);
        when(context.getBean("testB")).thenReturn(TEST_B_BEAN);
        when(context.getBean("testC_insideB")).thenReturn(TEST_C_BEAN);
    }

    @Test
    public void providesBeanOnlyFromSpecifiedPackages() throws Exception {
        List<ScriptBinding> result = provider.provide();

        assertThat(result, hasSize(2));
        assertThat(result, hasItem(new ScriptBinding("testA", TEST_A_BEAN)));
        assertThat(result, hasItem(new ScriptBinding("testC_insideB", TEST_C_BEAN)));
        assertThat(result, not(hasItem(new ScriptBinding("testB", TEST_B_BEAN))));
    }
}
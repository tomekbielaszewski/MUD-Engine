package org.grizz.game.model.repository;

import old.org.grizz.game.exception.NoSuchLocationException;
import old.org.grizz.game.exception.NoSuchScriptException;
import old.org.grizz.game.model.Location;
import old.org.grizz.game.model.Script;
import old.org.grizz.game.model.impl.LocationEntity;
import old.org.grizz.game.model.impl.ScriptEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ScriptRepoTest {
    private static final String SCRIPT_ID_1 = "script 1 id";
    private static final String SCRIPT_ID_2 = "script 2 id";

    @InjectMocks
    private ScriptRepo scriptRepo = new ScriptRepo();

    @Test
    public void addAndGetLocation() throws Exception {
        Script script = dummyScript(SCRIPT_ID_1);

        scriptRepo.add(script);
        Script result = scriptRepo.get(SCRIPT_ID_1);

        assertThat(result, is(equalTo(script)));
    }

    @Test
    public void addTwoDifferentLocationsAndGetTheFirstOne() throws Exception {
        Script expected = dummyScript(SCRIPT_ID_1);
        Script notExpected = dummyScript(SCRIPT_ID_2);

        scriptRepo.add(expected);
        scriptRepo.add(notExpected);
        Script result = scriptRepo.get(SCRIPT_ID_1);

        assertThat(result, is(equalTo(expected)));
        assertThat(result, is(not(equalTo(notExpected))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addSameLocationTwice() throws Exception {
        Script script = dummyScript(SCRIPT_ID_1);

        scriptRepo.add(script);
        scriptRepo.add(script);
    }

    @Test(expected = NoSuchScriptException.class)
    public void getNotExistingLocationWhenRepoIsEmpty() throws Exception {
        scriptRepo.get(SCRIPT_ID_1);
    }

    @Test(expected = NoSuchScriptException.class)
    public void getNotExistingLocationWhenRepoIsNotEmpty() throws Exception {
        Script script = dummyScript(SCRIPT_ID_1);

        scriptRepo.add(script);
        scriptRepo.get(SCRIPT_ID_2);
    }

    private Script dummyScript(String id) {
        return ScriptEntity.builder()
                .id(id)
                .build();
    }
}
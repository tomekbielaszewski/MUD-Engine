package org.grizz.game.loader.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.loader.Loader;
import org.grizz.game.model.Quest;
import org.grizz.game.model.impl.QuestEntity;
import org.grizz.game.model.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Grizz on 2015-04-17.
 */
@Slf4j
public class QuestLoader implements Loader {
    private final String _path;

    @Autowired
    private Repository<Quest> questRepo;

    public QuestLoader(String path) {
        this._path = path;
    }

    @Override
    public void load() {
        String path = "";

        List<QuestEntity> quests = readQuests(path);
        storeLocations(quests);
    }

    private List<QuestEntity> readQuests(String path) {
        return Lists.newArrayList();
    }

    private void storeLocations(List<QuestEntity> quests) {
        for (QuestEntity quest : quests) {
            questRepo.add(quest);
        }
    }
}

package org.grizz.game.model.repository.impl;

import org.grizz.game.model.Quest;
import org.grizz.game.model.repository.QuestRepo;
import org.springframework.stereotype.Service;

/**
 * Created by Grizz on 2015-04-21.
 */
@Service
public class QuestRepoImpl implements QuestRepo {
    @Override
    public void add(Quest quest) {
        System.out.println("Quest repo");
    }

    @Override
    public Quest get(String id) {
        return null;
    }
}

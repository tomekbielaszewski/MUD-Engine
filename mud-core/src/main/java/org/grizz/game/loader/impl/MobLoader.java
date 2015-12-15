package org.grizz.game.loader.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.loader.Loader;
import org.grizz.game.model.Mob;
import org.grizz.game.model.impl.MobEntity;
import org.grizz.game.model.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Grizz on 2015-04-17.
 */
@Slf4j
public class MobLoader implements Loader {
    private final String _path;

    @Autowired
    private Repository<Mob> mobRepo;

    public MobLoader(String path) {
        this._path = path;
    }

    @Override
    public void load() {
        String path = "";

        List<MobEntity> mobs = readMobs(path);
        storeMobs(mobs);
    }

    private List<MobEntity> readMobs(String path) {
        return Lists.newArrayList();
    }

    private void storeMobs(List<MobEntity> mobs) {
        for (MobEntity mob : mobs) {
            mobRepo.add(mob);
        }
    }
}

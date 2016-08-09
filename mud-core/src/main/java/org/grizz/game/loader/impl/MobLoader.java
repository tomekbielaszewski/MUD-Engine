package org.grizz.game.loader.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import old.org.grizz.game.model.Mob;
import old.org.grizz.game.model.impl.MobEntity;
import old.org.grizz.game.model.repository.Repository;
import org.grizz.game.loader.Loader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

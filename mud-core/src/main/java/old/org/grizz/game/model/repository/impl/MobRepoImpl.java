package old.org.grizz.game.model.repository.impl;

import old.org.grizz.game.model.Mob;
import old.org.grizz.game.model.repository.MobRepo;
import org.springframework.stereotype.Service;

/**
 * Created by Grizz on 2015-04-21.
 */
@Service
public class MobRepoImpl implements MobRepo {
    @Override
    public void add(Mob mob) {
        System.out.println("Mob repo");
    }

    @Override
    public Mob get(String id) {
        return null;
    }
}

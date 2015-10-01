package org.grizz.game;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Grizz on 2015-10-01.
 */
//@ContextConfiguration(classes = {GameConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class FightAlgorithmTest {

    public void testFight() {

    }

    private final int NUMBER_OF_INITIAL_ATTACKS_MODIFIER = 10;
    private final int MAX_ROUNDS = 20;

    private void algorithm(Player attacker, Player victim) {
        if (hasRangeWeapon(attacker)) {
            int numberOfAttacks = getNumberOfRangeWeaponAttacks(attacker);

            for (int i = 0; i < numberOfAttacks; i++) {
                attackWithRangeWeapon(attacker, victim);
            }
        }

        int roundCounter = 0;

        while (roundCounter < MAX_ROUNDS &&
                attacker.isAlive() && victim.isAlive()) {
            Player currentAttacker = chooseBasingOnSpeed(attacker, victim);
            Player currentVictim = currentAttacker == attacker ? victim : attacker;

            if (!dodge(currentVictim, currentAttacker)) {
                int damage = calculateDamage(currentAttacker, currentVictim);
                currentVictim.hit(damage);
            }
        }
    }

    private class Player {
        public boolean isAlive() {
            return false;
        }

        public void hit(int damage) {

        }
    }
}

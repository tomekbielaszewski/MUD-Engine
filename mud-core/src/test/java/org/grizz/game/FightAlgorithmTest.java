package org.grizz.game;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.impl.EquipmentEntity;
import org.grizz.game.model.impl.PlayerContextImpl;
import org.grizz.game.model.items.Weapon;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.service.simple.EquipmentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * Created by Grizz on 2015-10-01.
 */
@Slf4j
@ContextConfiguration(classes = {TestContext.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class FightAlgorithmTest {
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private ItemRepo itemRepo;

    private final int NUMBER_OF_INITIAL_ATTACKS_MODIFIER = 10;
    private final int MAX_ROUNDS = 20;

    @Test
    public void testFight() {
        Equipment equipment = EquipmentEntity.builder()
                .meleeWeapon((Weapon) itemRepo.getByName("Krótki miecz"))
                .build();

        Equipment grizzEquip = equipment;
        Equipment slimeEquip = equipment;

        Player attacker = new Player("Grizz", 5, 20, 1, 1, 1, 10, 100, grizzEquip, "", "", Maps.newHashMap());
        Player victim = new Player("Slime", 1, 20, 1, 1, 1, 10, 100, slimeEquip, "", "", Maps.newHashMap());

        algorithm(attacker, victim);
    }


    private void algorithm(Player attacker, Player victim) {
        if (attacker.hasRangeWeapon()) {
            int numberOfAttacks = getNumberOfRangeWeaponAttacks(attacker);

            log.info("{} has range weapon! Will attack {} times", attacker.getName(), numberOfAttacks);

            for (int i = 0; i < numberOfAttacks && victim.isAlive(); i++) {
                attackWithRangeWeapon(attacker, victim);
            }
        }

        int roundCounter = 0;

        log.info("Duel starts.");

        while (roundCounter < MAX_ROUNDS &&
                attacker.isAlive() && victim.isAlive()) {
            Player currentAttacker = chooseBasingOnSpeed(attacker, victim);
            Player currentVictim = currentAttacker == attacker ? victim : attacker;

            if (!dodge(currentVictim, currentAttacker)) {
                int damage = calculateDamage(currentAttacker, currentVictim);
                currentVictim.hit(damage);
            }
            roundCounter++;
        }

        log.info("Battle ended in {} rounds...\n" +
                        "{} has {}/{}HP\n" +
                        "{} has {}/{}HP", roundCounter,
                attacker.getName(), attacker.hp, attacker.getMaxHP(),
                victim.getName(), victim.hp, victim.getMaxHP());
    }

    private int calculateDamage(Player currentAttacker, Player currentVictim) {
        Weapon meleeWeapon = currentAttacker.getMeleeWeapon();
        int minDamage = meleeWeapon.getMinDamage();
        int maxDamage = meleeWeapon.getMaxDamage();

        int roll = (int) (Math.random() * (maxDamage - minDamage));
        int damage = roll + minDamage;

        damage += currentAttacker.getStrength();

        log.info("{} is attacking with {}", currentAttacker.getName(), meleeWeapon.getName());
        log.info("{} is hit by {} with {} pts of damage! {} HP left...", currentVictim.getName(), currentAttacker.getName(), damage, currentVictim.hp);

        return damage;
    }

    private Player chooseBasingOnSpeed(Player attacker, Player victim) {
        int speedSum = attacker.getDexterity() + victim.getDexterity();
        int roll = (int) (Math.random() * speedSum);
        return roll > attacker.getDexterity() ? victim : attacker;
    }

    private void attackWithRangeWeapon(Player attacker, Player victim) {
        Weapon rangeWeapon = attacker.getRangeWeapon();
        int minDamage = rangeWeapon.getMinDamage();
        int maxDamage = rangeWeapon.getMaxDamage();

        int damage = (int) (Math.random() * (maxDamage - minDamage) + minDamage);

        log.info("{} is attacking with {}", attacker.getName(), rangeWeapon.getName());
        victim.hit(damage);
        log.info("{} is hit by {} with {} pts of damage! {} HP left...", victim.getName(), attacker.getName(), damage, victim.hp);
    }

    private boolean dodge(Player currentVictim, Player currentAttacker) {
        int dexSum = currentAttacker.getDexterity() + (currentVictim.getDexterity() / 2);
        int roll = (int) (Math.random() * dexSum);
        boolean dodged = roll > currentAttacker.getDexterity();
        if (dodged) {
            log.info("{} dodged {}'s attack", currentVictim.getName(), currentAttacker.getName());
        }
        return dodged;
    }

    private int getNumberOfRangeWeaponAttacks(Player attacker) {
        return attacker.getDexterity() / NUMBER_OF_INITIAL_ATTACKS_MODIFIER;
    }

    private class Player extends PlayerContextImpl {
        private int hp;

        public Player(String name, int strength, int dexterity, int intelligence, int wisdom, int charisma, int endurance, int hp, Equipment equipment, String currentLocation, String pastLocation, Map<String, Object> parameters) {
            super("", name, strength, dexterity, endurance, intelligence, wisdom, charisma, (EquipmentEntity) equipment, currentLocation, pastLocation, parameters);
            if (hp > this.getMaxHP()) {
                this.hp = this.getMaxHP();
            } else {
                this.hp = hp;
            }
        }

        public boolean hasRangeWeapon() {
            return getEquipment().getRangeWeapon() != null;
        }

        public int getMaxHP() {
            return this.getEndurance() * 10;
        }

        public boolean isAlive() {
            boolean isAlive = this.hp > 0;
            if (!isAlive) {
                log.info("{} is dead...", this.getName());
            }
            return isAlive;
        }

        public void hit(int damage) {
            this.hp -= damage;
            if (this.hp < 0) this.hp = 0;
        }

        public Weapon getRangeWeapon() {
            return getEquipment().getRangeWeapon();
        }

        public Weapon getMeleeWeapon() {
            return getEquipment().getMeleeWeapon();
        }
    }
}

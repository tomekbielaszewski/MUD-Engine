package org.grizz.game.integration.tests.quests.tutorial;

import org.grizz.game.integration.IntegrationTest;
import org.grizz.game.model.PlayerResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class BringPackages extends IntegrationTest {

    @Test
    public void testPackageBringing() throws Exception {
        //TODO Debug this - hard!
        cannotPickupPackageCreatedByAdminOnStartingLocation();
        cannotExitLowerDeckAndMessageAppearWhenTriedTo();
        respawnsPackageAndShowsQuestHintOnFirstLocation();
        respawnsPackageAndShowsQuestHintOnSecondLocation();
        canPickupPackageOnFirstLocation();
        questHintIsNotShownOnFirstLocationAnymore();
        cannotPickupPackageWhenAnotherPlayerRespawnsPackageOnFirstLocation();
        questHintIsStillVisibleOnSecondLocation();
        cannotPickupNextPackageOnSecondLocationWhenHoldingPackageFromFirstLocation();
        cannotDropPackageOnLocationDifferentThanPackageCollectingPointUpstairs();
        canDropFirstPackageOnPackageCollectingPointLocation();
        canPickupPackageOnSecondLocation();
        canDropSecondPackageOnPackageCollectingPointLocationAndShowsMessageAboutNextQuest();
        questHintIsNotVisibleOnSecondLocationAnymore();
        cannotPickupPackageOnSecondLocationAnymore();
    }

    private void cannotPickupPackageCreatedByAdminOnStartingLocation() {
        PlayerResponse response;
        response = game.runCommand("admin poloz wor z towarem", ADMIN);
        response = game.runCommand("wez wor z towarem", PLAYER1);
    }

    private void cannotExitLowerDeckAndMessageAppearWhenTriedTo() {
        PlayerResponse response;
        response = game.runCommand("up", PLAYER1);
    }

    private void respawnsPackageAndShowsQuestHintOnFirstLocation() {
        PlayerResponse response;
        response = game.runCommand("west", PLAYER1);
    }

    private void respawnsPackageAndShowsQuestHintOnSecondLocation() {
        PlayerResponse response;
        response = game.runCommand("east", PLAYER1);
        response = game.runCommand("east", PLAYER1);
        response = game.runCommand("east", PLAYER1);
    }

    private void canPickupPackageOnFirstLocation() {
        PlayerResponse response;
        response = game.runCommand("west", PLAYER1);
        response = game.runCommand("west", PLAYER1);
        response = game.runCommand("west", PLAYER1);
        response = game.runCommand("wez wor z towarem", PLAYER1);
    }

    private void questHintIsNotShownOnFirstLocationAnymore() {
        PlayerResponse response;
        response = game.runCommand("east", PLAYER1);
        response = game.runCommand("west", PLAYER1);
    }

    private void cannotPickupPackageWhenAnotherPlayerRespawnsPackageOnFirstLocation() {
        PlayerResponse response;
        response = game.runCommand("west", PLAYER2);
        response = game.runCommand("wez wor z towarem", PLAYER1);
    }

    private void questHintIsStillVisibleOnSecondLocation() {
        PlayerResponse response;
        response = game.runCommand("east", PLAYER1);
        response = game.runCommand("east", PLAYER1);
        response = game.runCommand("east", PLAYER1);
    }

    private void cannotPickupNextPackageOnSecondLocationWhenHoldingPackageFromFirstLocation() {
        PlayerResponse response;
        response = game.runCommand("wez wor z towarem", PLAYER1);
    }

    private void cannotDropPackageOnLocationDifferentThanPackageCollectingPointUpstairs() {
        PlayerResponse response;
        response = game.runCommand("wyrzuc wor z towarem", PLAYER1);
        response = game.runCommand("west", PLAYER1);
        response = game.runCommand("west", PLAYER1);
        response = game.runCommand("wyrzuc wor z towarem", PLAYER1);
    }

    private void canDropFirstPackageOnPackageCollectingPointLocation() {
        PlayerResponse response;
        response = game.runCommand("up", PLAYER1);
        response = game.runCommand("south", PLAYER1);
        response = game.runCommand("wyrzuc wor z towarem", PLAYER1);
    }

    private void canPickupPackageOnSecondLocation() {
        PlayerResponse response;
        response = game.runCommand("north", PLAYER1);
        response = game.runCommand("down", PLAYER1);
        response = game.runCommand("east", PLAYER1);
        response = game.runCommand("east", PLAYER1);
        response = game.runCommand("wez wor z towarem", PLAYER1);
    }

    private void canDropSecondPackageOnPackageCollectingPointLocationAndShowsMessageAboutNextQuest() {
        PlayerResponse response;
        response = game.runCommand("west", PLAYER1);
        response = game.runCommand("west", PLAYER1);
        response = game.runCommand("up", PLAYER1);
        response = game.runCommand("south", PLAYER1);
        response = game.runCommand("wyrzuc wor z towarem", PLAYER1);
    }

    private void questHintIsNotVisibleOnSecondLocationAnymore() {
        PlayerResponse response;
        response = game.runCommand("north", PLAYER1);
        response = game.runCommand("down", PLAYER1);
        response = game.runCommand("east", PLAYER1);
        response = game.runCommand("east", PLAYER1);
    }

    private void cannotPickupPackageOnSecondLocationAnymore() {
        PlayerResponse response;
        response = game.runCommand("wez wor z towarem", PLAYER1);
    }
}

package org.grizz.game.integration.tests.quests.tutorial;

import org.grizz.game.integration.GameIntegrationTest;
import org.grizz.game.model.items.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;

@RunWith(SpringJUnit4ClassRunner.class)
public class BringPackages extends GameIntegrationTest {
    private static final String PACKAGE = "wor z towarem";
    private static final String FIRST_LOCATION = "2";
    private static final String SECOND_LOCATION = "4";

    @Test
    public void testPackageBringing() throws Exception {
        //TODO Debug this - hard!
        cannotPickupPackageCreatedByAdminOnStartingLocation();
        cannotExitLowerDeckAndMessageAppearWhenTriedTo();
        respawnsPackageAndShowsQuestHintOnFirstLocation();
        respawnsPackageAndShowsQuestHintOnSecondLocation();
        canPickupPackageOnFirstLocation();
        questHintIsNotShownOnFirstLocationAnymore();
        cannotPickupPackageSecondTimeOnFirstLocation();
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
        List<Item> locationItemsBefore = fromDB().locationOf(PLAYER1).getMobileItems();

        admin("admin poloz wor z towarem");
        assertThat(response, hasEvent("Dodales 1 Wór z towarem na tej lokacji"));
        Mockito.verify(notifier).notify(eq(PLAYER1), argThat(hasEvent("Administrator GameMaster polozyl 1szt. Wór z towarem na tej lokacji")));

        player1("wez wor z towarem");
        assertThat(response, hasEvent("Tej roboty nie masz w umowie. Zostaw to!"));

        List<Item> locationItemsAfter = fromDB().locationOf(PLAYER1).getMobileItems();
        List<Item> playerBackpack = fromDB().player(PLAYER1).getEquipment().getBackpack();

        assertThat(locationItemsBefore, hasSize(0));
        assertThat(locationItemsAfter, hasSize(1));
        assertThat(locationItemsAfter, hasItem(item(PACKAGE)));
        assertThat(playerBackpack, hasSize(0));
    }

    private void cannotExitLowerDeckAndMessageAppearWhenTriedTo() {
        String pastLocation = fromDB().player(PLAYER1).getCurrentLocation();
        player1("idź na górę");

        String currentLocation = fromDB().player(PLAYER1).getCurrentLocation();
        assertThat(currentLocation, is(pastLocation));
        assertThat(response, hasEventLike("Gdzie leziesz z pustymi łapami?!"));
    }

    private void respawnsPackageAndShowsQuestHintOnFirstLocation() {
        List<Item> locationItemsBefore = fromDB().location(FIRST_LOCATION).getMobileItems();
        player1("idź na zachód");

        List<Item> locationItemsAfter = fromDB().location(FIRST_LOCATION).getMobileItems();

        assertThat(locationItemsBefore, hasSize(0));
        assertThat(locationItemsAfter, hasSize(1));
        assertThat(locationItemsAfter, hasItem(item(PACKAGE)));
        assertThat(response, hasEvent("Zanieś ten towar na pokład!"));
    }

    private void respawnsPackageAndShowsQuestHintOnSecondLocation() {
        player1("east");
        player1("idź na wschód");

        List<Item> locationItemsBefore = fromDB().location(SECOND_LOCATION).getMobileItems();
        player1("wschod");

        List<Item> locationItemsAfter = fromDB().location(SECOND_LOCATION).getMobileItems();
        String currentLocation = fromDB().player(PLAYER1).getCurrentLocation();

        assertThat(locationItemsBefore, hasSize(0));
        assertThat(locationItemsAfter, hasSize(1));
        assertThat(currentLocation, is(SECOND_LOCATION));
        assertThat(response, hasEvent("Zanieś ten towar na pokład!"));
    }

    private void canPickupPackageOnFirstLocation() {
        player1("west");
        player1("west");
        player1("west");
        player1("wez wor z towarem");

        //assert that player has parameter regarding pickup
    }

    private void questHintIsNotShownOnFirstLocationAnymore() {
        player1("east");
        player1("west");
    }

    private void questHintIsStillVisibleOnSecondLocation() {
        player1("east");
        player1("east");
        player1("east");
    }

    private void cannotPickupNextPackageOnSecondLocationWhenHoldingPackageFromFirstLocation() {
        player1("wez wor z towarem");
    }

    private void cannotDropPackageOnLocationDifferentThanPackageCollectingPointUpstairs() {
        player1("wyrzuc wor z towarem");
        player1("west");
        player1("west");
        player1("wyrzuc wor z towarem");
    }

    private void canDropFirstPackageOnPackageCollectingPointLocation() {
        player1("up");
        player1("south");
        player1("wyrzuc wor z towarem");
    }

    private void cannotPickupPackageSecondTimeOnFirstLocation() {
        player1("north");
        player1("down");
        player2("west");
        player1("wez wor z towarem");
    }

    private void canPickupPackageOnSecondLocation() {
        player1("east");
        player1("east");
        player1("east");
        player1("wez wor z towarem");
    }

    private void canDropSecondPackageOnPackageCollectingPointLocationAndShowsMessageAboutNextQuest() {
        player1("west");
        player1("west");
        player1("up");
        player1("south");
        player1("wyrzuc wor z towarem");
    }

    private void questHintIsNotVisibleOnSecondLocationAnymore() {
        player1("north");
        player1("down");
        player1("east");
        player1("east");
    }

    private void cannotPickupPackageOnSecondLocationAnymore() {
        player3("east");
        player3("east");
        player1("wez wor z towarem");
    }
}

package org.grizz.game.integration.tests.quests.tutorial;

import org.grizz.game.integration.GameIntegrationTest;
import org.grizz.game.model.LocationItems;
import org.grizz.game.model.Player;
import org.grizz.game.model.items.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;

@RunWith(SpringJUnit4ClassRunner.class)
public class BringPackages extends GameIntegrationTest {
    private static final String PACKAGE = "wor z towarem";
    private static final String BRONZE_COIN = "Brązowa moneta";
    private static final String FIRST_PACKAGE_RESPAWN_LOCATION = "2";
    private static final String SECOND_PACKAGE_RESPAWN_LOCATION = "4";
    private static final String PACKAGE_COLLECTING_LOCATION = "6";

    @Test
    public void testPackageBringing() throws Exception {
        cannotPickupPackageCreatedByAdminOnStartingLocation();
        cannotExitLowerDeckAndMessageAppearWhenTriedTo();
        respawnsPackageAndShowsQuestHintOnFirstLocation();
        respawnsPackageAndShowsQuestHintOnSecondLocation();
        canPickupPackageOnFirstLocation();
        questHintIsNotShownOnFirstLocationAnymore();
        questHintIsStillVisibleOnSecondLocation();
        cannotPickupNextPackageOnSecondLocationWhenHoldingPackageFromFirstLocation();
        cannotDropPackageOnRespawnLocationWhichIsNotPackageCollectingPoint();
        cannotDropPackageOnLocationWhichIsNotPackageCollectingPoint();
        canDropFirstPackageOnPackageCollectingPointLocation();
        cannotPickupPackageSecondTimeOnFirstLocation();
        canPickupPackageOnSecondLocation();
        canDropSecondPackageOnPackageCollectingPointLocationAndShowsMessageAboutNextQuest();
        questHintIsNotVisibleOnSecondLocationAnymoreButStillRespawnsPackage();
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
        List<Item> locationItemsBefore = fromDB().location(FIRST_PACKAGE_RESPAWN_LOCATION).getMobileItems();
        player1("idź na zachód");

        List<Item> locationItemsAfter = fromDB().location(FIRST_PACKAGE_RESPAWN_LOCATION).getMobileItems();

        assertThat(locationItemsBefore, hasSize(0));
        assertThat(locationItemsAfter, hasSize(1));
        assertThat(locationItemsAfter, hasItem(item(PACKAGE)));
        assertThat(response, hasEvent("Zanieś ten towar na pokład!"));
    }

    private void respawnsPackageAndShowsQuestHintOnSecondLocation() {
        player1("east");
        player1("idź na wschód");

        List<Item> locationItemsBefore = fromDB().location(SECOND_PACKAGE_RESPAWN_LOCATION).getMobileItems();
        player1("wschod");

        List<Item> locationItemsAfter = fromDB().location(SECOND_PACKAGE_RESPAWN_LOCATION).getMobileItems();
        String currentLocation = fromDB().player(PLAYER1).getCurrentLocation();

        assertThat(locationItemsBefore, hasSize(0));
        assertThat(locationItemsAfter, hasSize(1));
        assertThat(currentLocation, is(SECOND_PACKAGE_RESPAWN_LOCATION));
        assertThat(response, hasEvent("Zanieś ten towar na pokład!"));
    }

    private void canPickupPackageOnFirstLocation() {
        player1("west");
        player1("west");
        player1("west");

        Player playerBefore = fromDB().player(PLAYER1);
        String currentLocation = playerBefore.getCurrentLocation();
        List<Item> locationItemsBefore = fromDB().locationOf(PLAYER1).getMobileItems();
        List<Item> backpackBefore = playerBefore.getEquipment().getBackpack();
        boolean playerFirstLocationPickUpParamBefore = playerBefore.hasParameter("quest:tutorial-ship-package-picked-on-locations-2");

        player1("wez wor z towarem");

        Player playerAfter = fromDB().player(PLAYER1);
        List<Item> locationItemsAfter = fromDB().locationOf(PLAYER1).getMobileItems();
        List<Item> backpackAfter = playerAfter.getEquipment().getBackpack();
        boolean playerFirstLocationPickUpParamAfter = playerAfter.hasParameter("quest:tutorial-ship-package-picked-on-locations-2");

        assertThat(currentLocation, is(FIRST_PACKAGE_RESPAWN_LOCATION));

        assertThat(locationItemsBefore, hasItem(item(PACKAGE)));
        assertThat(locationItemsAfter, hasSize(0));

        assertThat(backpackBefore, hasSize(0));
        assertThat(backpackAfter, hasItem(item(PACKAGE)));

        assertThat(playerFirstLocationPickUpParamBefore, is(false));
        assertThat(playerFirstLocationPickUpParamAfter, is(true));
    }

    private void questHintIsNotShownOnFirstLocationAnymore() {
        player1("east");
        player1("west");

        String currentLocation = fromDB().player(PLAYER1).getCurrentLocation();
        assertThat(currentLocation, is(FIRST_PACKAGE_RESPAWN_LOCATION));

        assertThat(response, hasNoEvents());
    }

    private void questHintIsStillVisibleOnSecondLocation() {
        player1("east");
        player1("east");
        player1("east");

        String currentLocation = fromDB().player(PLAYER1).getCurrentLocation();
        assertThat(currentLocation, is(SECOND_PACKAGE_RESPAWN_LOCATION));

        assertThat(response, hasEvent("Zanieś ten towar na pokład!"));
    }

    private void cannotPickupNextPackageOnSecondLocationWhenHoldingPackageFromFirstLocation() {
        Player playerBefore = fromDB().player(PLAYER1);
        List<Item> backpackBefore = playerBefore.getEquipment().getBackpack();

        player1("wez wor z towarem");

        List<Item> backpackAfter = fromDB().player(PLAYER1).getEquipment().getBackpack();

        assertThat(backpackBefore, hasSize(1));
        assertThat(backpackBefore, hasItem(item(PACKAGE)));
        assertThat(playerBefore.getCurrentLocation(), is(SECOND_PACKAGE_RESPAWN_LOCATION));
        assertThat(backpackAfter, hasSize(1));
        assertThat(backpackAfter, hasItem(item(PACKAGE)));
        assertThat(response, hasEvent("Masz już jeden pakunek w rękach!"));
    }

    private void cannotDropPackageOnRespawnLocationWhichIsNotPackageCollectingPoint() {
        Player playerBefore = fromDB().player(PLAYER1);
        List<Item> backpackBefore = playerBefore.getEquipment().getBackpack();

        player1("wyrzuc wor z towarem");

        List<Item> backpackAfter = fromDB().player(PLAYER1).getEquipment().getBackpack();

        assertThat(playerBefore.getCurrentLocation(), is(SECOND_PACKAGE_RESPAWN_LOCATION));
        assertThat(backpackBefore, hasSize(1));
        assertThat(backpackBefore, hasItem(item(PACKAGE)));
        assertThat(backpackAfter, hasSize(1));
        assertThat(backpackAfter, hasItem(item(PACKAGE)));
        assertThat(response, hasEvent("Gdzie to kładziesz?! Punkt rozładunku jest na pokładzie!"));
    }

    private void cannotDropPackageOnLocationWhichIsNotPackageCollectingPoint() {
        Player playerBefore = fromDB().player(PLAYER1);
        List<Item> backpackBefore = playerBefore.getEquipment().getBackpack();

        player1("west");
        player1("west");
        player1("wyrzuc wor z towarem");

        Player playerAfter = fromDB().player(PLAYER1);
        List<Item> backpackAfter = playerAfter.getEquipment().getBackpack();

        assertThat(playerBefore.getCurrentLocation(), is(SECOND_PACKAGE_RESPAWN_LOCATION));
        assertThat(playerAfter.getCurrentLocation(), is(STARTING_LOCATION));
        assertThat(backpackBefore, hasSize(1));
        assertThat(backpackBefore, hasItem(item(PACKAGE)));
        assertThat(backpackAfter, hasSize(1));
        assertThat(backpackAfter, hasItem(item(PACKAGE)));
        assertThat(response, hasEvent("Gdzie to kładziesz?! Punkt rozładunku jest na pokładzie!"));
    }

    private void canDropFirstPackageOnPackageCollectingPointLocation() {
        player1("up");
        player1("south");

        Player playerBefore = fromDB().player(PLAYER1);
        List<Item> backpackBefore = playerBefore.getEquipment().getBackpack();

        player1("wyrzuc wor z towarem");

        Player playerAfter = fromDB().player(PLAYER1);
        List<Item> backpackAfter = playerAfter.getEquipment().getBackpack();

        assertThat(playerAfter.getCurrentLocation(), is(PACKAGE_COLLECTING_LOCATION));
        assertThat(backpackBefore, hasSize(1));
        assertThat(backpackBefore, hasItem(item(PACKAGE)));
        assertThat(backpackAfter, hasSize(0));
        assertThat(response, hasEvent("Dobry majtek! Jeszcze jeden taki wór i wystarczy."));
        assertThat(response, hasEvent("Straciles 1 wor z towarem"));
    }

    private void cannotPickupPackageSecondTimeOnFirstLocation() {
        player2("west");
        assertThat(response, hasEvent("Zanieś ten towar na pokład!"));

        Player playerBefore = fromDB().player(PLAYER1);
        LocationItems locationBefore = fromDB().location(FIRST_PACKAGE_RESPAWN_LOCATION);
        List<Item> backpackBefore = playerBefore.getEquipment().getBackpack();

        player1("north");
        player1("down");
        player1("west");
        player1("wez wor z towarem");

        Player playerAfter = fromDB().player(PLAYER1);
        LocationItems locationAfter = fromDB().location(FIRST_PACKAGE_RESPAWN_LOCATION);
        List<Item> backpackAfter = playerAfter.getEquipment().getBackpack();

        assertThat(playerAfter.getCurrentLocation(), is(FIRST_PACKAGE_RESPAWN_LOCATION));
        assertThat(locationBefore.getMobileItems(), hasItem(item(PACKAGE)));
        assertThat(locationAfter.getMobileItems(), hasItem(item(PACKAGE)));
        assertThat(backpackBefore, hasSize(0));
        assertThat(backpackAfter, hasSize(0));
    }

    private void canPickupPackageOnSecondLocation() {
        player1("east");
        player1("east");
        player1("east");

        assertThat(response, hasEvent("Zanieś ten towar na pokład!"));
        Player playerBefore = fromDB().player(PLAYER1);
        List<Item> backpackBefore = playerBefore.getEquipment().getBackpack();
        LocationItems locationBefore = fromDB().location(SECOND_PACKAGE_RESPAWN_LOCATION);

        player1("wez wor z towarem");

        Player playerAfter = fromDB().player(PLAYER1);
        List<Item> backpackAfter = playerAfter.getEquipment().getBackpack();
        LocationItems locationAfter = fromDB().location(SECOND_PACKAGE_RESPAWN_LOCATION);

        assertThat(locationBefore.getMobileItems(), hasItem(item(PACKAGE)));
        assertThat(locationAfter.getMobileItems(), hasSize(0));

        assertThat(playerAfter.getCurrentLocation(), is(SECOND_PACKAGE_RESPAWN_LOCATION));
        assertThat(backpackBefore, hasSize(0));
        assertThat(backpackAfter, hasItem(item(PACKAGE)));
        assertThat(backpackAfter, hasSize(1));
    }

    private void canDropSecondPackageOnPackageCollectingPointLocationAndShowsMessageAboutNextQuest() {
        player1("west");
        player1("west");
        player1("up");
        player1("south");

        Player playerBefore = fromDB().player(PLAYER1);
        List<Item> backpackBefore = playerBefore.getEquipment().getBackpack();

        player1("wyrzuc wor z towarem");

        Player playerAfter = fromDB().player(PLAYER1);
        List<Item> backpackAfter = playerAfter.getEquipment().getBackpack();
        LocationItems locationAfter = fromDB().location(PACKAGE_COLLECTING_LOCATION);

        assertThat(playerAfter.getCurrentLocation(), is(PACKAGE_COLLECTING_LOCATION));
        assertThat(backpackBefore, hasItem(item(PACKAGE)));
        assertThat(backpackAfter, hasSize(4));
        assertThat(backpackAfter, hasItem(item(BRONZE_COIN)));
        assertThat(backpackAfter, not(hasItem(item(PACKAGE))));
        assertThat(locationAfter.getMobileItems(), hasSize(2));
        assertThat(locationAfter.getMobileItems(), hasItem(item(PACKAGE)));
        assertThat(response, hasEvent("Masz tu kilka miedziaków za uczciwą pracę!"));
        assertThat(response, hasEvent("Otrzymane przedmioty:"));
        assertThat(response, hasEventLike("4x Brązowa moneta"));
        assertThat(response, hasEventLike("Weź te papiery i zanieś je sternikowi!"));
        assertThat(response, not(hasEvent("Dobry majtek! Jeszcze jeden taki wór i wystarczy.")));
    }

    private void questHintIsNotVisibleOnSecondLocationAnymoreButStillRespawnsPackage() {
        player1("north");
        player1("down");
        player1("east");
        LocationItems locationBefore = fromDB().location(SECOND_PACKAGE_RESPAWN_LOCATION);
        player1("east");
        LocationItems locationAfter = fromDB().location(SECOND_PACKAGE_RESPAWN_LOCATION);

        assertThat(response, hasNoEvents());
        assertThat(locationBefore.getMobileItems(), hasSize(0));
        assertThat(locationAfter.getMobileItems(), hasSize(1));
        assertThat(locationAfter.getMobileItems(), hasItem(item(PACKAGE)));
    }

    private void cannotPickupPackageOnSecondLocationAnymore() {
        LocationItems locationBefore = fromDB().location(SECOND_PACKAGE_RESPAWN_LOCATION);

        player1("wez wor z towarem");

        LocationItems locationAfter = fromDB().location(SECOND_PACKAGE_RESPAWN_LOCATION);

        assertThat(locationBefore.getMobileItems(), hasSize(1));
        assertThat(locationBefore.getMobileItems(), hasItem(item(PACKAGE)));
        assertThat(locationAfter.getMobileItems(), hasSize(1));
        assertThat(response, hasEvent("Juz skonczyles pracowac! Nie musisz już nosić tych pakunków."));
    }
}

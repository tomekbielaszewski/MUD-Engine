package org.grizz.game.ui.impl;

import com.google.common.collect.Maps;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Item;
import org.grizz.game.ui.OutputFormatter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Grizz on 2015-04-27.
 */
public class OutputFormatterImpl implements OutputFormatter {
    @Override
    public String format(PlayerResponse playerResponse) {
        StringBuilder sb = new StringBuilder();

        appendPlayerEvents(playerResponse, sb);

        appendLocationName(playerResponse, sb);
        appendLocationDescription(playerResponse, sb);
        appendLocationStaticItems(playerResponse, sb);
        appendPlayersOnLocation(playerResponse, sb);
        appendPossibleExits(playerResponse, sb);
        appendVisibleItems(playerResponse, sb);
        appendEquipmentItems(playerResponse, sb);

        sb.append("\n\n");

        return sb.toString();
    }

    private void appendSeparator(StringBuilder sb, int width) {
        sb.append("\n");
        for (int i = 0; i < width; i++) {
            sb.append("-");
        }
        sb.append("\n");
    }

    private void appendLocationName(PlayerResponse playerResponse, StringBuilder sb) {
        if (playerResponse.getCurrentLocation() != null) {
            appendSeparator(sb, 79);
            sb.append(playerResponse.getCurrentLocation().getName());
        }
    }

    private void appendLocationDescription(PlayerResponse playerResponse, StringBuilder sb) {
        if (playerResponse.getCurrentLocation() != null) {
            appendSeparator(sb, 79);
            sb.append(playerResponse.getCurrentLocation().getDescription());
        }
    }

    private void appendLocationStaticItems(PlayerResponse playerResponse, StringBuilder sb) {
        String itemSeparator = " oraz ";
        if (!playerResponse.getLocationStaticItems().isEmpty()) {
            sb.append("\n");
            sb.append("Rozglądając się dookoła zauważyłeś ");
            for (Item item : playerResponse.getLocationStaticItems()) {
                sb.append(item.getDescription());
                sb.append(itemSeparator);
            }

            sb.setLength(sb.length() - itemSeparator.length());
            sb.append(".");
        }
    }

    private void appendPlayersOnLocation(PlayerResponse playerResponse, StringBuilder sb) {
        List<String> players = playerResponse.getPlayersOnLocation();
        if (!players.isEmpty()) {
            appendSeparator(sb, 79);
            if (players.size() > 1) {
                sb.append("Obok ciebie stoją ");
                for (String player : players) {
                    sb.append(player);
                    sb.append(", ");
                }
                sb.setLength(sb.length() - 2);
            } else {
                sb.append("Obok ciebie stoi ");
                sb.append(players.get(0));
                sb.append(".");
            }
        }
    }

    private void appendPossibleExits(PlayerResponse playerResponse, StringBuilder sb) {
        if (playerResponse.getCurrentLocation() != null) {
            appendSeparator(sb, 79);
            List<String> possibleExits = playerResponse.getPossibleExits();

            for (String possibleExit : possibleExits) {
                sb.append(possibleExit);
                sb.append(", ");
            }

            sb.setLength(sb.length() - 2);
        }
    }

    private void appendVisibleItems(PlayerResponse playerResponse, StringBuilder sb) {
        if (!playerResponse.getLocationItems().isEmpty()) {
            Map<Item, Integer> countedItems = countItems(playerResponse.getLocationItems());

            appendSeparator(sb, 79);
            sb.append("Widzisz tutaj:\n");

            for (Map.Entry<Item, Integer> countedItem : countedItems.entrySet()) {
                Item item = countedItem.getKey();
                Integer amount = countedItem.getValue();
                String itemDescription = String.format("%dx\t\t %s: %s", amount, item.getName(), item.getDescription());
                sb.append("\t" + itemDescription);
                sb.append("\n");
            }

            sb.setLength(sb.length() - 1);
        }
    }

    private void appendEquipmentItems(PlayerResponse playerResponse, StringBuilder sb) {
        if (!playerResponse.getEquipmentItems().isEmpty()) {
            Map<Item, Integer> countedItems = countItems(playerResponse.getEquipmentItems());

            appendSeparator(sb, 79);

            for (Map.Entry<Item, Integer> countedItem : countedItems.entrySet()) {
                Item item = countedItem.getKey();
                Integer amount = countedItem.getValue();
                String itemDescription = String.format("%dx\t\t %s: %s", amount, item.getName(), item.getDescription());
                sb.append("\t" + itemDescription);
                sb.append("\n");
            }

            sb.setLength(sb.length() - 1);
        }
    }

    private void appendPlayerEvents(PlayerResponse playerResponse, StringBuilder sb) {
        List<String> playerEvents = playerResponse.getPlayerEvents();

        if (!playerEvents.isEmpty()) {
            appendSeparator(sb, 79);
            for (String event : playerEvents) {
                sb.append(event);
                sb.append("\n");
            }

            sb.setLength(sb.length() - 1);
        }
    }

    private Map<Item, Integer> countItems(List<Item> equipmentItems) {
        Map<Item, Integer> countedItems = Maps.newTreeMap(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (Item item : equipmentItems) {
            countedItems.merge(item, 1, (v, vv) -> v + vv);
        }

        return countedItems;
    }
}

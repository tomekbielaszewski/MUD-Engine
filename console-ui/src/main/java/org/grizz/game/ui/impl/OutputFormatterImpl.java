package org.grizz.game.ui.impl;

import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Item;
import org.grizz.game.ui.OutputFormatter;

import java.util.List;

/**
 * Created by Grizz on 2015-04-27.
 */
public class OutputFormatterImpl implements OutputFormatter {
    @Override
    public String format(PlayerResponse playerResponse) {
        StringBuilder sb = new StringBuilder();

        appendLocationName(playerResponse, sb);
        appendLocationDescription(playerResponse, sb);
        appendPossibleExits(playerResponse, sb);
        appendVisibleItems(playerResponse, sb);
        appendEquipmentItems(playerResponse, sb);

        appendPlayerEvents(playerResponse, sb);

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
            appendSeparator(sb, 79);
            sb.append("Widzisz tutaj:\n");
            for (Item item : playerResponse.getLocationItems()) {
                String itemDescription = String.format("%s: %s", item.getName(), item.getDescription());
                sb.append("\t" + itemDescription);
                sb.append("\n");
            }
            sb.setLength(sb.length() - 1);
        }
    }

    private void appendEquipmentItems(PlayerResponse playerResponse, StringBuilder sb) {
        if (!playerResponse.getEquipmentItems().isEmpty()) {
            appendSeparator(sb, 79);
            sb.append("W ekwipunku posiadasz:\n");
            for (Item item : playerResponse.getEquipmentItems()) {
                String itemDescription = String.format("%s: %s", item.getName(), item.getDescription());
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


}

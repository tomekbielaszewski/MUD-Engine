package org.grizz.game.ui.impl;

import org.grizz.game.model.PlayerResponse;
import org.grizz.game.ui.OutputFormatter;

import java.util.List;

/**
 * Created by Grizz on 2015-04-27.
 */
public class OutputFormatterImpl implements OutputFormatter {
    @Override
    public String format(PlayerResponse playerResponse) {
        StringBuilder sb = new StringBuilder();

        appendSeparator(sb, 79);
        appendLocationName(playerResponse, sb);
        appendSeparator(sb, 79);
        appendLocationDescription(playerResponse, sb);
        appendSeparator(sb, 79);
        appendPossibleExits(playerResponse, sb);
        appendSeparator(sb, 79);
        if (!playerResponse.getLocationItems().isEmpty()) {
            appendVisibleItems(playerResponse, sb);
            appendSeparator(sb, 79);
        }
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
        sb.append(playerResponse.getLocationName());
    }

    private void appendLocationDescription(PlayerResponse playerResponse, StringBuilder sb) {
        sb.append(playerResponse.getLocationDescription());
    }

    private void appendPossibleExits(PlayerResponse playerResponse, StringBuilder sb) {
        List<String> possibleExits = playerResponse.getPossibleExits();

        for (String possibleExit : possibleExits) {
            sb.append(possibleExit);
            sb.append(", ");
        }

        sb.setLength(sb.length() - 2);
    }

    private void appendVisibleItems(PlayerResponse playerResponse, StringBuilder sb) {
        sb.append("Znajdujesz tutaj:\n");
        for (String itemDescription : playerResponse.getLocationItems()) {
            sb.append("\t" + itemDescription);
            sb.append("\n");
        }
        sb.setLength(sb.length() - 1);
    }

    private void appendPlayerEvents(PlayerResponse playerResponse, StringBuilder sb) {
        List<String> playerEvents = playerResponse.getPlayerEvents();

        for (String event : playerEvents) {
            sb.append(event);
            sb.append("\n");
        }

        sb.setLength(sb.length() - 1);
    }


}

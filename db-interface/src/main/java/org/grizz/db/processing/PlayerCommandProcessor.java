package org.grizz.db.processing;

import com.google.common.collect.Lists;
import org.grizz.db.model.PlayerCommand;
import org.grizz.db.model.ProcessedPlayerResponse;
import org.grizz.db.model.RawPlayerResponse;
import org.grizz.game.Game;
import org.grizz.game.model.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerCommandProcessor {
    @Autowired
    private Game game;
    @Autowired
    private ResponseCollector responseCollector;
    @Autowired
    private PlayerResponseFormatter playerResponseFormatter;

    public List<ProcessedPlayerResponse> process(PlayerCommand command) {
        PlayerResponse response = game.runCommand(command.getCommand(), command.getPlayer());
        responseCollector.collect(command.getPlayer(), response);
        command.setProcessed(true);
        return getResponses(command);
    }

    private List<ProcessedPlayerResponse> getResponses(PlayerCommand command) {
        List<RawPlayerResponse> rawResponses = responseCollector.getRawResponses();
        List<ProcessedPlayerResponse> processedPlayerResponses = Lists.newArrayList();

        for (RawPlayerResponse rawResponse : rawResponses) {
            ProcessedPlayerResponse processedPlayerResponse = ProcessedPlayerResponse.builder()
                    .playerCommand(command)
                    .receiver(rawResponse.getPlayerName())
                    .response(playerResponseFormatter.format(rawResponse.getPlayerName(), command.getCommand(), rawResponse.getResponse()))
                    .sent(false)
                    .build();
            processedPlayerResponses.add(processedPlayerResponse);
        }

        responseCollector.reset();
        return processedPlayerResponses;
    }
}

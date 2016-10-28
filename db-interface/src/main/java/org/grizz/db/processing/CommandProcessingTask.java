package org.grizz.db.processing;

import lombok.extern.slf4j.Slf4j;
import org.grizz.db.model.PlayerCommand;
import org.grizz.db.model.ProcessedPlayerResponse;
import org.grizz.db.model.repository.PlayerCommandRepository;
import org.grizz.db.model.repository.ProcessedPlayerResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommandProcessingTask implements Runnable {
    @Autowired
    private PlayerCommandRepository playerCommandRepository;
    @Autowired
    private ProcessedPlayerResponseRepository playerResponseRepository;
    @Autowired
    private PlayerCommandProcessor commandProcessor;

    @Override
    public void run() {
        List<PlayerCommand> commands = playerCommandRepository.findByProcessed(false);//TODO sort by timestamp

        if(!commands.isEmpty())
            log.info("Fetched {} unprocessed commands", commands.size());

        for (PlayerCommand command : commands) {
            List<ProcessedPlayerResponse> playerResponses = commandProcessor.process(command);
            playerResponseRepository.save(playerResponses);
            playerCommandRepository.save(command);

            for (ProcessedPlayerResponse response : playerResponses) {
                System.out.println("<--------------------------->");
                System.out.println(response.getResponse());
            }
        }
    }
}

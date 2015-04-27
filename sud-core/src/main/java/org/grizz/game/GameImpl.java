package org.grizz.game;

import com.google.common.collect.Lists;
import org.grizz.game.commands.CommandHandlerBus;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerContextImpl;
import org.grizz.game.model.repository.PlayerRepository;
import org.grizz.game.service.ResponseExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Grizz on 2015-04-17.
 */
@Service
public class GameImpl implements Game {
    //    @Autowired
//    private ScriptEngine scriptEngine;
//    @Autowired
//    private MovementService movementService;
    @Autowired
    private CommandHandlerBus commandHandlerBus;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ResponseExtractor responseExtractor;

    @Override
    public PlayerResponse runCommand(String command, String player) {
        PlayerContext context = playerRepository.get(player);
        commandHandlerBus.execute(command, context);

//        ScriptContext context = scriptEngine.getContext();
//        context.setWriter(new OutputStreamWriter(System.out));
//        try {
//            scriptEngine.eval("print('Dziala!');", context);
//        } catch (ScriptException e) {
//            e.printStackTrace();
//        }
//
//        PlayerContextImpl grizz = new PlayerContextImpl("Grizz",
//                100,
//                50,
//                20,
//                25,
//                45,
//                80,
//                "1",
//                "1",
//                Maps.newHashMap(),
//                Lists.newArrayList());
//        PlayerContextImpl grizzPlusStr = grizz.copy().strength(101).build();
//
//        System.out.println("Grizz:  " + grizz);
//        System.out.println("Grizz+: " + grizzPlusStr);
//
//        System.out.println("Moving Grizz NORTH");
//        movementService.move(Direction.NORTH, grizzPlusStr);
//
//        System.out.println(grizzPlusStr);
//
//        System.out.println("Moving Grizz NORTH");
//        movementService.move(Direction.NORTH, grizzPlusStr);
//        System.out.println(grizzPlusStr);
//
//        commandHandlerBus.execute("south", grizzPlusStr);
//        System.out.println(grizzPlusStr);
//
//        commandHandlerBus.execute("east", grizzPlusStr);
//        System.out.println(grizzPlusStr);
//
//        commandHandlerBus.execute("east", grizzPlusStr);
//        System.out.println(grizzPlusStr);

        PlayerResponse response = responseExtractor.extract(context);
        resetContext(context);

        return response;
    }

    private void resetContext(PlayerContext context) {
        PlayerContextImpl playerContext = (PlayerContextImpl) context;
        playerContext.setEvents(Lists.newArrayList());
    }
}

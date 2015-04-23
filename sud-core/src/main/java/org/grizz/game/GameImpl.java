package org.grizz.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.OutputStreamWriter;

/**
 * Created by Grizz on 2015-04-17.
 */
@Service
public class GameImpl implements Game {
    @Autowired
    private ScriptEngine scriptEngine;

    @Override
    public String runCommand(String command, String player) {

        ScriptContext context = scriptEngine.getContext();
        context.setWriter(new OutputStreamWriter(System.out));
        try {
            scriptEngine.eval("print('Dziala!');", context);
        } catch (ScriptException e) {
            e.printStackTrace();
        }


        return String.format("Uzyles komendy [%s] jako [%s]", command, player);
    }
}

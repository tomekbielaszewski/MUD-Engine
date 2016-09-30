package org.grizz.game;

import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.utils.FileUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws IOException {
        Template template = getFreemarkerTemplate("output.template");

        Game game = GameFactory.getInstance(
                (playerName, response) -> print(template, response)
        );
        Scanner sc = new Scanner(System.in);

        try {
            String player = "Grizz";
            String command = "spojrz";

            do {
                PlayerResponse response = game.runCommand(command, player);
                print(template, response);
            } while (!(command = sc.nextLine()).equals("q"));
        } finally {
            sc.close();
        }
    }

    private static void print(Template template, PlayerResponse response) {
        HashMap<Object, Object> model = Maps.newHashMap();
        model.put("response", response);
        Writer writer = new OutputStreamWriter(System.out);
        try {
            template.process(model, writer);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Template getFreemarkerTemplate(String templateName) throws IOException {
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(FileUtils.getFilepath("").toFile());
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return cfg.getTemplate(templateName);
    }
}

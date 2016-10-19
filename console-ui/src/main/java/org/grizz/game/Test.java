package org.grizz.game;

import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.converters.ItemListToItemStackConverter;
import org.grizz.game.utils.FileUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws IOException {
        Template template = getFreemarkerTemplate("output.ftl");

        Game game = GameFactory.getInstance(
                (playerName, response) -> print(playerName, template, response)
        );
        Scanner sc = new Scanner(System.in);

        try {
            String player = "Grizz";
            String command = "spojrz";

            do {
                PlayerResponse response = game.runCommand(command, player);
                print(player, template, response);
            } while (!(command = sc.nextLine()).equals("q"));
        } finally {
            sc.close();
        }
    }

    private static void print(String playerName, Template template, PlayerResponse response) {
        HashMap<Object, Object> model = Maps.newHashMap();
        model.put("response", response);
        model.put("player", playerName);

        Optional.ofNullable(response.getEquipment())
                .map(eq -> eq.getBackpack())
                .ifPresent(backpack -> model.put("backpack", new ItemListToItemStackConverter().convert(backpack)));

        Optional.ofNullable(response.getCurrentLocation())
                .map(location -> location.getItems().getMobileItems())
                .ifPresent(items -> model.put("locationItems", new ItemListToItemStackConverter().convert(items)));

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

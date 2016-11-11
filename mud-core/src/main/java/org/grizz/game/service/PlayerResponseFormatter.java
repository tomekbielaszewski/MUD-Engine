package org.grizz.game.service;

import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.converters.ItemListToItemStackConverter;
import org.grizz.game.utils.FileUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Optional;

@Service
public class PlayerResponseFormatter {
    private Template template = getFreemarkerTemplate("output.ftl");

    public String format(String playerName, String command, PlayerResponse response) {
        return format(playerName, command, response, template);
    }

    private String format(String playerName, String command, PlayerResponse response, Template template) {
        HashMap<Object, Object> model = getModel(playerName, command, response);
        return formatWithModel(template, model);
    }

    private HashMap<Object, Object> getModel(String playerName, String command, PlayerResponse response) {
        HashMap<Object, Object> model = Maps.newHashMap();
        model.put("response", response);
        model.put("player", playerName);
        model.put("command", command);

        Optional.ofNullable(response.getEquipment())
                .map(Equipment::getBackpack)
                .ifPresent(backpack -> model.put("backpack", new ItemListToItemStackConverter().convert(backpack)));

        Optional.ofNullable(response.getCurrentLocation())
                .map(location -> location.getItems().getMobileItems())
                .ifPresent(items -> model.put("locationItems", new ItemListToItemStackConverter().convert(items)));
        return model;
    }

    private String formatWithModel(Template template, HashMap<Object, Object> model) {
        StringWriter writer = new StringWriter();
        try {
            template.process(model, writer);
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }

        return writer.toString();
    }

    private static Template getFreemarkerTemplate(String templateName) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        try {
            cfg.setDirectoryForTemplateLoading(FileUtils.getFilepath("response").toFile());
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            return cfg.getTemplate(templateName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

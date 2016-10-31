package org.grizz.db.processing;

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

    public Object format(String playerName, PlayerResponse response) {
        return format(playerName, response, template);
    }

    private String format(String playerName, PlayerResponse response, Template template) {
        HashMap<Object, Object> model = Maps.newHashMap();
        model.put("response", response);
        model.put("player", playerName);

        Optional.ofNullable(response.getEquipment())
                .map(Equipment::getBackpack)
                .ifPresent(backpack -> model.put("backpack", new ItemListToItemStackConverter().convert(backpack)));

        Optional.ofNullable(response.getCurrentLocation())
                .map(location -> location.getItems().getMobileItems())
                .ifPresent(items -> model.put("locationItems", new ItemListToItemStackConverter().convert(items)));

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
            cfg.setDirectoryForTemplateLoading(FileUtils.getFilepath("").toFile());
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            return cfg.getTemplate(templateName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

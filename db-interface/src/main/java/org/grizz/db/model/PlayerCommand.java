package org.grizz.db.model;

import lombok.Data;
import lombok.experimental.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "commandQueue")
public class PlayerCommand {
    private String id;
    private String player;
    private String command;
    private boolean processed;
    private long timestamp;
}

package org.grizz.db.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
//@Document(collection = "commandResponses")
public class ProcessedPlayerResponse {
    private String id;
    private String receiver;
    private Object response;
    private PlayerCommand playerCommand;
    private boolean sent;
}

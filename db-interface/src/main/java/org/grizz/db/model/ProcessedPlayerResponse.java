package org.grizz.db.model;


import lombok.Data;
import lombok.experimental.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "commandResponses")
public class ProcessedPlayerResponse {
    private String id;
    private String receiver;
    private Object response;
    private String playerCommandId;
    private boolean sent;
}

package ptr.web.lunch.dto;

import java.io.Serializable;
import java.util.HashMap;

public class ResultDTO implements Serializable {

   private String status;
   private String payload;
   private String message;

    public ResultDTO() {
    }

    public ResultDTO(String payload, String status, String message) {
        this.payload = payload;
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }





}

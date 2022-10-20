package helpers;

import models.Message;
import org.json.JSONObject;

public class InfoResponse {
    private JSONObject jsonObject;

    public InfoResponse(String status, String message) {
        JSONObject response = new JSONObject();
        response.put("info", new JSONObject(new Message(status, message)));
        jsonObject = response;
    }

    @Override
    public String toString() {
        return jsonObject.toString();
    }

    public InfoResponse() {
    }
}

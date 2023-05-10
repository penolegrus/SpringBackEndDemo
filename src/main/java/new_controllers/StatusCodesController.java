package new_controllers;

import models.trains.StatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class StatusCodesController {

    private HttpServletResponse modifyServlet(HttpServletResponse servlet) {
        servlet.addHeader("Access-Control-Allow-Origin", "*");
        servlet.addHeader("Access-Control-Allow-Methods", "GET");
        servlet.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        servlet.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
        servlet.addHeader("Access-Control-Allow-Credentials", "true");
        servlet.addIntHeader("Access-Control-Max-Age", 10);
        return servlet;
    }

    @GetMapping(path = "/api/created", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public StatusCode getCreated201(HttpServletResponse servlet) {
        modifyServlet(servlet).setStatus(201);
        return new StatusCode(201, "created");
    }

    @GetMapping(path = "/api/no-content", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public StatusCode getNoContent204(HttpServletResponse response) {
        modifyServlet(response).setStatus(204);
        return new StatusCode(204, "No content");
    }

    @GetMapping(path = "/api/moved", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusCode getMoved301(HttpServletResponse response) {
        modifyServlet(response).setStatus(301);
        return new StatusCode(301, "Moved Permanently");
    }

    @GetMapping(path = "/api/bad-request", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusCode getBadRequest400(HttpServletResponse response) {
        modifyServlet(response).setStatus(400);
        return new StatusCode(400, "Bad Request");
    }

    @GetMapping(path = "/api/unauthorized", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusCode getUnauthorized401(HttpServletResponse response) {
        modifyServlet(response).setStatus(401);
        return new StatusCode(401, "Unauthorized");
    }

    @GetMapping(path = "/api/forbidden", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusCode getForbidden403(HttpServletResponse response) {
        modifyServlet(response).setStatus(403);
        return new StatusCode(403, "Forbidden");
    }

    @GetMapping(path = "/api/invalid-url", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusCode getInvalidUrl404(HttpServletResponse response) {
        modifyServlet(response).setStatus(404);
        return new StatusCode(404, "Not Found");
    }

}

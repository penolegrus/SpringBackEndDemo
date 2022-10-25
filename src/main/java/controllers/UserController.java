//package controllers;
//
//import app.UserDataBase;
//import helpers.Utils;
//import models.ChangeUserPass;
//import models.InfoMessage;
//import models.Message;
//import models.User;
//import org.json.JSONObject;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletResponse;
//import java.util.List;
//
//import static helpers.Constants.*;
//
////@RestController
//public class UserController {
//    private final int limitToDelete = 200;
//
//    private final UserDataBase userDataBase = new UserDataBase();
//
//    @GetMapping(path = USERS_API_URL + "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<?> getUser(@PathVariable Integer userId) {
//        User user = UserDataBase.getUser(userId);
//        if (user == null) {
//            return ResponseEntity.status(400).body(new InfoMessage("fail", "User not found"));
//        }
//        return ResponseEntity.ok(user);
//    }
//
//    @GetMapping(path = USERS_API_URL, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public List<User> getUsers() {
//        List<User> users = userDataBase.getAllUsers();
//
//        if (users.size() > limitToDelete) {
//            while (users.size() >= 100) {
//                users.remove(users.size() - 1);
//            }
//        }
//
//        userDataBase.updateDataBase(users);
//        return users;
//    }
//
//    @DeleteMapping(path = USERS_API_URL + "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public String deleteUser(@PathVariable Integer userId, HttpServletResponse servlet) {
//        if (!userDataBase.isUserExist(userId)) {
//            servlet.setStatus(400);
//            return new InfoResponse("fail", "User not found to delete").toString();
//        }
//        if (userDataBase.isUserInBaseUsers(userId)) {
//            servlet.setStatus(400);
//            return new InfoResponse("fail", "Cant delete base users").toString();
//        }
//
//        userDataBase.deleteUser(userId);
//        return new InfoResponse("success", "User successfully deleted").toString();
//    }
//
//
//    //TODO: здесь после авторизации должен прилетать jwt токен и усталавливаться заголовок Authorization для дальнейших запросов
//    @PostMapping(path = {LOGIN_API_URL}, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public String loginPost(@RequestBody User user) {
//        JSONObject response = new JSONObject();
//        if (userDataBase.isLoginExists(user.getLogin())) {
//            response.put("status", "success");
//            response.put("jwt", Utils.randomString(50));
//        } else {
//            return new InfoResponse("fail", "User not found").toString();
//        }
//        return response.toString();
//    }
//
//    @PostMapping(path = {REGISTER_API_URL}, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public String registerPost(@RequestBody User user, HttpServletResponse responseServlet) {
//        if (userDataBase.getAllUsers().size() > limitToDelete) {
//            userDataBase.removeLastUsers(100);
//        }
//        JSONObject response = new JSONObject();
//
//        if (userDataBase.isLoginExists(user.getLogin())) {
//            responseServlet.setStatus(400);
//            return new InfoResponse("fail", "Login already exist").toString();
//        }
//
//        if (user.getLogin() == null || user.getPass() == null) {
//            responseServlet.setStatus(400);
//            return new InfoResponse("fail", "Missing login or password").toString();
//        }
//
//        user.setId(Utils.getRandomInt());
//        response.put("info", new JSONObject((new Message("success", "User created"))));
//        response.put("register_data", new JSONObject(user));
//        responseServlet.setStatus(201);
//
//        userDataBase.createUser(user);
//
//        return response.toString();
//    }
//
//    @PutMapping(path = USERS_API_URL + "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public String updateUserPassword(@PathVariable Integer userId, @RequestBody ChangeUserPass passwordJson, HttpServletResponse responseServlet) {
//
//        if (passwordJson.getPassword() == null || passwordJson.getPassword().isEmpty()) {
//            return new InfoResponse("fail", "Body has no password parameter").toString();
//        }
//        User oldUser = userDataBase.getUser(userId);
//
//        if (oldUser == null) {
//            responseServlet.setStatus(400);
//            return new InfoResponse("fail", "User not found to delete").toString();
//        }
//
//        if (userDataBase.isUserInBaseUsers(oldUser)) {
//            responseServlet.setStatus(400);
//            return new InfoResponse("fail", "Cant update base users").toString();
//        }
//
//        userDataBase.updateUser(oldUser, passwordJson.getPassword());
//        return new InfoResponse("success", "User password successfully changed").toString();
//    }
//
//    @GetMapping(path = USERS_API_URL + "/addUsers/{count}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public String addUsersToDataBase(@PathVariable Integer count) {
//        if (count > 250) {
//            return new InfoResponse("fail", "You can add less than 250 random users").toString();
//        }
//
//        for (int i = 0; i < count; i++) {
//          //  userDataBase.createUser(new User("testLogin" + i, "testPassword" + i, Utils.getRandomInt()));
//        }
//        return new InfoResponse("success", count + " users added to database. Users count - "
//                + userDataBase.getAllUsers().size()).toString();
//    }
//}
//

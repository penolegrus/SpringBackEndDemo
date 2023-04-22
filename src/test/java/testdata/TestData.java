package testdata;


import db_models.User;
import dto.UserDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TestData {
    public static UserDTO ADMIN_USER = new UserDTO("admin", "admin", new ArrayList<>());
}

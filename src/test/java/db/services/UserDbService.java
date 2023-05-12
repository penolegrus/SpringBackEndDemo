package db.services;

import db.EmfBuilder;
import db.JpaService;
import models.user.User;


public class UserDbService extends JpaService {
    public UserDbService() {
        super(new EmfBuilder()
                .mySql()
                .jdbcUrl("jdbc:mysql://host:3306/db_schema")
                .username("user")
                .password("pass")
                .persistenceUnitName("test")
                .build()
                .createEntityManager());
    }

    public User get(long id) {
        return em.find(User.class, id);
    }

    public void save(User user) {
       persist(user);
    }

    public User getUserByLogin(String login) {
        return em.createQuery(
                        "from User where login=:login", User.class)
                .setParameter("login", login)
                .getSingleResult();
    }

    public void updatePassword(String login, String newPass) {
        User user = getUserByLogin(login);
        user.setPass(newPass);
        merge(user);
    }

    public void delete(long id) {
        transaction(x->x.createNativeQuery("delete from user where id=:id")
                .setParameter("id", id)
                .executeUpdate());
    }
}

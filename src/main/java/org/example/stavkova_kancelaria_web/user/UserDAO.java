package org.example.stavkova_kancelaria_web.user;

import org.example.stavkova_kancelaria_web.exceptions.NotFoundException;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

import static org.jooq.codegen.maven.example.Tables.TICKETS;
import static org.jooq.codegen.maven.example.Tables.USERS;

public class UserDAO {
    private final DSLContext dslContext;

    //zdroj https://www.jooq.org/doc/latest/manual/getting-started/tutorials/jooq-in-7-steps/
    public UserDAO(DSLContext dslContext) {
        this.dslContext = dslContext;
    }
    public boolean userExists(String username, String email) {
        return dslContext.fetchExists(
                DSL.selectOne()
                        .from(USERS)
                        .where(USERS.USERNAME.eq(username).or(USERS.EMAIL.eq(email)))
        );
    }

    public void insertUser(User user) {
        if (user == null) {
            throw new NotFoundException("Neplatný používateľ");
        }

        String username = user.getUsername();
        String hashedPassword = user.getPassword();
        String email = user.getEmail();
        Role role = user.getRole();
        Double balance = user.getBalance();

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Neplatné používateľské meno");
        }
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Neplatné heslo");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Neplatný email");
        }
        if (role == null) {
            throw new IllegalArgumentException("Neplatná rola");
        }
        if (balance == null || balance < 0) {
            throw new IllegalArgumentException("Neplatný zostatok");
        }

        dslContext.insertInto(USERS)
                .set(USERS.USERNAME, username)
                .set(USERS.PASSWORD, hashedPassword)
                .set(USERS.EMAIL, email)
                .set(USERS.BALANCE, balance)
                .set(USERS.ROLE, role.toString())
                .execute();
    }

    public void deleteUser(int userID) {
        dslContext.deleteFrom(TICKETS).where(TICKETS.USER_ID.eq(userID)).execute();
        dslContext.deleteFrom(USERS).where(USERS.USER_ID.eq(userID)).execute();
    }

    public void updateUser(User user) {
        if (user == null) {
            throw new NotFoundException("Neplatný používateľ");
        }
        // check if user exists
        var record = dslContext.selectFrom(USERS).where(USERS.USER_ID.eq(user.getUserId())).fetchOne();
        if (record == null) {
            throw new NotFoundException("Neplatný používateľ");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Neplatné používateľské meno");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Neplatný email");
        }
        if (user.getBalance() == null || user.getBalance() < 0) {
            throw new IllegalArgumentException("Neplatný zostatok");
        }
        if (user.getRole() == null) {
            throw new IllegalArgumentException("Neplatná rola");
        }

        dslContext.update(USERS)
                .set(USERS.USERNAME, user.getUsername())
                .set(USERS.EMAIL, user.getEmail())
                .set(USERS.BALANCE, user.getBalance())
                .set(USERS.ROLE, user.getRole().toString())
                .where(USERS.USER_ID.eq(user.getUserId()))
                .execute();
    }

    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        var result = dslContext.selectFrom(USERS).fetch();
        for (var record : result) {
            User user = new User();
            user.setUserId(record.getUserId());
            user.setUsername(record.getUsername());
            user.setEmail(record.getEmail());
            user.setBalance(record.getBalance());
            user.setRole(Role.valueOf(record.getRole()));
            users.add(user);
        }
        return users;
    }

    public User findUserById(int userId) {
        var record = dslContext.selectFrom(USERS).where(USERS.USER_ID.eq(userId)).fetchOne();
        if (record == null) {
            return null;
        }
        User user = new User();
        user.setUserId(record.getUserId());
        user.setUsername(record.getUsername());
        user.setEmail(record.getEmail());
        user.setBalance(record.getBalance());
        user.setRole(Role.valueOf(record.getRole()));
        return user;
    }

    public void updateBalanceAndStat(int userID, Double betAmount) {
        if (betAmount == null) {
            throw new IllegalArgumentException("Neplatná suma");
        }
        dslContext.update(USERS)
                .set(USERS.BALANCE, USERS.BALANCE.minus(betAmount))
                .set(USERS.TOTAL_BETS, USERS.TOTAL_BETS.plus(1))
                .set(USERS.MAX_BET,
                        DSL.when(USERS.MAX_BET.lessThan(betAmount), betAmount)
                                .otherwise(USERS.MAX_BET))
                .set(USERS.TOTAL_STAKES, USERS.TOTAL_STAKES.plus(betAmount))

                .where(USERS.USER_ID.eq(userID))
                .execute();
    }

    public String findEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Neplatný email");
        }
        return dslContext.select(USERS.EMAIL).from(USERS).where(USERS.EMAIL.eq(email)).fetchOneInto(String.class);
    }

    public void updatePassword(String newPassword, String email) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Neplatné heslo");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Neplatný email");
        }
        dslContext.update(USERS).set(USERS.PASSWORD, newPassword).where(USERS.EMAIL.eq(email)).execute();
    }

    public void updateStatistics() {
        dslContext.update(USERS)
                .set(USERS.AVERAGE_BET,
                        DSL.case_()
                                .when(USERS.TOTAL_BETS.isNotNull().and(USERS.TOTAL_BETS.gt(0)),
                                        DSL.round(
                                                USERS.TOTAL_STAKES.cast(Double.class).divide(USERS.TOTAL_BETS.cast(Double.class))
                                        ))
                                .otherwise(DSL.val(0.0)))
                .execute();
    }

    public void addBalance(int userID, Double amountValue) {
        if (amountValue == null) {
            throw new IllegalArgumentException("Neplatná suma");
        }
        dslContext.update(USERS)
                .set(USERS.BALANCE, USERS.BALANCE.plus(amountValue))
                .where(USERS.USER_ID.eq(userID))
                .execute();
    }

    public Double getBalance(int userID) {
        return dslContext.select(USERS.BALANCE)
                .from(USERS)
                .where(USERS.USER_ID.eq(userID))
                .fetchOneInto(Double.class);
    }

    public void updateBalanceWithTicket(Double stake, Double odds, int userID) {
        dslContext.update(USERS)
                .set(USERS.BALANCE, USERS.BALANCE.plus(stake * odds))
                .where(USERS.USER_ID.eq(userID))
                .execute();
    }

    public void updateWinRateAndTotalWinnings(Double roundedWinRate, Double totalWinnings, int userID) {
        dslContext.update(USERS)
                .set(USERS.WIN_RATE, roundedWinRate)
                .set(USERS.TOTAL_WINNINGS, totalWinnings)
                .where(USERS.USER_ID.eq(userID))
                .execute();
    }

    public void changeUsername(int userID, String username) {
        dslContext.update(USERS)
                .set(USERS.USERNAME, username)
                .where(USERS.USER_ID.eq(userID))
                .execute();
    }

    public void changePassword(int userID, String password) {
        dslContext.update(USERS)
                .set(USERS.PASSWORD, BCrypt.hashpw(password, BCrypt.gensalt()))
                .where(USERS.USER_ID.eq(userID))
                .execute();
    }

    public boolean menoExists(String text) {
        return dslContext.fetchExists(
                DSL.selectOne()
                        .from(USERS)
                        .where(USERS.USERNAME.eq(text)))
                ;
    }

    public List<User> getAllUsers() {
        return dslContext.selectFrom(USERS).fetchInto(User.class);
    }
}

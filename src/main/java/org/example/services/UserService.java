package org.example.services;

import org.dizitart.no2.objects.filters.ObjectFilters;
import org.example.database.Database;
import org.example.exceptions.UnauthorizedException;
import org.example.exceptions.UsernameAlreadyExistsException;
import org.example.model.*;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.example.database.Database.users;

public class UserService {

    public static void initUserDatabase() throws UsernameAlreadyExistsException {
        if (users.size() == 0) {
            UserService.addUser("Admin", "Admin1234", "Admin");
        }
    }

    public static void addOrder(String shopname, String customername, List<ProductToOrder> productsOrd) {
        Database.orders.insert(new Order(shopname, customername, productsOrd));
        Database.statuses.insert(new OrderStatus(new Order(shopname, customername, productsOrd), "Pending"));
    }

    public static void addUser(String username, String password, String role) throws UsernameAlreadyExistsException {
        checkUserDoesNotAlreadyExist(username);
        final User user = new User(username, encodePassword(username, password), role);
        users.insert(user);
    }

    private static void checkUserDoesNotAlreadyExist(String username) throws UsernameAlreadyExistsException {
        if (users.find(ObjectFilters.eq("username", username)).totalCount() != 0)
            throw new UsernameAlreadyExistsException(username);
    }

    public static String encodePassword(String salt, String password) {
        MessageDigest md = getMessageDigest();
        md.update(salt.getBytes(StandardCharsets.UTF_8));

        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        // This is the way a password should be encoded when checking the credentials
        return new String(hashedPassword, StandardCharsets.UTF_8)
                .replace("\"", "");
    }

    public static MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 does not exist!");
        }
        return md;
    }

    /**
     * Asserts that the login credentials match, otherwise fails.
     *
     * @throws UnauthorizedException if the user does not exist, does not match the password, or is of different type.
     */
    public static void checkUser(String username, String password, UserType type) {
        final User user = users.find(ObjectFilters.eq("username", username)).firstOrDefault();
        if (user == null ||
                !username.equals(user.getUsername()) ||
                !encodePassword(username, password).equals(user.getPassword()) ||
                !type.name().equals(user.getRole().toUpperCase())
        ) {
            JOptionPane.showMessageDialog(null, "Failed security test");
            throw new UnauthorizedException(username);
        }
    }
}

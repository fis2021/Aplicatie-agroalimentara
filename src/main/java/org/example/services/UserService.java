package org.example.services;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.example.admin.AdminController;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.customer.CustomerController;
import org.example.exceptions.CouldNotWriteOrderException;
import org.example.exceptions.CouldNotWriteUsersException;
import org.example.exceptions.UsernameAlreadyExistsException;
import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.ProductToOrder;
import org.example.model.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.example.store.StoreController;

import javax.swing.*;

public class UserService {

    private static ObjectRepository<Order> orders;
    private static ObjectRepository<User> users;
    private static ObjectRepository<OrderStatus>stats;
    private static final Path USERS_PATH = FileSystemService.getPathToFile("config", "users.db");
    private static final Path ORDERS_PATH=FileSystemService.getPathToFile("config","orders.db");
    private static final Path STATUS_PATH=FileSystemService.getPathToFile("config","status.db");

    public static void initUserDatabase() throws UsernameAlreadyExistsException {
        Nitrite database = Nitrite.builder()
                .filePath(USERS_PATH.toFile())
                .openOrCreate("test", "test");
        users = database.getRepository(User.class);
        if(users.size()==0)
        {
            UserService.addUser("Admin","Admin1234","Admin");
        }
    }

    public static void initOrderDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(ORDERS_PATH.toFile())
                .openOrCreate("test", "test");
        orders = database.getRepository(Order.class);
    }

    public static void initStatusDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(STATUS_PATH.toFile())
                .openOrCreate("test", "test");
        stats = database.getRepository(OrderStatus.class);
    }

    public static void addOrder(String shopname, String customername, ArrayList<ProductToOrder> productsOrd) {
        orders.insert(new Order(shopname, customername, productsOrd));
        stats.insert(new OrderStatus(new Order(shopname, customername, productsOrd),"Pending"));
    }

    public static void addUser(String username, String password, String role) throws UsernameAlreadyExistsException {
        //checkUserDoesNotAlreadyExist(username);
        users.insert(new User(username, encodePassword(username, password), role));
        if(Objects.equals(role,"Store")) {
            Path STORE_PATH = FileSystemService.getPathToFile("config", username + ".db");
            try {
                File myObj = new File(String.valueOf(STORE_PATH));
                if (myObj.createNewFile()) {
                    Nitrite database = Nitrite.builder()
                            .filePath(STORE_PATH.toFile())
                            .openOrCreate("test", "test");
                    System.out.println("File created: " + myObj.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    private static void checkUserDoesNotAlreadyExist(String username) throws UsernameAlreadyExistsException {
        for (User user : users.find()) {
            if (Objects.equals(username, user.getUsername()))
                throw new UsernameAlreadyExistsException(username);
        }
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

    public static ObjectRepository<User> getUsers() {
        return users;
    }

    public static ObjectRepository<Order> getOrders() {
        return orders;
    }

    public static void checkUsers(String username, String password, String role) throws IOException {
        int i = 0;
        for (User user : users.find()) {
            if (!Objects.equals(username, user.getUsername()))
                i++;
        }
        if (i == users.size()) {
            JOptionPane.showMessageDialog(null, "Wrong credentials");
        } else {
            for (User user : users.find()) {
                if (Objects.equals(username, user.getUsername())) {
                    if (Objects.equals(user.getPassword(), encodePassword(username, password))) {
                        if (Objects.equals(role, user.getRole())) {
                            if (Objects.equals(role, "Customer")) {


                                System.out.println("merge merge");


                                //CustomerController.openCustomerPanel();
                            } else if (Objects.equals(role, "Admin")) {
                                {
                                    AdminController.openAdminPanel();
                                }
                            } else if (Objects.equals(role, "Store")) {
                                Path STORE_PATH = FileSystemService.getPathToFile("config", username + ".db");


                                System.out.println("merge blana");


                                //StoreController.openStorePanel(STORE_PATH);
                            } else {
                                JOptionPane.showMessageDialog(null, "Wrong credentials");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Wrong credentials");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Wrong credentials");
                    }
                }
            }

        }
    }

}

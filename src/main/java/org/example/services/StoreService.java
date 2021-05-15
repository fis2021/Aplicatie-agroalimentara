package org.example.services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dizitart.no2.objects.ObjectRepository;
import org.example.exceptions.*;
import org.example.model.*;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class StoreService {
    private static ObjectRepository<Product> products;
    private static ObjectRepository<User> users=UserService.getUsers();
    private static final Path USER_PATH = FileSystemService.getPathToFile("config", "users.db");
    public static void loadUsers() throws IOException,UsernameAlreadyExistsException {
        users=UserService.getUsers();
    }
    public static ObjectRepository<OrderStatus> loadStatusFromFile(Path DATA_PATH) throws IOException {
        ObjectRepository<OrderStatus> stats;
        ObjectMapper objMap = new ObjectMapper();
        objMap.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        stats = objMap.readValue(DATA_PATH.toFile(), new TypeReference<ObjectRepository<OrderStatus>>() {
        });
        return stats;
    }

    public static ObjectRepository<Product> loadDataFromFile(Path DATA_PATH) throws IOException {
        ObjectRepository<Product> products;
        ObjectMapper objMap = new ObjectMapper();
        objMap.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        products = objMap.readValue(DATA_PATH.toFile(), new TypeReference<ObjectRepository<Product>>() {
        });
        return products;
    }
    public static void addData(String name, String price, String username,String password ) throws ProductAlreadyExistsException, IOException {
        Path pth;
        int i = 0;
        for (User user : users.find()) {
            if (!Objects.equals(username, user.getUsername()))
                i++;
        }
        if(i==users.size()){
            JOptionPane.showMessageDialog(null, "Failed security test");
        }else {
            for (User user : users.find()) {
                if (Objects.equals(username, user.getUsername())) {
                    if (Objects.equals(user.getPassword(), UserService.encodePassword(username, password))) {
                        ObjectRepository<Product> products;
                        pth = FileSystemService.getPathToFile("config", username + ".db");
                        products = loadDataFromFile(pth);
                        checkProductDoesNotAlreadyExist(name, pth);
                        products.insert(new Product(name, Double.parseDouble(price)));
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            objectMapper.writerWithDefaultPrettyPrinter().writeValue(pth.toFile(), products);
                        } catch (IOException e) {
                            throw new CouldNotWriteProductsException();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed security test");
                    }
                }
            }
        }
    }
    public static void deleteData(String prodname,String username,String password)throws IOException,ProductDoesNotExist,ProductAlreadyExistsException{
        Path pth;
        int i = 0;
        for (User user : users.find()) {
            if (!Objects.equals(username, user.getUsername()))
                i++;
        }
        if(i==users.size()){
            JOptionPane.showMessageDialog(null, "Failed security test");
        }else {
            for(User user:users.find()){
                if (Objects.equals(username, user.getUsername())){
                    if (Objects.equals(user.getPassword(), UserService.encodePassword(username, password))) {
                        ObjectRepository<Product> products;
                        pth = FileSystemService.getPathToFile("config", username + ".db");
                        products = loadDataFromFile(pth);
                        checkProductDoesNotExist(prodname,pth);
                        for (Iterator<Product> iter = (Iterator<Product>) products.find(); iter.hasNext(); ) {
                            Product a = iter.next();
                            if (Objects.equals(a.getName(),prodname)) {
                                iter.remove();
                            }
                        }
                        try {
                            FileWriter fwrite = new FileWriter(String.valueOf(pth));
                            fwrite.write("[]");
                            fwrite.close();
                        } catch (IOException e) {
                            System.out.println("An error occurred.");
                            e.printStackTrace();
                        }
                        for(Product prod:products.find()) {
                            String tmp=String.valueOf(prod.getPrice());
                            addData(prod.getName(), tmp, username, password);
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Failed security test");
                    }
                }
            }
        }
    }

    public static void editData(String curentNameInput,String newNameInput,String  newPriceInput,String usernameInput, String passwordInput) throws IOException, ProductAlreadyExistsException, ProductDoesNotExist {
        Path pth;
        int i = 0;
        for (User user : users.find()) {
            if (!Objects.equals(usernameInput, user.getUsername()))
                i++;
        }
        if(i==users.size()){
            JOptionPane.showMessageDialog(null, "Failed security test");
        }else{
            for(User user : users.find()){
                if (Objects.equals(usernameInput, user.getUsername())){
                    if (Objects.equals(user.getPassword(), UserService.encodePassword(usernameInput, passwordInput))){
                        pth = FileSystemService.getPathToFile("config", usernameInput + ".db");
                        deleteData(curentNameInput,usernameInput,passwordInput);
                        addData(newNameInput,newPriceInput,usernameInput,passwordInput);
                    }else{
                        JOptionPane.showMessageDialog(null, "Failed security test");
                    }
                }
            }
        }
    }
    private static void checkProductDoesNotAlreadyExist(String name,Path pth) throws ProductAlreadyExistsException,IOException {
        ObjectRepository<Product> products;
        products=loadDataFromFile(pth);
        for (Product prod:products.find()) {
            if (Objects.equals(name, prod.getName()))
                throw new ProductAlreadyExistsException(name);
        }
    }
    private static void checkProductDoesNotExist(String name, Path pth) throws ProductDoesNotExist,IOException{
        ObjectRepository<Product> products;
        products=loadDataFromFile(pth);
        int i=0;
        for(Product prod:products.find()){
            if(!Objects.equals(name, prod.getName()))
                i=i+1;
        }
        if(i==products.size()){
            throw new ProductDoesNotExist(name);
        }
    }
    public static void addOrdStatus(OrderStatus o) throws IOException {
        Path pth;
        pth = FileSystemService.getPathToFile("config",  "status.db");
        ObjectRepository<OrderStatus> stats;
        stats = loadStatusFromFile(pth);
        stats.insert(o);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(pth.toFile(),stats);
        } catch (IOException e) {
            throw new CouldNotWriteOrderException();
        }
    }
    public static void deleteStatus(String shopname, String customername, ObjectRepository<ProductToOrder> productsOrd ) throws IOException {
        Path pth;
        ObjectRepository<OrderStatus> stats;
        pth = FileSystemService.getPathToFile("config", "status.db");
        stats = loadStatusFromFile(pth);
        for (Iterator<OrderStatus> iter = (Iterator<OrderStatus>) stats.find(); iter.hasNext(); ) {
            OrderStatus a = iter.next();
            if (Objects.equals(shopname,a.getO().getShopname())) {
                if(Objects.equals(a.getO().getCustomername(),customername)){
                    if(Objects.equals(a.getO().getProductsOrd(),productsOrd)) {
                        iter.remove();
                    }
                }
            }
        }
        try {
            FileWriter fwrite = new FileWriter(String.valueOf(pth));
            fwrite.write("[]");
            fwrite.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"An error occurre");
            e.printStackTrace();
        }
        for(OrderStatus os:stats.find()) {
            StoreService.addOrdStatus(os);
        }

    }

}

package org.example.services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dizitart.no2.WriteResult;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.example.database.Database;
import org.example.exceptions.*;
import org.example.model.*;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class StoreService {

    public static ObjectRepository<OrderStatus> loadStatusFromFile(Path DATA_PATH) throws IOException {
        ObjectRepository<OrderStatus> stats;
        ObjectMapper objMap = new ObjectMapper();
        objMap.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        stats = objMap.readValue(DATA_PATH.toFile(), new TypeReference<ObjectRepository<OrderStatus>>() {
        });
        return stats;
    }

    public static void addData(String storeName,String password, Product product) throws ProductAlreadyExistsException {
        UserService.checkUser(storeName, password, UserType.STORE);

        ObjectRepository<Product> products = Database.productsForStore(storeName);
        checkProductDoesNotAlreadyExist(products, product.getName());
        final WriteResult x = products.insert(product);
        final int a = 1 + 1;
    }
    public static void deleteData(String productName,String storeName,String password)throws IOException,ProductDoesNotExist,ProductAlreadyExistsException{
        UserService.checkUser(storeName, password, UserType.STORE);
        final ObjectRepository<Product> products = Database.productsForStore(storeName);
        checkProductDoesNotExist(products,productName);
        final WriteResult result = products.remove(ObjectFilters.eq("name", productName));
        if(result.getAffectedCount() == 0) {
            System.out.printf("An error occurred while deleting product '%s'.%n", productName);
        }
    }

    public static void editData(String productName, Product product ,String storeName, String password) throws ProductAlreadyExistsException, ProductDoesNotExist {
        UserService.checkUser(storeName, password, UserType.STORE);
        Database.productsForStore(storeName).update(ObjectFilters.eq("name", productName), product);
    }
    private static void checkProductDoesNotAlreadyExist(ObjectRepository<Product> productRepo, String name) throws ProductAlreadyExistsException {
        if (productRepo.find(ObjectFilters.eq("name", name)).totalCount() > 0)
            throw new ProductAlreadyExistsException(name);
    }

    private static void checkProductDoesNotExist(ObjectRepository<Product> productRepo, String name) throws ProductDoesNotExist{
        if (productRepo.find(ObjectFilters.eq("name", name)).totalCount() > 0)
            throw new ProductDoesNotExist(name);
    }
    public static void addOrdStatus(OrderStatus o) {
        Database.statuses.insert(o);
    }
    public static void deleteStatus(String shopname, String customername, List<ProductToOrder> productsOrd ) throws IOException {
        Path pth;
        ObjectRepository<OrderStatus> stats;
        pth = FileSystemService.getPathToFile("config", "status.db");
        stats = loadStatusFromFile(pth);
        for (Iterator<OrderStatus> iter = (Iterator<OrderStatus>) stats.find(); iter.hasNext(); ) {
            OrderStatus a = iter.next();
            if (Objects.equals(shopname,a.getOrder().getShopName())) {
                if(Objects.equals(a.getOrder().getCustomerName(),customername)){
                    if(Objects.equals(a.getOrder().getProductsOrdered(),productsOrd)) {
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

    public static void updateOrderStatus(Order order, String status) {
        Database.statuses.update(ObjectFilters.eq("id", order.getId()), new OrderStatus(order, status));
    }

}

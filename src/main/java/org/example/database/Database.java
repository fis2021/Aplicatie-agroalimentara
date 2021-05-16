package org.example.database;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.Product;
import org.example.model.User;
import org.example.services.FileSystemService;

public class Database {

    public static Nitrite db = Nitrite.builder()
            .compressed()
            .filePath(FileSystemService.getPathToFile("agro.db").toString())
            .openOrCreate("test", "test");

    public static ObjectRepository<User> users = db.getRepository(User.class);

    public static ObjectRepository<Order> orders = db.getRepository(Order.class);

    public static ObjectRepository<OrderStatus> statuses = db.getRepository(OrderStatus.class);

    public static ObjectRepository<Product> productsForStore(String storeId) {
        return db.getRepository(storeId, Product.class);
    }
}

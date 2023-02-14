/**
 * The InventoryManagementSystem class is the main class for the Inventory Management System.
 * It contains the main method and extends the javafx.application.Application class to launch the program.
 *@author Matt McHugh
 */
package myview.software1java;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;

public class InventoryManagementSystem extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagementSystem.class.getResource("MainForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        Outsourced newPart1 = new Outsourced(1, "Part 1", 1.0, 5, 1, 10, "ACME Inc.");
        Outsourced newPart2 = new Outsourced(2, "Part 2", 2.0, 10, 2, 20, "XYZ Corp.");
        InHouse newPart3 = new InHouse(3, "Part 3", 3.0, 15, 3, 30, 123);
        InHouse newPart4 = new InHouse(4, "Part 4", 4.0, 20, 4, 40, 456);

        Inventory.addPart(newPart1);
        Inventory.addPart(newPart2);
        Inventory.addPart(newPart3);
        Inventory.addPart(newPart4);

        Product newProduct1 = new Product(1,"Product 1", 1.0, 5, 1, 10);
        Product newProduct2 = new Product(2,"Product 2", 2.0, 10, 2, 20);
        Product newProduct3 = new Product(3,"Product 3", 3.0, 15, 3, 30);
        Product newProduct4 = new Product(4,"Product 4", 4.0, 20, 4, 40);

        Inventory.addProduct(newProduct1);
        Inventory.addProduct(newProduct2);
        Inventory.addProduct(newProduct3);
        Inventory.addProduct(newProduct4);

        newProduct1.addAssociatedPart((Part) newPart1);
        launch();
    }
}
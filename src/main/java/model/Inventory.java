package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    public static Product lookupProductID(int productID) {
        Product returnedProduct = null;
        for (Product p : allProducts) {
            if (p.getId() == productID) {
                returnedProduct = p;
            }
        }
        return returnedProduct;
    }

    public static ObservableList<Product> lookupProductName(String productName) {
        ObservableList<Product> searchResults = FXCollections.observableArrayList();
        for(Product p : allProducts) {
            if(p.getName().startsWith(productName)) {
                searchResults.add(p);
            }
        }
        return searchResults;
    }

    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    public static boolean deleteProduct(Product selectedProduct) {
        Boolean isDeleted = false;
        for(int i = 0; i < allProducts.size(); i++) {
            if(allProducts.get(i).getId() == selectedProduct.getId()) {
                allProducts.remove(allProducts.get(i));
                isDeleted = true;
            }
        }
        return isDeleted;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    public static Part lookupPartID(int partID) {
        Part lookupPartIDResult = null;
        for(Part p : allParts) {
            if(p.getId() == partID) {
                lookupPartIDResult = p;
            }
        }
        return lookupPartIDResult;
    }

    public static ObservableList<Part> lookupPartName(String partName) {
        ObservableList<Part> searchResults = FXCollections.observableArrayList();
        for(Part p : allParts) {
            if(p.getName().startsWith(partName)) {
                searchResults.add(p);
            }
        }
        return searchResults;
    }

    public static Boolean deletePart(Part selectedPart) {
        Boolean isDeleted = false;
        for(int i = 0; i < allParts.size(); i++) {
            if(allParts.get(i).getId() == selectedPart.getId()) {
                allParts.remove(allParts.get(i));
                isDeleted = true;
            }
        }
        return isDeleted;
    }
}

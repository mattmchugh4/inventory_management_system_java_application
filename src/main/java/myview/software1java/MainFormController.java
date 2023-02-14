package myview.software1java;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import model.Part;
import model.Product;
import model.Inventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {

    public TextField partSearchField;
    public TextField productSearchField;
    @FXML
    private TableView<Part> partsTableView;
    @FXML
    private TableColumn<Part, Integer> partIdColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partStockColumn;
    @FXML
    private TableColumn<Part, Double> partPriceColumn;
    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<Product, Integer> productIdColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Integer> productStockColumn;
    @FXML
    private TableColumn<Product, Double> productPriceColumn;
    public Button partAddButton;
    private Parent scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsTableView.setItems(getParts());

        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTableView.setItems(getProducts());
    }

    public ObservableList<Part> getParts() {
        ObservableList<Part> parts = FXCollections.observableArrayList();
        parts.addAll(Inventory.getAllParts());
        return parts;
    }

    public ObservableList<Product> getProducts() {
        ObservableList<Product> allProducts = FXCollections.observableArrayList();
        allProducts.addAll(Inventory.getAllProducts());
        return allProducts;
    }

    public void onAddPartButtonClicked(ActionEvent actionEvent) throws IOException {
        Stage addPartButtonStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AddPartForm.fxml"));
        addPartButtonStage.setTitle("Add Part");
        addPartButtonStage.setScene(new Scene(scene));
        addPartButtonStage.show();
    }

    public void onModifyPartButtonClick(ActionEvent actionEvent) throws IOException{
        try {
            Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();

            Stage modifyPartButtonStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader modifyPartLoader = new FXMLLoader();
            modifyPartLoader.setLocation(getClass().getResource("ModifyPartForm.fxml"));
            modifyPartLoader.load();
            scene = modifyPartLoader.getRoot();
            ModifyPartFormController modPartController  = modifyPartLoader.getController();
            modPartController.getPart(selectedPart);
            modifyPartButtonStage.setTitle("Modify Part");
            modifyPartButtonStage.setScene(new Scene(scene));
            modifyPartButtonStage.show();
        } catch (NullPointerException e) {
            Alert noPartSelected = new Alert(Alert.AlertType.ERROR);
            noPartSelected.setTitle("Error Dialog");
            noPartSelected.setContentText("You must select a part to modify.");
            noPartSelected.showAndWait();
        }
    }

    public void onAddProductButtonClick(ActionEvent actionEvent) throws IOException{
        Stage addProductButtonStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AddProductForm.fxml"));
        addProductButtonStage.setTitle("Add Product");
        addProductButtonStage.setScene(new Scene(scene));
        addProductButtonStage.show();
    }

    public void onModifyProductButtonClick(ActionEvent actionEvent) throws IOException {
        try {
            Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

            Stage modifyProductButtonStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader modifyProductLoader = new FXMLLoader();
            modifyProductLoader.setLocation(getClass().getResource("ModifyProductForm.fxml"));
            modifyProductLoader.load();
            scene = modifyProductLoader.getRoot();
            ModifyProductFormController modProductController  = modifyProductLoader.getController();
            modProductController.getProduct(selectedProduct);
            modifyProductButtonStage.setTitle("Modify Product");
            modifyProductButtonStage.setScene(new Scene(scene));
            modifyProductButtonStage.show();
        } catch (NullPointerException e) {
            Alert noProductSelected = new Alert(Alert.AlertType.ERROR);
            noProductSelected.setTitle("Error Dialog");
            noProductSelected.setContentText("You must select a product to modify.");
            noProductSelected.showAndWait();
        }
    }

    public void onPartSearchInputChange(ActionEvent actionEvent) throws IOException {
        try {
            if (partSearchField.getText().length() == 0) {
                partsTableView.setItems(getParts());
                return;
            }
            ObservableList<Part> searchResults = FXCollections.observableArrayList();
            String searchString = partSearchField.getText();
            boolean isStringSearch = searchString.matches(".*[^0-9]+.*");
            if (!isStringSearch) {
                int searchID = Integer.parseInt(searchString);
                Part idSearchResult = Inventory.lookupPartID(searchID);
                searchResults.add(idSearchResult);
            } else {
                searchResults = Inventory.lookupPartName(searchString);
            }
            if (searchResults.get(0) == null) {
                Alert noPartsReturned = new Alert(Alert.AlertType.WARNING);
                noPartsReturned.setTitle("Alert");
                noPartsReturned.setContentText("No parts matched your search.");
                noPartsReturned.showAndWait();
            }
            partsTableView.setItems(searchResults);
        } catch (IndexOutOfBoundsException e) {
            Alert noPartsReturned = new Alert(Alert.AlertType.WARNING);
            noPartsReturned.setTitle("Alert");
            noPartsReturned.setContentText("No parts matched your search.");
            noPartsReturned.showAndWait();
        }
    }

    public void onDeletePartClick(ActionEvent actionEvent) throws IOException{
        try {
            Alert confirmPartDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmPartDelete.setTitle("Confirm Delete");
            confirmPartDelete.setContentText("Are you sure you want to delete this part?");

            ButtonType confirmPartDeleteButton = new ButtonType("Delete");
            ButtonType cancelPArtDeleteButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmPartDelete.getButtonTypes().setAll(confirmPartDeleteButton, cancelPArtDeleteButton);
            Optional<ButtonType> result = confirmPartDelete.showAndWait();

            if (result.get() == confirmPartDeleteButton){
                Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
                Boolean isDeleted = Inventory.deletePart(selectedPart);
                if(isDeleted) {
                    partsTableView.setItems(getParts());
                } else {
                    Alert noPartDeleted = new Alert(Alert.AlertType.ERROR);
                    noPartDeleted.setTitle("Error Dialog");
                    noPartDeleted.setContentText("No part was deleted. Please check your selection and try again.");
                    noPartDeleted.showAndWait();
                }
            }
        } catch (Exception e) {
            Alert noPartSelected = new Alert(Alert.AlertType.ERROR);
            noPartSelected.setTitle("Error Dialog");
            noPartSelected.setContentText("You must select a part to delete.");
            noPartSelected.showAndWait();
        }

    }

    public void onProductSearchInputChange(ActionEvent actionEvent) {
        try {
            if (productSearchField.getText().length() == 0) {
                productTableView.setItems(getProducts());
                return;
            }
            ObservableList<Product> productSearchResults = FXCollections.observableArrayList();
            String searchString = productSearchField.getText();
            boolean isStringSearch = searchString.matches(".*[^0-9]+.*");
            if (!isStringSearch) {
                int searchID = Integer.parseInt(searchString);
                Product idSearchResult = Inventory.lookupProductID(searchID);
                productSearchResults.add(idSearchResult);
            } else {
                productSearchResults = Inventory.lookupProductName(searchString);
            }
            if (productSearchResults.get(0) == null) {
                Alert noPartsReturned = new Alert(Alert.AlertType.WARNING);
                noPartsReturned.setTitle("Alert");
                noPartsReturned.setContentText("No products matched your search.");
                noPartsReturned.showAndWait();
            }
            productTableView.setItems(productSearchResults);
        } catch (IndexOutOfBoundsException e) {
            Alert noPartsReturned = new Alert(Alert.AlertType.WARNING);
            noPartsReturned.setTitle("Alert");
            noPartsReturned.setContentText("No products matched your search.");
            noPartsReturned.showAndWait();
        }
    }

    public void onDeleteProductClick(ActionEvent actionEvent) {
        try {
            Alert confirmPartDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmPartDelete.setTitle("Confirm Delete");
            confirmPartDelete.setContentText("Are you sure you want to delete this product?");

            ButtonType confirmPartDeleteButton = new ButtonType("Delete");
            ButtonType cancelPArtDeleteButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmPartDelete.getButtonTypes().setAll(confirmPartDeleteButton, cancelPArtDeleteButton);
            Optional<ButtonType> result = confirmPartDelete.showAndWait();

            if (result.get() == confirmPartDeleteButton){
                Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
                ObservableList<Part> associatedParts = selectedProduct.getAllAssociatedParts();

                if(associatedParts.size() != 0) {
                    Alert productHasAssociateParts = new Alert(Alert.AlertType.ERROR);
                    productHasAssociateParts.setTitle("Error Dialog");
                    productHasAssociateParts.setContentText("A product that has parts associated with it can not be deleted.");
                    productHasAssociateParts.showAndWait();
                    return;
                }
                Boolean isDeleted = Inventory.deleteProduct(selectedProduct);
                if(isDeleted) {
                    productTableView.setItems(getProducts());
                } else {
                    Alert noProductDeleted = new Alert(Alert.AlertType.ERROR);
                    noProductDeleted.setTitle("Error Dialog");
                    noProductDeleted.setContentText("No product was deleted. Please check your selection and try again.");
                    noProductDeleted.showAndWait();
                }
            }
        } catch (Exception e) {
            Alert noProductSelected = new Alert(Alert.AlertType.ERROR);
            noProductSelected.setTitle("Error Dialog");
            noProductSelected.setContentText("You must select a product to delete.");
            noProductSelected.showAndWait();
        }
    }

    public void onExitButtonClick(ActionEvent actionEvent) {
        Platform.exit();
    }
}
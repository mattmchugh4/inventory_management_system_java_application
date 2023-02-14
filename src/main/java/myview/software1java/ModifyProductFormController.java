package myview.software1java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyProductFormController implements Initializable {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    public TextField partSearchInput;
    @FXML
    private TableView<Part> associatedPartTable;
    @FXML
    private TableColumn<Part, Integer> associatedPartID;
    @FXML
    private TableColumn<Part, String> associatedPartName;
    @FXML
    private TableColumn<Part, Integer> associatedPartStock;
    @FXML
    private TableColumn<Part, Double> associatedPartPrice;
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
    public TextField modifyProductMin;
    public TextField modifyProductMax;
    public TextField modifyProductPriceCost;
    public TextField modifyProductInventory;
    public TextField modifyProductName;
    public TextField modifyProductID;
    private Parent scene;
    public int partID;
    private String productName;
    private double price;
    private int inven;
    private int min;
    private int max;
    private Product selectedProduct;

    public void getProduct(Product productToModify) {
        selectedProduct = productToModify;
        partID = productToModify.getId();
        productName = productToModify.getName();
        max = productToModify.getMax();
        min = productToModify.getMin();
        inven = productToModify.getStock();
        price = productToModify.getPrice();

        modifyProductID.setText(String.valueOf(productToModify.getId()));
        modifyProductName.setText(productName);
        modifyProductInventory.setText(String.valueOf(inven));
        modifyProductPriceCost.setText(String.valueOf(price));
        modifyProductMax.setText(String.valueOf(max));
        modifyProductMin.setText(String.valueOf(min));

        associatedPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        ObservableList<Part> associatedPartsToIterate = productToModify.getAllAssociatedParts();
        for(int i = 0; i < associatedPartsToIterate.size(); i++) {
            Part partToAdd = associatedPartsToIterate.get(i);
            associatedParts.add(partToAdd);
        }
        associatedPartTable.setItems(associatedParts);

    }

    public void onModifyProductCancelButtonClick(ActionEvent actionEvent) throws IOException {
        Stage cancelModifyProductButtonStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        cancelModifyProductButtonStage.setTitle("Inventory Management System");
        cancelModifyProductButtonStage.setScene(new Scene(scene));
        cancelModifyProductButtonStage.show();
    }

    public void onPartSearchInputChange(ActionEvent actionEvent) {
        try {
            if (partSearchInput.getText().length() == 0) {
                partsTableView.setItems(getParts());
                return;
            }
            ObservableList<Part> searchResults = FXCollections.observableArrayList();
            String searchString = partSearchInput.getText();
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsTableView.setItems(getParts());
    }

    public ObservableList<Part> getParts() {
        ObservableList<Part> allParts = FXCollections.observableArrayList();
        allParts.addAll(Inventory.getAllParts());
        return allParts;
    }

    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {
        try{
            int max = Integer.parseInt(modifyProductMax.getText());
            int min = Integer.parseInt(modifyProductMin.getText());
            int stock = Integer.parseInt(modifyProductInventory.getText());

            if (max <= min) {
                Alert minGreaterThanMax = new Alert(Alert.AlertType.ERROR);
                minGreaterThanMax.setTitle("Error Dialog");
                minGreaterThanMax.setContentText("An error has occurred. Minimum amount must be less than maximum.");
                minGreaterThanMax.showAndWait();
                return;
            }
            if (stock >= max || stock <= min) {
                Alert inventoryOutOfBounds = new Alert(Alert.AlertType.ERROR);
                inventoryOutOfBounds.setTitle("Error Dialog");
                inventoryOutOfBounds.setContentText("An error has occurred. Inventory amount must be between maximum and minimum values.");
                inventoryOutOfBounds.showAndWait();
                return;
            }
            selectedProduct.setName(modifyProductName.getText());
            selectedProduct.setStock(stock);
            selectedProduct.setMax(max);
            selectedProduct.setMin(min);
            selectedProduct.setPrice(Double.parseDouble(modifyProductPriceCost.getText()));
            int iteration = selectedProduct.getAllAssociatedParts().size();
            for(int i = iteration - 1; i >= 0; i--) {
                selectedProduct.deleteAssociatedPart(selectedProduct.getAllAssociatedParts().get(i));
            }
            for(int i = 0; i < associatedParts.size(); i++) {
                selectedProduct.addAssociatedPart(associatedParts.get(i));
            }
        } catch (NumberFormatException e) {
            Alert invalidInput = new Alert(Alert.AlertType.ERROR);
            invalidInput.setTitle("Error Dialog");
            invalidInput.setContentText("An error has occurred. Please ensure that your inputs are valid.");
            invalidInput.showAndWait();
            return;
        }
        Stage saveModifyProductButtonStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        saveModifyProductButtonStage.setTitle("Inventory Management System");
        saveModifyProductButtonStage.setScene(new Scene(scene));
        saveModifyProductButtonStage.show();
    }

    public void onRemovePartButtonClick(ActionEvent actionEvent) {
        Alert confirmPartRemoval = new Alert(Alert.AlertType.CONFIRMATION);
        confirmPartRemoval.setTitle("Confirm Removal");
        confirmPartRemoval.setContentText("Are you sure you want to remove this part?");

        ButtonType confirmPartDeleteButton = new ButtonType("Remove");
        ButtonType cancelPArtDeleteButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmPartRemoval.getButtonTypes().setAll(confirmPartDeleteButton, cancelPArtDeleteButton);
        Optional<ButtonType> result = confirmPartRemoval.showAndWait();

        if (result.get() == confirmPartDeleteButton) {
            Part selectedPart = associatedPartTable.getSelectionModel().getSelectedItem();
            for (int i = 0; i < associatedParts.size(); i++) {
                Part p = associatedParts.get(i);
                if (p.getId() == selectedPart.getId()) {
                    associatedParts.remove(i);
                }
            }
            associatedPartTable.setItems(associatedParts);
        }
    }

    public void onAddPartButtonClick(ActionEvent actionEvent) {
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        associatedParts.add(selectedPart);
        associatedPartTable.setItems(associatedParts);
    }
}
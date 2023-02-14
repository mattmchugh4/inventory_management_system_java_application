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

public class AddProductFormController implements Initializable {

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
    private Parent scene;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    public TextField partSearchInput;
    public TextField productMin;
    public TextField productMax;
    public TextField productPrice;
    public TextField productStock;
    public TextField productName;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsTableView.setItems(getParts());

        associatedPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartTable.setItems(associatedParts);
    }

    public ObservableList<Part> getParts() {
        ObservableList<Part> allParts = FXCollections.observableArrayList();
        allParts.addAll(Inventory.getAllParts());
        return allParts;
    }

    public void onAddProductCancelButtonClick(ActionEvent actionEvent) throws IOException {
        Stage cancelAddProductButtonStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        cancelAddProductButtonStage.setTitle("Inventory Management System");
        cancelAddProductButtonStage.setScene(new Scene(scene));
        cancelAddProductButtonStage.show();
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
    public void onAddPartButtonClick(ActionEvent actionEvent) {

        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        associatedParts.add(selectedPart);
        associatedPartTable.setItems(associatedParts);
    }

    public void onRemoveAssociatedPartClick(ActionEvent actionEvent) {
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
                if (associatedParts.get(i).getId() == selectedPart.getId()) {
                    associatedParts.remove(i);
                }
            }
            associatedPartTable.setItems(associatedParts);
        }
    }

    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {
        try{
            int newProductID = createID();
            String newProductName = productName.getText();
            int newProductStock = Integer.parseInt(productStock.getText());
            int newProductMax = Integer.parseInt(productMax.getText());
            int newProductMin = Integer.parseInt(productMin.getText());
            double newProductPrice = Double.parseDouble(productPrice.getText());


            if (newProductMax <= newProductMin) {
                Alert minGreaterThanMax = new Alert(Alert.AlertType.ERROR);
                minGreaterThanMax.setTitle("Error Dialog");
                minGreaterThanMax.setContentText("An error has occurred. Minimum amount must be less than maximum.");
                minGreaterThanMax.showAndWait();
                return;
            }
            if (newProductStock >= newProductMax || newProductStock <= newProductMin) {
                Alert inventoryOutOfBounds = new Alert(Alert.AlertType.ERROR);
                inventoryOutOfBounds.setTitle("Error Dialog");
                inventoryOutOfBounds.setContentText("An error has occurred. Inventory amount must be between maximum and minimum values.");
                inventoryOutOfBounds.showAndWait();
                return;
            }
            Product newProduct = new Product(newProductID, newProductName, newProductPrice, newProductStock, newProductMin, newProductMax);

            for(int i = 0; i < associatedParts.size(); i++ ) {
                Part partToAdd = (Part) associatedParts.get(i);
                newProduct.addAssociatedPart(partToAdd);
            }
            Inventory.addProduct(newProduct);

        } catch (NumberFormatException e) {
            Alert invalidInput = new Alert(Alert.AlertType.ERROR);
            invalidInput.setTitle("Error Dialog");
            invalidInput.setContentText("An error has occurred. Please ensure that your inputs are valid.");
            invalidInput.showAndWait();
            return;
        }
        Stage saveAddProductButtonStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        saveAddProductButtonStage.setTitle("Inventory Management System");
        saveAddProductButtonStage.setScene(new Scene(scene));
        saveAddProductButtonStage.show();
    }

    public static int createID() {
        int id = 1;
        for(int i = 0; i < Inventory.getAllProducts().size(); i++) {
            id++;
        }
        return id;
    }
}

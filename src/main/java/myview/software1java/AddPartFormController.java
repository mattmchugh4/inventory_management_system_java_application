package myview.software1java;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Outsourced;
import model.Inventory;
import model.InHouse;
import java.io.IOException;

public class AddPartFormController{
    public TextField companyORmachineIDInput;
    public TextField addPartMin;
    public TextField addPartMax;
    public TextField addPartInventory;
    public TextField addPartPriceCost;
    public TextField addPartName;
    public ToggleGroup inHouseORoutsourcedToggle;
    public RadioButton addPartRadioInHouse;
    private Parent scene;
    public Label companyORmachineID;

    public void onRadioSelectionOutsourced(ActionEvent actionEvent) {
        companyORmachineID.setText("Company Name");
    }

    public void onRadioSelectionInHouse(ActionEvent actionEvent) {
        companyORmachineID.setText("Machine ID");
    }

    public void onAddPartCancelClick(ActionEvent actionEvent) throws IOException {
        Stage cancelAddPartButtonStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        cancelAddPartButtonStage.setTitle("Inventory Management System");
        cancelAddPartButtonStage.setScene(new Scene(scene));
        cancelAddPartButtonStage.show();
    }

    public void onSaveAddPartButtonClick(ActionEvent actionEvent) throws IOException{
        try {
            boolean isInHouse = addPartRadioInHouse.isSelected();
            int partID = createID();
            String partName = addPartName.getText();
            int inventory = Integer.parseInt(addPartInventory.getText());
            double priceCost = Double.parseDouble(addPartPriceCost.getText());
            int max = Integer.parseInt(addPartMax.getText());
            int min = Integer.parseInt(addPartMin.getText());
            String machineOrCompany = companyORmachineIDInput.getText();

            if(max <= min) {
                Alert minGreaterThanMax = new Alert(Alert.AlertType.ERROR);
                minGreaterThanMax.setTitle("Error Dialog");
                minGreaterThanMax.setContentText("An error has occurred. Minimum amount must be less than maximum.");
                minGreaterThanMax.showAndWait();
                return;
            }
            if(inventory >= max || inventory <= min) {
                Alert inventoryOutOfBounds = new Alert(Alert.AlertType.ERROR);
                inventoryOutOfBounds.setTitle("Error Dialog");
                inventoryOutOfBounds.setContentText("An error has occurred. Inventory amount must be between maximum and minimum values.");
                inventoryOutOfBounds.showAndWait();
                return;
            }
            if (isInHouse) {
                int machineID = Integer.parseInt(companyORmachineIDInput.getText());
                InHouse newPart = new InHouse(partID, partName, priceCost, inventory, min, max, machineID);
                Inventory.addPart(newPart);
            } else {
                String companyName = machineOrCompany;
                Outsourced newPart = new Outsourced(partID, partName, priceCost, inventory, min, max, companyName);
                Inventory.addPart(newPart);
            }
        }catch (NumberFormatException e) {
            Alert invalidInput = new Alert(Alert.AlertType.ERROR);
            invalidInput.setTitle("Error Dialog");
            invalidInput.setContentText("An error has occurred. Please ensure that your inputs are valid.");
            invalidInput.showAndWait();
            return;
        }

        Stage saveAddPartButtonStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        saveAddPartButtonStage.setTitle("Inventory Management System");
        saveAddPartButtonStage.setScene(new Scene(scene));
        saveAddPartButtonStage.show();
    }

    public static int createID() {
        int id = 1;
        for(int i = 0; i < Inventory.getAllParts().size(); i++) {
            id++;
        }
        return id;
    }
}

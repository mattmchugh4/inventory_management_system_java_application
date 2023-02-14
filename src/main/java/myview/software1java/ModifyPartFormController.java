package myview.software1java;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Part;
import model.Outsourced;
import java.io.IOException;

public class ModifyPartFormController{
    public TextField modifyPartCompanyORmachineIDInput;
    public TextField modifyPartMin;
    public TextField modifyPartMax;
    public TextField modifyPartPriceCost;
    public TextField modifyPartInventory;
    public TextField modifyPartName;
    public TextField modifyPartID;
    public RadioButton inHouseRadio;
    public RadioButton outsourcedRadio;
    private Parent scene;
    public int partID;
    private String partName;
    private double price;
    private int inven;
    private int min;
    private int max;
    private int machineID;
    private String companyName;
    public Label companyORmachineID;
    public void onRadioSelectionInHouse(ActionEvent actionEvent) {
        companyORmachineID.setText("Machine ID");
    }
    public void onRadioSelectionOutsourced(ActionEvent actionEvent) {
        companyORmachineID.setText("Company Name");
    }
    public void getPart(Part partToModify) {
        this.partID = partToModify.getId();
        this.partName = partToModify.getName();
        this.max = partToModify.getMax();
        this.min = partToModify.getMin();
        this.inven = partToModify.getStock();
        this.price = partToModify.getPrice();

        if(partToModify instanceof InHouse) {
            inHouseRadio.setSelected(true);
            InHouse inHousePart = (InHouse) partToModify;
            this.machineID = inHousePart.getMachineId();
            modifyPartCompanyORmachineIDInput.setText(String.valueOf(this.machineID));
        } else {
            outsourcedRadio.setSelected(true);
            Outsourced outsourcedPart = (Outsourced) partToModify;
            this.companyName = outsourcedPart.getCompanyName();
            modifyPartCompanyORmachineIDInput.setText(this.companyName);
            companyORmachineID.setText("Company Name");

        }
        modifyPartID.setText(String.valueOf(partToModify.getId()));
        modifyPartName.setText(this.partName);
        modifyPartInventory.setText(String.valueOf(this.inven));
        modifyPartPriceCost.setText(String.valueOf(this.price));
        modifyPartMax.setText(String.valueOf(this.max));
        modifyPartMin.setText(String.valueOf(this.min));
    }

    public void onModifyPartCancelClick(ActionEvent actionEvent) throws IOException {
        Stage cancelModifyPartButtonStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        cancelModifyPartButtonStage.setTitle("Inventory Management System");
        cancelModifyPartButtonStage.setScene(new Scene(scene));
        cancelModifyPartButtonStage.show();
    }

    public void onModifyPartSaveClick(ActionEvent actionEvent) throws IOException{
        try {
            this.partName = modifyPartName.getText();
            this.max = Integer.parseInt(modifyPartMax.getText());
            this.min = Integer.parseInt(modifyPartMin.getText());
            this.inven = Integer.parseInt(modifyPartInventory.getText());
            this.price = Double.parseDouble(modifyPartPriceCost.getText());

            int partIndex = -1;

            for( int i = 0; i < Inventory.getAllParts().size(); i++) {
                int pID = Inventory.getAllParts().get(i).getId();
                if( pID == this.partID) {
                    partIndex = i;
                }
            }
            if(max <= min) {
                Alert minGreaterThanMax = new Alert(Alert.AlertType.ERROR);
                minGreaterThanMax.setTitle("Error Dialog");
                minGreaterThanMax.setContentText("An error has occurred. Minimum amount must be less than maximum.");
                minGreaterThanMax.showAndWait();
                return;
            }
            if(inven >= max || inven <= min) {
                Alert inventoryOutOfBounds = new Alert(Alert.AlertType.ERROR);
                inventoryOutOfBounds.setTitle("Error Dialog");
                inventoryOutOfBounds.setContentText("An error has occurred. Inventory amount must be between maximum and minimum values.");
                inventoryOutOfBounds.showAndWait();
                return;
            }
            if (inHouseRadio.isSelected()) {
                this.machineID = Integer.parseInt(modifyPartCompanyORmachineIDInput.getText());
                InHouse newPart = new InHouse(this.partID, this.partName, this.price, this.inven, this.min, this.max, this.machineID);
                Inventory.updatePart(partIndex, newPart);
            } else {
                this.companyName = modifyPartCompanyORmachineIDInput.getText();
                Outsourced newPart = new Outsourced(this.partID, this.partName, this.price, this.inven, this.min, this.max, this.companyName);
                Inventory.updatePart(partIndex, newPart);
            }
        } catch (Exception e) {
            Alert invalidInput = new Alert(Alert.AlertType.ERROR);
            invalidInput.setTitle("Error Dialog");
            invalidInput.setContentText("An error has occurred. Please check that your inputs are valid.");
            invalidInput.showAndWait();
            return;
        }
        Stage saveModifyPartButtonStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        saveModifyPartButtonStage.setTitle("Inventory Management System");
        saveModifyPartButtonStage.setScene(new Scene(scene));
        saveModifyPartButtonStage.show();
    }
}

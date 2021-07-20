package main.java.view.listview;

import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.java.controller.applicationcontroller.reserveaseat.table.ReserveTableSeatController;
import main.java.engineering.bean.createtable.TableBean;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.exceptions.OutOfTimeException;

public class DeletableTableElement extends ListElement {

	private Label lblTitle;
	private Label lblAddress;
	private Label lblDate;
	private Label lblTime;
	private Button btnDeleteTable;

	public DeletableTableElement(VBox vbox, Object obj) {
		super(vbox, obj);
	}

	@Override
	public void buildGraphic() {
		var bean = (TableBean) super.obj;

		anchor.setPrefHeight(100);
		anchor.setPrefWidth(590);
		anchor.setStyle("-fx-background-color: #CCE1E9");

		lblTitle = new Label(bean.getName() + " - " + bean.getCardGame());
		lblTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
		lblTitle.setAlignment(Pos.CENTER);

		lblAddress = new Label(bean.getAddress());
		lblAddress.setMaxWidth(550);
		lblDate = new Label(bean.getDate());
		lblDate.setPrefWidth(120);
		lblTime = new Label(bean.getTime());
		lblTime.setPrefWidth(60);

		btnDeleteTable = new Button("Delete");
		btnDeleteTable.setPrefWidth(80);
		btnDeleteTable.getStylesheets().add("/main/java/view/standalone/css/Style.css");
		btnDeleteTable.getStyleClass().add("btn");

		anchor.getChildren().addAll(lblTitle, lblAddress, lblDate, lblTime, btnDeleteTable);

		setUpConstraints();

		setButtonAction();

		attach();
	}

	private void setButtonAction() {

		btnDeleteTable.setOnMouseClicked(event -> {
			var confirmation = AlertFactory.getInstance().createAlert(
					"Are you sure? Once deleted, all participants will be notified.", AlertType.CONFIRMATION);
			var btnYes = new ButtonType("Yes", ButtonData.YES);
			var btnNo = new ButtonType("No", ButtonData.NO);
			confirmation.getButtonTypes().setAll(btnYes, btnNo);
			confirmation.showAndWait().ifPresent(type -> {
				if (type == btnYes) {
					var ctrl = new ReserveTableSeatController();
					try {
						ctrl.deleteTable((TableBean) super.obj);
						AlertFactory.getInstance()
								.createAlert("Table deleted succesfully. All the participants have been notified",
										AlertType.INFORMATION)
								.show();
						// remove the list element from the list
						super.detach();
					} catch (DAOException | DateParsingException | OutOfTimeException e) {
						AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
						return;
					}
				}
			});
		});
	}

	private void setUpConstraints() {
		AnchorPane.setTopAnchor(lblTitle, 5d);
		AnchorPane.setLeftAnchor(lblTitle, 10d);
		AnchorPane.setRightAnchor(lblTitle, 10d);

		AnchorPane.setLeftAnchor(lblAddress, 15d);
		AnchorPane.setTopAnchor(lblAddress, 35d);

		AnchorPane.setLeftAnchor(lblDate, 15d);
		AnchorPane.setTopAnchor(lblDate, 60d);

		AnchorPane.setLeftAnchor(lblTime, 150d);
		AnchorPane.setTopAnchor(lblTime, 60d);

		AnchorPane.setBottomAnchor(btnDeleteTable, 10d);
		AnchorPane.setRightAnchor(btnDeleteTable, 15d);
	}

}

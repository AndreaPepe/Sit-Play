package main.java.view.listview;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.java.controller.applicationcontroller.reserveaseat.table.ReserveTableSeatController;
import main.java.engineering.bean.createtable.TableBean;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.exceptions.DeleteSeatException;

public class TableElement extends ListElement {

	private Label tabTitle;
	private Label addr;
	private Label date;
	private Label time;
	private Label organizer;
	private Button btnLeaveTable;

	public TableElement(VBox vbox, Object obj) {
		super(vbox, obj);
	}

	@Override
	public void buildGraphic() {

		var bean = (TableBean) ((BeanContainer) obj).getCarriedBean();

		anchor.setPrefHeight(100);
		anchor.setPrefWidth(590);
		anchor.setStyle("-fx-background-color: #CCE1E9");

		tabTitle = new Label(bean.getName() + " - " + bean.getCardGame());
		tabTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
		tabTitle.setAlignment(Pos.CENTER);

		addr = new Label(bean.getAddress());
		addr.setMaxWidth(550);
		date = new Label(bean.getDate());
		date.setPrefWidth(120);
		time = new Label(bean.getTime());
		time.setPrefWidth(60);
		organizer = new Label("Organized by " + bean.getOrganizer());
		organizer.setPrefWidth(310);

		btnLeaveTable = new Button("Leave");
		btnLeaveTable.setPrefWidth(80);
		btnLeaveTable.getStylesheets().add("/main/java/view/standalone/css/Style.css");
		btnLeaveTable.getStyleClass().add("btn");

		anchor.getChildren().addAll(tabTitle, addr, date, time, organizer, btnLeaveTable);

		setConstraints();

		setActions();

		attach();
	}

	private void setConstraints() {
		AnchorPane.setTopAnchor(tabTitle, 5d);
		AnchorPane.setLeftAnchor(tabTitle, 10d);
		AnchorPane.setRightAnchor(tabTitle, 10d);

		AnchorPane.setLeftAnchor(addr, 15d);
		AnchorPane.setTopAnchor(addr, 35d);

		AnchorPane.setLeftAnchor(date, 15d);
		AnchorPane.setTopAnchor(date, 60d);

		AnchorPane.setLeftAnchor(time, 150d);
		AnchorPane.setTopAnchor(time, 60d);

		AnchorPane.setLeftAnchor(organizer, 250d);
		AnchorPane.setTopAnchor(organizer, 60d);

		AnchorPane.setBottomAnchor(btnLeaveTable, 10d);
		AnchorPane.setRightAnchor(btnLeaveTable, 15d);
	}

	private void setActions() {
		btnLeaveTable.setOnMouseClicked(event -> {

			var confirm = AlertFactory.getInstance().createAlert("Are you sure?", AlertType.CONFIRMATION);
			var btnYes = new ButtonType("Yes", ButtonData.YES);
			var btnNo = new ButtonType("No", ButtonData.NO);
			confirm.getButtonTypes().setAll(btnYes, btnNo);
			confirm.showAndWait().ifPresent(type -> {
				if (type == btnYes) {
					var ctrl = new ReserveTableSeatController();
					try {
						var tableBean = (TableBean) ((BeanContainer) obj).getCarriedBean();
						var currentUser = ((BeanContainer) obj).getUserBean();
						ctrl.removeParticipant(tableBean, currentUser);

						AlertFactory.getInstance()
								.createAlert("You have successfully left the table " + tableBean.getName(),
										AlertType.INFORMATION)
								.show();
						// remove this element from the list
						super.detach();

					} catch (DAOException | DeleteSeatException | DateParsingException e) {
						AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
						return;
					}
				}
			});
		});
	}
}

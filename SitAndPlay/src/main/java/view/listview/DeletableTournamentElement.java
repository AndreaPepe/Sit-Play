package main.java.view.listview;

import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.java.engineering.bean.tournaments.TournamentBean;
import main.java.engineering.exceptions.AlertFactory;

public class DeletableTournamentElement extends ListElement {

	private Label lblName;
	private Label lblAddr;
	private Label lblDate;
	private Label lblTime;
	private Label lblSponsoredBy;
	private Label lblSponsor;
	private ImageView ivLogo;
	private Button btnDelete;

	public DeletableTournamentElement(VBox vbox, Object obj) {
		super(vbox, obj);
	}

	@Override
	public void buildGraphic() {
		var tournament = (TournamentBean) super.obj;

		anchor.setPrefWidth(590d);
		anchor.setPrefHeight(150d);
		anchor.setStyle("-fx-background-color: #CCE1E9");

		lblName = new Label(tournament.getName() + " - " + tournament.getCardGame());
		lblName.setAlignment(Pos.CENTER);
		lblName.setFont(Font.font("System", FontWeight.BOLD, 14));

		lblAddr = new Label(tournament.getAddress());
		lblAddr.setMaxWidth(550);

		lblDate = new Label(tournament.getDate());
		lblDate.setPrefWidth(120);

		lblTime = new Label(tournament.getTime());
		lblTime.setPrefWidth(60);

		btnDelete = new Button("Delete");
		btnDelete.setPrefWidth(80);
		btnDelete.getStylesheets().add("/main/java/view/standalone/css/Style.css");
		btnDelete.getStyleClass().add("btn");

		if (tournament.getSponsor() != null) {
			lblSponsoredBy = new Label("Sponsored by");
			lblSponsor = new Label(tournament.getSponsor().getName());
			lblSponsor.setPrefHeight(35);
			lblSponsor.setPrefWidth(100);
			lblSponsor.setWrapText(true);

			if (tournament.getSponsor().getLogo() != null) {
				var image = new Image(tournament.getSponsor().getLogo());
				ivLogo = new ImageView();
				ivLogo.setFitHeight(50);
				ivLogo.setFitWidth(50);
				ivLogo.setImage(image);

				anchor.getChildren().addAll(lblName, lblAddr, lblDate, lblTime, lblSponsoredBy, lblSponsor, btnDelete,
						ivLogo);
				setAnchorConstraints(true, true);
			} else {
				anchor.getChildren().addAll(lblName, lblAddr, lblDate, lblTime, lblSponsoredBy, lblSponsor, btnDelete);
				setAnchorConstraints(true, false);
			}
		} else {
			anchor.getChildren().addAll(lblName, lblAddr, lblDate, lblTime, btnDelete);
			setAnchorConstraints(false, false);
		}

		setButtonClickedEvent();

		attach();
	}

	private void setButtonClickedEvent() {
		btnDelete.setOnMouseClicked(event -> {
			var dialog = AlertFactory.getInstance().createAlert(
					"Are you sure? All participant and eventually the sponsor will be notified",
					AlertType.CONFIRMATION);
			var btnYes = new ButtonType("Yes", ButtonData.YES);
			var btnNo = new ButtonType("No", ButtonData.NO);
			dialog.getButtonTypes().setAll(btnYes, btnNo);
			dialog.showAndWait().ifPresent(type -> {
				if (type == btnYes) {
					// TODO call ctrl
				}
			});
		});
	}

	private void setAnchorConstraints(boolean sponsor, boolean logo) {
		AnchorPane.setTopAnchor(lblName, 5d);
		AnchorPane.setLeftAnchor(lblName, 10d);
		AnchorPane.setRightAnchor(lblName, 10d);

		AnchorPane.setTopAnchor(lblAddr, 35d);
		AnchorPane.setLeftAnchor(lblAddr, 15d);

		AnchorPane.setTopAnchor(lblDate, 60d);
		AnchorPane.setLeftAnchor(lblDate, 15d);

		AnchorPane.setTopAnchor(lblTime, 60d);
		AnchorPane.setLeftAnchor(lblTime, 150d);

		AnchorPane.setBottomAnchor(btnDelete, 10d);
		AnchorPane.setRightAnchor(btnDelete, 15d);

		if (sponsor) {
			AnchorPane.setTopAnchor(lblSponsoredBy, 95d);
			AnchorPane.setLeftAnchor(lblSponsoredBy, 15d);

			AnchorPane.setTopAnchor(lblSponsor, 105d);
			AnchorPane.setLeftAnchor(lblSponsor, 15d);
			if (logo) {
				AnchorPane.setLeftAnchor(ivLogo, 150d);
				AnchorPane.setBottomAnchor(ivLogo, 10d);
			}
		}
	}

}

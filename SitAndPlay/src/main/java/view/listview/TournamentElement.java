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
import main.java.controller.applicationcontroller.reserveaseat.tournament.ReserveTournamentSeatController;
import main.java.engineering.bean.tournaments.TournamentBean;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DeleteSeatException;

public class TournamentElement extends ListElement {

	private Label title;
	private Label address;
	private Label date;
	private Label time;
	private Label organized;
	private Label sponsoredBy;
	private Label sponsor;
	private ImageView ivSponsor;
	private Button btnLeave;

	public TournamentElement(VBox vbox, Object obj) {
		super(vbox, obj);

	}

	@Override
	public void buildGraphic() {
		var tBean = (TournamentBean) ((BeanContainer) obj).getCarriedBean();

		anchor.setPrefWidth(590d);
		anchor.setPrefHeight(150d);
		anchor.setStyle("-fx-background-color: #CCE1E9");

		title = new Label(tBean.getName());
		title.setAlignment(Pos.CENTER);
		title.setFont(Font.font("System", FontWeight.BOLD, 14));

		address = new Label(tBean.getAddress());
		address.setMaxWidth(550);

		date = new Label(tBean.getDate());
		date.setPrefWidth(120);

		time = new Label(tBean.getTime());
		time.setPrefWidth(60);

		organized = new Label("Organized by " + tBean.getOrganizer());
		organized.setPrefWidth(310);

		btnLeave = new Button("Leave");
		btnLeave.setPrefWidth(80);
		btnLeave.getStylesheets().add("/main/java/view/standalone/css/Style.css");
		btnLeave.getStyleClass().add("btn");

		if (tBean.getSponsor() != null) {
			sponsoredBy = new Label("Sponsored by");
			sponsor = new Label(tBean.getSponsor().getName());
			sponsor.setPrefHeight(35);
			sponsor.setPrefWidth(140);
			sponsor.setWrapText(true);

			if (tBean.getSponsor().getLogo() != null) {
				var img = new Image(tBean.getSponsor().getLogo());
				ivSponsor = new ImageView();
				ivSponsor.setFitHeight(50);
				ivSponsor.setFitWidth(50);
				ivSponsor.setImage(img);

				anchor.getChildren().addAll(title, address, date, time, organized, sponsoredBy, sponsor, btnLeave,
						ivSponsor);
				setConstraints(true, true);
			} else {
				anchor.getChildren().addAll(title, address, date, time, organized, sponsoredBy, sponsor, btnLeave);
				setConstraints(true, false);
			}
		} else {
			anchor.getChildren().addAll(title, address, date, time, organized, btnLeave);
			setConstraints(false, false);
		}

		setButtonAction();

		attach();
	}

	private void setButtonAction() {

		btnLeave.setOnMouseClicked(event -> {

			var confirmationAlert = AlertFactory.getInstance().createAlert("Are you sure?", AlertType.CONFIRMATION);
			var btnYes = new ButtonType("Yes", ButtonData.YES);
			var btnNo = new ButtonType("No", ButtonData.NO);
			confirmationAlert.getButtonTypes().setAll(btnYes, btnNo);
			confirmationAlert.showAndWait().ifPresent(type -> {
				var ctrl = new ReserveTournamentSeatController();
				try {
					var tBean = (TournamentBean) ((BeanContainer) obj).getCarriedBean();
					var userBean = ((BeanContainer) obj).getUserBean();
					ctrl.removeParticipant(tBean, userBean);

					AlertFactory.getInstance()
							.createAlert("You have successfully left the tournament " + tBean.getName(),
									AlertType.INFORMATION)
							.show();
					// remove this element from the list
					super.detach();
					detach();
				} catch (DAOException | DeleteSeatException e) {
					AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
					return;
				}
			});
		});

	}

	private void setConstraints(boolean hasSponsor, boolean sponsorHasLogo) {
		AnchorPane.setTopAnchor(title, 5d);
		AnchorPane.setLeftAnchor(title, 10d);
		AnchorPane.setRightAnchor(title, 10d);

		AnchorPane.setTopAnchor(address, 35d);
		AnchorPane.setLeftAnchor(address, 15d);

		AnchorPane.setTopAnchor(date, 60d);
		AnchorPane.setLeftAnchor(date, 15d);

		AnchorPane.setTopAnchor(time, 60d);
		AnchorPane.setLeftAnchor(time, 150d);

		AnchorPane.setTopAnchor(organized, 60d);
		AnchorPane.setLeftAnchor(organized, 250d);

		AnchorPane.setBottomAnchor(btnLeave, 10d);
		AnchorPane.setRightAnchor(btnLeave, 15d);

		if (hasSponsor) {
			AnchorPane.setTopAnchor(sponsoredBy, 85d);
			AnchorPane.setLeftAnchor(sponsoredBy, 15d);

			AnchorPane.setTopAnchor(sponsor, 105d);
			AnchorPane.setLeftAnchor(sponsor, 15d);
			if (sponsorHasLogo) {
				AnchorPane.setLeftAnchor(ivSponsor, 180d);
				AnchorPane.setBottomAnchor(ivSponsor, 5d);
			}
		}
	}

}

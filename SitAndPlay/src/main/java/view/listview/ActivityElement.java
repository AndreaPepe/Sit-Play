package main.java.view.listview;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import main.java.controller.applicationcontroller.business.ManageActivitiesController;
import main.java.engineering.bean.businessactivity.BusinessActivityBean;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.exceptions.DeleteActivityException;

public class ActivityElement extends ListElement {

	private ImageView logo;
	private Label description;
	private Button btnModifyLogo;
	private Button btnDelete;

	private File newLogoFile = null;

	public ActivityElement(VBox vbox, Object obj) {
		super(vbox, obj);
	}

	@Override
	public void buildGraphic() {
		var bean = (BusinessActivityBean) super.obj;
		anchor.setPrefWidth(590d);
		anchor.setPrefHeight(100d);
		anchor.setStyle("-fx-background-color: #abcdef");

		Image image;
		if (bean.getLogo() == null) {
			image = new Image(getClass().getResourceAsStream("../../../resources/upload_img.png"), 80d, 80d, false,
					false);
		} else {
			image = new Image(bean.getLogo(), 80d, 80d, false, false);
		}
		logo = new ImageView(image);
		logo.setFitHeight(80);
		logo.setFitWidth(80);
		logo.setStyle("-fx-cursor: hand");

		description = new Label();
		description.setFont(Font.font(16));
		description.setText(bean.getName());
		description.setPrefWidth(280d);

		btnModifyLogo = new Button();
		btnDelete = new Button();

		btnModifyLogo.setPrefWidth(100d);
		btnModifyLogo.setText("Modify logo");

		btnDelete.setPrefWidth(100d);
		btnDelete.setText("Delete");

		btnModifyLogo.getStylesheets().add("/main/java/view/standalone/css/Style.css");
		btnDelete.getStylesheets().add("/main/java/view/standalone/css/Style.css");
		btnModifyLogo.getStyleClass().add("btn");
		btnDelete.getStyleClass().add("btn");

		anchor.getChildren().addAll(logo, description, btnModifyLogo, btnDelete);

		setContraints();

		setActions();

		// attach the new built anchor pane to the VBox container
		attach();
	}

	private void setContraints() {

		AnchorPane.setLeftAnchor(logo, 20d);
		AnchorPane.setTopAnchor(logo, 10d);
		AnchorPane.setBottomAnchor(logo, 10d);

		AnchorPane.setTopAnchor(description, 35d);
		AnchorPane.setLeftAnchor(description, 130d);

		AnchorPane.setTopAnchor(btnModifyLogo, 15d);
		AnchorPane.setRightAnchor(btnModifyLogo, 30d);

		AnchorPane.setBottomAnchor(btnDelete, 15d);
		AnchorPane.setRightAnchor(btnDelete, 30d);
	}

	private void setActions() {
		logo.setOnMouseClicked(event -> uploadLogo());

		btnModifyLogo.setOnMouseClicked(event -> modifyLogo());

		btnDelete.setOnMouseClicked(event -> deleteActivity());
	}

	private void uploadLogo() {
		var chooser = new FileChooser();
		chooser.setTitle("Upload logo");
		chooser.getExtensionFilters().add(new ExtensionFilter("Image", "*.png", "*.jpg", "*.jpeg"));
		var uploadedFile = chooser.showOpenDialog(super.getContainer().getScene().getWindow());
		newLogoFile = uploadedFile;
		if (newLogoFile != null) {
			var uploadedLogo = new Image(newLogoFile.toURI().toString(), 80, 80, false, false);
			logo.setImage(uploadedLogo);
		}
	}

	private void modifyLogo() {
		var existingBean = (BusinessActivityBean) super.obj;
		if (newLogoFile == null) {
			var bean = new BusinessActivityBean(existingBean.getName(), null, existingBean.getOwner());
			try {
				var ctrl = new ManageActivitiesController();
				ctrl.modifyLogo(bean);

				AlertFactory.getInstance().createAlert("The logo has been successfully updated!", AlertType.INFORMATION)
						.show();

			} catch (DAOException e) {
				AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
			}
		} else {
			// try with resources, to guarantee the closure of the stream in each case
			try (InputStream fis = new FileInputStream(newLogoFile)) {
				var bean = new BusinessActivityBean(existingBean.getName(), fis, existingBean.getOwner());

				var ctrl = new ManageActivitiesController();
				if (Boolean.TRUE.equals(ctrl.modifyLogo(bean))) {
					AlertFactory.getInstance()
							.createAlert("The logo has been successfully updated!", AlertType.INFORMATION).show();
				}
			} catch (IOException e) {
				AlertFactory.getInstance()
						.createAlert("Something went wrong loading the logo; please try to upload it again.",
								AlertType.ERROR)
						.show();
			} catch (DAOException e) {
				AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
			}
		}

	}

	private void deleteActivity() {
		var alert = AlertFactory.getInstance().createAlert("Are you sure?", AlertType.CONFIRMATION);
		var btnYes = new ButtonType("Yes", ButtonData.YES);
		var btnNo = new ButtonType("No", ButtonData.NO);
		alert.getButtonTypes().setAll(btnYes, btnNo);
		alert.showAndWait().ifPresent(type -> {
			if (type == btnYes) {
				var thisBean = (BusinessActivityBean) super.obj;
				var ctrl = new ManageActivitiesController();
				try {
					ctrl.deleteBusinessActivity(thisBean);
					super.detach();
				} catch (DAOException | DeleteActivityException | DateParsingException e) {
					AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
				}
			}
		});
	}

}

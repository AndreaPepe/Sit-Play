package main.java.controller.guicontroller.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import main.java.controller.applicationcontroller.business.CreateBusinessActivityController;
import main.java.controller.guicontroller.GuiBasicInternalPageController;
import main.java.engineering.bean.businessactivity.BusinessActivityBean;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.BeanCheckException;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.WrongUserTypeException;
import main.java.engineering.utils.Session;

public class GuiManageActivitiesController extends GuiBasicInternalPageController {

	public GuiManageActivitiesController(Session ssn) {
		super(ssn);
	}

	@FXML
	private ToggleButton toggleAdd;

	@FXML
	private ToggleButton toggleMyActivities;

	@FXML
	private AnchorPane apMyActivities;

	@FXML
	private AnchorPane apAddActivity;

	@FXML
	private TextField tfActivityName;

	@FXML
	private Button btnUpload;

	@FXML
	private ImageView ivLogo;

	@FXML
	private Button btnCreateActivity;

	private File logoFile = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		var group = new ToggleGroup();
		toggleAdd.setToggleGroup(group);
		toggleMyActivities.setToggleGroup(group);
		// avoid unselected toggle button
		group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null) {
				oldVal.setSelected(true);
			}
		});
		toggleAdd.fire();
	}

	@FXML
	void handleToggleAdd(ActionEvent event) {
		apMyActivities.toBack();
		apAddActivity.toFront();
	}

	@FXML
	void handleToggleMyActivities(ActionEvent event) {
		apAddActivity.toBack();
		apMyActivities.toFront();
	}

	@FXML
	void uploadLogo(ActionEvent event) {
		var chooser = new FileChooser();
		chooser.setTitle("Upload logo");
		chooser.getExtensionFilters().add(new ExtensionFilter("Image", "*.png", "*.jpg", "*.jpeg"));
		var uploadedFile = chooser.showOpenDialog(btnUpload.getScene().getWindow());
		logoFile = uploadedFile;
		var uploadedLogo = new Image(logoFile.toURI().toString(), 150, 150, false, false);
		ivLogo.setImage(uploadedLogo);
	}

	@FXML
	void createNewActivity(ActionEvent event) {
		var activityName = tfActivityName.getText();
		if (activityName == null) {
			AlertFactory.getInstance().createAlert("Please insert a name for the activity", AlertType.ERROR).show();
		}
		BusinessActivityBean bean;
		if (logoFile == null) {
			bean = new BusinessActivityBean(activityName, null, ssn.getUser().getUsername());
			try {
				bean.checkFields();

				var ctrl = new CreateBusinessActivityController();
				ctrl.createActivity(bean, ssn.getUser());

				AlertFactory.getInstance()
						.createAlert("The new activity has been successfully created!", AlertType.INFORMATION).show();

			} catch (BeanCheckException | DAOException | WrongUserTypeException e) {
				AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
			}
		} else {
			// try with resources, to guarantee the closure of the stream in each case
			try (InputStream fis = new FileInputStream(logoFile)) {
				bean = new BusinessActivityBean(activityName, fis, ssn.getUser().getUsername());

				bean.checkFields();

				var ctrl = new CreateBusinessActivityController();
				ctrl.createActivity(bean, ssn.getUser());

				AlertFactory.getInstance()
						.createAlert("The new activity has been successfully created!", AlertType.INFORMATION).show();
			} catch (IOException e) {
				AlertFactory.getInstance()
						.createAlert("Something went wrong loading the logo; please try to upload it again.",
								AlertType.ERROR)
						.show();
			} catch (BeanCheckException | DAOException | WrongUserTypeException e) {
				AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
			}
		}

	}

}

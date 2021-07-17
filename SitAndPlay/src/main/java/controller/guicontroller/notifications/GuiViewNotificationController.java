package main.java.controller.guicontroller.notifications;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import main.java.controller.applicationcontroller.notifications.ViewNotificationController;
import main.java.controller.guicontroller.GuiBasicInternalPageController;
import main.java.engineering.bean.notifications.NotificationBean;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.utils.Session;
import main.java.view.listview.ListElementFactory;
import main.java.view.listview.ListElementType;

public class GuiViewNotificationController extends GuiBasicInternalPageController{

	@FXML
    private RadioButton rbTen;

    @FXML
    private RadioButton rbTwenty;

    @FXML
    private RadioButton rbForty;
    
    @FXML
    private VBox vbox;

	public GuiViewNotificationController(Session ssn) {
		super(ssn);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		var toggleGroup = new ToggleGroup();
		rbTen.setToggleGroup(toggleGroup);
		rbTwenty.setToggleGroup(toggleGroup);
		rbForty.setToggleGroup(toggleGroup);
		toggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
			// avoid unselected radio button
			if (newVal == null) {
				oldVal.setSelected(true);
			}else if ((RadioButton) newVal == rbForty) {
				loadNotifications(40);
			}else if ((RadioButton) newVal == rbTwenty) {
				loadNotifications(20);
			}else {
				loadNotifications(10);
			}
			
		});
		
		rbTen.fire();
	}

	private void loadNotifications(int quantity) {
		var ctrl = new ViewNotificationController();
		try {
			var notificationBeans = ctrl.retrieveLastNotification(ssn.getUser(), quantity);
			vbox.getChildren().clear();
			for (NotificationBean bean : notificationBeans) {
				ListElementFactory.getInstance().createElement(ListElementType.NOTIFICATION, vbox, bean);
			}
		} catch (DAOException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}
	}

}

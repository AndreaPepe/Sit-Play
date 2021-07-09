package main.java.controller.guicontroller.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import main.java.controller.applicationcontroller.RegistrationController;
import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.EmptyDataException;
import main.java.model.UserType;

public class GuiRegistrationController implements Initializable{
	
	private static final String LOGIN_PAGE = "/main/java/view/standalone/login/Login.fxml";
	private static final String EMPTY_USERNAME_ERR_MSG = "Username can't be empty!";
	private static final String EMPTY_PASSWORD_ERR_MSG = "Password can't be empty!";
	private static final String GENERIC_ERR_MSG = "Something went wrong, please retry";
	private static final String SUCCESSFUL_REGISTRATION_MSG = "Registration succedeed! Go back to login page to log in";
	
	@FXML
	private AnchorPane basePane;

	@FXML
    private Button btnClose;

    @FXML
    private TextField tfUsername;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private RadioButton rbPlayer;

    @FXML
    private ToggleGroup tgUserType;

    @FXML
    private RadioButton rbOrganizer;

    @FXML
    private RadioButton rbBusinessman;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnRegistrate;

    @FXML
    private Label lblGenericError;
    
    @FXML
    private Label lblSuccess;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// nothing to init
	}
	
	@FXML
	public void handleBack(ActionEvent event) throws IOException {
		var loader = new FXMLLoader(getClass().getResource(LOGIN_PAGE));
    	loader.setControllerFactory(c -> new GuiLoginController());
    	Parent root = loader.load();
    	basePane.getChildren().removeAll();
    	basePane.getChildren().setAll(root);
	}
	
	@FXML
	public void handleRegistration(ActionEvent event){
		
		resetLabels();
		var beanUser = new BeanUser();
		
		try {
			beanUser.setUsername(tfUsername.getText());
			beanUser.setPassword(pfPassword.getText());
			String userType = ((RadioButton)tgUserType.getSelectedToggle()).getText();
			switch(userType) {
			case "Player":	beanUser.setUserType(UserType.PLAYER); break;
			case "Organizer": beanUser.setUserType(UserType.ORGANIZER); break;
			case "Businessman": beanUser.setUserType(UserType.BUSINESSMAN); break;
			default: break;
			}
			
			// instantiate application controller
			var ctrl = new RegistrationController();
			boolean result = ctrl.signIn(beanUser);
			
			if(result) {
				lblSuccess.setText(SUCCESSFUL_REGISTRATION_MSG);
				lblSuccess.setVisible(true);
			}
		} catch (EmptyDataException e) {
			int code = e.getErrorCode();
			if (code == 0) {
				lblGenericError.setText(EMPTY_USERNAME_ERR_MSG);
				lblGenericError.setVisible(true);
			}else if (code == 3) {
				lblGenericError.setText(EMPTY_PASSWORD_ERR_MSG);
				lblGenericError.setVisible(true);
			}
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, String.format("EmptyDataException catched: %s" , e.getMessage()));
		} catch (DAOException e) {
			lblGenericError.setText(e.getMessage());
			lblGenericError.setVisible(true);
		} catch (Exception e) {
		lblGenericError.setText(GENERIC_ERR_MSG);
		lblGenericError.setVisible(true);
		Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, String.format("Generic exception catched: %s", e.getMessage()));
	}
		
	}
	
	 private void resetLabels() {
	    	lblGenericError.setText("");
			lblGenericError.setVisible(false);
			
			lblSuccess.setText("");
			lblSuccess.setVisible(false);
			
	    }
	    
}

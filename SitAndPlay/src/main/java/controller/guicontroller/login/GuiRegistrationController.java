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
	
	private final String loginPage = "/main/java/view/standalone/login/Login.fxml";
	private final String emptyUsernameErrorMsg = "Username can't be empty!";
	private final String emptyPasswordErrorMsg = "Password can't be empty!";
	private final String genericErrorMsg = "Something went wrong, please retry";
	private final String successfulSignInMsg = "Registration succedeed! Go back to login page to log in";
	
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
		
	}
	
	@FXML
	public void handleBack(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(this.loginPage));
    	loader.setControllerFactory(c -> new GuiLoginController());
    	Parent root = loader.load();
    	basePane.getChildren().removeAll();
    	basePane.getChildren().setAll(root);
	}
	
	@FXML
	public void handleRegistration(ActionEvent event) throws IOException {
		
		resetLabels();
		BeanUser beanUser = new BeanUser();
		
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
			RegistrationController ctrl = new RegistrationController();
			boolean result = ctrl.signIn(beanUser);
			
			if(result) {
				lblSuccess.setText(successfulSignInMsg);
				lblSuccess.setVisible(true);
			}
		} catch (EmptyDataException e) {
			int code = e.getErrorCode();
			if (code == 0) {
				lblGenericError.setText(emptyUsernameErrorMsg);
				lblGenericError.setVisible(true);
			}else if (code == 3) {
				lblGenericError.setText(emptyPasswordErrorMsg);
				lblGenericError.setVisible(true);
			}
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "EmptyDataException catched: " + e.getMessage());
		} catch (DAOException e) {
			lblGenericError.setText(e.getMessage());
			lblGenericError.setVisible(true);
		} catch (Exception e) {
		lblGenericError.setText(genericErrorMsg);
		lblGenericError.setVisible(true);
		Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Generic exception catched: " + e.getMessage());
	}
		
	}
	
	 private void resetLabels() {
	    	lblGenericError.setText("");
			lblGenericError.setVisible(false);
			
			lblSuccess.setText("");
			lblSuccess.setVisible(false);
			
	    }
	    
}

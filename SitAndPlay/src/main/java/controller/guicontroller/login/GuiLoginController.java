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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.controller.applicationcontroller.LoginController;
import main.java.controller.guicontroller.GuiHomePageController;
import main.java.engineering.bean.login.BeanLoggedUser;
import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.EmptyDataException;
import main.java.engineering.exceptions.WrongCredentialsExceptions;
import main.java.engineering.utils.Session;


public class GuiLoginController implements Initializable{
	
	private static final String REGISTRATION_PAGE = "/main/java/view/standalone/login/Registration.fxml";
	private static final String EMPTY_USERNAME_ERROR_MESSAGE = "Username can't be empty!";
	private static final String EMPTY_PASSWORD_ERROR_MESSAGE = "Password can't be empty!";
	private static final String DAO_ERROR_MESSAGE = "Error with database, please retry";
	private static final String GENERIC_ERROR_MESSAGE = "Something went wrong, please retry";
	
	// TODO: select this page dynamically
	private static final String START_PAGE = "/main/java/view/standalone/HomePage.fxml";
	
	@FXML
	private AnchorPane basePane;
	
	@FXML
    private Label lblErrorUsername;

    @FXML
    private TextField tfUsername;

    @FXML
    private Label lblErrorPassword;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Hyperlink hyRegistrate;
    
    @FXML
    private Button btnClose;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Nothing is needed
	}
    

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
    	
    	resetLabels();
    	
    	// create bean to store data
    	var beanUser = new BeanUser();
    	try {
			beanUser.setUsername(tfUsername.getText());
			beanUser.setPassword(pfPassword.getText());
			
			// new instance of application controller
			var ctrl = new LoginController();
			BeanLoggedUser resultLogin = ctrl.login(beanUser);
			
			// create a new desktop Session -> isWeb = false
	    	var ssn = new Session(false);
	    	
			//Registration of the user into Session
			ssn.setLoggedUser(resultLogin);
			ssn.setHomePage();
			
			var loader = new FXMLLoader(getClass().getResource(START_PAGE));
			loader.setControllerFactory(c -> new GuiHomePageController(ssn));
			Parent root = loader.load();
			// create a new scene with different dimensions
			var scene = new Scene(root);
			// retrieve the application Stage to set a new scene of different dimensions
			var appStage = (Stage)((Node) event.getSource()).getScene().getWindow();
			appStage.setTitle("Sit&Play");
			appStage.setScene(scene);
			appStage.setX(20);
			appStage.setY(20);
			appStage.show();
			
		} catch (EmptyDataException e) {
			int code = e.getErrorCode();
			if (code == 0) {
				lblErrorUsername.setText(EMPTY_USERNAME_ERROR_MESSAGE);
				lblErrorUsername.setVisible(true);
			}else if (code == 3) {
				lblErrorPassword.setText(EMPTY_PASSWORD_ERROR_MESSAGE);
				lblErrorPassword.setVisible(true);
			}
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, String.format("EmptyDataException catched: %s", e.getMessage()));
		} catch (WrongCredentialsExceptions e) {
			lblErrorUsername.setText(e.getMessage());
			lblErrorUsername.setVisible(true);
		} catch (DAOException e) {
			lblErrorUsername.setText(DAO_ERROR_MESSAGE);
			lblErrorUsername.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		lblErrorUsername.setText(GENERIC_ERROR_MESSAGE);
		lblErrorUsername.setVisible(true);
		Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, String.format("General Exception catched: %s", e.getMessage()));
		}
    }
    
    

    @FXML
    private void handleRegistration(ActionEvent event) throws IOException {
    	var loader = new FXMLLoader(getClass().getResource(REGISTRATION_PAGE));
    	loader.setControllerFactory(c -> new GuiRegistrationController());
    	Parent root = loader.load();
    	basePane.getChildren().removeAll();
    	basePane.getChildren().setAll(root);
    }
    
    private void resetLabels() {
    	lblErrorUsername.setText("");
		lblErrorUsername.setVisible(false);
		
		lblErrorPassword.setText("");
		lblErrorPassword.setVisible(false);
		
    }
    
}

package main.java.controller.GUIctrl.login;

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
import main.java.controller.GUIctrl.GuiHomePageController;
import main.java.controller.applicationCtrl.LoginController;
import main.java.engineering.bean.login.BeanLoggedUser;
import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.EmptyDataException;
import main.java.engineering.exceptions.WrongCredentialsExceptions;
import main.java.engineering.utils.Session;


public class GuiLoginController implements Initializable{
	
	private final String registrationPage = "/main/java/view/standalone/login/Registration.fxml";
	private final String emptyUsernameErrorMsg = "Username can't be empty!";
	private final String emptyPasswordErrorMsg = "Password can't be empty!";
	private final String daoErrorMessage = "Error with database, please retry";
	private final String genericErrorMsg = "Something went wrong, please retry";
	
	// TODO: select this page dynamically
	private final String startPage = "/main/java/view/standalone/HomePage.fxml";
	
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
    private Hyperlink hySignIn;
    
    @FXML
    private Button btnClose;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
    

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
    	
    	resetLabels();
    	
    	// create bean to store data
    	BeanUser beanUser = new BeanUser();
    	try {
			beanUser.setUsername(tfUsername.getText());
			beanUser.setPassword(pfPassword.getText());
			
			// new instance of application controller
			LoginController ctrl = new LoginController();
			BeanLoggedUser resultLogin = ctrl.login(beanUser);
			
			// create a new desktop Session -> isWeb = false
	    	Session ssn = new Session(false);
	    	
			//Registration of the user into Session
			ssn.setLoggedUser(resultLogin);
			ssn.setHomePage();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(startPage));
			loader.setControllerFactory(c -> new GuiHomePageController(ssn));
			Parent root = loader.load();
			// create a new scene with different dimensions
			Scene scene = new Scene(root);
			// retrieve the application Stage to set a new scene of different dimensions
			Stage appStage = (Stage)((Node) event.getSource()).getScene().getWindow();
			appStage.setTitle("Sit&Play");
			appStage.setScene(scene);
			appStage.setX(20);
			appStage.setY(20);
			appStage.show();
			
		} catch (EmptyDataException e) {
			int code = e.getErrorCode();
			if (code == 0) {
				lblErrorUsername.setText(emptyUsernameErrorMsg);
				lblErrorUsername.setVisible(true);
			}else if (code == 3) {
				lblErrorPassword.setText(emptyPasswordErrorMsg);
				lblErrorPassword.setVisible(true);
			}
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "EmptyDataException catched: " + e.getMessage());
		} catch (WrongCredentialsExceptions e) {
			lblErrorUsername.setText(e.getMessage());
			lblErrorUsername.setVisible(true);
		} catch (DAOException e) {
			lblErrorUsername.setText(daoErrorMessage);
			lblErrorUsername.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		lblErrorUsername.setText(genericErrorMsg);
		lblErrorUsername.setVisible(true);
		Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "General Exception catched: " + e.getMessage());
		}
    }
    
    

    @FXML
    private void handleSignIn(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource(this.registrationPage));
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

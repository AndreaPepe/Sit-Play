package main.java.view.standalone;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.controller.GUIctrl.login.GuiLoginController;

public class HomePage extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("login/Login.fxml"));
		loader.setControllerFactory(c -> new GuiLoginController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Sit&Play Login");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	

}
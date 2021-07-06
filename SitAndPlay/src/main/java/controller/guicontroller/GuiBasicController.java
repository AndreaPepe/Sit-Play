package main.java.controller.guicontroller;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import main.java.engineering.utils.Session;

/**
 * This is an Abstract class used to group common elements among GuiControllers
 * like the basic AnchorPane and the pagePane;
 * GuiControllers used for login are not part of this family
 * @author Andrea Pepe
 *
 */

public abstract class GuiBasicController implements Initializable{

	protected Session ssn;
	
	@FXML
	protected AnchorPane basePane;
	
	@FXML
	protected Pane pnPage;
	
	
	protected GuiBasicController(Session ssn) {
		this.ssn = ssn;
	}
	

	
}

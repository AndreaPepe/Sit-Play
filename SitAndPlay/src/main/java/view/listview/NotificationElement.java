package main.java.view.listview;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.java.engineering.bean.notifications.NotificationBean;

public class NotificationElement extends ListElement {

	private HBox hbox;
	private Label description;
	
	public NotificationElement(VBox vbox, Object obj) {
		super(vbox, obj);
	}

	
	@Override
	public void buildGraphic() {
		var notifBean = (NotificationBean) super.obj;
		anchor.setPrefWidth(590d);
		anchor.setPrefHeight(100d);
		anchor.setStyle("-fx-background-color: #CCE1E9");
		
		hbox = new HBox();
		hbox.setStyle("-fx-border-color: #296581; -fx-border-width: 2px; -fx-border-radius: 5;");
		hbox.setAlignment(Pos.CENTER);
		
		description = new Label(notifBean.getMsg());
		description.setPrefHeight(60);
		description.setPrefWidth(500);
		description.setWrapText(true);
		description.setFont(Font.font(14));
		description.setAlignment(Pos.CENTER);
		
		hbox.getChildren().add(description);
		anchor.getChildren().add(hbox);
		
		setContraints();
		
		attach();
	}


	private void setContraints() {
		AnchorPane.setBottomAnchor(hbox, 10d);	
		AnchorPane.setLeftAnchor(hbox, 10d);	
		AnchorPane.setRightAnchor(hbox, 10d);	
		AnchorPane.setTopAnchor(hbox, 10d);	
	}

}

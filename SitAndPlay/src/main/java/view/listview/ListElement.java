package main.java.view.listview;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public abstract class ListElement {

	private VBox container;
	protected AnchorPane anchor;
	protected Object obj;
	
	protected ListElement(VBox vbox, Object obj) {
		this.container = vbox;
		this.anchor = new AnchorPane();
		setObj(obj);
	}
	
	public VBox getContainer() {
		return container;
	}

	public AnchorPane getAnchor() {
		return anchor;
	}

	public void setAnchor(AnchorPane anchor) {
		this.anchor = anchor;
	}

	public void setObj(Object obj) {
		this.obj = obj;
		buildGraphic();
	}

	public void attach() {
		container.getChildren().add(anchor);
	}
	
	public void detach() {
		container.getChildren().remove(anchor);
	}

	public abstract void buildGraphic();
}

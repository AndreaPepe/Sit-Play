package main.java.view.listview;

import javafx.scene.layout.VBox;

public class ListElementFactory {

	// singleton
	private static ListElementFactory instance = null;

	private ListElementFactory() {

	}

	public static ListElementFactory getInstance() {
		if (instance == null) {
			instance = new ListElementFactory();
		}
		return instance;
	}

	public ListElement createElement(ListElementType type, VBox container, Object obj) {
		ListElement elem = null;
		switch (type) {
		case ACTIVITY:
			elem = new ActivityElement(container, obj);
			break;
		// TODO: add other cases
		default:
			break;
		}
		return elem;
	}
}

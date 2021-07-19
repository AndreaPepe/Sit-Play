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

	// factory method
	public ListElement createElement(ListElementType type, VBox container, Object obj) {
		ListElement elem = null;
		switch (type) {
		case ACTIVITY:
			elem = new ActivityElement(container, obj);
			break;
		case NOTIFICATION:
			elem = new NotificationElement(container, obj);
			break;
		case OPEN_TOURNAMENT:
			elem = new TournamentElement(container, obj);
			break;
		case OPEN_TABLE:
			elem = new TableElement(container, obj);
			break;
		// TODO: add other cases
		default:
			break;
		}
		return elem;
	}
}

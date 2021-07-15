package main.java.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;
import main.java.engineering.exceptions.WindowNotFoundException;

public class PopupFxFactory {
	private static PopupFxFactory instance = null;
	// 10 second duration
	private static final int POPUP_DURATION = 5000;

	private PopupFxFactory() {

	}

	public static PopupFxFactory getInstance() {
		if (instance == null) {
			instance = new PopupFxFactory();
		}
		return instance;
	}

	private Popup generatePopup(String message) {
		var popup = new Popup();
		popup.setAutoFix(true);
		var pane = new Pane();
		pane.setMinWidth(400);
		pane.setMinHeight(80);
		var msg = new Label(message);
		msg.setPrefHeight(80);
		msg.setPrefWidth(400);
		msg.setFont(Font.font(14));
		msg.setAlignment(Pos.CENTER);
		msg.getStylesheets().add("/main/java/view/standalone/css/Style.css");
		msg.getStyleClass().add("popup");
		pane.getChildren().add(msg);
		popup.getContent().add(pane);

		return popup;
	}

	public void showPopup(String message) throws WindowNotFoundException {
		var window = Window.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
		if (window == null) {
			throw new WindowNotFoundException("Unable to find window to display popup message");
		}
		var popup = generatePopup(message);
		popup.setOnShown(e -> {
			popup.setX(window.getX() + window.getWidth() / 2 - popup.getWidth() / 2 + 125);
			popup.setY(window.getY() + 40);
		});
		popup.show(window);

		window.yProperty().addListener(
				(obs, oldVal, newVal) -> popup.setY(window.getY() + 40));
		window.xProperty().addListener(
				(obs, oldVal, newVal) -> popup.setX(window.getX() + window.getWidth() / 2 - popup.getWidth() / 2 + 125));

		new Timeline(new KeyFrame(Duration.millis(POPUP_DURATION), ae -> {
			if (popup.isShowing()) {
				popup.hide();
			}
		})).play();
	}
}

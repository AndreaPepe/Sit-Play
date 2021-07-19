package main.java.view;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;
import main.java.engineering.exceptions.WindowNotFoundException;

public class PopupFxFactory {
	private static PopupFxFactory instance = null;
	// 5 second duration
	private static final int POPUP_DURATION = 3500;
	private static final String SOUND_URL_STRING = "src/main/resources/notification_sound.mp3";

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
		var pane = new AnchorPane();
		pane.setMinWidth(400);
		pane.setMinHeight(80);
		var msg = new Label(message);
		msg.setPrefHeight(60);
		msg.setPrefWidth(380);
		msg.setFont(Font.font(14));
		msg.setAlignment(Pos.CENTER);
		msg.setTextAlignment(TextAlignment.CENTER);
		msg.setWrapText(true);
		msg.getStylesheets().add("/main/java/view/standalone/css/Style.css");
		msg.getStyleClass().add("popup");
		pane.getChildren().add(msg);
		
		AnchorPane.setTopAnchor(msg, 10d);
		AnchorPane.setBottomAnchor(msg, 10d);
		AnchorPane.setLeftAnchor(msg, 10d);
		AnchorPane.setRightAnchor(msg, 10d);
		
		popup.getContent().add(pane);

		return popup;
	}

	public synchronized void showPopup(String message) throws WindowNotFoundException {
		var window = Window.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
		if (window == null) {
			throw new WindowNotFoundException("Unable to find window to display popup message");
		}
		var popup = generatePopup(message);
		popup.setOnShown(e -> {
			popup.setX(window.getX() + window.getWidth() / 2 - popup.getWidth() / 2 + 125);
			popup.setY(window.getY() + 40);
		});
		

		window.yProperty().addListener(
				(obs, oldVal, newVal) -> popup.setY(window.getY() + 40));
		window.xProperty().addListener(
				(obs, oldVal, newVal) -> popup.setX(window.getX() + window.getWidth() / 2 - popup.getWidth() / 2 + 125));
		
		var sound = new Media(new File(SOUND_URL_STRING).toURI().toString());
		var mediaPlayer = new MediaPlayer(sound);
		// JavaFX Thread safe
		Platform.runLater(() -> {
			popup.show(window);
			mediaPlayer.play();
			
			new Timeline(new KeyFrame(Duration.millis(POPUP_DURATION), ae -> {
				if (popup.isShowing()) {
					popup.hide();
				}
			})).play();
		});
		
	}
}

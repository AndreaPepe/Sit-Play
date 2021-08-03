package main.java.view.standalone;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import javafx.application.Platform;
import javafx.geometry.Side;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import main.java.controller.MapboxController;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.MapboxException;
import main.java.engineering.utils.map.MapPlace;
import main.java.engineering.utils.map.MapPlaceAdapter;

public class SearchMapPlaceTextField extends AutocompleteTextField<MapPlace> {

	public SearchMapPlaceTextField() {
		this(new TextField());
		setPos(Side.BOTTOM);
	}
	
	public SearchMapPlaceTextField(TextField textField){
		super(textField);
		setPos(Side.BOTTOM);
		// setting a value of 3 as the minimum number of characters 
		// to start performing autocompletion to avoid a lot of request to mapbox
		setCharacterLowerBound(3);
	}
	
	
	@Override
	public List<MapPlace> getPredictions(String text) {
		var controller = new MapboxController();
		List<MapPlace> mapPlaces = new ArrayList<>();
		try {
			List<JSONObject> results = controller.getPlaces(text);
			for(JSONObject obj : results) {
				// the adapter performs the parsing of the json and stores data as a place
				mapPlaces.add(new MapPlaceAdapter(obj));
			}
			return mapPlaces;
		}catch (MapboxException e) {
			Platform.runLater(() -> 
				AlertFactory.getInstance().createAlert(String.format("Mapbox Error: %s", e.getMessage()), AlertType.ERROR).showAndWait()
			);
		}
		return mapPlaces;
		
		
	}

}

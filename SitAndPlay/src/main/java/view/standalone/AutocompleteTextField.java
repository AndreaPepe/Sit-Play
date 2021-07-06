package main.java.view.standalone;

import java.util.LinkedList;
import java.util.regex.Pattern;
import javafx.scene.control.TextField;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import java.util.List;
import java.util.ArrayList;


/*This class defines a text field object with autocompletion functionality,
 *based on a supplied list of entries.<p>
 *
 *@param <S> data type stored in the context menu
 */
public abstract class AutocompleteTextField<S>{
	
		//The text field that we modify with an autocompletion feature
		private Side pos;
		private final TextField textField;
		private boolean blocked;
		private final ObjectProperty<S> lastSelectedItem=new SimpleObjectProperty<>();
		
		//The entries of the popUp
		private final List<S> entries;
		
		//The number of input characters after that autocompletion is provided
		private int characterLowerBound;
		private boolean enable=true;
		private ObservableList<S> filteredEntries=FXCollections.observableArrayList();
		private ContextMenu entriesPopup;
		private boolean popupHidden=false;
		
		//the number of max entries showed in popup
		private int maxEntries=10;
		
		
		protected AutocompleteTextField() {
			this(new TextField());
		}
		
		protected AutocompleteTextField(TextField textField) {
			this.textField=textField;
			this.entries=new ArrayList<>();
			this.filteredEntries.addAll(this.entries);
			
			//At the start all of the entries are considered filtered
			entriesPopup=new ContextMenu();
			this.textField.textProperty().addListener((observableValue, oldValue,  newValue)->{
				if(this.textField.getText()==null||this.textField.getText().length()==0) {
					filteredEntries.clear();
					filteredEntries.addAll(this.entries);
					entriesPopup.hide();
					entries.clear();
				}
				else {
					if(this.blocked)
						return;
					this.textField.setText(this.textField.getText().substring(0,1).toUpperCase()+this.textField.getText().substring(1));
					if(enable && this.textField.getText().length()>characterLowerBound) {
						// perform autocompletion options
					new Thread(()->{
						List<S> predictions = getPredictions(this.textField.getText());
						Platform.runLater(()->
							this.setUpPopUp(predictions)
							);
					}).start();
					
				}
				}
				});
			this.textField.focusedProperty().addListener((observable,oldValue,newValue)->{
				if(this.textField.isFocused()) {
				    this.enable=true;
					
				}
				else {
					this.enable=false;
					this.entries.clear();
					this.entriesPopup.hide();
				}
			});
			
		}
		
		/**
		 * This method set up the context menu with the entries of the 
		 * provided predictions.
		 * 
		 * @param predictions
		 */
		protected final void setUpPopUp(List<S> predictions) {
			this.entries.clear();
			for(S predict: predictions) {
				entries.add(predict);
			}
				LinkedList<S> searchResult= new LinkedList<>();
				String input=this.textField.getText();
				
				Pattern pattern;
				
				pattern=Pattern.compile(".*"+input+".*",Pattern.CASE_INSENSITIVE);
				for(S entry: this.entries) {
					var matcher = pattern.matcher(entry.toString());
					if(matcher.matches()) {
						searchResult.add(entry);
					}
				}
				if(!this.entries.isEmpty()) {
					filteredEntries.clear();
					filteredEntries.addAll(searchResult);
					if(!popupHidden) {
						
						// populate the context menu with the filtered entries
						populate(searchResult,input);
						if(!entriesPopup.isShowing()) {
							entriesPopup.show(this.textField,pos,0,0);
							
						}
					}
				}
				else {
					//hide the context menu
					entriesPopup.hide();
				}
		}

		/**
		 * Abstract method to define the logic of prediction's retrieve.
		 *
		 * @param text is the text to use for the filtering of predictions
		 * @return List<S> a list of data chosen to store predictions
		 */
		protected abstract List<S> getPredictions(String text);
		
		private void populate(LinkedList<S> searchResult, String input) {
			// build the menu items to add in the popup menu
			LinkedList<CustomMenuItem> menuItems= new LinkedList<>();
			int count=Math.min(searchResult.size(),maxEntries);
			for(var i=0;i<count;i++) {
				final var result = searchResult.get(i).toString();
				final var object = searchResult.get(i);
				int occurence;
				
				occurence = result.toLowerCase().indexOf(input.toLowerCase());
				
				if(occurence<0)
					continue;
				//part before occurrence (might be empty)
				var pre = new Text(result.substring(0,occurence));
				//part of first occurrence
				var occ = new Text(result.substring(occurence,occurence + input.length()));
				occ.setStyle("-fx-font-weight: bold;"
						+ "-fx-fill: rgb(0,0,0);");
				//part after occurrence
				var post = new Text(result.substring(occurence+input.length()));
				var entryFlow = new TextFlow(pre,occ,post);
				var item = new CustomMenuItem(entryFlow,true);
				item.setOnAction((ActionEvent e)->{
					lastSelectedItem.set(object);
					
					//disable autocomplete search to avoid another call to prediction controller
					this.enable=false;
					this.textField.setText(lastSelectedItem.get().toString());
					searchResult.clear();
					entriesPopup.hide();
				});
				menuItems.add(item);
			}
			// clear the popup menu and set the new built items
			entriesPopup.getItems().clear();
			entriesPopup.getItems().addAll(menuItems);
		}
		public ObservableList<S> getFilteredEntries() {
			return filteredEntries;
		}
		public void setFilteredEntries(ObservableList<S> filteredEntries) {
			this.filteredEntries = filteredEntries;
		}
		public boolean isPopupHidden() {
			return popupHidden;
		}
		public void setPopupHidden(boolean popupHidden) {
			this.popupHidden = popupHidden;
		}
		public int getMaxEntries() {
			return maxEntries;
		}
		public void setMaxEntries(int maxEntries) {
			this.maxEntries = maxEntries;
		}
		public ObjectProperty<S> getLastSelectedItem() {
			return lastSelectedItem;
		}
		public List<S> getEntries() {
			return entries;
		}
		protected int getCharacterLowerBound() {
			return this.characterLowerBound;
		}
		protected void setCharacterLowerBound(int lowerBound) {
			this.characterLowerBound=lowerBound;
		}
		public TextField getTextField() {
			return this.textField;
		}
		public boolean isBlocked() {
			return blocked;
		}
		public void setBlocked(boolean val) {
			this.blocked=val;
		}
		protected void setPos(Side pos) {
			this.pos=pos;
		}
}

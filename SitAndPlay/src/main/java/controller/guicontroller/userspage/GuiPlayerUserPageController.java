package main.java.controller.guicontroller.userspage;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.java.controller.applicationcontroller.reserveaseat.table.ReserveTableSeatController;
import main.java.controller.applicationcontroller.reserveaseat.tournament.ReserveTournamentSeatController;
import main.java.controller.applicationcontroller.userspage.StatisticsController;
import main.java.controller.guicontroller.GuiBasicInternalPageController;
import main.java.engineering.bean.createtable.TableBean;
import main.java.engineering.bean.tournaments.TournamentBean;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.utils.Session;
import main.java.view.listview.BeanContainer;
import main.java.view.listview.ListElementFactory;
import main.java.view.listview.ListElementType;

public class GuiPlayerUserPageController extends GuiBasicInternalPageController {

	public GuiPlayerUserPageController(Session ssn) {
		super(ssn);
	}

	@FXML
	private ToggleButton btnProfile;

	@FXML
	private ToggleButton btnSeats;

	@FXML
	private AnchorPane apnProfile;

	@FXML
	private Label lblUsername;

	@FXML
	private AnchorPane apnSeats;

	@FXML
	private VBox vboxTables;

	@FXML
	private VBox vboxTournaments;
	
	@FXML
    private Label lblTablesJoined;

    @FXML
    private Label lblTablesWon;

    @FXML
    private Label lblTablesPerc;

    @FXML
    private Label lblTournamentsJoined;

    @FXML
    private Label lblTournamentsWon;

    @FXML
    private Label lblTournamentsPerc;

    @FXML
    private Label lblOrgTables;
    
    @FXML
    private Label lblTotMoney;
    
    private static final String JOINED_TABLES_STRING = "Joined tables : %d";
    private static final String WON_TABLES_STRING = "Won tables : %d";
    private static final String TABLES_PERCENTAGE_STRING = "You have won the %.2f %% of the joined tables";
    private static final String JOINED_TOURNAMENTS_STRING = "Joined tournaments : %d";
    private static final String WON_TOURNAMENTS_STRING = "Won tournaments : %d";
    private static final String TOURNAMENTS_PERCENTAGE_STRING = "You have won the %.2f %% of the joined tournaments";
    private static final String ORGANIZED_TABLES_STRING = "You have organized %d tables";
    private static final String TOT_MONEY_STRING = "With the tournaments you have won %.2f €";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		var tgGroup = new ToggleGroup();
		btnProfile.setToggleGroup(tgGroup);
		btnSeats.setToggleGroup(tgGroup);
		// avoid unselected button
		tgGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
			if (newVal == null) {
				oldVal.setSelected(true);
			}
		});
		btnProfile.fire();
	}

	@FXML
	public void profilePressed(ActionEvent event) {
		apnSeats.toBack();
		lblUsername.setText(ssn.getUser().getUsername());
		loadStats();
		apnProfile.toFront();
	}

	private void loadStats() {
		var controller = new StatisticsController();
		try {
			var stats = controller.getStats(ssn.getUser());
			lblTablesJoined.setText(String.format(JOINED_TABLES_STRING, stats.getJoinedTables()));
			lblTablesWon.setText(String.format(WON_TABLES_STRING, stats.getWonTables()));
			lblTablesPerc.setText(String.format(TABLES_PERCENTAGE_STRING, stats.getTablesWinningPercentage()));
			lblTournamentsJoined.setText(String.format(JOINED_TOURNAMENTS_STRING, stats.getJoinedTournaments()));
			lblTournamentsWon.setText(String.format(WON_TOURNAMENTS_STRING, stats.getWonTournaments()));
			lblTournamentsPerc.setText(String.format(TOURNAMENTS_PERCENTAGE_STRING, stats.getTournamentWinningPercentage()));
			lblTotMoney.setText(String.format(TOT_MONEY_STRING, stats.getTotalMoney()));
			lblOrgTables.setText(String.format(ORGANIZED_TABLES_STRING, stats.getOrgTables()));
		} catch (DAOException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}
	}

	@FXML
	public void displayReservedSeats(ActionEvent event) {
		apnProfile.toBack();
		apnSeats.toFront();

		loadTables();
		loadTournaments();
	}

	private void loadTournaments() {
		var ctrl = new ReserveTournamentSeatController();
		try {
			var beans = ctrl.retrieveActiveJoinedTournaments(ssn.getUser());
			vboxTournaments.getChildren().clear();
			for (TournamentBean bean : beans) {
				var beanContainer = new BeanContainer(bean, ssn.getUser());
				ListElementFactory.getInstance().createElement(ListElementType.OPEN_TOURNAMENT,
						vboxTournaments, beanContainer);
			}
		} catch (DAOException | DateParsingException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}

	}

	private void loadTables() {
		var controller = new ReserveTableSeatController();
		try {
			vboxTables.getChildren().clear();
			var tableBeans = controller.retrieveActiveJoinedTables(ssn.getUser());
			for(TableBean bean : tableBeans) {
				var beanContainer = new BeanContainer(bean, ssn.getUser());
				ListElementFactory.getInstance().createElement(ListElementType.OPEN_TABLE, vboxTables, beanContainer);
			}
		} catch (DateParsingException | DAOException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}

	}

}

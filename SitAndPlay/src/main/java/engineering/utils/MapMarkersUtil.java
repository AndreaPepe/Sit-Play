package main.java.engineering.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.commons.io.IOUtils;
import main.java.engineering.bean.tournaments.TournamentBean;

public class MapMarkersUtil {

	private MapMarkersUtil() {
		
	}
	
	public static String buildLoadTournamentMarkersScript(TournamentBean bean) throws IOException {
		var lat = bean.getLatitude();
		var lng = bean.getLongitude();
		var sponsorBean = bean.getSponsor();
		var icon = "\"" + "star" + "\"";
		var color = "\"" + "green" + "\"";
		var idname = "\"" + bean.getName() + "\"";
		var cardGame = "\"" + bean.getCardGame() + "\"";
		var address = "\"" + bean.getAddress() + "\"";
		var date = "\"" + bean.getDate() + "\"";
		var time = "\"" + bean.getTime() + "\"";
		var organizer = "\"" + bean.getOrganizer() + "\"";
		
		var maxParticipants = "\"" + bean.getMaxParticipants() +"\"";
		var price = "\"" + String.format("€ %.2f", bean.getPrice()) + "\"";
		var award = "\"" + String.format("€ %.2f", bean.getAward()) + "\"";
		String sponsor;
		String imgType;
		String logo;
		if(sponsorBean != null) {
			sponsor ="\"" + sponsorBean.getName() + "\"";
			imgType ="\"" + "*" + "\"";
			logo = sponsorBean.getLogo()!=null ? "\"" + encodeToBase64UTF8(sponsorBean.getLogo()) + "\"" : "\"" + "null" + "\"";
			
		}else {
			sponsor = "\"" + "null" + "\"";
			imgType = "\"" + "null" + "\"";
			logo = "\"" + "null" + "\"";
		}
		
		return "addMarker("+lat+"," + lng + "," + icon
				+ "," + color + "," + idname + "," + cardGame + "," + address + "," + date + "," + time	+ "," + organizer
				+ "," + maxParticipants + "," + price + "," + award + "," + sponsor + "," + imgType + "," + logo 
				+");";
	}
	
	public static String encodeToBase64UTF8(InputStream is) throws IOException {
		byte[] byteArray = IOUtils.toByteArray(is);
		byte[] base64 = Base64.getEncoder().encode(byteArray);
		return new String(base64, StandardCharsets.UTF_8);
	}
}

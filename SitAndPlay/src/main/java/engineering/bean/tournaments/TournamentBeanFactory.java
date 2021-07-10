package main.java.engineering.bean.tournaments;

import main.java.engineering.bean.businessactivity.BusinessActivityBean;
import main.java.engineering.utils.DatetimeUtil;
import main.java.model.Tournament;

public class TournamentBeanFactory {

	private TournamentBeanFactory() {
		
	}
	
	public static TournamentBean createBean(Tournament tournament) {
		var bean = new TournamentBean();
		
		var place = tournament.getPlace();
		var pInfo = tournament.getParticipationInfo();
		var datetime = tournament.getDatetime();
		var sponsor = tournament.getSponsor();
		
		bean.setName(tournament.getName());
		bean.setAddress(place.getAddress());
		bean.setLatitude(place.getLatitude());
		bean.setLongitude(place.getLongitude());
		bean.setCardGame(tournament.getCardGame().toString());
		bean.setDate(DatetimeUtil.getDate(datetime));
		bean.setTime(DatetimeUtil.getTime(datetime));
		bean.setOrganizer(tournament.getOrganizer());
		bean.setMaxParticipants(pInfo.getMaxParticipants());
		bean.setPrice(pInfo.getPrice());
		bean.setAward(pInfo.getAward());
		bean.setInRequestForSponsor(tournament.getInRequestForSponsor());
		
		if (tournament.getWinner() != null) {
			bean.setWinner(tournament.getWinner());
		}
		
		if (sponsor != null) {
			var sponsorBean = new BusinessActivityBean(sponsor.getActivityName(), sponsor.getLogo(), sponsor.getBusinessman());
			bean.setSponsor(sponsorBean);
		}
		
		if (tournament.getParticipants() != null && !tournament.getParticipants().isEmpty()) {
			bean.setParticipants(tournament.getParticipants());
		}
		return bean;
	}
}

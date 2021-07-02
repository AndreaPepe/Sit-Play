package main.java.engineering.utils;

public enum Cities {
	TORINO("Torino"),
	AOSTA("Aosta"),
	GENOVA("Genova"),
	MILANO("Milano"),
	TRENTO("Trento"),
	TRIESTE("Trieste"),
	VENEZIA("Venezia"),
	BOLOGNA("Bologna"),
	FIRENZE("Firenze"),
	ANCONA("Ancona"),
	PERUGIA("Perugia"),
	ROMA("Roma"),
	AQUILA("L Aquila"),
	CAMPOBASSO("Campobasso"),
	NAPOLI("Napoli"),
	BARI("Bari"),
	POTENZA("Potenza"),
	CATANZARO("Catanzaro"),
	PALERMO("Palermo"),
	CAGLIARI("Cagliari");
	
	public final String nome;
	private Cities(String nome)
	{
		this.nome=nome;
	}
	
	@Override
	public String toString() {
		return this.nome;
	}
}

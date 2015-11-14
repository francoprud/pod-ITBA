package model;

public class Movie {
//	private String Title;
//	private String Year;
//	private String Rated;
//	private String Released;
//	private String Genre;
//	private String Director;
//	private String Writer;
	private String Actors;
//	private String Plot;
//	private String Language;
//	private String Country;
//	private String Metascore;
//	private String imdbRating;
	private String imdbVotes;
	private String Type;
	
	private String[] actors = null;

	public Movie(String Actors, String imbdVotes, String Type) {
//		this.Title = Title;
//		this.Year = Year;
//		this.Rated = Rated;
//		this.Released = Released;
//		this.Genre = Genre;
//		this.Director = Director;
//		this.Writer = Writer;
		this.Actors = Actors;
		this.imdbVotes = imbdVotes;
		this.Type = Type;
	}
	
	public String[] getActors() {
		if (actors == null) {
			actors = Actors.split(", ");
			Actors = null; // For GC purpose
		}
		return actors;
	}
}

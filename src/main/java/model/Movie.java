package model;

import java.io.Serializable;

public class Movie implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String NULL = "N/A";
	
	private String Title;
	private String Actors;
	private String imdbVotes;
	private String Type;
	private String Year;
	private String Metascore;
//	private String Rated;
//	private String Released;
//	private String Genre;
//	private String Director;
//	private String Writer;
//	private String Plot;
//	private String Language;
//	private String Country;
//	private String imdbRating;
	
	// Variables for "cache"
	private String[] actors;
	private Integer votes;
	private Integer year;
	private Integer metascore;

	public Movie(String Title, String Actors, String imbdVotes, String Type, String Year, String Metascore) {
		this.Title = Title;
		this.Actors = Actors;
		this.imdbVotes = imbdVotes;
		this.Type = Type;
		this.Year = Year;
		this.Metascore = Metascore;
//		this.Rated = Rated;
//		this.Released = Released;
//		this.Genre = Genre;
//		this.Director = Director;
//		this.Writer = Writer;
	}
	
	public String[] getActors() {
		if (actors == null) {
			actors = Actors.split(", ");
			Actors = null; // For GC purpose
		}
		return actors;
	}
	
	public String getTitle() {
		return Title;
	}
	
	public Integer getVotes() {
		if (votes == null) {
			votes = Integer.parseInt(imdbVotes.replace(",", ""));
		}
		return votes;
	}
	
	public String getType() {
		return Type;
	}

	public Integer getYear() {
		if (year == null) {
			year = Integer.parseInt(Year);
		}
		return year;
	}
	
	public Integer getMetascore() {
		if (metascore == null) {
			metascore = (Metascore == NULL) ? 0 : Integer.parseInt(Metascore);
		}
		return metascore;
	}
}

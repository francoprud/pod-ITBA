package model;

import java.io.Serializable;

public class Movie implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Title;
	private String Actors;
	private String imdbVotes;
	private String Type;
//	private String Year;
//	private String Rated;
//	private String Released;
//	private String Genre;
//	private String Director;
//	private String Writer;
//	private String Plot;
//	private String Language;
//	private String Country;
//	private String Metascore;
//	private String imdbRating;
	
	// Variables for "cache"
	private String[] actors = null;
	private Integer votes = null;

	public Movie(String Title, String Actors, String imbdVotes, String Type) {
		this.Title = Title;
		this.Actors = Actors;
		this.imdbVotes = imbdVotes;
		this.Type = Type;
//		this.Year = Year;
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
			imdbVotes = null;
		}
		return votes;
	}
}

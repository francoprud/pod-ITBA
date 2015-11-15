package edu.itba.pod.hazel.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Movie implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String NULL = "N/A";
	private static final String MOVIE = "movie";
	private static final String SERIES = "series";
	private static final String GAME = "game";
	
	private String Title;
	private String Actors;
	private String imdbVotes;
	private String Type;
	private String Year;
	private String Metascore;
	private String Director;
	
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
	
	public String getDirector() {
		return Director;
	}
	
	public Boolean isMovie() {
		return Type.equals(MOVIE);
	}
	
	public Boolean isSeries() {
		return Type.equals(SERIES);
	}
	
	public Boolean isGame() {
		return Type.equals(GAME);
	}
	
	public List<ActorDuet> getActorDuets() {
		int length = getActors().length;
		List<ActorDuet> actor_duets = new ArrayList<ActorDuet>();
		
		for (int i = 0; i < length; i++) {
			for (int j = i + 1; j < length; j++) {
				actor_duets.add(new ActorDuet(actors[i], actors[j]));
			}
		}
		return actor_duets;
	}
}

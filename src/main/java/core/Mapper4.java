package core;

import model.Movie;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class Mapper4 implements Mapper<String, Movie, String, String[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void map(String title, Movie movie, Context<String, String[]> context) {
		context.emit(movie.getDirector(), movie.getActors());
	}
}

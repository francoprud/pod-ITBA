package core;

import model.Movie;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class Mapper2 implements Mapper<String, Movie, Integer, Movie> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void map(String title, Movie movie, Context<Integer, Movie> context) {
		context.emit(movie.getYear(), movie);
	}
}

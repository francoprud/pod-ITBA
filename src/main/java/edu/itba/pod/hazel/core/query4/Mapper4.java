package edu.itba.pod.hazel.core.query4;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import edu.itba.pod.hazel.model.Movie;

public class Mapper4 implements Mapper<String, Movie, String, String[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void map(String title, Movie movie, Context<String, String[]> context) {
		context.emit(movie.getDirector(), movie.getActors());
	}
}

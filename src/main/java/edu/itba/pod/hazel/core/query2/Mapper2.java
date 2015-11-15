package edu.itba.pod.hazel.core.query2;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import edu.itba.pod.hazel.model.Movie;

public class Mapper2 implements Mapper<String, Movie, Integer, Movie> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void map(String title, Movie movie, Context<Integer, Movie> context) {
		context.emit(movie.getYear(), movie);
	}
}

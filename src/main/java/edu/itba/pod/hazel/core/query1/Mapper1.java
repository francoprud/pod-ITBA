package edu.itba.pod.hazel.core.query1;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import edu.itba.pod.hazel.model.Movie;

public class Mapper1 implements Mapper<String, Movie, String, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void map(String title, Movie movie, Context<String, Integer> context) {
		for (String actor: movie.getActors()) {
			context.emit(actor, movie.getVotes());
		}
	}
}

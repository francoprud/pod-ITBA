package edu.itba.pod.hazel.core;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import edu.itba.pod.hazel.model.ActorDuet;
import edu.itba.pod.hazel.model.Movie;

public class Mapper3 implements Mapper<String, Movie, ActorDuet, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void map(String title, Movie movie, Context<ActorDuet, String> context) {
		for (ActorDuet actor_duet: movie.getActorDuets()) {
			context.emit(actor_duet, movie.getTitle());
		}
	}
}

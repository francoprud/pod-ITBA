package edu.itba.pod.hazel.core.query3;

import java.util.ArrayList;
import java.util.List;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import edu.itba.pod.hazel.model.ActorDuet;

public class Reducer3 implements ReducerFactory<ActorDuet, String, List<String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Reducer<String, List<String>> newReducer(ActorDuet arg0) {
		return new Reducer<String, List<String>>() {
			List<String> movies = new ArrayList<String>();
			
			@Override
			public void reduce(String movie_title) {
				movies.add(movie_title);
			}

			@Override
			public List<String> finalizeReduce() {
				return movies;
			}
		};
	}
}

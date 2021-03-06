package edu.itba.pod.hazel.core.query2;

import java.util.ArrayList;
import java.util.List;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import edu.itba.pod.hazel.model.Movie;

public class Reducer2 implements ReducerFactory<Integer, Movie, List<String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Reducer<Movie, List<String>> newReducer(Integer arg0) {
		return new Reducer<Movie, List<String>>() {
			private List<String> movies;
			private Integer highest_metascore = 0;

			public void beginReduce() {
				movies = new ArrayList<String>();
			}

			public void reduce(Movie movie) {
				int partial_metascore = movie.getMetascore();
				
				if (partial_metascore >= highest_metascore) {
					if (partial_metascore > highest_metascore) {
						highest_metascore = partial_metascore;
						movies = new ArrayList<String>();						
					}
					movies.add(movie.getTitle());
				}
			}

			public List<String> finalizeReduce() {
				return movies;
			}
		};
	}
}

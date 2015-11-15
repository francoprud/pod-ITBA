package edu.itba.pod.hazel.core;

import java.util.concurrent.ExecutionException;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import edu.itba.pod.hazel.model.Movie;

public abstract class Query {
	private static final String MAP_NAME = "movies";
	private static final String DEFAULT = "default";
	
	private HazelcastInstance client;
	private IMap<String, Movie> map;
	private Movie[] movies;
	private Job<String, Movie> job;
	
	public Query(HazelcastInstance client, Movie[] movies) {
		this.client = client;
		this.movies = movies;
		map = client.getMap(MAP_NAME);
	}
	
	public abstract void run() throws InterruptedException, ExecutionException;

	public IMap<String, Movie> getMap() {
		return map;
	}
	
	public Movie[] getMovies() {
		return movies;
	}
	
	public Job<String, Movie> getJob() {
		if (job == null) {
			JobTracker tracker = client.getJobTracker(DEFAULT);
			KeyValueSource<String, Movie> kv_source = KeyValueSource
					.fromMap(map);
			job = tracker.newJob(kv_source);			
		}
		return job;
	}
	
	public void populateMapOnlyWithMovies() {
		for (Movie movie : movies) {
			if (movie.isMovie())
				map.put(movie.getTitle(), movie);
		}
	}
	
	public void populateMapWithOnlyMoviesAndByYears(int year) {
		for (Movie movie : movies) {
			if (movie.isMovie() && movie.getYear() > year)
				map.put(movie.getTitle(), movie);
		}
	}
}
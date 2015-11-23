package edu.itba.pod.hazel.core;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import edu.itba.pod.hazel.model.Movie;

public abstract class Query {
	private static final String DEFAULT = "default";
	
	private HazelcastInstance client;
	private IMap<String, Movie> map;
	private Movie[] movies;
	private Job<String, Movie> job;
	
	public Query(HazelcastInstance client, Movie[] movies) {
		this.client = client;
		this.movies = movies;
		// This random generates a new map name to avoid overriding of maps
		map = client.getMap(String.valueOf((int) Math.random() * 10000));
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
	
	public void printMetrics(long start_reading_file,
			long end_reading_file, long start_query_run, long end_query_run) {
		SimpleDateFormat date_parser = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSSS");
		
		System.out.println("Reading file start time: " + "\t" + date_parser.format(start_reading_file));
		System.out.println("Reading file end time: " + "\t\t" + date_parser.format(end_reading_file));
		System.out.println("Estimated time for reading the file: " + (end_reading_file - start_reading_file) + " ms");
		System.out.println("MapReduce start time: " + "\t\t" + date_parser.format(start_query_run));
		System.out.println("MapReduce end time: " + "\t\t" + date_parser.format(end_query_run));
		System.out.println("Estimated time for MapReduce: " + (end_query_run - start_query_run) + " ms");
	}
}
package edu.itba.pod.hazel.core;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import edu.itba.pod.hazel.model.Movie;

public abstract class Query {
	private static final String NAME = "group";
	private static final String PASSWORD = "group";
	private static final String DEFAULT = "default";

	private HazelcastInstance client;
	private IMap<String, Movie> map;
	private Movie[] movies;
	private Job<String, Movie> job;

	// Variables for metrics purpose
	private long distributionStartTime;
	private long distributionEndTime;
	private long mapReduceStartTime;
	private long mapReduceEndTime;

	public Query(Movie[] movies) {
		this.movies = movies;
		this.client = setHazlecastConfiguration(NAME, PASSWORD);
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
		setDistributionStartTime();
		for (Movie movie : movies) {
			if (movie.isMovie())
				map.put(movie.getTitle(), movie);
		}
		setDistributionEndTime();
	}

	public void populateMapWithOnlyMoviesAndByYears(int year) {
		setDistributionStartTime();
		for (Movie movie : movies) {
			if (movie.isMovie() && movie.getYear() > year)
				map.put(movie.getTitle(), movie);
		}
		setDistributionEndTime();
	}

	public void setDistributionStartTime() {
		this.distributionStartTime = System.currentTimeMillis();
	}

	public void setDistributionEndTime() {
		this.distributionEndTime = System.currentTimeMillis();
	}

	public void setMapReduceStartTime() {
		this.mapReduceStartTime = System.currentTimeMillis();
	}

	public void setMapReduceEndTime() {
		this.mapReduceEndTime = System.currentTimeMillis();
	}

	public void printMetrics() {
		SimpleDateFormat date_parser = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss:SSSS");

		System.out.println("Reading file start time: " + "\t"
				+ date_parser.format(distributionStartTime));
		System.out.println("Reading file end time: " + "\t\t"
				+ date_parser.format(distributionEndTime));
		System.out.println("Estimated time for reading the file: "
				+ (distributionEndTime - distributionStartTime) + " ms");
		System.out.println("MapReduce start time: " + "\t\t"
				+ date_parser.format(mapReduceStartTime));
		System.out.println("MapReduce end time: " + "\t\t"
				+ date_parser.format(mapReduceEndTime));
		System.out.println("Estimated time for MapReduce: "
				+ (mapReduceEndTime - mapReduceStartTime) + " ms");
	}

	private static HazelcastInstance setHazlecastConfiguration(String name,
			String password) {
		ClientConfig ccfg = new ClientConfig();
		ccfg.getGroupConfig().setName(name).setPassword(password);
		return HazelcastClient.newHazelcastClient(ccfg);
	}
}
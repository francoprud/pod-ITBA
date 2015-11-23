package edu.itba.pod.hazel.core.query4;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;

import edu.itba.pod.hazel.core.Query;
import edu.itba.pod.hazel.model.Movie;

public class Query4 extends Query {

	public Query4(HazelcastInstance client, Movie[] movies) {
		super(client, movies);
	}

	@Override
	public void run() throws InterruptedException, ExecutionException {
		long start_reading_file = System.currentTimeMillis(); 		// Metrics Purpose
		populateMapOnlyWithMovies();
		long end_reading_file = System.currentTimeMillis();			// Metrics Purpose

		long start_query_run = System.currentTimeMillis();			// Metrics Purpose
		ICompletableFuture<Map<String, List<String>>> comp_future = getJob()
				.mapper(new Mapper4()).reducer(new Reducer4()).submit();
		Set<Entry<String, List<String>>> set = comp_future.get().entrySet();
		
		printAnser(set);
		long end_query_run = System.currentTimeMillis();			// Metrics Purpose
		
		printMetrics(start_reading_file, end_reading_file, start_query_run, end_query_run);
	}

	private void printAnser(Set<Entry<String, List<String>>> set) {
		for (Entry<String, List<String>> entry : set) {
			System.out.println("Director: " + entry.getKey());
			for (String actor : entry.getValue()) {
				System.out.println("\t" + actor);
			}
		}
	}
}
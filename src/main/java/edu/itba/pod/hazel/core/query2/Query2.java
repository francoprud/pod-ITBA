package edu.itba.pod.hazel.core.query2;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;

import edu.itba.pod.hazel.core.Query;
import edu.itba.pod.hazel.model.Movie;

public class Query2 extends Query {

	private static final String TOPE = "tope";
	
	private int min_year;

	public Query2(HazelcastInstance client, Movie[] movies)
			throws NumberFormatException {
		super(client, movies);
		min_year = Integer.parseInt(System.getProperty(TOPE));
	}

	@Override
	public void run() throws InterruptedException, ExecutionException {
		populateMapWithOnlyMoviesAndByYears(min_year);

		ICompletableFuture<Map<Integer, List<String>>> comp_future = getJob()
				.mapper(new Mapper2()).reducer(new Reducer2()).submit();
		Set<Entry<Integer, List<String>>> set = comp_future.get().entrySet();

		printAnswer(set);
	}

	public void printAnswer(Set<Entry<Integer, List<String>>> set) {
		for (Entry<Integer, List<String>> entry : set) {
			System.out.println("Year: " + entry.getKey());
			for (String movie : entry.getValue()) {
				System.out.println("\t" + movie);
			}
		}
	}
}
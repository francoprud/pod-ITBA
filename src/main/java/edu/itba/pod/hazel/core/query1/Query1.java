package edu.itba.pod.hazel.core.query1;

import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.hazelcast.core.ICompletableFuture;

import edu.itba.pod.hazel.core.Query;
import edu.itba.pod.hazel.model.Movie;
import edu.itba.pod.hazel.utils.ActorVotesEntryComparator;

public class Query1 extends Query {

	private static final String N = "N";

	private int number;

	public Query1(Movie[] movies) throws NumberFormatException {
		super(movies);
		number = Integer.parseInt(System.getProperty(N));
	}

	@Override
	public void run() throws InterruptedException, ExecutionException {
		populateMapOnlyWithMovies();

		setMapReduceStartTime();
		ICompletableFuture<Map<String, Integer>> comp_future = getJob()
				.mapper(new Mapper1()).reducer(new Reducer1()).submit();
		Set<Entry<String, Integer>> set = comp_future.get().entrySet();

		printAnswer(set, number);
		setMapReduceEndTime();

		printMetrics();
	}

	private void printAnswer(Set<Entry<String, Integer>> set, int number) {
		PriorityQueue<Entry<String, Integer>> pq = fetchFirstNActors(set);
		for (int i = 0; i < number; i++) {
			System.out.println(pq.poll());
		}
	}

	private static PriorityQueue<Entry<String, Integer>> fetchFirstNActors(
			Set<Entry<String, Integer>> set) {
		PriorityQueue<Entry<String, Integer>> pq = new PriorityQueue<Entry<String, Integer>>(
				new ActorVotesEntryComparator());
		pq.addAll(set);
		return pq;
	}
}
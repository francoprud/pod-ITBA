package edu.itba.pod.hazel.core.query3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.hazelcast.core.ICompletableFuture;

import edu.itba.pod.hazel.core.Query;
import edu.itba.pod.hazel.model.ActorDuet;
import edu.itba.pod.hazel.model.Movie;

public class Query3 extends Query {

	public Query3(Movie[] movies) {
		super(movies);
	}

	@Override
	public void run() throws InterruptedException, ExecutionException {
		populateMapOnlyWithMovies();

		setMapReduceStartTime();
		ICompletableFuture<Map<ActorDuet, List<String>>> comp_future = getJob()
				.mapper(new Mapper3()).reducer(new Reducer3()).submit();
		Set<Entry<ActorDuet, List<String>>> set = comp_future.get().entrySet();

		printAnswer(set);
		setMapReduceEndTime();

		printMetrics();
	}

	public void printAnswer(Set<Entry<ActorDuet, List<String>>> set) {
		List<Entry<ActorDuet, List<String>>> actor_duets = fetchActorDuets(set);
		for (Entry<ActorDuet, List<String>> entry : actor_duets) {
			System.out.println("Actors Duet: " + entry.getKey().toString());
			for (String movie_title : entry.getValue()) {
				System.out.println("\t" + movie_title);
			}
		}
	}

	private static List<Entry<ActorDuet, List<String>>> fetchActorDuets(
			Set<Entry<ActorDuet, List<String>>> entries) {
		List<Entry<ActorDuet, List<String>>> ans = null;
		int most_performances = 0;

		for (Entry<ActorDuet, List<String>> entry : entries) {
			int performances = entry.getValue().size();
			if (performances >= most_performances) {
				if (performances > most_performances) {
					ans = new ArrayList<Map.Entry<ActorDuet, List<String>>>();
					most_performances = performances;
				}
				ans.add(entry);
			}
		}
		return ans;
	}
}

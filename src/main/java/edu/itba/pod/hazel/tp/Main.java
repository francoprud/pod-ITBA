package edu.itba.pod.hazel.tp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import edu.itba.pod.hazel.core.Mapper1;
import edu.itba.pod.hazel.core.Mapper2;
import edu.itba.pod.hazel.core.Mapper3;
import edu.itba.pod.hazel.core.Mapper4;
import edu.itba.pod.hazel.core.Reducer1;
import edu.itba.pod.hazel.core.Reducer2;
import edu.itba.pod.hazel.core.Reducer3;
import edu.itba.pod.hazel.core.Reducer4;
import edu.itba.pod.hazel.model.ActorDuet;
import edu.itba.pod.hazel.model.Movie;
import edu.itba.pod.hazel.parser.Parser;
import edu.itba.pod.hazel.utils.ActorVotesEntryComparator;

public class Main {
	private static final String NAME = "group";
	private static final String PASSWORD = "group";
	private static final String MAP_NAME = "movies";
	private static final String DEFAULT = "default";

	public static void main(String[] args) throws IOException,
			InterruptedException, ExecutionException {
		HazelcastInstance client = setHazlecastConfiguration(NAME, PASSWORD);

		try {
			String path = System.getProperty("path");
			Movie[] movies = new Parser(path).parseMovieJson();

			IMap<String, Movie> map = client.getMap(MAP_NAME);

			int query = Integer.parseInt(System.getProperty("query"));

			switch (query) {
			case 1:
				int number = Integer.parseInt(System.getProperty("N"));

				populateMapOnlyWithMovies(map, movies);
				JobTracker tracker = client.getJobTracker(DEFAULT);
				KeyValueSource<String, Movie> kv_source = KeyValueSource
						.fromMap(map);
				Job<String, Movie> job = tracker.newJob(kv_source);

				ICompletableFuture<Map<String, Integer>> comp_future = job
						.mapper(new Mapper1()).reducer(new Reducer1()).submit();
				Set<Entry<String, Integer>> reduced_set = comp_future.get()
						.entrySet();

				PriorityQueue<Entry<String, Integer>> pq = fetchFirstNActors(reduced_set);

				for (int i = 0; i < number; i++) {
					System.out.println(pq.poll());
				}
				break;
			case 2:
				int tope = Integer.parseInt(System.getProperty("tope"));
				populateMapWithMoviesAndYears(map, movies, tope);

				JobTracker tracker2 = client.getJobTracker(DEFAULT);
				KeyValueSource<String, Movie> kv_source2 = KeyValueSource
						.fromMap(map);
				Job<String, Movie> job2 = tracker2.newJob(kv_source2);

				ICompletableFuture<Map<Integer, List<String>>> comp_future2 = job2
						.mapper(new Mapper2()).reducer(new Reducer2()).submit();
				Set<Entry<Integer, List<String>>> reduced_set2 = comp_future2
						.get().entrySet();
				
				for (Entry<Integer, List<String>> entry: reduced_set2) {
					System.out.println("Year: " + entry.getKey());
					for (String movie: entry.getValue()) {
						System.out.println("\t" + movie);
					}
				}
				break;
			case 3:
				populateMapOnlyWithMovies(map, movies);
				
				JobTracker tracker3 = client.getJobTracker(DEFAULT);
				KeyValueSource<String, Movie> kv_source3 = KeyValueSource
						.fromMap(map);
				Job<String, Movie> job3 = tracker3.newJob(kv_source3);
				
				ICompletableFuture<Map<ActorDuet, List<String>>> comp_future3 = job3
						.mapper(new Mapper3()).reducer(new Reducer3()).submit();
				Set<Entry<ActorDuet, List<String>>> reduced_set3 = comp_future3
						.get().entrySet();
				
				List<Entry<ActorDuet, List<String>>> actor_duets = fetchActorDuets(reduced_set3);
				for (Entry<ActorDuet, List<String>> entry: actor_duets) {
					System.out.println("Actors Duet: " + entry.getKey().toString());
					for (String movie_title: entry.getValue()) {
						System.out.println("\t" + movie_title);
					}
				}
				break;
			case 4:
				populateMapOnlyWithMovies(map, movies);
				
				JobTracker tracker4 = client.getJobTracker(DEFAULT);
				KeyValueSource<String, Movie> kv_source4 = KeyValueSource
						.fromMap(map);
				Job<String, Movie> job4 = tracker4.newJob(kv_source4);

				ICompletableFuture<Map<String, List<String>>> comp_future4 = job4
						.mapper(new Mapper4()).reducer(new Reducer4()).submit();
				Set<Entry<String, List<String>>> reduced_set4 = comp_future4
						.get().entrySet();
				
				for (Entry<String, List<String>> entry: reduced_set4) {
					System.out.println("Director: " + entry.getKey());
					for (String actor: entry.getValue()) {
						System.out.println("\t" + actor);
					}
				}
				break;
			}
		} catch (NumberFormatException e) {
			System.out.println("The query number do not exist.");
		} catch (IOException e) {
			System.out.println("The specified path is not valid.");
		}
	}

	private static void populateMapWithMoviesAndYears(IMap<String, Movie> map,
			Movie[] movies, int year) {
		for (Movie movie : movies) {
			if (movie.isMovie() && movie.getYear() > year)
				map.put(movie.getTitle(), movie);
		}
	}

	private static HazelcastInstance setHazlecastConfiguration(String name,
			String password) {
		ClientConfig ccfg = new ClientConfig();
		ccfg.getGroupConfig().setName(name).setPassword(password);
		return HazelcastClient.newHazelcastClient(ccfg);
	}

	private static void populateMapOnlyWithMovies(IMap<String, Movie> map,
			Movie[] movies) {
		for (Movie movie : movies) {
			if (movie.isMovie())
				map.put(movie.getTitle(), movie);
		}
	}

	private static PriorityQueue<Entry<String, Integer>> fetchFirstNActors(
			Set<Entry<String, Integer>> reduced_set) {
		PriorityQueue<Entry<String, Integer>> pq = new PriorityQueue<Entry<String, Integer>>(
				new ActorVotesEntryComparator());
		pq.addAll(reduced_set);
		return pq;
	}
	
	private static List<Entry<ActorDuet, List<String>>> fetchActorDuets(Set<Entry<ActorDuet, List<String>>> entries) {
		List<Entry<ActorDuet, List<String>>> ans = null;
		int most_performances = 0;
		
		for (Entry<ActorDuet, List<String>> entry: entries) {
			int performances = entry.getValue().size();
			if (performances >= most_performances) {
				if (performances > most_performances) {					
					ans = new ArrayList<Map.Entry<ActorDuet,List<String>>>();
					most_performances = performances;
				}
				ans.add(entry);
			}
		}
		return ans;
	}
}
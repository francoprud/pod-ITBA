package main;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import model.Movie;
import parser.Parser;
import utils.ActorVotesEntryComparator;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import core.Mapper1;
import core.Reducer1;

public class Main {
	private static final String PATH = "/home/prudi/Desktop/imdb-40.json";
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
			
			switch(query) {
			case 1:
				int number = Integer.parseInt(System.getProperty("N"));
				
				populateMapOnlyWithMovies(map, movies);
				JobTracker tracker = client.getJobTracker(DEFAULT);
				KeyValueSource<String, Movie> kv_source = KeyValueSource.fromMap(map);
				Job<String, Movie> job = tracker.newJob(kv_source);

				ICompletableFuture<Map<String, Integer>> comp_future1 = job
						.mapper(new Mapper1()).reducer(new Reducer1()).submit();
				Set<Entry<String, Integer>> reduced_set = comp_future1.get().entrySet();
				
				PriorityQueue<Entry<String, Integer>> pq = fetchFirstNActors(reduced_set);
				
				for (int i = 0; i < number; i++) {
					System.out.println(pq.poll());
				}
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			}
		} catch(NumberFormatException e) {
			System.out.println("The query number do not exist.");
		} catch(IOException e) {
			System.out.println("The specified path is not valid.");
		}
		
		// QUERY 1
		
		

		// QUERY 2
//		IMap<String, Movie> map2 = client.getMap(MAP_NAME);
//		populateMapWithMoviesAndYears(map1, movies, 1994);
//		
//		JobTracker tracker2 = client.getJobTracker(DEFAULT);
//		KeyValueSource<String, Movie> kv_source2 = KeyValueSource.fromMap(map2);
//		Job<String, Movie> job2 = tracker2.newJob(kv_source2);
//		
//		ICompletableFuture<Map<String, Integer>> comp_future2 = job2
//				.mapper(new Mapper2()).reducer(new Reducer2()).submit();
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
}
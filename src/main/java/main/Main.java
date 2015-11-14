package main;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import model.Movie;

import org.apache.commons.io.FileUtils;

import utils.ActorVotesEntryComparator;

import com.google.gson.Gson;
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
	private static final String NAME = "comandante";
	private static final String PASSWORD = "1234";
	private static final String MAP_NAME = "movies";
	private static final String DEFAULT = "default";
	private static final Integer NUMBER = 5;

	public static void main(String[] args) throws IOException,
			InterruptedException, ExecutionException {
		HazelcastInstance client = setHazlecastConfiguration(NAME, PASSWORD);
		// Parser
		String json_string = FileUtils.readFileToString(new File(PATH));
		Gson gson = new Gson();
		Movie[] movies = gson.fromJson(json_string, Movie[].class);

		IMap<String, Movie> map = client.getMap(MAP_NAME);
		populateMapWithMovies(map, movies);

		JobTracker tracker = client.getJobTracker(DEFAULT);
		KeyValueSource<String, Movie> kv_source = KeyValueSource.fromMap(map);
		Job<String, Movie> job = tracker.newJob(kv_source);

		ICompletableFuture<Map<String, Integer>> comp_future = job
				.mapper(new Mapper1()).reducer(new Reducer1()).submit();
		Set<Entry<String, Integer>> set_reduced = comp_future.get().entrySet();

		PriorityQueue<Entry<String, Integer>> pq = fetchFirstNActors(set_reduced);

		for (int i = 0; i < NUMBER; i++) {
			System.out.println(pq.poll());
		}
	}

	private static HazelcastInstance setHazlecastConfiguration(String name,
			String password) {
		ClientConfig ccfg = new ClientConfig();
		ccfg.getGroupConfig().setName(name).setPassword(password);
		return HazelcastClient.newHazelcastClient(ccfg);
	}

	private static void populateMapWithMovies(IMap<String, Movie> map,
			Movie[] movies) {
		for (Movie movie : movies) {
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
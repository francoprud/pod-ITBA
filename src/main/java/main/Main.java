package main;

import java.io.File;
import java.io.IOException;

import model.Movie;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.mapreduce.Job;

public class Main {
	private static final String PATH = "/home/prudi/Desktop/imdb-40.json";
	private static final String NAME = "comandante";
	private static final String PASSWORD = "1234";
	private static Job<String, Movie> job;
	
	public static void main(String[] args) throws IOException {
		HazelcastInstance client = setHazlecastConfiguration(NAME, PASSWORD);
		String json_string = FileUtils.readFileToString(new File(PATH));
		Gson gson = new Gson();
		Movie[] movies = gson.fromJson(json_string, Movie[].class);
		
	}
	
	private static HazelcastInstance setHazlecastConfiguration(String name, String password) {
		Config ccfg = new Config();
		ccfg.getGroupConfig().setName(name).setPassword(password);
		return Hazelcast.newHazelcastInstance(ccfg);
	}
}
//ClientConfig ccfg = new ClientConfig();
//IMap<String, Movie> myMap = client.getMap(MAP_NAME);
//KeyValueSource<String, Movie> source = KeyValueSource.fromMap(myMap);
//job = tracker.newJob(source);

//IMap<String, String> map = hazelcastInstance.getMap( "articles" );
//KeyValueSource<String, String> source = KeyValueSource.fromMap( map );
//Job<String, String> job = jobTracker.newJob( source );
package edu.itba.pod.hazel.tp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import edu.itba.pod.hazel.core.query1.Query1;
import edu.itba.pod.hazel.core.query2.Query2;
import edu.itba.pod.hazel.core.query3.Query3;
import edu.itba.pod.hazel.core.query4.Query4;
import edu.itba.pod.hazel.model.Movie;
import edu.itba.pod.hazel.parser.Parser;

public class Main {
	private static final String NAME = "group";
	private static final String PASSWORD = "group";

	public static void main(String[] args) throws IOException,
			InterruptedException, ExecutionException {
		long start_reading_file = 0;
		long end_reading_file = 0;
		long start_query_run = 0;
		long end_query_run = 0;
		
		HazelcastInstance client = setHazlecastConfiguration(NAME, PASSWORD);

		try {
			String path = System.getProperty("path");
			
			start_reading_file = System.currentTimeMillis(); 		// Metrics Purpose
			Movie[] movies = new Parser(path).parseMovieJson();
			end_reading_file = System.currentTimeMillis();			// Metrics Purpose
			
			int query = Integer.parseInt(System.getProperty("query"));
			
			start_query_run = System.currentTimeMillis();			// Metrics Purpose
			switch (query) {
			case 1:
				new Query1(client, movies).run();
				break;
			case 2:
				new Query2(client, movies).run();
				break;
			case 3:
				new Query3(client, movies).run();
				break;
			case 4:
				new Query4(client, movies).run();
				break;
			default:
				System.out.println("The query number is not valid.");
			}
		} catch (InterruptedException|ExecutionException e) {
			System.out.println("Internal server error.");
		} catch (NumberFormatException e) {
			System.out.println("The arguments are not valid.");
		} catch (IOException e) {
			System.out.println("The specified path is not valid.");
		}
		end_query_run = System.currentTimeMillis();					// Metrics Purpose
		
		printMetrics(start_reading_file, end_reading_file, start_query_run, end_query_run);
	}

	private static void printMetrics(long start_reading_file,
			long end_reading_file, long start_query_run, long end_query_run) {
		SimpleDateFormat date_parser = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSSS");
		
		System.out.println("Reading file start time: " + "\t" + date_parser.format(start_reading_file));
		System.out.println("Reading file end time: " + "\t" + date_parser.format(end_reading_file));
		System.out.println("MapReduce start time: " + "\t" + date_parser.format(start_query_run));
		System.out.println("MapReduce end time: " + "\t" + date_parser.format(end_query_run));
	}

	private static HazelcastInstance setHazlecastConfiguration(String name,
			String password) {
		ClientConfig ccfg = new ClientConfig();
		ccfg.getGroupConfig().setName(name).setPassword(password);
		return HazelcastClient.newHazelcastClient(ccfg);
	}
}
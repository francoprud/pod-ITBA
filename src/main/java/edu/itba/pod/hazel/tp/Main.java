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
	private static final String PATH = "path";
	private static final String QUERY = "query";

	public static void main(String[] args) throws IOException,
			InterruptedException, ExecutionException {
		long start_query_run = 0;
		long end_query_run = 0;
		
		HazelcastInstance client = setHazlecastConfiguration(NAME, PASSWORD);

		try {
			String path = System.getProperty(PATH);
			
			Movie[] movies = new Parser(path).parseMovieJson();
			
			int query = Integer.parseInt(System.getProperty(QUERY));
			
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
		} catch (NumberFormatException e) {
			System.out.println("The arguments are not valid.");
		} catch (IOException e) {
			System.out.println("The specified path is not valid.");
		} catch (InterruptedException e) {
			System.out.println("Internal server error.");
		} catch (ExecutionException e) {
			System.out.println("Internal server error.");
		}
	}

	private static HazelcastInstance setHazlecastConfiguration(String name,
			String password) {
		ClientConfig ccfg = new ClientConfig();
		ccfg.getGroupConfig().setName(name).setPassword(password);
		return HazelcastClient.newHazelcastClient(ccfg);
	}
}
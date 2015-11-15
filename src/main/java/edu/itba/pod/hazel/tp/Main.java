package edu.itba.pod.hazel.tp;

import java.io.IOException;
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
		HazelcastInstance client = setHazlecastConfiguration(NAME, PASSWORD);

		try {
			String path = System.getProperty("path");
			Movie[] movies = new Parser(path).parseMovieJson();

			int query = Integer.parseInt(System.getProperty("query"));

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
	}

	private static HazelcastInstance setHazlecastConfiguration(String name,
			String password) {
		ClientConfig ccfg = new ClientConfig();
		ccfg.getGroupConfig().setName(name).setPassword(password);
		return HazelcastClient.newHazelcastClient(ccfg);
	}
}
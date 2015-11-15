package edu.itba.pod.hazel.core;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import edu.itba.pod.hazel.model.Movie;

public class Mapper3 implements Mapper<String, Movie, String, Movie> {

	public void map(String arg0, Movie arg1, Context<String, Movie> arg2) {
		// TODO Auto-generated method stub
		
	}

}

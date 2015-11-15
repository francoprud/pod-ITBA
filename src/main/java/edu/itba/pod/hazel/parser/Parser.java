package edu.itba.pod.hazel.parser;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import edu.itba.pod.hazel.model.Movie;

public class Parser {
	private String path;
	
	public Parser(String path) {
		this.path = path;
	}
	
	public Movie[] parseMovieJson() throws IOException {
		String json = FileUtils.readFileToString(new File(path));
		Gson gson = new Gson();
		return gson.fromJson(json, Movie[].class);
	}
}
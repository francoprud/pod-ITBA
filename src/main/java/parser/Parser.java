package parser;

import java.io.File;
import java.io.IOException;

import model.Movie;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

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
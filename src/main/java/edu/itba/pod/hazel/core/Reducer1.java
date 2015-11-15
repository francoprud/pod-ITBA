package edu.itba.pod.hazel.core;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class Reducer1 implements ReducerFactory<String, Integer, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Reducer<Integer, Integer> newReducer(String arg0) {
		return new Reducer<Integer, Integer>() {
			private Integer total_votes;

			public void beginReduce() {
				total_votes = 0;
			}

			public void reduce(Integer votes) {
				total_votes += votes;
			}

			public Integer finalizeReduce() {
				return total_votes;
			}
		};
	}

}

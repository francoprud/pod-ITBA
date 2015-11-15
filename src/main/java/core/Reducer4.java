package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class Reducer4 implements ReducerFactory<String, String[], List<String>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Reducer<String[], List<String>> newReducer(String arg0) {
		return new Reducer<String[], List<String>>() {
			Map<String, Integer> performances;
			
			public void beginReduce() {
				performances = new HashMap<String, Integer>();
			}

			public void reduce(String[] actors) {
				for (String actor: actors) {
					if (performances.containsKey(actor)) {
						performances.put(actor, performances.get(actor) + 1);						
					} else {
						performances.put(actor, 1);						
					}
				}
			}

			public List<String> finalizeReduce() {
				List<String> actors = new ArrayList<String>();
				int more_performances = 0;

				for (String actor : performances.keySet()) {
					int apprearences = performances.get(actor);
					if (apprearences >= more_performances) {
						if (apprearences > more_performances) {
							actors = new ArrayList<String>();
						}
						actors.add(actor);
					}
				}
				return actors;
			}
		};
	}

}

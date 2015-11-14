package utils;

import java.util.Comparator;
import java.util.Map.Entry;

public class ActorVotesEntryComparator implements Comparator<Entry<String, Integer>> {

	public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
		return o2.getValue() - o1.getValue();
	}
}

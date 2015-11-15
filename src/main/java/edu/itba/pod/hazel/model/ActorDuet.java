package edu.itba.pod.hazel.model;

import java.io.Serializable;

public class ActorDuet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String actor1;
	private String actor2;

	public ActorDuet(String actor1, String actor2) {
		this.actor1 = actor1;
		this.actor2 = actor2;
	}

	public String getActor1() {
		return actor1;
	}

	public String getActor2() {
		return actor2;
	}
	
	public String toString() {
		return actor1 + " - " + actor2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actor1 == null) ? 0 : actor1.hashCode());
		result = prime * result + ((actor2 == null) ? 0 : actor2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActorDuet other = (ActorDuet) obj;
		if (actor1 == null) {
			if (other.actor1 != null)
				return false;
		} else if (!actor1.equals(other.actor1))
			return false;
		if (actor2 == null) {
			if (other.actor2 != null)
				return false;
		} else if (!actor2.equals(other.actor2))
			return false;
		return true;
	}
}

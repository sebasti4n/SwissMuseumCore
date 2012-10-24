package ch.sebastienzurfluh.swissmuseum.core.client.patterns;

/**
 * Parce que j'emmerde Java.
 *
 * @author Sebastien Zurfluh
 *
 * @param <T>
 */
public class MutablePrimitiveReference<T> {
	public MutablePrimitiveReference(T value) {
		this.value = value;
	}

	private T value;

	public T getValue() {
		return value;	
	}

	public void setValue(T value) {
		this.value = value;	
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MutablePrimitiveReference)
			return super.equals(obj);
		return getValue().equals(obj);
	}
	
	@Override
	public String toString() {
		return getValue().toString();
	}
}
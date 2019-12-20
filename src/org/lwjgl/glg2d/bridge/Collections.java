package org.lwjgl.glg2d.bridge;

public class Collections {

	/** When true, {@link Iterable#iterator()} for {@link Array}, and other collections will allocate a new
	 * iterator for each invocation. When false, the iterator is reused and nested use will throw an exception. Default is
	 * false. */
	public static boolean allocateIterators;

}

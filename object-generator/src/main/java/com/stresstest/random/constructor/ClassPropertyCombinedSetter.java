package com.stresstest.random.constructor;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

/**
 * Combination of {@link ClassPropertySetter} for specified {@link Class}.
 * 
 * @author Anton Oparin
 * 
 * @param <T>
 *            parameterized {@link Class}.
 */
final class ClassPropertyCombinedSetter<T> extends ClassPropertySetter<T> {

	/**
	 * Source {@link Colleciton} of {@link ClassPropertySetter}.
	 */
	final private Collection<ClassPropertySetter<?>> propertySetters;

	/**
	 * Default constructor
	 * 
	 * @param propertySetters
	 *            source {@link Collection} of {@link ClassPropertySetter}.
	 */
	public ClassPropertyCombinedSetter(final Collection<ClassPropertySetter<?>> propertySetters) {
		this.propertySetters = ImmutableList.<ClassPropertySetter<?>> copyOf(propertySetters);
	}

	@Override
	public void setProperties(Object generatedObject) {
		// Step 1. Call set methods for collections
		for (ClassPropertySetter<?> propertySetter : propertySetters) {
			propertySetter.setProperties(generatedObject);
		}
	}

	@Override
	protected Class<?> getAffectedClass() {
		throw new UnsupportedOperationException();
	}

}
package com.stresstest.random.constructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.stresstest.random.ValueGenerator;

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

	@Override
	public List<ValueGenerator<?>> getValueGenerators() {
		List<ValueGenerator<?>> valueGenerators = new ArrayList<ValueGenerator<?>>();
		for(ClassPropertySetter<?> propertySetter: propertySetters)
			valueGenerators.addAll(propertySetter.getValueGenerators());
		return valueGenerators;
	}

}
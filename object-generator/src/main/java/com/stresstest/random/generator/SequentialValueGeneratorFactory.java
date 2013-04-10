package com.stresstest.random.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.stresstest.random.AbstractValueGeneratorFactory;
import com.stresstest.random.ValueGenerator;
import com.stresstest.random.constructor.ClassPropertySetterManager;

public class SequentialValueGeneratorFactory extends AbstractValueGeneratorFactory {

	public SequentialValueGeneratorFactory() {
		super(new ClassPropertySetterManager(), SequentialValueGenerator.DEFAULT_GENERATORS);
	}

	public SequentialValueGeneratorFactory(ClassPropertySetterManager propertySetterManager) {
		super(propertySetterManager, SequentialValueGenerator.DEFAULT_GENERATORS);
	}

	@Override
	protected <T> ValueGenerator<T> enumValueGenerator(Class<T> klass) {
		return SequentialValueGenerator.enumValueGenerator(klass);
	}

	public <T> ValueGenerator<T> replace(ValueGenerator<T> valueGeneratorToReplace) {
		// Step 1. Sanity check
		if(valueGeneratorToReplace == null || valueGeneratorToReplace instanceof SequentialValueGenerator)
			return valueGeneratorToReplace;
		// Step 2. Checking generated class
		T generatedValue = valueGeneratorToReplace.generate();
		if(generatedValue == null)
			return valueGeneratorToReplace;
		@SuppressWarnings("unchecked")
		Class<T> targetClass = (Class<T>) valueGeneratorToReplace.generate().getClass();
		// Step 3. Constructing generator, based on the class
		return getValueGenerator(targetClass);
	}
	
	public List<ValueGenerator<?>> replace(Collection<ValueGenerator<?>> generators) {
		List<ValueGenerator<?>> newGenerators = new ArrayList<ValueGenerator<?>>();
		for(ValueGenerator<?> constructorGenerator: generators) {
			ValueGenerator<?> newConstructorGenerator = replace(constructorGenerator);
			newGenerators.add(newConstructorGenerator);
		}
		return newGenerators;
	}

}

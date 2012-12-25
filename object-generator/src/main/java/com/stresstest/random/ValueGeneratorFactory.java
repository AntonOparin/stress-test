package com.stresstest.random;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.stresstest.random.impl.ClassConstructor;
import com.stresstest.random.impl.ClassPropertySetter;
import com.stresstest.random.impl.ClassValueGenerator;
import com.stresstest.utils.ReflectionUtils;

/**
 * 
 * Factory for ValueGenerators
 * 
 * @author Anton Oparin
 * 
 */
public class ValueGeneratorFactory {

    /**
     * Produces {@link ValueGenerator} for specified {@link Class}.
     * 
     * @param klass
     *            generated {@ling Class}
     * @return {@link ValueGenerator} for procided {@link Class}
     */
    @SuppressWarnings("unchecked")
    public <T> ValueGenerator<T> getValueGenerator(Class<T> klass) {
        // Step 1. Checking that it can be replaced with standard constructors
        if (ValueGenerator.DEFAULT_GENERATORS.containsKey(klass))
            return (ValueGenerator<T>) ValueGenerator.DEFAULT_GENERATORS.get(klass);
        // Step 2. If this is enum replace with Random value generator
        if (klass.isEnum())
            return (ValueGenerator<T>) ValueGenerator.randomValueGenerator(Arrays.asList(klass.getEnumConstants()));
        // Step 3. Initialize value generator with primarily public access
        ValueGenerator<T> valueGenerator = construct(ClassReflectionAccessWrapper.createPublicAccessor(klass));
        if (valueGenerator != null)
            return valueGenerator;
        // Step 4. Trying to initialize with all available access
        valueGenerator = construct(ClassReflectionAccessWrapper.createAllMethodsAccessor(klass));
        if (valueGenerator != null)
            return valueGenerator;
        // Step 5. If there is no result throw IllegalArgumentException
        throw new IllegalArgumentException();
    }

    /**
     * Produces {@link ValueGenerator} for provided {@link ClassReflectionAccessWrapper}, returned {@link ValueGenerator} can return any subtype of the target
     * {@link Class}.
     * 
     * @param sourceClass
     *            {@link ClassReflectionAccessWrapper} with defined level of access.
     * @return {@link ValueGenerator} for provided class if, there is possible to create one.
     */
    @SuppressWarnings("unchecked")
    private <T> ValueGenerator<T> construct(ClassReflectionAccessWrapper<T> sourceClass) {
        ValueGenerator<T> valueGenerator = tryConstruct(sourceClass);
        if (valueGenerator != null)
            return valueGenerator;
        // Step 3.1 Trying to initialize sub classes
        Collection<Class<? extends T>> subClasses = ReflectionUtils.getPossibleImplementations(sourceClass.getSourceClass());
        // Step 3.2 Checking extended list of candidates
        for (Class<?> subClass : subClasses) {
            valueGenerator = (ValueGenerator<T>) tryConstruct(sourceClass.wrap(subClass));
            if (valueGenerator != null)
                return valueGenerator;
        }
        return null;
    }

    /**
     * Produces {@link ValueGenerator} for a {@link Class} with restricted access.
     * 
     * @param classToGenerate
     *            {@link ClassReflectionAccessWrapper} for the class.
     * @return {@link ValueGenerator} if it is possible to create one, with defined access level, <code>null</code> otherwise.
     */
    private <T> ValueGenerator<T> tryConstruct(final ClassReflectionAccessWrapper<T> classToGenerate) {
        // Step 0. Checking abstract class
        if ((classToGenerate.getModifiers() & (Modifier.INTERFACE)) != 0)
            return null;
        // Step 1. Selecting appropriate constructor
        ClassConstructor<T> objectConstructor = ClassConstructor.construct(classToGenerate, this);
        if (objectConstructor == null)
            return null;
        // Step 2. Selecting list of applicable specific selectors from specific properties
        Collection<PropertySetter<?>> propertySetters = PropertySetter.extractAvailableProperties(classToGenerate);
        ClassPropertySetter<T> classPropertySetter = new ClassPropertySetter<T>(propertySetters);
        // Step 3. Generating final ClassGenerator for the type
        return new ClassValueGenerator<T>(objectConstructor, classPropertySetter);
    }

    /**
     * Produces {@Collection} of {@link ValueGenerator} for provided {@link Class}es.
     * 
     * @param parameters
     *            Collection of {@link Class}s to generate.
     * @return {@link Collection} of {@link ValueGenerator} to use.
     */
    final public Collection<ValueGenerator<?>> getValueGenerators(Class<?>[] parameters) {
        Collection<ValueGenerator<?>> valueGenerators = new ArrayList<ValueGenerator<?>>();
        for (Class<?> parameter : parameters) {
            valueGenerators.add(getValueGenerator(parameter));
        }
        return valueGenerators;
    }
}

package com.stresstest.random.construction.external;

import junit.framework.Assert;

import org.junit.Test;

import com.stresstest.random.ObjectValueGenerator;
import com.stresstest.random.ObjectGenerator;
import com.stresstest.random.ValueGenerator;
import com.stresstest.random.constructor.ClassConstructorBuilder;
import com.stresstest.random.generator.RandomValueGeneratorFactory;
import com.stresstest.random.generator.ValueGeneratorFactory;

public class DefaultBasedGenerationTest {

    final private ValueGeneratorFactory valueGeneratorFactory = new RandomValueGeneratorFactory();

    @Test
    public void testDefaultInterfaceCreation() {
        DefaultInterface<?> defaultInterface = ObjectGenerator.generate(DefaultInterface.class);
        Assert.assertTrue(defaultInterface instanceof DefaultInterfaceImpl);
    }

    @Test
    public void testDefaultAbstractClassCreation() {
        DefaultAbstractClass<?> defaultAbstractClass = ObjectGenerator.generate(DefaultAbstractClass.class);
        Assert.assertTrue(defaultAbstractClass instanceof DefaultAbstractClassImpl);
    }

    @Test
    public void testDefaultFactoryMethodClassCreation() {
        DefaultFactoryMethodClass defaultFactoryMethodClass = ObjectGenerator.generate(DefaultFactoryMethodClass.class);
        Assert.assertNotNull(defaultFactoryMethodClass);
    }

    @Test
    public void testDefaultBuilderBasedClassCreation() {
        DefaultAbstractBuilderBasedClass defaultBuilderBasedClass = ObjectGenerator.generate(DefaultAbstractBuilderBasedClass.class);
        Assert.assertNotNull(defaultBuilderBasedClass);
    }

    @Test
    public void testDefaultFactoryConstructorUsed() {
        ValueGenerator<DefaultBuilderBasedClass> factoryGenerator = valueGeneratorFactory.getValueGenerator(DefaultBuilderBasedClass.class);
        ObjectValueGenerator<DefaultBuilderBasedClass> classValueGenerator = (ObjectValueGenerator<DefaultBuilderBasedClass>) factoryGenerator;
        Assert.assertTrue(classValueGenerator.getObjectConstructor() instanceof ClassConstructorBuilder);
    }

}

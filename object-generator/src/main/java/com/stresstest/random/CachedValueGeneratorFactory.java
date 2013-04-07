package com.stresstest.random;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * {@link ValueGeneratorFactory} implementation that uses caching to optimize {@link ValueGenerator} production.
 * 
 * @author Anton Oparin
 * 
 */
public class CachedValueGeneratorFactory extends AbstractValueGeneratorFactory {

	final private ValueGeneratorFactory valueGeneratorFactory;
	
	/**
	 * Google LoadingCache that is used as a primary cache implementation.
	 */
	final private LoadingCache<Class<?>, ValueGenerator<?>> cachedValueGenerators = CacheBuilder.newBuilder().build(
			new CacheLoader<Class<?>, ValueGenerator<?>>() {
				@Override
				public ValueGenerator<?> load(Class<?> klass) throws Exception {
					return valueGeneratorFactory.getValueGenerator(klass);
				}
			});
	
	public CachedValueGeneratorFactory(ValueGeneratorFactory newValueGeneratorFactory) {
		this.valueGeneratorFactory = newValueGeneratorFactory == null ? new RandomValueGeneratorFactory() : newValueGeneratorFactory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> ValueGenerator<T> getValueGenerator(Class<T> klass) {
		try {
			return (ValueGenerator<T>) cachedValueGenerators.get(klass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

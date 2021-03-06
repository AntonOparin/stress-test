package com.clemble.test.spring.runners;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.test.runners.CheckAfter;
import com.clemble.test.runners.RunInParallel;
import com.clemble.test.runners.RunTimes;
import com.clemble.test.runners.SingleRun;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringJUnit4FrequentClassRunnerSingleRunTest.EmptySpringConfiguration.class)
@RunInParallel(maxThreads = 50)
@RunTimes(500)
public class SpringJUnit4FrequentClassRunnerSingleRunTest {

	@Configuration
	public static class EmptySpringConfiguration {

		@Bean
		public AtomicInteger onceCounter() {
			return new AtomicInteger(0);
		}
		
		@Bean
		public AtomicInteger parallelCounter() {
			return new AtomicInteger(0);
		}
	}

	@Autowired
	public AtomicInteger onceCounter;

	@Autowired
	public AtomicInteger parallelCounter;

	@CheckAfter
	public void checkAfter() {
		assertEquals(onceCounter.get(), 1);
		assertEquals(parallelCounter.get(), 500);
	}

	@SingleRun
	@Test
	public void increaseCounterOnceTest() {
		onceCounter.incrementAndGet();
	}
	
	@Test
	public void increaseCounter() {
		parallelCounter.incrementAndGet();
	}
}

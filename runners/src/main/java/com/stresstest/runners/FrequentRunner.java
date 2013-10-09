package com.stresstest.runners;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.AfterClass;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class FrequentRunner extends BlockJUnit4ClassRunner {

    final private AtomicReference<Object> createdTestRef = new AtomicReference<Object>();
    final private Class<?> klassToRun;

    public FrequentRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.klassToRun = klass;
    }

    @Override
    public void run(final RunNotifier notifier) {
        // Step 1. Executing klassToRun
        FrequentTestRunner.run(klassToRun, new Runnable() {
            @Override
            public void run() {
                execute(notifier);
            }
        });
        // Step 2. Invoking CheckAfter
        FrequentRunnerUtils.runAfterChecks(notifier, createdTestRef.get(), getTestClass());
    }

    @Override
    protected Statement withAfterClasses(Statement statement) {
        List<FrameworkMethod> afters= getTestClass().getAnnotatedMethods(AfterClass.class);
        return afters.isEmpty() ? statement : new RunAfters(statement, afters, null);
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        return FrequentTestRunner.order(super.getChildren());
    }

    @Override
    protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
        FrequentTestRunner.run(method.getMethod(), new Runnable() {
            @Override
            public void run() {
                executeChild(method, notifier);
            }
        });
    }

    @Override
    public Object createTest() throws Exception {
         Object cratedTest = super.createTest();
         createdTestRef.set(cratedTest);
         return cratedTest;
    }

    public void execute(final RunNotifier notifier) {
        super.run(notifier);
    }

    public void executeChild(final FrameworkMethod method, RunNotifier notifier) {
        super.runChild(method, notifier);
    }

}

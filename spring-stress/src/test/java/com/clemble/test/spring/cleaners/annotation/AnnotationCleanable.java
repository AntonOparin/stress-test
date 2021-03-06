package com.clemble.test.spring.cleaners.annotation;

import com.clemble.test.spring.cleaners.annotation.Cleaner;

public class AnnotationCleanable {

	private boolean cleanCalled = false;

	@Cleaner
	public void clean() {
		setCleanCalled(true);
	}

	public boolean isCleanCalled() {
		return cleanCalled;
	}

	public void setCleanCalled(boolean cleanCalled) {
		this.cleanCalled = cleanCalled;
	}

}

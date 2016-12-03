package com.app;

/**
 * Created by anna.karri on 01/12/16.
 */

public class AppException extends Exception {
	public AppException(String s) {
		super(s);
	}

	public AppException(String s, Exception e) {
		super(s, e);
	}
}

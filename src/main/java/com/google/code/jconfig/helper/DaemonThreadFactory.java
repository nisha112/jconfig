package com.google.code.jconfig.helper;

import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory implements ThreadFactory {

	public Thread newThread(Runnable runnable) {
		Thread thread = new Thread(runnable);
		thread.setDaemon(true);
		return thread;
	}

}

package org.opencredo.batch.modules.concurrent;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * A simple RejectedExecutionHandler that does nothing more than log, increment a counter and then discard.
 * 
 * It would probably 
 * 
 * @author Neale
 *
 */
@ManagedResource
public class SimpleRejectedExecutionHandler implements RejectedExecutionHandler {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final AtomicInteger rejectionCount = new AtomicInteger();
	

	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		rejectionCount.incrementAndGet();
		log.warn("{} rejected the task {} due to lack of resources", executor.toString(), r.getClass().getName());
		
		// or do CALLER_RUNS policy
//		r.run();
		
		// or reject
		throw new RejectedExecutionException("I could run your task in your thread, but I won't");
	}
	
	@ManagedAttribute
	public int getRejectionCount() {
		return rejectionCount.get();
	}
	
	@ManagedOperation
	public void resetRejectionCount() {
		rejectionCount.set(0);
	}
}

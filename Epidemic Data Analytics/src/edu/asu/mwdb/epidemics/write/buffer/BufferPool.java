package edu.asu.mwdb.epidemics.write.buffer;

import java.util.LinkedList;
import java.util.Queue;

public class BufferPool {
	
	private Queue<Buffer> pool;
	private boolean finished = false;
	
	public BufferPool() {
		pool = new LinkedList<Buffer>();
	}
	
	public synchronized Buffer getNextBuffer() {
		return pool.poll();
	}
	
	public synchronized boolean hasNext() {
		boolean moreElements = false;
		if(pool.peek() != null)
			moreElements = true;
		return moreElements;
	}
	
	public synchronized void add(Buffer buffer) {
		this.pool.add(buffer);
	}

	public synchronized boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
}

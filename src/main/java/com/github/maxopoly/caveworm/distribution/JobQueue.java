package com.github.maxopoly.caveworm.distribution;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.github.maxopoly.caveworm.Caveworm;

public class JobQueue {

    private Queue<GlobalDistributor> distributionsPending;
    private GlobalDistributor currentJob;
    
    private static JobQueue instance;

    // private to avoid explicit instanciating
    private JobQueue() {
	this.distributionsPending = new ConcurrentLinkedQueue<GlobalDistributor>();
    }

    public static JobQueue getInstance() {
	if (instance == null) {
	    instance = new JobQueue();
	}
	return instance;
    }

    public synchronized void addJob(GlobalDistributor dist) {
	distributionsPending.add(dist);
	checkForNextQueue();
    }

    public synchronized boolean hasActiveJob() {
	return currentJob != null;
    }
    
    public synchronized void notifyCompletion(GlobalDistributor dist) {
	if (dist != currentJob) {
	    Caveworm.getInstance().severe("Sync failure in job queue");
	    return;
	}
	Caveworm.getInstance().info("Completed distribution, currently " + distributionsPending.size() + " more in queue");
	currentJob = null;
	checkForNextQueue();
    }
    
    private synchronized void checkForNextQueue() {
	if (currentJob != null) {
	    return;
	}
	if (!distributionsPending.isEmpty()) {
	    currentJob = distributionsPending.remove();
	    currentJob.distribute();
	}
    }
    
    public int getWaitingJobCount() {
	return distributionsPending.size();
    }

}

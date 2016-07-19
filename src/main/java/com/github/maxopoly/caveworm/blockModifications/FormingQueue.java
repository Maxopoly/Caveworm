package com.github.maxopoly.caveworm.blockModifications;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.github.maxopoly.caveworm.CaveWormAPI;
import com.github.maxopoly.caveworm.Caveworm;
import com.github.maxopoly.caveworm.WormConfig;
import com.github.maxopoly.caveworm.caveFormers.SimplexSphereFormer;
import com.github.maxopoly.caveworm.distribution.JobQueue;

public class FormingQueue implements Runnable {

    private Queue<Location> pending;
    private int modificationsPerTick;
    private WormConfig config;
    private SimplexSphereFormer former;
    private JobQueue jobs;

    // private to avoid explicit instanciating
    public FormingQueue(WormConfig config, int modificationsPerTick) {
	this.pending = new ConcurrentLinkedQueue<Location>();
	Bukkit.getScheduler().scheduleSyncRepeatingTask(Caveworm.getInstance(),
		this, 1L, 1L);
	this.modificationsPerTick = modificationsPerTick;
	this.config = config;
	former = CaveWormAPI.getCaveFormer(config, 0);
	this.jobs = JobQueue.getInstance();
    }

    public void add(Location modification) {
	pending.add(modification);
    }

    public void addAll(Collection<Location> locations) {
	pending.addAll(locations);
    }

    @Override
    public void run() {
	if (pending.size() == 0) {
	    return;
	}
	int i = 0;
	while (!pending.isEmpty() && i < modificationsPerTick) {
	    former.extendLocation(pending.remove());
	    i++;
	}
	Caveworm.getInstance().info(
		"Finished expanding locations for this tick, currently left in queue: "
			+ pending.size() + "; Additional job not started yet: "
			+ jobs.getWaitingJobCount());
    }
}

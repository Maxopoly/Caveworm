package com.github.maxopoly.caveworm.events;

import com.github.maxopoly.caveworm.WormConfig;

import vg.civcraft.mc.civmodcore.interfaces.CustomEvent;

public class CaveWormGenerationCompletionEvent extends CustomEvent {
    
    private WormConfig configUsed;
    
    public CaveWormGenerationCompletionEvent(WormConfig config) {
	this.configUsed = config;
    }
    
    public WormConfig getConfig() {
	return configUsed;
    }

}

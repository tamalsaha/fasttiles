package com.tamal.tiles;

public interface IMonitor {
	public static final int NEW_SOL = 0x01;

	public void update(int event);
}

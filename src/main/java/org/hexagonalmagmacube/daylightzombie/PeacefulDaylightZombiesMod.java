package org.hexagonalmagmacube.daylightzombie;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeacefulDaylightZombiesMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("PeacefulDaylightZombies");

	@Override
	public void onInitialize() {
		LOGGER.info("Executing Daylight Zombie Initialize method.");

		// The full implementation is in the Mixin.
	}
}
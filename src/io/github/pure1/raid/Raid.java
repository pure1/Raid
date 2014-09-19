/*  
 * Copyright 2014 Oliver Strik <pure1.github.io>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * 
 *
 *
 *
 *
 * If my code looks messy or something like that, it's my code,
 * it's like handwriting, We all code differently and this is how I code.
 * If there is anything that really could be done better, I welcome suggestions.
 * 
 */

package io.github.pure1.raid;

import io.github.pure1.plugin.utils.ConfigAccessor;
import io.github.pure1.plugin.utils.Metrics;
import io.github.pure1.raid.listeners.PlayerListener;
import io.github.pure1.raid.party.PartyHandler;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class Raid extends JavaPlugin {

	/*
	 * This update:
	 * 
	 * 
	 */

	/*
	 * 
	 * 
	 */

	/*
	 * SET TRUE ON RELEASE COMPILE
	 */
	public static final boolean released = false;

	public final Logger logger = Logger.getLogger("Minecraft");
		
	public static ConfigAccessor config;
	
	
	private PlayerListener playerListener;
	
	private RaidCommand raidCommand;
	
	private PartyHandler partyHandler;
	
	/** on enable */
	@Override
	public void onEnable() {

		init();
		registerListeners();
		registerCommands();
		registerManagers();
		Metrics();
		
		//initConfig();
	}

	/** Initialise variables */
	public void init(){
		//config = new ConfigAccessor(this, "config.yml");
		playerListener = new PlayerListener(this);
		partyHandler = new PartyHandler(this);
		raidCommand = new RaidCommand(this);
	}
	
	/** Register Listeners */
	public void registerListeners(){
		getServer().getPluginManager().registerEvents(playerListener, this);
	}
	
	/** Register Commands */
	public void registerCommands(){
		getCommand("Raid").setExecutor(raidCommand);
	}
	
	/** Register Managers */
	public void registerManagers(){
		
	}
	
	/** Metrics */
	public void Metrics(){
		if (released) {
			try {
				Metrics metrics = new Metrics(this);
				metrics.start();
			} catch (IOException e) {
			}
		}
	}
	
	/** Initalize Config */
	public void initConfig(){
		config.saveDefaultConfig();
		config.getConfig();
		config.saveConfig();
	}
	
	@Override
	public void onDisable() {
	}

	public PartyHandler getPartyHandler() {
		return partyHandler;
	}

}

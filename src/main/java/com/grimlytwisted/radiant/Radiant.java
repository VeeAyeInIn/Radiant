package com.grimlytwisted.radiant;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * My first Minecraft Mod. Relatively simple, just changing how the player can
 * manipulate brightness / gamma in-game. Feel free to use anything here, as
 * I want to make sure other people can create mods. I personally just got into
 * creating mods, so I want to open up a path for other people.
 *
 *
 * @author GrimlyTwisted
 * @version 1.0
 */
@Mod(modid = Radiant.MOD_ID, name = Radiant.NAME, version = Radiant.VERSION)
public class Radiant {
	
	private static final List<KeyBinding> keyBindings = new ArrayList<>();
	
	/**
	 * The Mod ID.
	 */
	static final String MOD_ID = "radiant";
	
	/**
	 * The Name of the mod.
	 */
	static final String NAME = "Radiant";
	
	/**
	 * The Version of the mod.
	 */
	static final String VERSION = "1.0";
	
	
	/**
	 * The Mod Logger
	 */
	private static Logger logger;
	
	/**
	 * Pre initialization, see {@link #init(FMLInitializationEvent)}.
	 *
	 * @param event the event
	 */
	@EventHandler // This is an event, and will be handled
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
	}
	
	/**
	 * Initialize
	 *
	 * @param event the event
	 */
	@EventHandler // This is an event, and will be handled
	public void init(FMLInitializationEvent event) {
		
		logger.log(Level.INFO, "I'm up and runnin'!");
		
		// Gets a handler list, so you can register custom handlers
		MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
		
		keyBindings.add(new KeyBinding("Gamma Override", Keyboard.KEY_P, "Radiant"));
		
		// Essentially running, ClientRegister.registerKeyBinding( KeyBinding Here ); for each
		// instance of a KeyBinding in the list
		keyBindings.forEach(ClientRegistry :: registerKeyBinding);
	}
	
	@Mod.EventBusSubscriber // Means this has some event handlers in here
	@SideOnly(Side.CLIENT) // Only CLIENT, the SERVER does not deal with this
	private class KeyInputHandler {

		// Is it currently enabled?
		private boolean gammaOverwrite = false;
		
		// What was the gamma before modifying it?
		private float previousGamma = Minecraft.getMinecraft().gameSettings.gammaSetting > 100f ?
				100f : Minecraft.getMinecraft().gameSettings.gammaSetting;
		
		/**
		 * On Key Input
		 *
		 * @param event the key input event
		 */
		@SubscribeEvent
		public void onInput(InputEvent.KeyInputEvent event) {
			
			// Which button did they press?
			
			if(Radiant.keyBindings.get(0).isPressed()) {
				
				// Is is currently enabled, or disabled?
				
				if(gammaOverwrite) {
					gammaOverwrite = false;
					Minecraft.getMinecraft().gameSettings.gammaSetting = previousGamma;
				} else {
					previousGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
					gammaOverwrite = true;
					Minecraft.getMinecraft().gameSettings.gammaSetting = 666.0f;
				}
			}
		}
	}
}
package thederpgamer.modtemplate;

import api.config.BlockConfig;
import api.mod.StarMod;
import org.apache.commons.io.IOUtils;
import org.schema.schine.event.EventManager;
import org.schema.schine.resource.ResourceLoader;
import thederpgamer.modtemplate.element.ElementManager;
import thederpgamer.modtemplate.element.items.ExampleItem;
import thederpgamer.modtemplate.manager.ConfigManager;
import thederpgamer.modtemplate.manager.LogManager;
import thederpgamer.modtemplate.manager.ResourceManager;

import javax.annotation.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.logging.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ModTemplate extends StarMod {

	//Instance
	private static ModTemplate instance;
	public static ModTemplate getInstance() {
		return instance;
	}
	public ModTemplate() {
		instance = this;
	}

	//Logging
	private static Logger log;
	public static void logInfo(String message) {
		log.info("[" + instance.getName() + "]" + message);
	}

	public static void logWarning(String message, @Nullable Exception exception) {
		log.warning("[" + instance.getName() + "]" + message);
		if(exception != null) exception.printStackTrace();
	}

	public static void logError(String message, @Nullable Exception exception) {
		log.severe("[" + instance.getName() + "]" + message);
		if(exception != null) exception.printStackTrace();
	}

	//Other
	private final String[] overwriteClasses = { //Use this to overwrite specific vanilla classes

	};

	@Override
	public void onEnable() {
		ConfigManager.initialize(this);
		initLogger();
		EventManager.initialize(this);
		PacketManager.initialize();
	}

	@Override
	public byte[] onClassTransform(String className, byte[] byteCode) {
		for (String name : overwriteClasses) if (className.endsWith(name)) return overwriteClass(className, byteCode);
		return super.onClassTransform(className, byteCode);
	}

	@Override
	public void onResourceLoad(ResourceLoader loader) {
		ResourceManager.loadResources();
	}

	@Override
	public void onBlockConfigLoad(BlockConfig config) {
		ElementManager.addItem(new ExampleItem());

		ElementManager.initialize();
	}

	/**
	 * Use to register mod listeners.
	 */
	private void registerListeners() {

	}

	/**
	 * Use to register mod packets.
	 */
	private void registerPackets() {

	}

	private byte[] overwriteClass(String className, byte[] byteCode) {
		byte[] bytes = null;
		try {
			ZipInputStream file =
					new ZipInputStream(Files.newInputStream(this.getSkeleton().getJarFile().toPath()));
			while (true) {
				ZipEntry nextEntry = file.getNextEntry();
				if (nextEntry == null) break;
				if (nextEntry.getName().endsWith(className + ".class")) bytes = IOUtils.toByteArray(file);
			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (bytes != null) return bytes;
		else return byteCode;
	}
}

package username.modtemplate;

import api.config.BlockConfig;
import api.mod.StarMod;
import org.apache.commons.io.IOUtils;
import org.schema.schine.resource.ResourceLoader;
import username.modtemplate.element.ElementManager;
import username.modtemplate.element.items.ExampleItem;
import username.modtemplate.manager.ConfigManager;
import username.modtemplate.manager.EventManager;
import username.modtemplate.manager.PacketManager;
import username.modtemplate.manager.ResourceManager;

import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ModTemplate extends StarMod {

	//Instance
	private static ModTemplate instance;

	//Use this to overwrite specific vanilla classes
	private final String[] overwriteClasses = {};

	public ModTemplate() {
		instance = this;
	}

	public static ModTemplate getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		ConfigManager.initialize(this);
		EventManager.initialize(this);
		PacketManager.initialize();
	}

	@Override
	public byte[] onClassTransform(String className, byte[] byteCode) {
		for(String name : overwriteClasses) if(className.endsWith(name)) return overwriteClass(className, byteCode);
		return super.onClassTransform(className, byteCode);
	}

	@Override
	public void onResourceLoad(ResourceLoader loader) {
		ResourceManager.loadResources(this, loader);
	}

	@Override
	public void onBlockConfigLoad(BlockConfig config) {
		ElementManager.addItem(new ExampleItem());
		ElementManager.initialize();
	}

	@Override
	public void logInfo(String message) {
		super.logInfo(message);
		System.out.println("[INFO] " + message);
	}

	@Override
	public void logWarning(String message) {
		super.logWarning(message);
		System.err.println("[WARNING] " + message);
	}

	@Override
	public void logException(String message, Exception exception) {
		super.logException(message, exception);
		System.err.println("[EXCEPTION] " + message + "\n" + exception.getMessage() + "\n" + Arrays.toString(exception.getStackTrace()));
	}

	@Override
	public void logFatal(String message, Exception exception) {
		logException(message, exception);
		if(GameCommon.getGameState().isOnServer()) GameServer.getServerState().addCountdownMessage(10, "Server will perform an emergency shutdown due to a fatal error: " + message);
	}

	private byte[] overwriteClass(String className, byte[] byteCode) {
		byte[] bytes = null;
		try {
			ZipInputStream file = new ZipInputStream(Files.newInputStream(getSkeleton().getJarFile().toPath()));
			while(true) {
				ZipEntry nextEntry = file.getNextEntry();
				if(nextEntry == null) break;
				if(nextEntry.getName().endsWith(className + ".class")) bytes = IOUtils.toByteArray(file);
			}
			file.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		if(bytes != null) return bytes;
		else return byteCode;
	}
}

package thederpgamer.modtemplate;

import api.config.BlockConfig;
import api.mod.StarMod;
import org.apache.commons.io.IOUtils;
import org.schema.schine.resource.ResourceLoader;
import thederpgamer.modtemplate.element.ElementManager;
import thederpgamer.modtemplate.element.items.ExampleItem;
import thederpgamer.modtemplate.manager.ConfigManager;
import thederpgamer.modtemplate.manager.LogManager;
import thederpgamer.modtemplate.manager.ResourceManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * StarMade mod starting template.
 *
 * @author TheDerpGamer
 * @version 1.0 - [03/05/2022]
 */
public class ModTemplate extends StarMod {

	//Instance
	private static ModTemplate instance;
	public static ModTemplate getInstance() {
		return instance;
	}
	public static void main(String[] args) { }
	public ModTemplate() { }

	//Other
	private final String[] overwriteClasses = { //Use this to overwrite specific vanilla classes

	};

	@Override
	public void onEnable() {
		instance = this;
		ConfigManager.initialize(this);
		LogManager.initialize();
		registerListeners();
		registerPackets();
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
					new ZipInputStream(new FileInputStream(this.getSkeleton().getJarFile()));
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

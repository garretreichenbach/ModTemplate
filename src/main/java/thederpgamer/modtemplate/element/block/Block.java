package thederpgamer.modtemplate.element.block;

import api.config.BlockConfig;
import org.schema.game.common.data.element.ElementCategory;
import org.schema.game.common.data.element.ElementInformation;
import thederpgamer.modtemplate.ModTemplate;
import thederpgamer.modtemplate.element.ElementManager;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @version 1.0 - [03/05/2022]
 */
public abstract class Block {

	protected ElementInformation blockInfo;

	public Block(String name, ElementCategory category) {
		blockInfo = BlockConfig.newElement(ModTemplate.getInstance(), name, new short[6]);
		BlockConfig.setElementCategory(blockInfo, category);
		ElementManager.addBlock(this);
	}

	public final ElementInformation getBlockInfo() {
		return blockInfo;
	}

	public final short getId() {
		return blockInfo.getId();
	}

	public abstract void initialize();
}
package net.minecraft.client.data.models.blockstates;

import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.world.level.block.Block;

public interface BlockModelDefinitionGenerator {
  Block block();
  
  BlockModelDefinition create();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/blockstates/BlockModelDefinitionGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
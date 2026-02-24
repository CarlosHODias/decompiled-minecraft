package net.minecraft.client.gui.screens.worldselection;

import java.nio.file.Path;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.level.storage.PrimaryLevelData;

@FunctionalInterface
public interface CreateWorldCallback {
  boolean create(CreateWorldScreen paramCreateWorldScreen, LayeredRegistryAccess<RegistryLayer> paramLayeredRegistryAccess, PrimaryLevelData paramPrimaryLevelData, Path paramPath);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/worldselection/CreateWorldCallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
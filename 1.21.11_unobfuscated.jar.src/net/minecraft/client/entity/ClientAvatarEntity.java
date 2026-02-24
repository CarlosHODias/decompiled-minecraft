package net.minecraft.client.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.player.PlayerSkin;

public interface ClientAvatarEntity {
  ClientAvatarState avatarState();
  
  PlayerSkin getSkin();
  
  Component belowNameDisplay();
  
  Parrot.Variant getParrotVariantOnShoulder(boolean paramBoolean);
  
  boolean showExtraEars();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/entity/ClientAvatarEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
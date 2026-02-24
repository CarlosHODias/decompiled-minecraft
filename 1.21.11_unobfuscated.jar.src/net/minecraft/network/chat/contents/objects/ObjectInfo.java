package net.minecraft.network.chat.contents.objects;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.FontDescription;

public interface ObjectInfo {
  FontDescription fontDescription();
  
  String description();
  
  MapCodec<? extends ObjectInfo> codec();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/contents/objects/ObjectInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
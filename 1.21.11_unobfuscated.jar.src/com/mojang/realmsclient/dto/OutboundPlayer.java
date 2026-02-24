package com.mojang.realmsclient.dto;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.mojang.util.UUIDTypeAdapter;
import java.util.UUID;

public class OutboundPlayer implements ReflectionBasedSerialization {
  @SerializedName("name")
  public String name;
  
  @SerializedName("uuid")
  @JsonAdapter(UUIDTypeAdapter.class)
  public UUID uuid;
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/OutboundPlayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
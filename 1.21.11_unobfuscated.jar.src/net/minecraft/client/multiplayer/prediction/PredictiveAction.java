package net.minecraft.client.multiplayer.prediction;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;

@FunctionalInterface
public interface PredictiveAction {
  Packet<ServerGamePacketListener> predict(int paramInt);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/prediction/PredictiveAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
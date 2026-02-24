package net.minecraft.network.syncher;

import java.util.List;

public interface SyncedDataHolder {
  void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor);
  
  void onSyncedDataUpdated(List<SynchedEntityData.DataValue<?>> paramList);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/syncher/SyncedDataHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
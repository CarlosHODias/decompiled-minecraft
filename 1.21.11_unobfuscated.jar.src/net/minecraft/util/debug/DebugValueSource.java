package net.minecraft.util.debug;

import net.minecraft.server.level.ServerLevel;

public interface DebugValueSource {
  void registerDebugValues(ServerLevel paramServerLevel, Registration paramRegistration);
  
  public static interface ValueGetter<T> {
    T get();
  }
  
  public static interface Registration {
    <T> void register(DebugSubscription<T> param1DebugSubscription, DebugValueSource.ValueGetter<T> param1ValueGetter);
  }
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/debug/DebugValueSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
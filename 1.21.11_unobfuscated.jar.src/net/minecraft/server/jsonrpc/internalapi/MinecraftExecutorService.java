package net.minecraft.server.jsonrpc.internalapi;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface MinecraftExecutorService {
  <V> CompletableFuture<V> submit(Supplier<V> paramSupplier);
  
  CompletableFuture<Void> submit(Runnable paramRunnable);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/internalapi/MinecraftExecutorService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
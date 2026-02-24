package net.minecraft.client.resources.server;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.server.packs.DownloadQueue;

public interface PackDownloader {
  void download(Map<UUID, DownloadQueue.DownloadRequest> paramMap, Consumer<DownloadQueue.BatchResult> paramConsumer);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/server/PackDownloader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
/*    */ package net.minecraft.client.sounds;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.blaze3d.audio.SoundBuffer;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionException;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.client.resources.sounds.Sound;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceProvider;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class SoundBufferLibrary
/*    */ {
/*    */   private final ResourceProvider resourceManager;
/* 21 */   private final Map<Identifier, CompletableFuture<SoundBuffer>> cache = Maps.newHashMap();
/*    */   
/*    */   public SoundBufferLibrary(ResourceProvider resourceProvider) {
/* 24 */     this.resourceManager = resourceProvider;
/*    */   }
/*    */   
/*    */   public CompletableFuture<SoundBuffer> getCompleteBuffer(Identifier location) {
/* 28 */     return this.cache.computeIfAbsent(location, l -> CompletableFuture.supplyAsync((), (Executor)Util.nonCriticalIoPool()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CompletableFuture<AudioStream> getStream(Identifier location, boolean looping) {
/* 41 */     return CompletableFuture.supplyAsync(() -> {
/*    */           try {
/*    */             InputStream is = this.resourceManager.open(location);
/*    */             return looping ? new LoopingAudioStream(JOrbisAudioStream::new, is) : new JOrbisAudioStream(is);
/* 45 */           } catch (IOException e) {
/*    */             throw new CompletionException(e);
/*    */           } 
/* 48 */         }, (Executor)Util.nonCriticalIoPool());
/*    */   }
/*    */   
/*    */   public void clear() {
/* 52 */     this.cache.values().forEach(future -> future.thenAccept(SoundBuffer::discardAlBuffer));
/* 53 */     this.cache.clear();
/*    */   }
/*    */   
/*    */   public CompletableFuture<?> preload(Collection<Sound> sounds) {
/* 57 */     return CompletableFuture.allOf((CompletableFuture<?>[])sounds.stream().map(sound -> getCompleteBuffer(sound.getPath())).toArray(x$0 -> new CompletableFuture[x$0]));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/sounds/SoundBufferLibrary.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
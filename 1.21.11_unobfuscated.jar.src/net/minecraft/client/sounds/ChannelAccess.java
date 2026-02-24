/*    */ package net.minecraft.client.sounds;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import com.mojang.blaze3d.audio.Channel;
/*    */ import com.mojang.blaze3d.audio.Library;
/*    */ import java.util.Iterator;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class ChannelAccess
/*    */ {
/*    */   public class ChannelHandle
/*    */   {
/*    */     private Channel channel;
/*    */     private boolean stopped;
/*    */     
/*    */     public boolean isStopped() {
/* 22 */       return this.stopped;
/*    */     }
/*    */     
/*    */     public ChannelHandle(Channel channel) {
/* 26 */       this.channel = channel;
/*    */     }
/*    */     
/*    */     public void execute(Consumer<Channel> action) {
/* 30 */       ChannelAccess.this.executor.execute(() -> {
/*    */             if (this.channel != null) {
/*    */               action.accept(this.channel);
/*    */             }
/*    */           });
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void release() {
/* 40 */       this.stopped = true;
/* 41 */       ChannelAccess.this.library.releaseChannel(this.channel);
/* 42 */       this.channel = null;
/*    */     }
/*    */   }
/*    */   
/* 46 */   private final Set<ChannelHandle> channels = Sets.newIdentityHashSet();
/*    */   
/*    */   private final Library library;
/*    */   
/*    */   private final Executor executor;
/*    */   
/*    */   public ChannelAccess(Library library, Executor executor) {
/* 53 */     this.library = library;
/* 54 */     this.executor = executor;
/*    */   }
/*    */   
/*    */   public CompletableFuture<ChannelHandle> createHandle(Library.Pool pool) {
/* 58 */     CompletableFuture<ChannelHandle> result = new CompletableFuture<>();
/* 59 */     this.executor.execute(() -> {
/*    */           Channel channel = this.library.acquireChannel(pool);
/*    */           if (channel != null) {
/*    */             ChannelHandle handle = new ChannelHandle(channel);
/*    */             this.channels.add(handle);
/*    */             result.complete(handle);
/*    */           } else {
/*    */             result.complete(null);
/*    */           } 
/*    */         });
/* 69 */     return result;
/*    */   }
/*    */   
/*    */   public void executeOnChannels(Consumer<Stream<Channel>> action) {
/* 73 */     this.executor.execute(() -> action.accept(this.channels.stream().map(()).filter(Objects::nonNull)));
/*    */   }
/*    */   
/*    */   public void scheduleTick() {
/* 77 */     this.executor.execute(() -> {
/*    */           Iterator<ChannelHandle> it = this.channels.iterator();
/*    */           while (it.hasNext()) {
/*    */             ChannelHandle handle = it.next();
/*    */             handle.channel.updateStream();
/*    */             if (handle.channel.stopped()) {
/*    */               handle.release();
/*    */               it.remove();
/*    */             } 
/*    */           } 
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 92 */     this.channels.forEach(ChannelHandle::release);
/* 93 */     this.channels.clear();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/sounds/ChannelAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
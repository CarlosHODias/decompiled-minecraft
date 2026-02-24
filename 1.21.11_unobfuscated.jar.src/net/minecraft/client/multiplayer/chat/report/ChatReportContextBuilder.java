/*     */ package net.minecraft.client.multiplayer.chat.report;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.IntCollection;
/*     */ import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.multiplayer.chat.ChatLog;
/*     */ import net.minecraft.client.multiplayer.chat.LoggedChatEvent;
/*     */ import net.minecraft.client.multiplayer.chat.LoggedChatMessage;
/*     */ import net.minecraft.network.chat.MessageSignature;
/*     */ import net.minecraft.network.chat.PlayerChatMessage;
/*     */ 
/*     */ public class ChatReportContextBuilder
/*     */ {
/*     */   private final int leadingCount;
/*  19 */   private final List<Collector> activeCollectors = new ArrayList<>();
/*     */   
/*     */   public ChatReportContextBuilder(int leadingCount) {
/*  22 */     this.leadingCount = leadingCount;
/*     */   }
/*     */   
/*     */   public void collectAllContext(ChatLog chatLog, IntCollection roots, Handler handler) {
/*  26 */     IntRBTreeSet intRBTreeSet = new IntRBTreeSet(roots);
/*     */     
/*  28 */     int id = intRBTreeSet.lastInt();
/*  29 */     while (id >= chatLog.start() && (isActive() || !intRBTreeSet.isEmpty())) {
/*  30 */       LoggedChatEvent loggedChatEvent = chatLog.lookup(id); if (loggedChatEvent instanceof LoggedChatMessage.Player) { LoggedChatMessage.Player event = (LoggedChatMessage.Player)loggedChatEvent;
/*  31 */         boolean context = acceptContext(event.message());
/*  32 */         if (intRBTreeSet.remove(id)) {
/*  33 */           trackContext(event.message());
/*  34 */           handler.accept(id, event);
/*  35 */         } else if (context) {
/*  36 */           handler.accept(id, event);
/*     */         }  }
/*     */       
/*  39 */       id--;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void trackContext(PlayerChatMessage message) {
/*  44 */     this.activeCollectors.add(new Collector(message));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean acceptContext(PlayerChatMessage message) {
/*     */     boolean collected = false;
/*  50 */     Iterator<Collector> iterator = this.activeCollectors.iterator();
/*  51 */     while (iterator.hasNext()) {
/*  52 */       Collector collector = iterator.next();
/*  53 */       if (collector.accept(message)) {
/*  54 */         collected = true;
/*  55 */         if (collector.isComplete()) {
/*  56 */           iterator.remove();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  61 */     return collected;
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/*  65 */     return !this.activeCollectors.isEmpty();
/*     */   }
/*     */   
/*     */   public static interface Handler
/*     */   {
/*     */     void accept(int param1Int, LoggedChatMessage.Player param1Player);
/*     */   }
/*     */   
/*     */   private class Collector {
/*     */     private final Set<MessageSignature> lastSeenSignatures;
/*     */     private PlayerChatMessage lastChainMessage;
/*     */     private boolean collectingChain = true;
/*     */     private int count;
/*     */     
/*     */     private Collector(PlayerChatMessage fromMessage) {
/*  80 */       this.lastSeenSignatures = (Set<MessageSignature>)new ObjectOpenHashSet(fromMessage.signedBody().lastSeen().entries());
/*  81 */       this.lastChainMessage = fromMessage;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean accept(PlayerChatMessage message) {
/*  86 */       if (message.equals(this.lastChainMessage)) {
/*  87 */         return false;
/*     */       }
/*  89 */       boolean selected = this.lastSeenSignatures.remove(message.signature());
/*  90 */       if (this.collectingChain && this.lastChainMessage.sender().equals(message.sender())) {
/*  91 */         if (this.lastChainMessage.link().isDescendantOf(message.link())) {
/*  92 */           selected = true;
/*  93 */           this.lastChainMessage = message;
/*     */         } else {
/*  95 */           this.collectingChain = false;
/*     */         } 
/*     */       }
/*  98 */       if (selected) {
/*  99 */         this.count++;
/*     */       }
/* 101 */       return selected;
/*     */     }
/*     */     
/*     */     private boolean isComplete() {
/* 105 */       return (this.count >= ChatReportContextBuilder.this.leadingCount || (!this.collectingChain && this.lastSeenSignatures.isEmpty()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/chat/report/ChatReportContextBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
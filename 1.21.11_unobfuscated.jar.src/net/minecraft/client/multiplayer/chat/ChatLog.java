/*    */ package net.minecraft.client.multiplayer.chat;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ChatLog {
/*    */   private final LoggedChatEvent[] buffer;
/*    */   private int nextId;
/*    */   
/*    */   public static Codec<ChatLog> codec(int capacity) {
/* 12 */     return Codec.list(LoggedChatEvent.CODEC).comapFlatMap(loggedChatEvents -> { int parsedSize = loggedChatEvents.size(); return (parsedSize > capacity) ? DataResult.error(()) : DataResult.success(new ChatLog(capacity, loggedChatEvents)); }, ChatLog::loggedChatEvents);
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
/*    */   public ChatLog(int capacity) {
/* 25 */     this.buffer = new LoggedChatEvent[capacity];
/*    */   }
/*    */   
/*    */   private ChatLog(int capacity, List<LoggedChatEvent> buffer) {
/* 29 */     this.buffer = (LoggedChatEvent[])buffer.toArray(size -> new LoggedChatEvent[capacity]);
/* 30 */     this.nextId = buffer.size();
/*    */   }
/*    */   
/*    */   private List<LoggedChatEvent> loggedChatEvents() {
/* 34 */     List<LoggedChatEvent> loggedChatEvents = new ArrayList<>(size());
/* 35 */     for (int i = start(); i <= end(); i++) {
/* 36 */       loggedChatEvents.add(lookup(i));
/*    */     }
/* 38 */     return loggedChatEvents;
/*    */   }
/*    */   
/*    */   public void push(LoggedChatEvent event) {
/* 42 */     this.buffer[index(this.nextId++)] = event;
/*    */   }
/*    */   
/*    */   public LoggedChatEvent lookup(int id) {
/* 46 */     return (id >= start() && id <= end()) ? this.buffer[index(id)] : null;
/*    */   }
/*    */   
/*    */   private int index(int id) {
/* 50 */     return id % this.buffer.length;
/*    */   }
/*    */   
/*    */   public int start() {
/* 54 */     return Math.max(this.nextId - this.buffer.length, 0);
/*    */   }
/*    */   
/*    */   public int end() {
/* 58 */     return this.nextId - 1;
/*    */   }
/*    */   
/*    */   private int size() {
/* 62 */     return end() - start() + 1;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/chat/ChatLog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
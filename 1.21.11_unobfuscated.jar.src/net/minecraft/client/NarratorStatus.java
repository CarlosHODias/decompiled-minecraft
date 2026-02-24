/*    */ package net.minecraft.client;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public enum NarratorStatus
/*    */ {
/* 10 */   OFF(0, "options.narrator.off"),
/* 11 */   ALL(1, "options.narrator.all"),
/* 12 */   CHAT(2, "options.narrator.chat"),
/* 13 */   SYSTEM(3, "options.narrator.system");
/*    */ 
/*    */   
/* 16 */   private static final IntFunction<NarratorStatus> BY_ID = ByIdMap.continuous(NarratorStatus::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/* 17 */   public static final Codec<NarratorStatus> LEGACY_CODEC = Codec.INT.xmap(NarratorStatus::byId, NarratorStatus::getId);
/*    */   
/*    */   private final int id;
/*    */   private final Component name;
/*    */   
/*    */   NarratorStatus(int id, String key) {
/* 23 */     this.id = id;
/* 24 */     this.name = (Component)Component.translatable(key);
/*    */   }
/*    */   
/*    */   public int getId() {
/* 28 */     return this.id;
/*    */   }
/*    */   
/*    */   public Component getName() {
/* 32 */     return this.name;
/*    */   }
/*    */   
/*    */   public static NarratorStatus byId(int id) {
/* 36 */     return BY_ID.apply(id);
/*    */   }
/*    */   
/*    */   public boolean shouldNarrateChat() {
/* 40 */     return (this == ALL || this == CHAT);
/*    */   }
/*    */   
/*    */   public boolean shouldNarrateSystem() {
/* 44 */     return (this == ALL || this == SYSTEM);
/*    */   }
/*    */   
/*    */   public boolean shouldNarrateSystemOrChat() {
/* 48 */     return (this == ALL || this == SYSTEM || this == CHAT);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/NarratorStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
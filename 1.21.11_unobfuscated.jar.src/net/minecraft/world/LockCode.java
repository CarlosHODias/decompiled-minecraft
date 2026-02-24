/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.advancements.criterion.ItemPredicate;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public final class LockCode extends Record {
/*    */   private final ItemPredicate predicate;
/*    */   
/* 10 */   public LockCode(ItemPredicate predicate) { this.predicate = predicate; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/LockCode;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/LockCode; } public ItemPredicate predicate() { return this.predicate; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/LockCode;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/LockCode; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/LockCode;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/LockCode;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public static final LockCode NO_LOCK = new LockCode(ItemPredicate.Builder.item().build());
/*    */   
/* 13 */   public static final com.mojang.serialization.Codec<LockCode> CODEC = ItemPredicate.CODEC.xmap(LockCode::new, LockCode::predicate);
/*    */   
/*    */   public static final String TAG_LOCK = "lock";
/*    */   
/*    */   public boolean unlocksWith(net.minecraft.world.item.ItemStack itemStack) {
/* 18 */     return this.predicate.test(itemStack);
/*    */   }
/*    */   
/*    */   public void addToTag(ValueOutput parent) {
/* 22 */     if (this != NO_LOCK) {
/* 23 */       parent.store("lock", CODEC, this);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean canUnlock(Player player) {
/* 28 */     return (player.isSpectator() || unlocksWith(player.getMainHandItem()));
/*    */   }
/*    */   
/*    */   public static LockCode fromTag(net.minecraft.world.level.storage.ValueInput parent) {
/* 32 */     return parent.read("lock", CODEC).orElse(NO_LOCK);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/LockCode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
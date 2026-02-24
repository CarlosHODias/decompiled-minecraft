/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.level.GameType;
/*    */ 
/*    */ public final class GameTypePredicate extends Record {
/*    */   private final List<GameType> types;
/*    */   
/*  9 */   public GameTypePredicate(List<GameType> types) { this.types = types; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/GameTypePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/GameTypePredicate; } public List<GameType> types() { return this.types; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/GameTypePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/GameTypePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/GameTypePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/GameTypePredicate;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public static final GameTypePredicate ANY = of(GameType.values());
/* 11 */   public static final GameTypePredicate SURVIVAL_LIKE = of(new GameType[] { GameType.SURVIVAL, GameType.ADVENTURE });
/*    */   
/* 13 */   public static final com.mojang.serialization.Codec<GameTypePredicate> CODEC = GameType.CODEC.listOf().xmap(GameTypePredicate::new, GameTypePredicate::types);
/*    */   
/*    */   public static GameTypePredicate of(GameType... types) {
/* 16 */     return new GameTypePredicate(java.util.Arrays.<GameType>stream(types).toList());
/*    */   }
/*    */   
/*    */   public boolean matches(GameType type) {
/* 20 */     return this.types.contains(type);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/GameTypePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
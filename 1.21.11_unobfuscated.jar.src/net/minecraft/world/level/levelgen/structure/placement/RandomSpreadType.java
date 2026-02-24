/*    */ package net.minecraft.world.level.levelgen.structure.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum RandomSpreadType implements StringRepresentable {
/*  8 */   LINEAR("linear"),
/*  9 */   TRIANGULAR("triangular");
/*    */   
/* 11 */   public static final Codec<RandomSpreadType> CODEC = (Codec<RandomSpreadType>)StringRepresentable.fromEnum(RandomSpreadType::values);
/*    */   
/*    */   private final String id;
/*    */   
/*    */   RandomSpreadType(String id) {
/* 16 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 21 */     return this.id;
/*    */   }
/*    */   
/*    */   public int evaluate(RandomSource random, int limit) {
/* 25 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return (
/*    */       
/* 27 */       random.nextInt(limit) + random.nextInt(limit)) / 2;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/placement/RandomSpreadType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
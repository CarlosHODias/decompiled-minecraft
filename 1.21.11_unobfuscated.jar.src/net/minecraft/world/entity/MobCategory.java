/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum MobCategory implements StringRepresentable {
/*  7 */   MONSTER("monster", 70, false, false, 128),
/*  8 */   CREATURE("creature", 10, true, true, 128),
/*  9 */   AMBIENT("ambient", 15, true, false, 128),
/*    */   
/* 11 */   AXOLOTLS("axolotls", 5, true, false, 128),
/* 12 */   UNDERGROUND_WATER_CREATURE("underground_water_creature", 5, true, false, 128),
/* 13 */   WATER_CREATURE("water_creature", 5, true, false, 128),
/* 14 */   WATER_AMBIENT("water_ambient", 20, true, false, 64),
/* 15 */   MISC("misc", -1, true, true, 128);
/*    */ 
/*    */   
/* 18 */   public static final Codec<MobCategory> CODEC = (Codec<MobCategory>)StringRepresentable.fromEnum(MobCategory::values);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   private final int noDespawnDistance = 32; private final int max;
/*    */   private final boolean isFriendly;
/*    */   
/*    */   MobCategory(String name, int max, boolean isFriendly, boolean isPersistent, int despawnDistance) {
/* 28 */     this.name = name;
/* 29 */     this.max = max;
/* 30 */     this.isFriendly = isFriendly;
/* 31 */     this.isPersistent = isPersistent;
/* 32 */     this.despawnDistance = despawnDistance;
/*    */   }
/*    */   private final boolean isPersistent; private final String name; private final int despawnDistance;
/*    */   public String getName() {
/* 36 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 41 */     return this.name;
/*    */   }
/*    */   
/*    */   public int getMaxInstancesPerChunk() {
/* 45 */     return this.max;
/*    */   }
/*    */   
/*    */   public boolean isFriendly() {
/* 49 */     return this.isFriendly;
/*    */   }
/*    */   
/*    */   public boolean isPersistent() {
/* 53 */     return this.isPersistent;
/*    */   }
/*    */   
/*    */   public int getDespawnDistance() {
/* 57 */     return this.despawnDistance;
/*    */   }
/*    */   
/*    */   public int getNoDespawnDistance() {
/* 61 */     return 32;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/MobCategory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
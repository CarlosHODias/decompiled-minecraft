/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class JigsawJunction
/*    */ {
/*    */   private final int sourceX;
/*    */   private final int sourceGroundY;
/*    */   
/*    */   public JigsawJunction(int sourceX, int sourceGroundY, int sourceZ, int deltaY, StructureTemplatePool.Projection destProjection) {
/* 15 */     this.sourceX = sourceX;
/* 16 */     this.sourceGroundY = sourceGroundY;
/* 17 */     this.sourceZ = sourceZ;
/* 18 */     this.deltaY = deltaY;
/* 19 */     this.destProjection = destProjection;
/*    */   }
/*    */   private final int sourceZ; private final int deltaY; private final StructureTemplatePool.Projection destProjection;
/*    */   public int getSourceX() {
/* 23 */     return this.sourceX;
/*    */   }
/*    */   
/*    */   public int getSourceGroundY() {
/* 27 */     return this.sourceGroundY;
/*    */   }
/*    */   
/*    */   public int getSourceZ() {
/* 31 */     return this.sourceZ;
/*    */   }
/*    */   
/*    */   public int getDeltaY() {
/* 35 */     return this.deltaY;
/*    */   }
/*    */   
/*    */   public StructureTemplatePool.Projection getDestProjection() {
/* 39 */     return this.destProjection;
/*    */   }
/*    */   
/*    */   public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
/* 43 */     ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();
/*    */     
/* 45 */     builder.put(ops.createString("source_x"), ops.createInt(this.sourceX))
/* 46 */       .put(ops.createString("source_ground_y"), ops.createInt(this.sourceGroundY))
/* 47 */       .put(ops.createString("source_z"), ops.createInt(this.sourceZ))
/* 48 */       .put(ops.createString("delta_y"), ops.createInt(this.deltaY))
/* 49 */       .put(ops.createString("dest_proj"), ops.createString(this.destProjection.getName()));
/*    */     
/* 51 */     return new Dynamic(ops, ops.createMap((Map)builder.build()));
/*    */   }
/*    */   
/*    */   public static <T> JigsawJunction deserialize(Dynamic<T> input) {
/* 55 */     return new JigsawJunction(
/* 56 */         input.get("source_x").asInt(0), 
/* 57 */         input.get("source_ground_y").asInt(0), 
/* 58 */         input.get("source_z").asInt(0), 
/* 59 */         input.get("delta_y").asInt(0), 
/* 60 */         StructureTemplatePool.Projection.byName(input.get("dest_proj").asString("")));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 66 */     if (this == o) {
/* 67 */       return true;
/*    */     }
/* 69 */     if (o == null || getClass() != o.getClass()) {
/* 70 */       return false;
/*    */     }
/*    */     
/* 73 */     JigsawJunction that = (JigsawJunction)o;
/*    */     
/* 75 */     if (this.sourceX != that.sourceX) {
/* 76 */       return false;
/*    */     }
/* 78 */     if (this.sourceZ != that.sourceZ) {
/* 79 */       return false;
/*    */     }
/* 81 */     if (this.deltaY != that.deltaY) {
/* 82 */       return false;
/*    */     }
/* 84 */     return (this.destProjection == that.destProjection);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 89 */     int result = this.sourceX;
/* 90 */     result = 31 * result + this.sourceGroundY;
/* 91 */     result = 31 * result + this.sourceZ;
/* 92 */     result = 31 * result + this.deltaY;
/* 93 */     result = 31 * result + this.destProjection.hashCode();
/* 94 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 99 */     return "JigsawJunction{sourceX=" + this.sourceX + ", sourceGroundY=" + this.sourceGroundY + ", sourceZ=" + this.sourceZ + ", deltaY=" + this.deltaY + ", destProjection=" + String.valueOf(this.destProjection) + "}";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/pools/JigsawJunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
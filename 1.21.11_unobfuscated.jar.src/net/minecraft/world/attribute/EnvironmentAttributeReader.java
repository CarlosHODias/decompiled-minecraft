/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface EnvironmentAttributeReader {
/*  8 */   public static final EnvironmentAttributeReader EMPTY = new EnvironmentAttributeReader()
/*    */     {
/*    */       public <Value> Value getDimensionValue(EnvironmentAttribute<Value> attribute) {
/* 11 */         return attribute.defaultValue();
/*    */       }
/*    */ 
/*    */       
/*    */       public <Value> Value getValue(EnvironmentAttribute<Value> attribute, Vec3 pos, SpatialAttributeInterpolator biomeInterpolator) {
/* 16 */         return attribute.defaultValue();
/*    */       }
/*    */     };
/*    */   
/*    */   <Value> Value getDimensionValue(EnvironmentAttribute<Value> paramEnvironmentAttribute);
/*    */   
/*    */   default <Value> Value getValue(EnvironmentAttribute<Value> attribute, BlockPos pos) {
/* 23 */     return getValue(attribute, Vec3.atCenterOf((Vec3i)pos));
/*    */   }
/*    */   
/*    */   default <Value> Value getValue(EnvironmentAttribute<Value> attribute, Vec3 pos) {
/* 27 */     return getValue(attribute, pos, null);
/*    */   }
/*    */   
/*    */   <Value> Value getValue(EnvironmentAttribute<Value> paramEnvironmentAttribute, Vec3 paramVec3, SpatialAttributeInterpolator paramSpatialAttributeInterpolator);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/attribute/EnvironmentAttributeReader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
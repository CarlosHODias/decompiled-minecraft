/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityFurnaceBurnTimeFix
/*    */   extends NamedEntityFix {
/*    */   public BlockEntityFurnaceBurnTimeFix(Schema outputSchema, String entityType) {
/* 11 */     super(outputSchema, false, "BlockEntityFurnaceBurnTimeFix" + entityType, References.BLOCK_ENTITY, entityType);
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixBurnTime(Dynamic<?> data) {
/* 15 */     data = data.renameField("CookTime", "cooking_time_spent");
/* 16 */     data = data.renameField("CookTimeTotal", "cooking_total_time");
/* 17 */     data = data.renameField("BurnTime", "lit_time_remaining");
/*    */     
/* 19 */     data = data.setFieldIfPresent("lit_total_time", data.get("lit_time_remaining").result());
/* 20 */     return data;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 25 */     return entity.update(DSL.remainderFinder(), this::fixBurnTime);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/BlockEntityFurnaceBurnTimeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
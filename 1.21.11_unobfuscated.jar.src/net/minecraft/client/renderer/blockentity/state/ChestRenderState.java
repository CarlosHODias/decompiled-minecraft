/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import net.minecraft.world.level.block.state.properties.ChestType;
/*    */ 
/*    */ public class ChestRenderState extends BlockEntityRenderState {
/*  6 */   public ChestType type = ChestType.SINGLE;
/*    */   public float open;
/*    */   public float angle;
/*  9 */   public ChestMaterialType material = ChestMaterialType.REGULAR;
/*    */   
/*    */   public enum ChestMaterialType {
/* 12 */     ENDER_CHEST,
/* 13 */     CHRISTMAS,
/* 14 */     TRAPPED,
/* 15 */     COPPER_UNAFFECTED,
/* 16 */     COPPER_EXPOSED,
/* 17 */     COPPER_WEATHERED,
/* 18 */     COPPER_OXIDIZED,
/* 19 */     REGULAR;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/state/ChestRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
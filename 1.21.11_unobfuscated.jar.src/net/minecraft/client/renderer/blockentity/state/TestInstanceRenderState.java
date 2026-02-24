/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*    */ 
/*    */ public class TestInstanceRenderState
/*    */   extends BlockEntityRenderState {
/*    */   public BeaconRenderState beaconRenderState;
/*    */   public BlockEntityWithBoundingBoxRenderState blockEntityWithBoundingBoxRenderState;
/* 11 */   public final List<TestInstanceBlockEntity.ErrorMarker> errorMarkers = new ArrayList<>();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/state/TestInstanceRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
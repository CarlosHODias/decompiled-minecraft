/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.client.renderer.state.MapRenderState;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.saveddata.maps.MapId;
/*    */ 
/*    */ public class ItemFrameRenderState
/*    */   extends EntityRenderState {
/* 10 */   public Direction direction = Direction.NORTH;
/* 11 */   public final ItemStackRenderState item = new ItemStackRenderState();
/*    */   public int rotation;
/*    */   public boolean isGlowFrame;
/*    */   public MapId mapId;
/* 15 */   public final MapRenderState mapRenderState = new MapRenderState();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/ItemFrameRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
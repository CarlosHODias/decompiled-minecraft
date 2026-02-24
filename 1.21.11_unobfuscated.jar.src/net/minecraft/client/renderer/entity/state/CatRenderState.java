/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public class CatRenderState
/*    */   extends FelineRenderState {
/*  8 */   private static final Identifier DEFAULT_TEXTURE = Identifier.withDefaultNamespace("textures/entity/cat/tabby.png");
/*    */   
/* 10 */   public Identifier texture = DEFAULT_TEXTURE;
/*    */   public boolean isLyingOnTopOfSleepingPlayer;
/*    */   public DyeColor collarColor;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/CatRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
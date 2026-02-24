/*    */ package net.minecraft.client.renderer.state;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ public class MapRenderState
/*    */ {
/*    */   public Identifier texture;
/* 13 */   public final List<MapDecorationRenderState> decorations = new ArrayList<>();
/*    */   
/*    */   public static class MapDecorationRenderState {
/*    */     public TextureAtlasSprite atlasSprite;
/*    */     public byte x;
/*    */     public byte y;
/*    */     public byte rot;
/*    */     public boolean renderOnFrame;
/*    */     public Component name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/state/MapRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
/*    */ package net.minecraft.client.gui.render.state.pip;public final class GuiBannerResultRenderState extends Record implements PictureInPictureRenderState { private final net.minecraft.client.model.object.banner.BannerFlagModel flag; private final net.minecraft.world.item.DyeColor baseColor; private final net.minecraft.world.level.block.entity.BannerPatternLayers resultBannerPatterns;
/*    */   private final int x0;
/*    */   private final int y0;
/*    */   private final int x1;
/*    */   private final int y1;
/*    */   private final net.minecraft.client.gui.navigation.ScreenRectangle scissorArea;
/*    */   private final net.minecraft.client.gui.navigation.ScreenRectangle bounds;
/*    */   
/*  9 */   public GuiBannerResultRenderState(net.minecraft.client.model.object.banner.BannerFlagModel flag, net.minecraft.world.item.DyeColor baseColor, net.minecraft.world.level.block.entity.BannerPatternLayers resultBannerPatterns, int x0, int y0, int x1, int y1, net.minecraft.client.gui.navigation.ScreenRectangle scissorArea, net.minecraft.client.gui.navigation.ScreenRectangle bounds) { this.flag = flag; this.baseColor = baseColor; this.resultBannerPatterns = resultBannerPatterns; this.x0 = x0; this.y0 = y0; this.x1 = x1; this.y1 = y1; this.scissorArea = scissorArea; this.bounds = bounds; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/render/state/pip/GuiBannerResultRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/client/gui/render/state/pip/GuiBannerResultRenderState; } public net.minecraft.client.model.object.banner.BannerFlagModel flag() { return this.flag; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/render/state/pip/GuiBannerResultRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/render/state/pip/GuiBannerResultRenderState; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/render/state/pip/GuiBannerResultRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/render/state/pip/GuiBannerResultRenderState;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.item.DyeColor baseColor() { return this.baseColor; } public net.minecraft.world.level.block.entity.BannerPatternLayers resultBannerPatterns() { return this.resultBannerPatterns; } public int x0() { return this.x0; } public int y0() { return this.y0; } public int x1() { return this.x1; } public int y1() { return this.y1; } public net.minecraft.client.gui.navigation.ScreenRectangle scissorArea() { return this.scissorArea; } public net.minecraft.client.gui.navigation.ScreenRectangle bounds() { return this.bounds; }
/*    */   
/*    */   public GuiBannerResultRenderState(net.minecraft.client.model.object.banner.BannerFlagModel flag, net.minecraft.world.item.DyeColor baseColor, net.minecraft.world.level.block.entity.BannerPatternLayers resultBannerPatterns, int x0, int y0, int x1, int y1, net.minecraft.client.gui.navigation.ScreenRectangle scissorArea) {
/* 12 */     this(flag, baseColor, resultBannerPatterns, x0, y0, x1, y1, scissorArea, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
/*    */   }
/*    */ 
/*    */   
/*    */   public float scale() {
/* 17 */     return 16.0F;
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/state/pip/GuiBannerResultRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
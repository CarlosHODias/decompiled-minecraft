/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.CloudStatus;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.TextureFilteringMethod;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ 
/*    */ public class DebugEntrySimplePerformanceImpactors
/*    */   implements DebugScreenEntry
/*    */ {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 16 */     Minecraft minecraft = Minecraft.getInstance();
/* 17 */     Options options = minecraft.options;
/*    */     
/* 19 */     displayer.addLine(String.format(Locale.ROOT, "%s%s B: %d", new Object[] {
/* 20 */             (Boolean)options.improvedTransparency().get() ? "improved-transparency" : "", 
/* 21 */             (options.cloudStatus().get() == CloudStatus.OFF) ? "" : ((options.cloudStatus().get() == CloudStatus.FAST) ? " fast-clouds" : " fancy-clouds"), 
/* 22 */             options.biomeBlendRadius().get()
/*    */           }));
/*    */     
/* 25 */     TextureFilteringMethod filteringMethod = (TextureFilteringMethod)options.textureFiltering().get();
/* 26 */     if (filteringMethod == TextureFilteringMethod.ANISOTROPIC) {
/* 27 */       displayer.addLine(String.format(Locale.ROOT, "Filtering: %s %dx", new Object[] { filteringMethod.caption().getString(), options.maxAnisotropyValue() }));
/*    */     } else {
/* 29 */       displayer.addLine(String.format(Locale.ROOT, "Filtering: %s", new Object[] { filteringMethod.caption().getString() }));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 35 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntrySimplePerformanceImpactors.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
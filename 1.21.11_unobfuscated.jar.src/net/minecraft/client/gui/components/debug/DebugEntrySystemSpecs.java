/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.GLX;
/*    */ import com.mojang.blaze3d.systems.GpuDevice;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntrySystemSpecs
/*    */   implements DebugScreenEntry
/*    */ {
/* 16 */   private static final Identifier GROUP = Identifier.withDefaultNamespace("system");
/*    */ 
/*    */   
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 20 */     GpuDevice device = RenderSystem.getDevice();
/*    */     
/* 22 */     displayer.addToGroup(GROUP, List.of(
/* 23 */           String.format(Locale.ROOT, "Java: %s", new Object[] { System.getProperty("java.version")
/* 24 */             }), String.format(Locale.ROOT, "CPU: %s", new Object[] { GLX._getCpuInfo()
/* 25 */             }), String.format(Locale.ROOT, "Display: %dx%d (%s)", new Object[] { Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight(), device.getVendor()
/* 26 */             }), device.getRenderer(), 
/* 27 */           String.format(Locale.ROOT, "%s %s", new Object[] { device.getBackendName(), device.getVersion() })));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 33 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntrySystemSpecs.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
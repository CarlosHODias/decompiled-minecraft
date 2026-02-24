/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ 
/*    */ public class DebugEntryFps
/*    */   implements DebugScreenEntry
/*    */ {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 14 */     Minecraft minecraft = Minecraft.getInstance();
/* 15 */     int framerateLimit = minecraft.getFramerateLimitTracker().getFramerateLimit();
/* 16 */     Options options = minecraft.options;
/*    */     
/* 18 */     displayer.addPriorityLine(String.format(Locale.ROOT, "%d fps T: %s%s", new Object[] {
/* 19 */             minecraft.getFps(), 
/* 20 */             (framerateLimit == 260) ? "inf" : framerateLimit, 
/* 21 */             (Boolean)options.enableVsync().get() ? " vsync" : ""
/*    */           }));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 27 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntryFps.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
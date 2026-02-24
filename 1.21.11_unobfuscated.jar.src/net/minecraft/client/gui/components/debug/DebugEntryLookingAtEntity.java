/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryLookingAtEntity
/*    */   implements DebugScreenEntry
/*    */ {
/* 16 */   private static final Identifier GROUP = Identifier.withDefaultNamespace("looking_at_entity");
/*    */ 
/*    */   
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 20 */     Minecraft minecraft = Minecraft.getInstance();
/* 21 */     Entity entity = minecraft.crosshairPickEntity;
/* 22 */     List<String> result = new ArrayList<>();
/* 23 */     if (entity != null) {
/* 24 */       result.add(String.valueOf(ChatFormatting.UNDERLINE) + "Targeted Entity");
/* 25 */       result.add(String.valueOf(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType())));
/*    */     } 
/*    */ 
/*    */     
/* 29 */     displayer.addToGroup(GROUP, result);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntryLookingAtEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class DebugEntryLookingAtBlock implements DebugScreenEntry {
/* 24 */   private static final Identifier GROUP = Identifier.withDefaultNamespace("looking_at_block");
/*    */ 
/*    */   
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 28 */     Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
/* 29 */     Level clientOrServerLevel = SharedConstants.DEBUG_SHOW_SERVER_DEBUG_VALUES ? serverOrClientLevel : (Level)(Minecraft.getInstance()).level;
/* 30 */     if (cameraEntity == null || clientOrServerLevel == null) {
/*    */       return;
/*    */     }
/*    */     
/* 34 */     HitResult block = cameraEntity.pick(20.0D, 0.0F, false);
/* 35 */     List<String> result = new ArrayList<>();
/*    */     
/* 37 */     if (block.getType() == HitResult.Type.BLOCK) {
/* 38 */       BlockPos pos = ((BlockHitResult)block).getBlockPos();
/* 39 */       BlockState blockState = clientOrServerLevel.getBlockState(pos);
/*    */       
/* 41 */       result.add(String.valueOf(ChatFormatting.UNDERLINE) + "Targeted Block: " + String.valueOf(ChatFormatting.UNDERLINE) + ", " + pos.getX() + ", " + pos.getY());
/* 42 */       result.add(String.valueOf(BuiltInRegistries.BLOCK.getKey(blockState.getBlock())));
/*    */       
/* 44 */       for (Map.Entry<Property<?>, Comparable<?>> entry : (Iterable<Map.Entry<Property<?>, Comparable<?>>>)blockState.getValues().entrySet()) {
/* 45 */         result.add(getPropertyValueString(entry));
/*    */       }
/*    */       
/* 48 */       Objects.requireNonNull(result); blockState.getTags().map(e -> "#" + String.valueOf(e.location())).forEach(result::add);
/*    */     } 
/*    */ 
/*    */     
/* 52 */     displayer.addToGroup(GROUP, result);
/*    */   }
/*    */   
/*    */   private String getPropertyValueString(Map.Entry<Property<?>, Comparable<?>> entry) {
/* 56 */     Property<?> property = entry.getKey();
/* 57 */     Comparable<?> value = entry.getValue();
/* 58 */     String valueString = Util.getPropertyName(property, value);
/*    */     
/* 60 */     if (Boolean.TRUE.equals(value)) {
/* 61 */       valueString = String.valueOf(ChatFormatting.GREEN) + String.valueOf(ChatFormatting.GREEN);
/* 62 */     } else if (Boolean.FALSE.equals(value)) {
/* 63 */       valueString = String.valueOf(ChatFormatting.RED) + String.valueOf(ChatFormatting.RED);
/*    */     } 
/*    */     
/* 66 */     return property.getName() + ": " + property.getName();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntryLookingAtBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
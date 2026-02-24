/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum StructureMode implements StringRepresentable {
/*  9 */   SAVE("save"),
/* 10 */   LOAD("load"),
/* 11 */   CORNER("corner"),
/* 12 */   DATA("data");
/*    */ 
/*    */   
/*    */   @Deprecated
/* 16 */   public static final Codec<StructureMode> LEGACY_CODEC = ExtraCodecs.legacyEnum(StructureMode::valueOf);
/*    */   
/*    */   private final String name;
/*    */   private final Component displayName;
/*    */   
/*    */   StructureMode(String name) {
/* 22 */     this.name = name;
/* 23 */     this.displayName = (Component)Component.translatable("structure_block.mode_info." + name);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 28 */     return this.name;
/*    */   }
/*    */   
/*    */   public Component getDisplayName() {
/* 32 */     return this.displayName;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/StructureMode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
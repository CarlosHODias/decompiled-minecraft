/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WorldPresetTags
/*    */ {
/* 12 */   public static final TagKey<WorldPreset> NORMAL = create("normal");
/*    */   
/* 14 */   public static final TagKey<WorldPreset> EXTENDED = create("extended");
/*    */   
/*    */   private static TagKey<WorldPreset> create(String name) {
/* 17 */     return TagKey.create(Registries.WORLD_PRESET, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/tags/WorldPresetTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
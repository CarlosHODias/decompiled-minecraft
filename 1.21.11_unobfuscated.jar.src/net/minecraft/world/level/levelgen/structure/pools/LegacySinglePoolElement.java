/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ 
/*    */ public class LegacySinglePoolElement extends SinglePoolElement {
/*    */   static {
/* 23 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)templateCodec(), (App)processorsCodec(), (App)projectionCodec(), (App)overrideLiquidSettingsCodec()).apply((Applicative)i, LegacySinglePoolElement::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<LegacySinglePoolElement> CODEC;
/*    */ 
/*    */   
/*    */   protected LegacySinglePoolElement(Either<Identifier, StructureTemplate> template, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection projection, Optional<LiquidSettings> liquidSettings) {
/* 31 */     super(template, processors, projection, liquidSettings);
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructurePlaceSettings getSettings(Rotation rotation, BoundingBox chunkBB, LiquidSettings liquidSettings, boolean keepJigsaws) {
/* 36 */     StructurePlaceSettings settings = super.getSettings(rotation, chunkBB, liquidSettings, keepJigsaws);
/* 37 */     settings.popProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK);
/* 38 */     settings.addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR);
/* 39 */     return settings;
/*    */   }
/*    */ 
/*    */   
/*    */   public StructurePoolElementType<?> getType() {
/* 44 */     return StructurePoolElementType.LEGACY;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 49 */     return "LegacySingle[" + String.valueOf(this.template) + "]";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/pools/LegacySinglePoolElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
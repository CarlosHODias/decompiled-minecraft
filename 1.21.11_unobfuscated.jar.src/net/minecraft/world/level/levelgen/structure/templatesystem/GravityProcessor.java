/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class GravityProcessor extends StructureProcessor {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Heightmap.Types.CODEC.fieldOf("heightmap").orElse(Heightmap.Types.WORLD_SURFACE_WG).forGetter(()), (App)Codec.INT.fieldOf("offset").orElse(0).forGetter(())).apply((Applicative)i, GravityProcessor::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<GravityProcessor> CODEC;
/*    */   private final Heightmap.Types heightmap;
/*    */   private final int offset;
/*    */   
/*    */   public GravityProcessor(Heightmap.Types heightmap, int offset) {
/* 22 */     this.heightmap = heightmap;
/* 23 */     this.offset = offset;
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/*    */     Heightmap.Types heightmap;
/* 29 */     if (level instanceof net.minecraft.server.level.ServerLevel) {
/*    */       
/* 31 */       if (this.heightmap == Heightmap.Types.WORLD_SURFACE_WG) {
/* 32 */         heightmap = Heightmap.Types.WORLD_SURFACE;
/* 33 */       } else if (this.heightmap == Heightmap.Types.OCEAN_FLOOR_WG) {
/* 34 */         heightmap = Heightmap.Types.OCEAN_FLOOR;
/*    */       } else {
/* 36 */         heightmap = this.heightmap;
/*    */       } 
/*    */     } else {
/* 39 */       heightmap = this.heightmap;
/*    */     } 
/* 41 */     BlockPos pos = processedBlockInfo.pos();
/* 42 */     int height = level.getHeight(heightmap, pos.getX(), pos.getZ()) + this.offset;
/* 43 */     int delta = originalBlockInfo.pos().getY();
/* 44 */     return new StructureTemplate.StructureBlockInfo(new BlockPos(pos.getX(), height + delta, pos.getZ()), processedBlockInfo.state(), processedBlockInfo.nbt());
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 49 */     return StructureProcessorType.GRAVITY;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/GravityProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
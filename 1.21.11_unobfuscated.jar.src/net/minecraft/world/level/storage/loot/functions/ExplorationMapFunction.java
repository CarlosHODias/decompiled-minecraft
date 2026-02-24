/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.StructureTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.saveddata.maps.MapDecorationType;
/*     */ import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ExplorationMapFunction extends LootItemConditionalFunction {
/*  30 */   public static final TagKey<Structure> DEFAULT_DESTINATION = StructureTags.ON_TREASURE_MAPS;
/*  31 */   public static final Holder<MapDecorationType> DEFAULT_DECORATION = MapDecorationTypes.WOODLAND_MANSION; public static final byte DEFAULT_ZOOM = 2; public static final int DEFAULT_SEARCH_RADIUS = 50;
/*     */   public static final boolean DEFAULT_SKIP_EXISTING = true;
/*     */   public static final com.mojang.serialization.MapCodec<ExplorationMapFunction> CODEC;
/*     */   
/*     */   static {
/*  36 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)TagKey.codec(Registries.STRUCTURE).optionalFieldOf("destination", DEFAULT_DESTINATION).forGetter(()), (App)MapDecorationType.CODEC.optionalFieldOf("decoration", DEFAULT_DECORATION).forGetter(()), (App)Codec.BYTE.optionalFieldOf("zoom", (byte)2).forGetter(()), (App)Codec.INT.optionalFieldOf("search_radius", 50).forGetter(()), (App)Codec.BOOL.optionalFieldOf("skip_existing_chunks", true).forGetter(()))).apply((Applicative)i, ExplorationMapFunction::new));
/*     */   }
/*     */ 
/*     */   
/*     */   private final TagKey<Structure> destination;
/*     */   
/*     */   private final Holder<MapDecorationType> mapDecoration;
/*     */   
/*     */   private final byte zoom;
/*     */   
/*     */   private final int searchRadius;
/*     */   
/*     */   private final boolean skipKnownStructures;
/*     */ 
/*     */   
/*     */   private ExplorationMapFunction(List<LootItemCondition> predicates, TagKey<Structure> destination, Holder<MapDecorationType> mapDecoration, byte zoom, int searchRadius, boolean skipKnownStructures) {
/*  52 */     super(predicates);
/*  53 */     this.destination = destination;
/*  54 */     this.mapDecoration = mapDecoration;
/*  55 */     this.zoom = zoom;
/*  56 */     this.searchRadius = searchRadius;
/*  57 */     this.skipKnownStructures = skipKnownStructures;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType<ExplorationMapFunction> getType() {
/*  62 */     return LootItemFunctions.EXPLORATION_MAP;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<ContextKey<?>> getReferencedContextParams() {
/*  67 */     return Set.of(LootContextParams.ORIGIN);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  72 */     if (!itemStack.is(Items.MAP)) {
/*  73 */       return itemStack;
/*     */     }
/*     */     
/*  76 */     Vec3 lootPos = (Vec3)context.getOptionalParameter(LootContextParams.ORIGIN);
/*  77 */     if (lootPos != null) {
/*  78 */       ServerLevel level = context.getLevel();
/*     */       
/*  80 */       BlockPos nearestMapStructure = level.findNearestMapStructure(this.destination, BlockPos.containing((Position)lootPos), this.searchRadius, this.skipKnownStructures);
/*  81 */       if (nearestMapStructure != null) {
/*  82 */         ItemStack map = MapItem.create(level, nearestMapStructure.getX(), nearestMapStructure.getZ(), this.zoom, true, true);
/*  83 */         MapItem.renderBiomePreviewMap(level, map);
/*  84 */         MapItemSavedData.addTargetDecoration(map, nearestMapStructure, "+", this.mapDecoration);
/*  85 */         return map;
/*     */       } 
/*     */     } 
/*     */     
/*  89 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*  93 */     private TagKey<Structure> destination = ExplorationMapFunction.DEFAULT_DESTINATION;
/*  94 */     private Holder<MapDecorationType> mapDecoration = ExplorationMapFunction.DEFAULT_DECORATION;
/*  95 */     private byte zoom = 2;
/*  96 */     private int searchRadius = 50;
/*     */     
/*     */     private boolean skipKnownStructures = true;
/*     */     
/*     */     protected Builder getThis() {
/* 101 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDestination(TagKey<Structure> destination) {
/* 105 */       this.destination = destination;
/* 106 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMapDecoration(Holder<MapDecorationType> mapDecoration) {
/* 110 */       this.mapDecoration = mapDecoration;
/* 111 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setZoom(byte zoom) {
/* 115 */       this.zoom = zoom;
/* 116 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSearchRadius(int searchRadius) {
/* 120 */       this.searchRadius = searchRadius;
/* 121 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSkipKnownStructures(boolean skipKnownStructures) {
/* 125 */       this.skipKnownStructures = skipKnownStructures;
/* 126 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootItemFunction build() {
/* 131 */       return new ExplorationMapFunction(getConditions(), this.destination, this.mapDecoration, this.zoom, this.searchRadius, this.skipKnownStructures);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder makeExplorationMap() {
/* 136 */     return new Builder();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/ExplorationMapFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
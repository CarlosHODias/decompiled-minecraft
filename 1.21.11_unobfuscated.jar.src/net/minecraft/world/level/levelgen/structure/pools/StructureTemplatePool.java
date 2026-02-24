/*     */ package net.minecraft.world.level.levelgen.structure.pools;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.GravityProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class StructureTemplatePool {
/*  30 */   private static final MutableObject<Codec<Holder<StructureTemplatePool>>> CODEC_REFERENCE = new MutableObject(); private static final int SIZE_UNSET = -2147483648; public static final Codec<StructureTemplatePool> DIRECT_CODEC;
/*     */   static {
/*  32 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.lazyInitialized((Supplier)CODEC_REFERENCE).fieldOf("fallback").forGetter(StructureTemplatePool::getFallback), (App)Codec.mapPair(StructurePoolElement.CODEC.fieldOf("element"), Codec.intRange(1, 150).fieldOf("weight")).codec().listOf().fieldOf("elements").forGetter(())).apply((Applicative)i, StructureTemplatePool::new));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  41 */     java.util.Objects.requireNonNull(CODEC_REFERENCE); } public static final Codec<Holder<StructureTemplatePool>> CODEC = (Codec<Holder<StructureTemplatePool>>)Util.make(RegistryFileCodec.create(Registries.TEMPLATE_POOL, DIRECT_CODEC), CODEC_REFERENCE::setValue); private final List<Pair<StructurePoolElement, Integer>> rawTemplates; private final ObjectArrayList<StructurePoolElement> templates;
/*     */   private final Holder<StructureTemplatePool> fallback;
/*     */   
/*  44 */   public enum Projection implements StringRepresentable { TERRAIN_MATCHING("terrain_matching", 
/*     */       
/*  46 */       ImmutableList.of(new GravityProcessor(Heightmap.Types.WORLD_SURFACE_WG, -1))),
/*     */     
/*  48 */     RIGID("rigid", 
/*     */       
/*  50 */       ImmutableList.of());
/*     */ 
/*     */     
/*  53 */     public static final StringRepresentable.EnumCodec<Projection> CODEC = StringRepresentable.fromEnum(Projection::values);
/*     */     
/*     */     private final String name;
/*     */     private final ImmutableList<StructureProcessor> processors;
/*     */     
/*     */     Projection(String name, ImmutableList<StructureProcessor> processors) {
/*  59 */       this.name = name;
/*  60 */       this.processors = processors;
/*     */     }
/*     */     
/*     */     public String getName() {
/*  64 */       return this.name;
/*     */     }
/*     */     
/*     */     public static Projection byName(String name) {
/*  68 */       return (Projection)CODEC.byName(name);
/*     */     }
/*     */     
/*     */     public ImmutableList<StructureProcessor> getProcessors() {
/*  72 */       return this.processors;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/*  77 */       return this.name;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   private int maxSize = Integer.MIN_VALUE;
/*     */   
/*     */   public StructureTemplatePool(Holder<StructureTemplatePool> fallback, List<Pair<StructurePoolElement, Integer>> templates) {
/*  87 */     this.rawTemplates = templates;
/*  88 */     this.templates = new ObjectArrayList();
/*  89 */     for (Pair<StructurePoolElement, Integer> templateDef : templates) {
/*  90 */       StructurePoolElement element = (StructurePoolElement)templateDef.getFirst();
/*  91 */       for (int i = 0; i < (Integer)templateDef.getSecond(); i++) {
/*  92 */         this.templates.add(element);
/*     */       }
/*     */     } 
/*     */     
/*  96 */     this.fallback = fallback;
/*     */   }
/*     */   
/*     */   public StructureTemplatePool(Holder<StructureTemplatePool> fallback, List<Pair<Function<Projection, ? extends StructurePoolElement>, Integer>> templates, Projection projection) {
/* 100 */     this.rawTemplates = Lists.newArrayList();
/* 101 */     this.templates = new ObjectArrayList();
/* 102 */     for (Pair<Function<Projection, ? extends StructurePoolElement>, Integer> templateDef : templates) {
/* 103 */       StructurePoolElement element = ((Function<Projection, StructurePoolElement>)templateDef.getFirst()).apply(projection);
/* 104 */       this.rawTemplates.add(Pair.of(element, templateDef.getSecond()));
/* 105 */       for (int i = 0; i < (Integer)templateDef.getSecond(); i++) {
/* 106 */         this.templates.add(element);
/*     */       }
/*     */     } 
/*     */     
/* 110 */     this.fallback = fallback;
/*     */   }
/*     */   
/*     */   public int getMaxSize(StructureTemplateManager manager) {
/* 114 */     if (this.maxSize == Integer.MIN_VALUE) {
/* 115 */       this
/*     */ 
/*     */ 
/*     */         
/* 119 */         .maxSize = this.templates.stream().filter(t -> (t != EmptyPoolElement.INSTANCE)).mapToInt(t -> t.getBoundingBox(manager, BlockPos.ZERO, Rotation.NONE).getYSpan()).max().orElse(0);
/*     */     }
/* 121 */     return this.maxSize;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public List<Pair<StructurePoolElement, Integer>> getTemplates() {
/* 126 */     return this.rawTemplates;
/*     */   }
/*     */   
/*     */   public Holder<StructureTemplatePool> getFallback() {
/* 130 */     return this.fallback;
/*     */   }
/*     */   
/*     */   public StructurePoolElement getRandomTemplate(RandomSource random) {
/* 134 */     if (this.templates.isEmpty()) {
/* 135 */       return EmptyPoolElement.INSTANCE;
/*     */     }
/* 137 */     return (StructurePoolElement)this.templates.get(random.nextInt(this.templates.size()));
/*     */   }
/*     */   
/*     */   public List<StructurePoolElement> getShuffledTemplates(RandomSource random) {
/* 141 */     return Util.shuffledCopy(this.templates, random);
/*     */   }
/*     */   
/*     */   public int size() {
/* 145 */     return this.templates.size();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/pools/StructureTemplatePool.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
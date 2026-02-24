/*     */ package net.minecraft.client.data.models.blockstates;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.data.models.MultiVariant;
/*     */ import net.minecraft.client.renderer.block.model.BlockModelDefinition;
/*     */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*     */ import net.minecraft.client.renderer.block.model.VariantMutator;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class MultiVariantGenerator
/*     */   implements BlockModelDefinitionGenerator {
/*     */   private final Block block;
/*     */   private final List<Entry> entries;
/*     */   private final Set<Property<?>> seenProperties;
/*     */   
/*     */   private MultiVariantGenerator(Block block, List<Entry> entries, Set<Property<?>> seenProperties) {
/*  24 */     this.block = block;
/*  25 */     this.entries = entries;
/*  26 */     this.seenProperties = seenProperties;
/*     */   }
/*     */   
/*     */   private static Set<Property<?>> validateAndExpandProperties(Set<Property<?>> seenProperties, Block block, PropertyDispatch<?> generator) {
/*  30 */     List<Property<?>> addedProperties = generator.getDefinedProperties();
/*     */     
/*  32 */     addedProperties.forEach(property -> {
/*     */           if (block.getStateDefinition().getProperty(property.getName()) != property) {
/*     */             throw new IllegalStateException("Property " + String.valueOf(property) + " is not defined for block " + String.valueOf(block));
/*     */           }
/*     */           
/*     */           if (seenProperties.contains(property)) {
/*     */             throw new IllegalStateException("Values of property " + String.valueOf(property) + " already defined for block " + String.valueOf(block));
/*     */           }
/*     */         });
/*     */     
/*  42 */     Set<Property<?>> newSeenProperties = new HashSet<>(seenProperties);
/*  43 */     newSeenProperties.addAll(addedProperties);
/*  44 */     return newSeenProperties;
/*     */   }
/*     */   
/*     */   public MultiVariantGenerator with(PropertyDispatch<VariantMutator> newStage) {
/*  48 */     Set<Property<?>> newSeenProperties = validateAndExpandProperties(this.seenProperties, this.block, newStage);
/*     */ 
/*     */     
/*  51 */     List<Entry> newEntries = this.entries.stream()
/*  52 */       .flatMap(entry -> entry.apply(newStage))
/*  53 */       .toList();
/*     */     
/*  55 */     return new MultiVariantGenerator(this.block, newEntries, newSeenProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiVariantGenerator with(VariantMutator singleMutator) {
/*  60 */     List<Entry> newEntries = this.entries.stream()
/*  61 */       .flatMap(entry -> entry.apply(singleMutator))
/*  62 */       .toList();
/*     */     
/*  64 */     return new MultiVariantGenerator(this.block, newEntries, this.seenProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockModelDefinition create() {
/*  69 */     Map<String, BlockStateModel.Unbaked> variants = new HashMap<>();
/*  70 */     for (Entry entry : this.entries) {
/*  71 */       variants.put(entry.properties.getKey(), entry.variant.toUnbaked());
/*     */     }
/*  73 */     return new BlockModelDefinition(
/*  74 */         Optional.of(new BlockModelDefinition.SimpleModelSelectors(variants)), 
/*  75 */         Optional.empty());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Block block() {
/*  81 */     return this.block;
/*     */   }
/*     */   
/*     */   public static Empty dispatch(Block block) {
/*  85 */     return new Empty(block);
/*     */   }
/*     */   
/*     */   public static MultiVariantGenerator dispatch(Block block, MultiVariant initialModel) {
/*  89 */     return new MultiVariantGenerator(block, List.of(new Entry(PropertyValueList.EMPTY, initialModel)), Set.of());
/*     */   }
/*     */   
/*     */   public static class Empty {
/*     */     private final Block block;
/*     */     
/*     */     public Empty(Block block) {
/*  96 */       this.block = block;
/*     */     }
/*     */     
/*     */     public MultiVariantGenerator with(PropertyDispatch<MultiVariant> newStage) {
/* 100 */       Set<Property<?>> newSeenProperties = MultiVariantGenerator.validateAndExpandProperties(Set.of(), this.block, newStage);
/* 101 */       List<MultiVariantGenerator.Entry> newEntries = newStage.getEntries().entrySet().stream().map(e -> new MultiVariantGenerator.Entry((PropertyValueList)e.getKey(), (MultiVariant)e.getValue())).toList();
/* 102 */       return new MultiVariantGenerator(this.block, newEntries, newSeenProperties);
/*     */     } }
/*     */   private static final class Entry extends Record { private final PropertyValueList properties; private final MultiVariant variant;
/*     */     
/* 106 */     private Entry(PropertyValueList properties, MultiVariant variant) { this.properties = properties; this.variant = variant; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/data/models/blockstates/MultiVariantGenerator$Entry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #106	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 106 */       //   0	7	0	this	Lnet/minecraft/client/data/models/blockstates/MultiVariantGenerator$Entry; } public PropertyValueList properties() { return this.properties; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/data/models/blockstates/MultiVariantGenerator$Entry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #106	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/data/models/blockstates/MultiVariantGenerator$Entry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/data/models/blockstates/MultiVariantGenerator$Entry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #106	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/data/models/blockstates/MultiVariantGenerator$Entry;
/* 106 */       //   0	8	1	o	Ljava/lang/Object; } public MultiVariant variant() { return this.variant; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Stream<Entry> apply(PropertyDispatch<VariantMutator> stage) {
/* 112 */       return stage.getEntries().entrySet().stream().map(property -> {
/*     */             PropertyValueList newSelector = this.properties.extend((PropertyValueList)property.getKey());
/*     */             MultiVariant newVariants = this.variant.with((VariantMutator)property.getValue());
/*     */             return new Entry(newSelector, newVariants);
/*     */           });
/*     */     }
/*     */     
/*     */     public Stream<Entry> apply(VariantMutator mutator) {
/* 120 */       return Stream.of(new Entry(this.properties, this.variant.with(mutator)));
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/blockstates/MultiVariantGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
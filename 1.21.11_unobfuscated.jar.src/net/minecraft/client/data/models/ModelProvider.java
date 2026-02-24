/*     */ package net.minecraft.client.data.models;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonElement;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
/*     */ import net.minecraft.client.data.models.model.ItemModelUtils;
/*     */ import net.minecraft.client.data.models.model.ModelInstance;
/*     */ import net.minecraft.client.data.models.model.ModelLocationUtils;
/*     */ import net.minecraft.client.renderer.block.model.BlockModelDefinition;
/*     */ import net.minecraft.client.renderer.item.ClientItem;
/*     */ import net.minecraft.client.renderer.item.ItemModel;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ public class ModelProvider
/*     */   implements DataProvider
/*     */ {
/*     */   private final PackOutput.PathProvider blockStatePathProvider;
/*     */   private final PackOutput.PathProvider itemInfoPathProvider;
/*     */   private final PackOutput.PathProvider modelPathProvider;
/*     */   
/*     */   public ModelProvider(PackOutput output) {
/*  41 */     this.blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
/*  42 */     this.itemInfoPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
/*  43 */     this.modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
/*     */   }
/*     */   
/*     */   private static class SimpleModelCollector implements BiConsumer<Identifier, ModelInstance> {
/*  47 */     private final Map<Identifier, ModelInstance> models = new HashMap<>();
/*     */ 
/*     */     
/*     */     public void accept(Identifier id, ModelInstance contents) {
/*  51 */       Supplier<JsonElement> prev = (Supplier<JsonElement>)this.models.put(id, contents);
/*  52 */       if (prev != null) {
/*  53 */         throw new IllegalStateException("Duplicate model definition for " + String.valueOf(id));
/*     */       }
/*     */     }
/*     */     
/*     */     public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
/*  58 */       Objects.requireNonNull(pathProvider); return DataProvider.saveAll(cache, Supplier::get, pathProvider::json, this.models);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class BlockStateGeneratorCollector implements Consumer<BlockModelDefinitionGenerator> {
/*  63 */     private final Map<Block, BlockModelDefinitionGenerator> generators = new HashMap<>();
/*     */ 
/*     */     
/*     */     public void accept(BlockModelDefinitionGenerator generator) {
/*  67 */       Block block = generator.block();
/*  68 */       BlockModelDefinitionGenerator prev = this.generators.put(block, generator);
/*  69 */       if (prev != null) {
/*  70 */         throw new IllegalStateException("Duplicate blockstate definition for " + String.valueOf(block));
/*     */       }
/*     */     }
/*     */     
/*     */     public void validate() {
/*  75 */       Stream<Holder.Reference<Block>> holders = BuiltInRegistries.BLOCK.listElements().filter(entry -> true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  83 */       List<Identifier> missingDefinitions = holders.filter(e -> !this.generators.containsKey(e.value()))
/*  84 */         .map(e -> e.key().identifier())
/*  85 */         .toList();
/*     */       
/*  87 */       if (!missingDefinitions.isEmpty()) {
/*  88 */         throw new IllegalStateException("Missing blockstate definitions for: " + String.valueOf(missingDefinitions));
/*     */       }
/*     */     }
/*     */     
/*     */     public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
/*  93 */       Map<Block, BlockModelDefinition> definitions = Maps.transformValues(this.generators, BlockModelDefinitionGenerator::create);
/*     */       Function<Block, Path> pathGetter = block -> pathProvider.json(block.builtInRegistryHolder().key().identifier());
/*  95 */       return DataProvider.saveAll(cache, BlockModelDefinition.CODEC, pathGetter, definitions);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ItemInfoCollector implements ItemModelOutput {
/* 100 */     private final Map<Item, ClientItem> itemInfos = new HashMap<>();
/* 101 */     private final Map<Item, Item> copies = new HashMap<>();
/*     */ 
/*     */     
/*     */     public void accept(Item item, ItemModel.Unbaked model, ClientItem.Properties properties) {
/* 105 */       register(item, new ClientItem(model, properties));
/*     */     }
/*     */     
/*     */     private void register(Item item, ClientItem itemInfo) {
/* 109 */       ClientItem prev = this.itemInfos.put(item, itemInfo);
/* 110 */       if (prev != null) {
/* 111 */         throw new IllegalStateException("Duplicate item model definition for " + String.valueOf(item));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void copy(Item donor, Item acceptor) {
/* 117 */       this.copies.put(acceptor, donor);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void finalizeAndValidate() {
/* 123 */       BuiltInRegistries.ITEM.forEach(item -> {
/*     */             if (this.copies.containsKey(item))
/*     */               return; 
/*     */             if (item instanceof BlockItem) {
/*     */               BlockItem blockItem = (BlockItem)item;
/*     */               if (!this.itemInfos.containsKey(blockItem)) {
/*     */                 Identifier targetModel = ModelLocationUtils.getModelLocation(blockItem.getBlock());
/*     */                 accept((Item)blockItem, ItemModelUtils.plainModel(targetModel));
/*     */               } 
/*     */             } 
/*     */           });
/* 134 */       this.copies.forEach((acceptor, donor) -> {
/*     */             ClientItem donorInfo = this.itemInfos.get(donor);
/*     */             
/*     */             if (donorInfo == null) {
/*     */               throw new IllegalStateException("Missing donor: " + String.valueOf(donor) + " -> " + String.valueOf(acceptor));
/*     */             }
/*     */             register(acceptor, donorInfo);
/*     */           });
/* 142 */       List<Identifier> missingDefinitions = BuiltInRegistries.ITEM.listElements()
/* 143 */         .filter(e -> !this.itemInfos.containsKey(e.value()))
/* 144 */         .map(e -> e.key().identifier())
/* 145 */         .toList();
/*     */       
/* 147 */       if (!missingDefinitions.isEmpty()) {
/* 148 */         throw new IllegalStateException("Missing item model definitions for: " + String.valueOf(missingDefinitions));
/*     */       }
/*     */     }
/*     */     
/*     */     public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
/* 153 */       return DataProvider.saveAll(cache, ClientItem.CODEC, item -> pathProvider.json(item.builtInRegistryHolder().key().identifier()), this.itemInfos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<?> run(CachedOutput cache) {
/* 159 */     ItemInfoCollector itemModels = new ItemInfoCollector();
/* 160 */     BlockStateGeneratorCollector blockStateGenerators = new BlockStateGeneratorCollector();
/* 161 */     SimpleModelCollector simpleModels = new SimpleModelCollector();
/*     */     
/* 163 */     new BlockModelGenerators(blockStateGenerators, itemModels, simpleModels).run();
/* 164 */     new ItemModelGenerators(itemModels, simpleModels).run();
/*     */     
/* 166 */     blockStateGenerators.validate();
/* 167 */     itemModels.finalizeAndValidate();
/*     */     
/* 169 */     return CompletableFuture.allOf((CompletableFuture<?>[])new CompletableFuture[] {
/* 170 */           blockStateGenerators.save(cache, this.blockStatePathProvider), 
/* 171 */           simpleModels.save(cache, this.modelPathProvider), 
/* 172 */           itemModels.save(cache, this.itemInfoPathProvider)
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 178 */     return "Model Definitions";
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/ModelProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
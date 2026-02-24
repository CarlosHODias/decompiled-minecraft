/*     */ package net.minecraft.client.renderer.block.model.multipart;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.client.renderer.block.model.BlockModelPart;
/*     */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.ModelBaker;
/*     */ import net.minecraft.client.resources.model.ResolvableModel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class MultiPartModel
/*     */   implements BlockStateModel {
/*     */   private final SharedBakedState shared;
/*     */   private final BlockState blockState;
/*     */   private List<BlockStateModel> models;
/*     */   
/*     */   private MultiPartModel(SharedBakedState shared, BlockState blockState) {
/*  26 */     this.shared = shared;
/*  27 */     this.blockState = blockState;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureAtlasSprite particleIcon() {
/*  32 */     return this.shared.particleIcon;
/*     */   }
/*     */ 
/*     */   
/*     */   public void collectParts(RandomSource random, List<BlockModelPart> output) {
/*  37 */     if (this.models == null) {
/*  38 */       this.models = this.shared.selectModels(this.blockState);
/*     */     }
/*     */     
/*  41 */     long seed = random.nextLong();
/*     */     
/*  43 */     for (BlockStateModel model : this.models) {
/*  44 */       random.setSeed(seed);
/*  45 */       model.collectParts(random, output);
/*     */     } 
/*     */   }
/*     */   public static final class Selector<T> extends Record { private final Predicate<BlockState> condition; private final T model;
/*  49 */     public Selector(Predicate<BlockState> condition, T model) { this.condition = condition; this.model = model; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Selector;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #49	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Selector;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  49 */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Selector<TT;>; } public Predicate<BlockState> condition() { return this.condition; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Selector;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #49	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Selector;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Selector<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Selector;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #49	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Selector;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  49 */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Selector<TT;>; } public T model() { return this.model; }
/*     */      public <S> Selector<S> with(S newModel) {
/*  51 */       return new Selector(this.condition, (T)newModel);
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class SharedBakedState
/*     */   {
/*     */     private final List<MultiPartModel.Selector<BlockStateModel>> selectors;
/*     */     
/*     */     private final TextureAtlasSprite particleIcon;
/*     */     
/*  62 */     private final Map<BitSet, List<BlockStateModel>> subsets = new ConcurrentHashMap<>();
/*     */     
/*     */     private static BlockStateModel getFirstModel(List<MultiPartModel.Selector<BlockStateModel>> selectors) {
/*  65 */       if (selectors.isEmpty()) {
/*  66 */         throw new IllegalArgumentException("Model must have at least one selector");
/*     */       }
/*  68 */       return ((MultiPartModel.Selector<BlockStateModel>)selectors.getFirst()).model();
/*     */     }
/*     */     
/*     */     public SharedBakedState(List<MultiPartModel.Selector<BlockStateModel>> selectors) {
/*  72 */       this.selectors = selectors;
/*  73 */       BlockStateModel firstModel = getFirstModel(selectors);
/*  74 */       this.particleIcon = firstModel.particleIcon();
/*     */     }
/*     */     
/*     */     public List<BlockStateModel> selectModels(BlockState state) {
/*  78 */       BitSet selectedModels = new BitSet();
/*  79 */       for (int i = 0; i < this.selectors.size(); i++) {
/*  80 */         if (((MultiPartModel.Selector)this.selectors.get(i)).condition.test(state)) {
/*  81 */           selectedModels.set(i);
/*     */         }
/*     */       } 
/*     */       
/*  85 */       return this.subsets.computeIfAbsent(selectedModels, selected -> {
/*     */             ImmutableList.Builder<BlockStateModel> result = ImmutableList.builder();
/*     */             for (int i = 0; i < this.selectors.size(); i++) {
/*     */               if (selected.get(i))
/*     */                 result.add((BlockStateModel)((MultiPartModel.Selector)this.selectors.get(i)).model); 
/*     */             } 
/*     */             return result.build();
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Unbaked
/*     */     implements BlockStateModel.UnbakedRoot {
/*     */     private final List<MultiPartModel.Selector<BlockStateModel.Unbaked>> selectors;
/*     */     
/* 100 */     private final ModelBaker.SharedOperationKey<MultiPartModel.SharedBakedState> sharedStateKey = new ModelBaker.SharedOperationKey<MultiPartModel.SharedBakedState>()
/*     */       {
/*     */         public MultiPartModel.SharedBakedState compute(ModelBaker modelBakery)
/*     */         {
/* 104 */           ImmutableList.Builder<MultiPartModel.Selector<BlockStateModel>> selectors = ImmutableList.builderWithExpectedSize(MultiPartModel.Unbaked.this.selectors.size());
/* 105 */           for (MultiPartModel.Selector<BlockStateModel.Unbaked> selector : MultiPartModel.Unbaked.this.selectors) {
/* 106 */             selectors.add(selector.with(((BlockStateModel.Unbaked)selector.model).bake(modelBakery)));
/*     */           }
/* 108 */           return new MultiPartModel.SharedBakedState((List<MultiPartModel.Selector<BlockStateModel>>)selectors.build());
/*     */         }
/*     */       };
/*     */     
/*     */     public Unbaked(List<MultiPartModel.Selector<BlockStateModel.Unbaked>> selectors) {
/* 113 */       this.selectors = selectors;
/*     */     }
/*     */     public Object visualEqualityGroup(BlockState blockState) {
/*     */       static final class Key extends Record { private final MultiPartModel.Unbaked model; private final IntList selectors;
/*     */         
/* 118 */         Key(MultiPartModel.Unbaked model, IntList selectors) { this.model = model; this.selectors = selectors; } public final String toString() { // Byte code:
/*     */           //   0: aload_0
/*     */           //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Unbaked$1Key;)Ljava/lang/String;
/*     */           //   6: areturn
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #118	-> 0
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Unbaked$1Key; } public final int hashCode() { // Byte code:
/*     */           //   0: aload_0
/*     */           //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Unbaked$1Key;)I
/*     */           //   6: ireturn
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #118	-> 0
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Unbaked$1Key; } public final boolean equals(Object o) { // Byte code:
/*     */           //   0: aload_0
/*     */           //   1: aload_1
/*     */           //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Unbaked$1Key;Ljava/lang/Object;)Z
/*     */           //   7: ireturn
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #118	-> 0
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Unbaked$1Key;
/* 118 */           //   0	8	1	o	Ljava/lang/Object; } public MultiPartModel.Unbaked model() { return this.model; } public IntList selectors() { return this.selectors; }
/*     */          };
/* 120 */       IntArrayList intArrayList = new IntArrayList();
/* 121 */       for (int i = 0; i < this.selectors.size(); i++) {
/* 122 */         if (((MultiPartModel.Selector)this.selectors.get(i)).condition.test(blockState)) {
/* 123 */           intArrayList.add(i);
/*     */         }
/*     */       } 
/* 126 */       return new Key(this, (IntList)intArrayList);
/*     */     }
/*     */ 
/*     */     
/*     */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 131 */       this.selectors.forEach(s -> ((BlockStateModel.Unbaked)s.model).resolveDependencies(resolver));
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockStateModel bake(BlockState blockState, ModelBaker modelBakery) {
/* 136 */       MultiPartModel.SharedBakedState shared = (MultiPartModel.SharedBakedState)modelBakery.compute(this.sharedStateKey);
/* 137 */       return new MultiPartModel(shared, blockState);
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements ModelBaker.SharedOperationKey<SharedBakedState> {
/*     */     public MultiPartModel.SharedBakedState compute(ModelBaker modelBakery) {
/*     */       ImmutableList.Builder<MultiPartModel.Selector<BlockStateModel>> selectors = ImmutableList.builderWithExpectedSize(MultiPartModel.Unbaked.this.selectors.size());
/*     */       for (MultiPartModel.Selector<BlockStateModel.Unbaked> selector : MultiPartModel.Unbaked.this.selectors)
/*     */         selectors.add(selector.with(((BlockStateModel.Unbaked)selector.model).bake(modelBakery))); 
/*     */       return new MultiPartModel.SharedBakedState((List<MultiPartModel.Selector<BlockStateModel>>)selectors.build());
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Key extends Record {
/*     */     private final MultiPartModel.Unbaked model;
/*     */     private final IntList selectors;
/*     */     
/*     */     Key(MultiPartModel.Unbaked model, IntList selectors) {
/*     */       this.model = model;
/*     */       this.selectors = selectors;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Unbaked$1Key;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #118	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Unbaked$1Key;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Unbaked$1Key;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #118	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Unbaked$1Key;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Unbaked$1Key;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #118	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/multipart/MultiPartModel$Unbaked$1Key;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public MultiPartModel.Unbaked model() {
/*     */       return this.model;
/*     */     }
/*     */     
/*     */     public IntList selectors() {
/*     */       return this.selectors;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/multipart/MultiPartModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
/*     */ package net.minecraft.client.renderer.block.model;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.ModelBaker;
/*     */ import net.minecraft.client.resources.model.ResolvableModel;
/*     */ import net.minecraft.client.resources.model.WeightedVariants;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.random.Weighted;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public interface BlockStateModel {
/*     */   void collectParts(RandomSource paramRandomSource, List<BlockModelPart> paramList);
/*     */   
/*     */   default List<BlockModelPart> collectParts(RandomSource random) {
/*  29 */     ObjectArrayList objectArrayList = new ObjectArrayList();
/*  30 */     collectParts(random, (List<BlockModelPart>)objectArrayList);
/*  31 */     return (List<BlockModelPart>)objectArrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   TextureAtlasSprite particleIcon();
/*     */ 
/*     */   
/*     */   public static interface Unbaked
/*     */     extends ResolvableModel
/*     */   {
/*     */     public static final Codec<Weighted<Variant>> ELEMENT_CODEC;
/*     */     
/*     */     public static final Codec<WeightedVariants.Unbaked> HARDCODED_WEIGHTED_CODEC;
/*     */     
/*     */     public static final Codec<Unbaked> CODEC;
/*     */ 
/*     */     
/*     */     static {
/*  49 */       ELEMENT_CODEC = RecordCodecBuilder.create(i -> i.group((App)Variant.MAP_CODEC.forGetter(Weighted::value), (App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("weight", 1).forGetter(Weighted::weight)).apply((Applicative)i, Weighted::new));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  55 */       HARDCODED_WEIGHTED_CODEC = ExtraCodecs.nonEmptyList(ELEMENT_CODEC.listOf()).flatComapMap(w -> new WeightedVariants.Unbaked(WeightedList.of(Lists.transform(w, ()))), unbaked -> {
/*     */             List<Weighted<Unbaked>> entries = unbaked.entries().unwrap();
/*     */             
/*     */             List<Weighted<Variant>> result = new ArrayList<>(entries.size());
/*     */             
/*     */             for (Weighted<Unbaked> entry : entries) {
/*     */               Object patt0$temp = entry.value();
/*     */               
/*     */               if (patt0$temp instanceof SingleVariant.Unbaked) {
/*     */                 SingleVariant.Unbaked singleVariant = (SingleVariant.Unbaked)patt0$temp;
/*     */                 result.add(new Weighted(singleVariant.variant(), entry.weight()));
/*     */                 continue;
/*     */               } 
/*     */               return DataResult.error(());
/*     */             } 
/*     */             return DataResult.success(result);
/*     */           });
/*  72 */       CODEC = Codec.either(HARDCODED_WEIGHTED_CODEC, SingleVariant.Unbaked.CODEC).flatComapMap(v -> (Unbaked)v.map((), ()), o -> {
/*     */             // Byte code:
/*     */             //   0: aload_0
/*     */             //   1: dup
/*     */             //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */             //   5: pop
/*     */             //   6: astore_1
/*     */             //   7: iconst_0
/*     */             //   8: istore_2
/*     */             //   9: aload_1
/*     */             //   10: iload_2
/*     */             //   11: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */             //   16: lookupswitch default -> 76, 0 -> 44, 1 -> 59
/*     */             //   44: aload_1
/*     */             //   45: checkcast net/minecraft/client/renderer/block/model/SingleVariant$Unbaked
/*     */             //   48: astore_3
/*     */             //   49: aload_3
/*     */             //   50: invokestatic right : (Ljava/lang/Object;)Lcom/mojang/datafixers/util/Either;
/*     */             //   53: invokestatic success : (Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*     */             //   56: goto -> 84
/*     */             //   59: aload_1
/*     */             //   60: checkcast net/minecraft/client/resources/model/WeightedVariants$Unbaked
/*     */             //   63: astore #4
/*     */             //   65: aload #4
/*     */             //   67: invokestatic left : (Ljava/lang/Object;)Lcom/mojang/datafixers/util/Either;
/*     */             //   70: invokestatic success : (Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*     */             //   73: goto -> 84
/*     */             //   76: <illegal opcode> get : ()Ljava/util/function/Supplier;
/*     */             //   81: invokestatic error : (Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;
/*     */             //   84: areturn
/*     */             // Line number table:
/*     */             //   Java source line number -> byte code offset
/*     */             //   #74	-> 0
/*     */             //   #75	-> 44
/*     */             //   #76	-> 59
/*     */             //   #77	-> 76
/*     */             //   #76	-> 84
/*     */             // Local variable table:
/*     */             //   start	length	slot	name	descriptor
/*     */             //   49	10	3	single	Lnet/minecraft/client/renderer/block/model/SingleVariant$Unbaked;
/*     */             //   65	11	4	multiple	Lnet/minecraft/client/resources/model/WeightedVariants$Unbaked;
/*     */             //   7	77	1	selector0$temp	Lnet/minecraft/client/renderer/block/model/BlockStateModel$Unbaked;
/*     */             //   9	75	2	index$1	I
/*     */             //   0	85	0	o	Lnet/minecraft/client/renderer/block/model/BlockStateModel$Unbaked;
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     default BlockStateModel.UnbakedRoot asRoot() {
/*  84 */       return new BlockStateModel.SimpleCachedUnbakedRoot(this);
/*     */     }
/*     */     
/*     */     BlockStateModel bake(ModelBaker param1ModelBaker);
/*     */   }
/*     */   
/*     */   public static class SimpleCachedUnbakedRoot
/*     */     implements UnbakedRoot
/*     */   {
/*     */     private final BlockStateModel.Unbaked contents;
/*     */     
/*  95 */     private final ModelBaker.SharedOperationKey<BlockStateModel> bakingKey = new ModelBaker.SharedOperationKey<BlockStateModel>()
/*     */       {
/*     */         public BlockStateModel compute(ModelBaker modelBakery) {
/*  98 */           return BlockStateModel.SimpleCachedUnbakedRoot.this.contents.bake(modelBakery);
/*     */         }
/*     */       };
/*     */     
/*     */     public SimpleCachedUnbakedRoot(BlockStateModel.Unbaked contents) {
/* 103 */       this.contents = contents;
/*     */     }
/*     */ 
/*     */     
/*     */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 108 */       this.contents.resolveDependencies(resolver);
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockStateModel bake(BlockState blockState, ModelBaker modelBakery) {
/* 113 */       return (BlockStateModel)modelBakery.compute(this.bakingKey);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object visualEqualityGroup(BlockState blockState) {
/* 119 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements ModelBaker.SharedOperationKey<BlockStateModel> {
/*     */     public BlockStateModel compute(ModelBaker modelBakery) {
/*     */       return BlockStateModel.SimpleCachedUnbakedRoot.this.contents.bake(modelBakery);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface UnbakedRoot extends ResolvableModel {
/*     */     BlockStateModel bake(BlockState param1BlockState, ModelBaker param1ModelBaker);
/*     */     
/*     */     Object visualEqualityGroup(BlockState param1BlockState);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/BlockStateModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
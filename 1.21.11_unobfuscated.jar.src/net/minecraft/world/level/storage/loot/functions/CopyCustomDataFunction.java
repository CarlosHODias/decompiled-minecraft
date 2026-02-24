/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.CustomData;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class CopyCustomDataFunction extends LootItemConditionalFunction {
/*     */   public static final com.mojang.serialization.MapCodec<CopyCustomDataFunction> CODEC;
/*     */   private final NbtProvider source;
/*     */   private final List<CopyOperation> operations;
/*     */   
/*     */   private static final class CopyOperation extends Record {
/*     */     private final NbtPathArgument.NbtPath sourcePath;
/*     */     private final NbtPathArgument.NbtPath targetPath;
/*     */     private final CopyCustomDataFunction.MergeStrategy op;
/*     */     public static final com.mojang.serialization.Codec<CopyOperation> CODEC;
/*     */     
/*  31 */     private CopyOperation(NbtPathArgument.NbtPath sourcePath, NbtPathArgument.NbtPath targetPath, CopyCustomDataFunction.MergeStrategy op) { this.sourcePath = sourcePath; this.targetPath = targetPath; this.op = op; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  31 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation; } public NbtPathArgument.NbtPath sourcePath() { return this.sourcePath; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;
/*  31 */       //   0	8	1	o	Ljava/lang/Object; } public NbtPathArgument.NbtPath targetPath() { return this.targetPath; } public CopyCustomDataFunction.MergeStrategy op() { return this.op; } static {
/*  32 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)NbtPathArgument.NbtPath.CODEC.fieldOf("source").forGetter(CopyOperation::sourcePath), (App)NbtPathArgument.NbtPath.CODEC.fieldOf("target").forGetter(CopyOperation::targetPath), (App)CopyCustomDataFunction.MergeStrategy.CODEC.fieldOf("op").forGetter(CopyOperation::op)).apply((Applicative)i, CopyOperation::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void apply(Supplier<Tag> target, Tag source) {
/*     */       try {
/*  40 */         List<Tag> sourceTags = this.sourcePath.get(source);
/*  41 */         if (!sourceTags.isEmpty()) {
/*  42 */           this.op.merge(target.get(), this.targetPath, sourceTags);
/*     */         }
/*  44 */       } catch (CommandSyntaxException commandSyntaxException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/*  50 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)net.minecraft.world.level.storage.loot.providers.nbt.NbtProviders.CODEC.fieldOf("source").forGetter(()), (App)CopyOperation.CODEC.listOf().fieldOf("ops").forGetter(()))).apply((Applicative)i, CopyCustomDataFunction::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CopyCustomDataFunction(List<net.minecraft.world.level.storage.loot.predicates.LootItemCondition> predicates, NbtProvider source, List<CopyOperation> operations) {
/*  59 */     super(predicates);
/*  60 */     this.source = source;
/*  61 */     this.operations = List.copyOf(operations);
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType<CopyCustomDataFunction> getType() {
/*  66 */     return LootItemFunctions.COPY_CUSTOM_DATA;
/*     */   }
/*     */ 
/*     */   
/*     */   public java.util.Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/*  71 */     return this.source.getReferencedContextParams();
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  76 */     Tag sourceTag = this.source.get(context);
/*  77 */     if (sourceTag == null) {
/*  78 */       return itemStack;
/*     */     }
/*     */     
/*  81 */     MutableObject<CompoundTag> result = new MutableObject();
/*     */     Supplier<Tag> lazyTargetCopy = () -> {
/*     */         if (result.get() == null) {
/*     */           result.setValue(((CustomData)itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)).copyTag());
/*     */         }
/*     */         return result.get();
/*     */       };
/*  88 */     this.operations.forEach(op -> op.apply(lazyTargetCopy, sourceTag));
/*  89 */     CompoundTag resultTag = (CompoundTag)result.get();
/*  90 */     if (resultTag != null) {
/*  91 */       CustomData.set(DataComponents.CUSTOM_DATA, itemStack, resultTag);
/*     */     }
/*     */     
/*  94 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*     */     private final NbtProvider source;
/*  99 */     private final List<CopyCustomDataFunction.CopyOperation> ops = com.google.common.collect.Lists.newArrayList();
/*     */     
/*     */     private Builder(NbtProvider source) {
/* 102 */       this.source = source;
/*     */     }
/*     */     
/*     */     public Builder copy(String sourcePath, String targetPath, CopyCustomDataFunction.MergeStrategy mergeStrategy) {
/*     */       try {
/* 107 */         this.ops.add(new CopyCustomDataFunction.CopyOperation(NbtPathArgument.NbtPath.of(sourcePath), NbtPathArgument.NbtPath.of(targetPath), mergeStrategy));
/* 108 */       } catch (CommandSyntaxException e) {
/* 109 */         throw new IllegalArgumentException(e);
/*     */       } 
/* 111 */       return this;
/*     */     }
/*     */     
/*     */     public Builder copy(String sourcePath, String targetPath) {
/* 115 */       return copy(sourcePath, targetPath, CopyCustomDataFunction.MergeStrategy.REPLACE);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Builder getThis() {
/* 120 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootItemFunction build() {
/* 125 */       return new CopyCustomDataFunction(getConditions(), this.source, this.ops);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Builder copyData(NbtProvider source) {
/* 134 */     return new Builder(source);
/*     */   }
/*     */   
/*     */   public static Builder copyData(LootContext.EntityTarget source) {
/* 138 */     return new Builder(net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider.forContextEntity(source));
/*     */   }
/*     */   
/*     */   public enum MergeStrategy implements net.minecraft.util.StringRepresentable {
/* 142 */     REPLACE("replace")
/*     */     {
/*     */       public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/* 145 */         path.set(target, (Tag)com.google.common.collect.Iterables.getLast(sources));
/*     */       }
/*     */     },
/* 148 */     APPEND("append")
/*     */     {
/*     */       public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/* 151 */         List<Tag> targets = path.getOrCreate(target, net.minecraft.nbt.ListTag::new);
/* 152 */         targets.forEach(tag -> {
/*     */               
/*     */               if (tag instanceof net.minecraft.nbt.ListTag) {
/*     */                 sources.forEach(());
/*     */               }
/*     */             });
/*     */       }
/*     */     },
/* 160 */     MERGE("merge")
/*     */     {
/*     */       public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/* 163 */         List<Tag> targets = path.getOrCreate(target, CompoundTag::new);
/* 164 */         targets.forEach(tag -> {
/*     */               if (tag instanceof CompoundTag) {
/*     */                 sources.forEach(());
/*     */               }
/*     */             });
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     public static final com.mojang.serialization.Codec<MergeStrategy> CODEC = (com.mojang.serialization.Codec<MergeStrategy>)net.minecraft.util.StringRepresentable.fromEnum(MergeStrategy::values);
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     MergeStrategy(String name) {
/* 184 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 189 */       return this.name;
/*     */     }
/*     */     
/*     */     public abstract void merge(Tag param1Tag, NbtPathArgument.NbtPath param1NbtPath, List<Tag> param1List) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/*     */       path.set(target, (Tag)com.google.common.collect.Iterables.getLast(sources));
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/*     */       List<Tag> targets = path.getOrCreate(target, net.minecraft.nbt.ListTag::new);
/*     */       targets.forEach(tag -> {
/*     */             if (tag instanceof net.minecraft.nbt.ListTag)
/*     */               sources.forEach(()); 
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/*     */       List<Tag> targets = path.getOrCreate(target, CompoundTag::new);
/*     */       targets.forEach(tag -> {
/*     */             if (tag instanceof CompoundTag)
/*     */               sources.forEach(()); 
/*     */           });
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class TypedEntityData<IdType> implements TooltipProvider {
/*  33 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final String TYPE_TAG = "id";
/*     */   private final IdType type;
/*     */   private final CompoundTag tag;
/*     */   
/*     */   public static <T> Codec<TypedEntityData<T>> codec(final Codec<T> typeCodec) {
/*  38 */     return new Codec<TypedEntityData<T>>()
/*     */       {
/*     */         public <V> DataResult<Pair<TypedEntityData<T>, V>> decode(DynamicOps<V> ops, V input)
/*     */         {
/*  42 */           return CustomData.COMPOUND_TAG_CODEC.decode(ops, input).flatMap(pair -> {
/*     */                 CompoundTag tagWithoutType = ((CompoundTag)pair.getFirst()).copy();
/*     */                 Tag typeTag = tagWithoutType.remove("id");
/*     */                 return (typeTag == null) ? DataResult.error(()) : typeCodec.parse(asNbtOps(ops), typeTag).map(());
/*     */               });
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public <V> DataResult<V> encode(TypedEntityData<T> input, DynamicOps<V> ops, V prefix) {
/*  54 */           return typeCodec.encodeStart(asNbtOps(ops), input.type).flatMap(typeTag -> {
/*     */                 CompoundTag tag = input.tag.copy();
/*     */                 tag.put("id", typeTag);
/*     */                 return CustomData.COMPOUND_TAG_CODEC.encode(tag, ops, prefix);
/*     */               });
/*     */         }
/*     */         
/*     */         private static <T> DynamicOps<Tag> asNbtOps(DynamicOps<T> ops) {
/*  62 */           if (ops instanceof RegistryOps) { RegistryOps<T> registryOps = (RegistryOps<T>)ops;
/*  63 */             return (DynamicOps<Tag>)registryOps.withParent((DynamicOps)NbtOps.INSTANCE); }
/*     */           
/*  65 */           return (DynamicOps<Tag>)NbtOps.INSTANCE;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <B extends io.netty.buffer.ByteBuf, T> StreamCodec<B, TypedEntityData<T>> streamCodec(StreamCodec<B, T> typeCodec) {
/*  72 */     return StreamCodec.composite(typeCodec, TypedEntityData::type, ByteBufCodecs.COMPOUND_TAG, TypedEntityData::tag, TypedEntityData::new);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypedEntityData(IdType type, CompoundTag data) {
/*  84 */     this.type = type;
/*  85 */     this.tag = stripId(data);
/*     */   }
/*     */   
/*     */   public static <T> TypedEntityData<T> of(T type, CompoundTag data) {
/*  89 */     return new TypedEntityData<>(type, data);
/*     */   }
/*     */   
/*     */   private static CompoundTag stripId(CompoundTag tag) {
/*  93 */     if (tag.contains("id")) {
/*  94 */       CompoundTag copy = tag.copy();
/*  95 */       copy.remove("id");
/*  96 */       return copy;
/*     */     } 
/*  98 */     return tag;
/*     */   }
/*     */   
/*     */   public IdType type() {
/* 102 */     return this.type;
/*     */   }
/*     */   
/*     */   public boolean contains(String name) {
/* 106 */     return this.tag.contains(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 111 */     if (obj == this) {
/* 112 */       return true;
/*     */     }
/* 114 */     if (obj instanceof TypedEntityData) { TypedEntityData<?> customData = (TypedEntityData)obj;
/* 115 */       return (this.type == customData.type && this.tag.equals(customData.tag)); }
/*     */     
/* 117 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 122 */     return 31 * this.type.hashCode() + this.tag.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 127 */     return String.valueOf(this.type) + " " + String.valueOf(this.type);
/*     */   }
/*     */   
/*     */   public void loadInto(Entity entity) {
/* 131 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), LOGGER); 
/* 132 */     try { TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, (HolderLookup.Provider)entity.registryAccess());
/* 133 */       entity.saveWithoutId((ValueOutput)output);
/* 134 */       CompoundTag entityData = output.buildResult();
/*     */       
/* 136 */       UUID uuid = entity.getUUID();
/* 137 */       entityData.merge(getUnsafe());
/* 138 */       entity.load(TagValueInput.create((ProblemReporter)reporter, (HolderLookup.Provider)entity.registryAccess(), entityData));
/*     */ 
/*     */       
/* 141 */       entity.setUUID(uuid);
/* 142 */       reporter.close(); }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 146 */      } public boolean loadInto(BlockEntity blockEntity, HolderLookup.Provider registries) { ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), LOGGER); 
/* 147 */     try { TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, registries);
/* 148 */       blockEntity.saveCustomOnly((ValueOutput)output);
/* 149 */       CompoundTag entityTag = output.buildResult();
/* 150 */       CompoundTag oldTag = entityTag.copy();
/* 151 */       entityTag.merge(getUnsafe());
/* 152 */       if (!entityTag.equals(oldTag))
/*     */ 
/*     */         
/* 155 */         try { blockEntity.loadCustomOnly(TagValueInput.create((ProblemReporter)reporter, registries, entityTag));
/* 156 */           blockEntity.setChanged();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           boolean bool1 = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 170 */           reporter.close(); return bool1; } catch (Exception e) { LOGGER.warn("Failed to apply custom data to block entity at {}", blockEntity.getBlockPos(), e); try { blockEntity.loadCustomOnly(TagValueInput.create(reporter.forChild(() -> "(rollback)"), registries, oldTag)); } catch (Exception e2) { LOGGER.warn("Failed to rollback block entity at {} after failure", blockEntity.getBlockPos(), e2); }  }   boolean bool = false; reporter.close(); return bool; }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 174 */      } private CompoundTag tag() { return this.tag; }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public CompoundTag getUnsafe() {
/* 180 */     return this.tag;
/*     */   }
/*     */   
/*     */   public CompoundTag copyTagWithoutId() {
/* 184 */     return this.tag.copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 189 */     if (this.type.getClass() == EntityType.class) {
/* 190 */       EntityType<?> type = (EntityType)this.type;
/* 191 */       if (context.isPeaceful() && !type.isAllowedInPeaceful())
/* 192 */         consumer.accept(Component.translatable("item.spawn_egg.peaceful").withStyle(ChatFormatting.RED)); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/TypedEntityData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
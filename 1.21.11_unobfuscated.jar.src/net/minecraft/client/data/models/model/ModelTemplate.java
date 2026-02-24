/*    */ package net.minecraft.client.data.models.model;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Streams;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class ModelTemplate {
/*    */   private final Optional<Identifier> model;
/*    */   private final Set<TextureSlot> requiredSlots;
/*    */   private final Optional<String> suffix;
/*    */   
/*    */   public ModelTemplate(Optional<Identifier> model, Optional<String> suffix, TextureSlot... requiredSlots) {
/* 25 */     this.model = model;
/* 26 */     this.suffix = suffix;
/* 27 */     this.requiredSlots = (Set<TextureSlot>)ImmutableSet.copyOf((Object[])requiredSlots);
/*    */   }
/*    */   
/*    */   public Identifier getDefaultModelLocation(Block block) {
/* 31 */     return ModelLocationUtils.getModelLocation(block, this.suffix.orElse(""));
/*    */   }
/*    */   
/*    */   public Identifier create(Block block, TextureMapping textures, BiConsumer<Identifier, ModelInstance> output) {
/* 35 */     return create(ModelLocationUtils.getModelLocation(block, this.suffix.orElse("")), textures, output);
/*    */   }
/*    */   
/*    */   public Identifier createWithSuffix(Block block, String extraSuffix, TextureMapping textures, BiConsumer<Identifier, ModelInstance> output) {
/* 39 */     return create(ModelLocationUtils.getModelLocation(block, extraSuffix + extraSuffix), textures, output);
/*    */   }
/*    */   
/*    */   public Identifier createWithOverride(Block block, String suffixOverride, TextureMapping textures, BiConsumer<Identifier, ModelInstance> output) {
/* 43 */     return create(ModelLocationUtils.getModelLocation(block, suffixOverride), textures, output);
/*    */   }
/*    */   
/*    */   public Identifier create(Item item, TextureMapping textures, BiConsumer<Identifier, ModelInstance> output) {
/* 47 */     return create(ModelLocationUtils.getModelLocation(item, this.suffix.orElse("")), textures, output);
/*    */   }
/*    */   
/*    */   public Identifier create(Identifier target, TextureMapping textures, BiConsumer<Identifier, ModelInstance> output) {
/* 51 */     Map<TextureSlot, Identifier> slots = createMap(textures);
/* 52 */     output.accept(target, () -> {
/*    */           JsonObject result = new JsonObject();
/*    */           this.model.ifPresent(());
/*    */           if (!slots.isEmpty()) {
/*    */             JsonObject textureObj = new JsonObject();
/*    */             slots.forEach(());
/*    */             result.add("textures", (JsonElement)textureObj);
/*    */           } 
/*    */           return result;
/*    */         });
/* 62 */     return target;
/*    */   }
/*    */   
/*    */   private Map<TextureSlot, Identifier> createMap(TextureMapping mapping) {
/* 66 */     Objects.requireNonNull(mapping); return (Map<TextureSlot, Identifier>)Streams.concat(new Stream[] { this.requiredSlots.stream(), mapping.getForced() }).collect(ImmutableMap.toImmutableMap(Function.identity(), mapping::get));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/model/ModelTemplate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
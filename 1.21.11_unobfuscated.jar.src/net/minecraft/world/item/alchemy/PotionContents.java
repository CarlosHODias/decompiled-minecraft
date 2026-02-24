/*     */ package net.minecraft.world.item.alchemy;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffectUtil;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public final class PotionContents extends Record implements net.minecraft.world.item.component.ConsumableListener, net.minecraft.world.item.component.TooltipProvider {
/*     */   private final Optional<Holder<Potion>> potion;
/*     */   private final Optional<Integer> customColor;
/*     */   private final List<MobEffectInstance> customEffects;
/*     */   private final Optional<String> customName;
/*     */   
/*  44 */   public PotionContents(Optional<Holder<Potion>> potion, Optional<Integer> customColor, List<MobEffectInstance> customEffects, Optional<String> customName) { this.potion = potion; this.customColor = customColor; this.customEffects = customEffects; this.customName = customName; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/alchemy/PotionContents;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #44	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  44 */     //   0	7	0	this	Lnet/minecraft/world/item/alchemy/PotionContents; } public Optional<Holder<Potion>> potion() { return this.potion; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/alchemy/PotionContents;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #44	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/alchemy/PotionContents; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/alchemy/PotionContents;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #44	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/alchemy/PotionContents;
/*  44 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Integer> customColor() { return this.customColor; } public Optional<String> customName() { return this.customName; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   public static final PotionContents EMPTY = new PotionContents(Optional.empty(), Optional.empty(), List.of(), Optional.empty());
/*     */   
/*  52 */   private static final Component NO_EFFECT = (Component)Component.translatable("effect.none").withStyle(ChatFormatting.GRAY); public static final int BASE_POTION_COLOR = -13083194; private static final Codec<PotionContents> FULL_CODEC;
/*     */   
/*     */   static {
/*  55 */     FULL_CODEC = RecordCodecBuilder.create(i -> i.group((App)Potion.CODEC.optionalFieldOf("potion").forGetter(PotionContents::potion), (App)Codec.INT.optionalFieldOf("custom_color").forGetter(PotionContents::customColor), (App)MobEffectInstance.CODEC.listOf().optionalFieldOf("custom_effects", List.of()).forGetter(PotionContents::customEffects), (App)Codec.STRING.optionalFieldOf("custom_name").forGetter(PotionContents::customName)).apply((Applicative)i, PotionContents::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public static final Codec<PotionContents> CODEC = Codec.withAlternative(FULL_CODEC, Potion.CODEC, PotionContents::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, PotionContents> STREAM_CODEC = StreamCodec.composite(
/*  69 */       Potion.STREAM_CODEC.apply(ByteBufCodecs::optional), PotionContents::potion, 
/*  70 */       ByteBufCodecs.INT.apply(ByteBufCodecs::optional), PotionContents::customColor, 
/*  71 */       MobEffectInstance.STREAM_CODEC.apply(ByteBufCodecs.list()), PotionContents::customEffects, 
/*  72 */       ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs::optional), PotionContents::customName, PotionContents::new);
/*     */ 
/*     */ 
/*     */   
/*     */   public PotionContents(Holder<Potion> potion) {
/*  77 */     this(Optional.of(potion), Optional.empty(), List.of(), Optional.empty());
/*     */   }
/*     */   
/*     */   public static ItemStack createItemStack(Item item, Holder<Potion> potion) {
/*  81 */     ItemStack itemStack = new ItemStack((net.minecraft.world.level.ItemLike)item);
/*  82 */     itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
/*  83 */     return itemStack;
/*     */   }
/*     */   
/*     */   public boolean is(Holder<Potion> potion) {
/*  87 */     return (this.potion.isPresent() && ((Holder)this.potion.get()).is(potion) && this.customEffects.isEmpty());
/*     */   }
/*     */   
/*     */   public Iterable<MobEffectInstance> getAllEffects() {
/*  91 */     if (this.potion.isEmpty()) {
/*  92 */       return this.customEffects;
/*     */     }
/*  94 */     if (this.customEffects.isEmpty()) {
/*  95 */       return ((Potion)((Holder)this.potion.get()).value()).getEffects();
/*     */     }
/*  97 */     return com.google.common.collect.Iterables.concat(((Potion)((Holder)this.potion.get()).value()).getEffects(), this.customEffects);
/*     */   }
/*     */   
/*     */   public void forEachEffect(Consumer<MobEffectInstance> consumer, float durationScale) {
/* 101 */     if (this.potion.isPresent()) {
/* 102 */       for (MobEffectInstance effect : ((Potion)((Holder)this.potion.get()).value()).getEffects()) {
/* 103 */         consumer.accept(effect.withScaledDuration(durationScale));
/*     */       }
/*     */     }
/* 106 */     for (MobEffectInstance effect : this.customEffects) {
/* 107 */       consumer.accept(effect.withScaledDuration(durationScale));
/*     */     }
/*     */   }
/*     */   
/*     */   public PotionContents withPotion(Holder<Potion> potion) {
/* 112 */     return new PotionContents(Optional.of(potion), this.customColor, this.customEffects, this.customName);
/*     */   }
/*     */   
/*     */   public PotionContents withEffectAdded(MobEffectInstance effect) {
/* 116 */     return new PotionContents(this.potion, this.customColor, Util.copyAndAdd(this.customEffects, effect), this.customName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColor() {
/* 122 */     return getColorOr(-13083194);
/*     */   }
/*     */   
/*     */   public int getColorOr(int defaultColor) {
/* 126 */     if (this.customColor.isPresent()) {
/* 127 */       return (Integer)this.customColor.get();
/*     */     }
/* 129 */     return getColorOptional(getAllEffects()).orElse(defaultColor);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getName(String prefix) {
/* 134 */     String suffix = this.customName.or(() -> this.potion.map(()))
/* 135 */       .orElse("empty");
/*     */     
/* 137 */     return (Component)Component.translatable(prefix + prefix);
/*     */   }
/*     */   
/*     */   public static OptionalInt getColorOptional(Iterable<MobEffectInstance> effects) {
/* 141 */     int red = 0;
/* 142 */     int green = 0;
/* 143 */     int blue = 0;
/* 144 */     int totalWeight = 0;
/*     */     
/* 146 */     for (MobEffectInstance effect : effects) {
/* 147 */       if (!effect.isVisible()) {
/*     */         continue;
/*     */       }
/*     */       
/* 151 */       int color = ((MobEffect)effect.getEffect().value()).getColor();
/* 152 */       int amplifier = effect.getAmplifier() + 1;
/* 153 */       red += amplifier * ARGB.red(color);
/* 154 */       green += amplifier * ARGB.green(color);
/* 155 */       blue += amplifier * ARGB.blue(color);
/* 156 */       totalWeight += amplifier;
/*     */     } 
/*     */     
/* 159 */     if (totalWeight == 0) {
/* 160 */       return OptionalInt.empty();
/*     */     }
/*     */     
/* 163 */     return OptionalInt.of(ARGB.color(red / totalWeight, green / totalWeight, blue / totalWeight));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasEffects() {
/* 171 */     if (!this.customEffects.isEmpty()) {
/* 172 */       return true;
/*     */     }
/* 174 */     return (this.potion.isPresent() && !((Potion)((Holder)this.potion.get()).value()).getEffects().isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MobEffectInstance> customEffects() {
/* 179 */     return Lists.transform(this.customEffects, MobEffectInstance::new);
/*     */   }
/*     */   public void applyToLivingEntity(LivingEntity entity, float durationScale) {
/*     */     ServerLevel serverLevel;
/* 183 */     Level level = entity.level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*     */     else
/*     */     { return; }
/*     */     
/* 187 */     Player playerEntity = (Player)entity, player = (entity instanceof Player) ? playerEntity : null;
/* 188 */     forEachEffect(effect -> { if (((MobEffect)effect.getEffect().value()).isInstantenous()) { ((MobEffect)effect.getEffect().value()).applyInstantenousEffect(serverLevel, (Entity)player, (Entity)player, entity, effect.getAmplifier(), 1.0D); } else { entity.addEffect(effect); }  }, durationScale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addPotionTooltip(Iterable<MobEffectInstance> effects, Consumer<Component> lines, float durationScale, float tickrate) {
/* 198 */     List<Pair<Holder<Attribute>, AttributeModifier>> modifiers = Lists.newArrayList();
/*     */     
/*     */     boolean noEffects = true;
/* 201 */     for (MobEffectInstance effect : effects) {
/* 202 */       noEffects = false;
/*     */       
/* 204 */       Holder<MobEffect> mobEffect = effect.getEffect();
/* 205 */       int amplifier = effect.getAmplifier();
/* 206 */       ((MobEffect)mobEffect.value()).createModifiers(amplifier, (attribute, modifier) -> modifiers.add(new Pair(attribute, modifier)));
/* 207 */       MutableComponent line = getPotionDescription(mobEffect, amplifier);
/* 208 */       if (!effect.endsWithin(20)) {
/* 209 */         line = Component.translatable("potion.withDuration", new Object[] { line, MobEffectUtil.formatDuration(effect, durationScale, tickrate) });
/*     */       }
/*     */       
/* 212 */       lines.accept(line.withStyle(((MobEffect)mobEffect.value()).getCategory().getTooltipFormatting()));
/*     */     } 
/*     */     
/* 215 */     if (noEffects) {
/* 216 */       lines.accept(NO_EFFECT);
/*     */     }
/*     */     
/* 219 */     if (!modifiers.isEmpty()) {
/* 220 */       lines.accept(net.minecraft.network.chat.CommonComponents.EMPTY);
/* 221 */       lines.accept(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
/*     */       
/* 223 */       for (Pair<Holder<Attribute>, AttributeModifier> entry : modifiers) {
/* 224 */         double displayAmount; AttributeModifier modifier = (AttributeModifier)entry.getSecond();
/* 225 */         double amount = modifier.amount();
/*     */ 
/*     */         
/* 228 */         if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE || modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
/* 229 */           displayAmount = modifier.amount() * 100.0D;
/*     */         } else {
/* 231 */           displayAmount = modifier.amount();
/*     */         } 
/*     */         
/* 234 */         if (amount > 0.0D) {
/* 235 */           lines.accept(
/* 236 */               Component.translatable("attribute.modifier.plus." + modifier.operation().id(), new Object[] {
/* 237 */                   ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(displayAmount), 
/* 238 */                   Component.translatable(((Attribute)((Holder)entry.getFirst()).value()).getDescriptionId())
/* 239 */                 }).withStyle(ChatFormatting.BLUE)); continue;
/*     */         } 
/* 241 */         if (amount < 0.0D) {
/* 242 */           displayAmount *= -1.0D;
/* 243 */           lines.accept(
/* 244 */               Component.translatable("attribute.modifier.take." + modifier.operation().id(), new Object[] {
/* 245 */                   ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(displayAmount), 
/* 246 */                   Component.translatable(((Attribute)((Holder)entry.getFirst()).value()).getDescriptionId())
/* 247 */                 }).withStyle(ChatFormatting.RED));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static MutableComponent getPotionDescription(Holder<MobEffect> mobEffect, int amplifier) {
/* 255 */     MutableComponent line = Component.translatable(((MobEffect)mobEffect.value()).getDescriptionId());
/* 256 */     if (amplifier > 0) {
/* 257 */       return Component.translatable("potion.withAmplifier", new Object[] { line, Component.translatable("potion.potency." + amplifier) });
/*     */     }
/* 259 */     return line;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onConsume(Level level, LivingEntity user, ItemStack stack, net.minecraft.world.item.component.Consumable consumable) {
/* 264 */     applyToLivingEntity(user, (Float)stack.getOrDefault(DataComponents.POTION_DURATION_SCALE, 1.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, net.minecraft.world.item.TooltipFlag flag, DataComponentGetter components) {
/* 269 */     addPotionTooltip(getAllEffects(), consumer, (Float)components.getOrDefault(DataComponents.POTION_DURATION_SCALE, 1.0F), context.tickRate());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/alchemy/PotionContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
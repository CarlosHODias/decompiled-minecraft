/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.criterion.CriterionValidator;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ 
/*     */ public final class Advancement extends Record {
/*     */   private final Optional<Identifier> parent;
/*     */   private final Optional<DisplayInfo> display;
/*     */   private final AdvancementRewards rewards;
/*     */   private final Map<String, Criterion<?>> criteria;
/*     */   private final AdvancementRequirements requirements;
/*     */   private final boolean sendsTelemetryEvent;
/*     */   private final Optional<Component> name;
/*     */   private static final Codec<Map<String, Criterion<?>>> CRITERIA_CODEC;
/*     */   public static final Codec<Advancement> CODEC;
/*     */   
/*  29 */   public Advancement(Optional<Identifier> parent, Optional<DisplayInfo> display, AdvancementRewards rewards, Map<String, Criterion<?>> criteria, AdvancementRequirements requirements, boolean sendsTelemetryEvent, Optional<Component> name) { this.parent = parent; this.display = display; this.rewards = rewards; this.criteria = criteria; this.requirements = requirements; this.sendsTelemetryEvent = sendsTelemetryEvent; this.name = name; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/Advancement;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #29	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  29 */     //   0	7	0	this	Lnet/minecraft/advancements/Advancement; } public Optional<Identifier> parent() { return this.parent; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/Advancement;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #29	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/Advancement; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/Advancement;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #29	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/Advancement;
/*  29 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<DisplayInfo> display() { return this.display; } public AdvancementRewards rewards() { return this.rewards; } public Map<String, Criterion<?>> criteria() { return this.criteria; } public AdvancementRequirements requirements() { return this.requirements; } public boolean sendsTelemetryEvent() { return this.sendsTelemetryEvent; } public Optional<Component> name() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  39 */     CRITERIA_CODEC = Codec.unboundedMap((Codec)Codec.STRING, Criterion.CODEC).validate(criteria -> criteria.isEmpty() ? DataResult.error(()) : DataResult.success(criteria));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.optionalFieldOf("parent").forGetter(Advancement::parent), (App)DisplayInfo.CODEC.optionalFieldOf("display").forGetter(Advancement::display), (App)AdvancementRewards.CODEC.optionalFieldOf("rewards", AdvancementRewards.EMPTY).forGetter(Advancement::rewards), (App)CRITERIA_CODEC.fieldOf("criteria").forGetter(Advancement::criteria), (App)AdvancementRequirements.CODEC.optionalFieldOf("requirements").forGetter(()), (App)Codec.BOOL.optionalFieldOf("sends_telemetry_event", false).forGetter(Advancement::sendsTelemetryEvent)).apply((com.mojang.datafixers.kinds.Applicative)i, ())).validate(Advancement::validate);
/*     */   }
/*  53 */   public static final net.minecraft.network.codec.StreamCodec<RegistryFriendlyByteBuf, Advancement> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.ofMember(Advancement::write, Advancement::read);
/*     */   
/*     */   private static DataResult<Advancement> validate(Advancement advancement) {
/*  56 */     return advancement.requirements().validate(advancement.criteria().keySet()).map(r -> advancement);
/*     */   }
/*     */   
/*     */   public Advancement(Optional<Identifier> parent, Optional<DisplayInfo> display, AdvancementRewards rewards, Map<String, Criterion<?>> criteria, AdvancementRequirements requirements, boolean sendsTelemetryEvent) {
/*  60 */     this(parent, display, rewards, Map.copyOf(criteria), requirements, sendsTelemetryEvent, display.map(Advancement::decorateName));
/*     */   }
/*     */   
/*     */   private static Component decorateName(DisplayInfo display) {
/*  64 */     Component displayTitle = display.getTitle();
/*  65 */     net.minecraft.ChatFormatting color = display.getType().getChatColor();
/*     */     
/*  67 */     net.minecraft.network.chat.MutableComponent mutableComponent1 = net.minecraft.network.chat.ComponentUtils.mergeStyles(displayTitle.copy(), Style.EMPTY.withColor(color)).append("\n").append(display.getDescription());
/*  68 */     net.minecraft.network.chat.MutableComponent mutableComponent2 = displayTitle.copy().withStyle(s -> s.withHoverEvent((HoverEvent)new HoverEvent.ShowText(tooltip)));
/*     */     
/*  70 */     return (Component)net.minecraft.network.chat.ComponentUtils.wrapInSquareBrackets((Component)mutableComponent2).withStyle(color);
/*     */   }
/*     */   
/*     */   public static Component name(AdvancementHolder holder) {
/*  74 */     return holder.value().name().orElseGet(() -> Component.literal(holder.id().toString()));
/*     */   }
/*     */   
/*     */   private void write(RegistryFriendlyByteBuf output) {
/*  78 */     output.writeOptional(this.parent, FriendlyByteBuf::writeIdentifier);
/*  79 */     DisplayInfo.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs::optional).encode(output, this.display);
/*  80 */     this.requirements.write((FriendlyByteBuf)output);
/*  81 */     output.writeBoolean(this.sendsTelemetryEvent);
/*     */   }
/*     */   
/*     */   private static Advancement read(RegistryFriendlyByteBuf input) {
/*  85 */     return new Advancement(
/*  86 */         input.readOptional(FriendlyByteBuf::readIdentifier), (Optional<DisplayInfo>)
/*  87 */         DisplayInfo.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs::optional).decode(input), AdvancementRewards.EMPTY, 
/*     */         
/*  89 */         Map.of(), new AdvancementRequirements((FriendlyByteBuf)input), 
/*     */         
/*  91 */         input.readBoolean());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRoot() {
/*  96 */     return this.parent.isEmpty();
/*     */   }
/*     */   
/*     */   public void validate(ProblemReporter reporter, net.minecraft.core.HolderGetter.Provider lootData) {
/* 100 */     this.criteria.forEach((name, criterion) -> {
/*     */           CriterionValidator validator = new CriterionValidator(reporter.forChild((ProblemReporter.PathElement)new ProblemReporter.RootFieldPathElement(name)), lootData);
/*     */           criterion.triggerInstance().validate(validator);
/*     */         });
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 107 */     private Optional<Identifier> parent = Optional.empty();
/* 108 */     private Optional<DisplayInfo> display = Optional.empty();
/* 109 */     private AdvancementRewards rewards = AdvancementRewards.EMPTY;
/* 110 */     private final com.google.common.collect.ImmutableMap.Builder<String, Criterion<?>> criteria = com.google.common.collect.ImmutableMap.builder();
/* 111 */     private Optional<AdvancementRequirements> requirements = Optional.empty();
/* 112 */     private AdvancementRequirements.Strategy requirementsStrategy = AdvancementRequirements.Strategy.AND;
/*     */     private boolean sendsTelemetryEvent;
/*     */     
/*     */     public static Builder advancement() {
/* 116 */       return new Builder().sendsTelemetryEvent();
/*     */     }
/*     */     
/*     */     public static Builder recipeAdvancement() {
/* 120 */       return new Builder();
/*     */     }
/*     */     
/*     */     public Builder parent(AdvancementHolder parent) {
/* 124 */       this.parent = Optional.of(parent.id());
/* 125 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated(forRemoval = true)
/*     */     public Builder parent(Identifier parent) {
/* 131 */       this.parent = Optional.of(parent);
/* 132 */       return this;
/*     */     }
/*     */     
/*     */     public Builder display(net.minecraft.world.item.ItemStack icon, Component title, Component description, Identifier background, AdvancementType frame, boolean showToast, boolean announceChat, boolean hidden) {
/* 136 */       return display(new DisplayInfo(icon, title, description, Optional.<Identifier>ofNullable(background).map(net.minecraft.core.ClientAsset.ResourceTexture::new), frame, showToast, announceChat, hidden));
/*     */     }
/*     */     
/*     */     public Builder display(net.minecraft.world.level.ItemLike icon, Component title, Component description, Identifier background, AdvancementType frame, boolean showToast, boolean announceChat, boolean hidden) {
/* 140 */       return display(new DisplayInfo(new net.minecraft.world.item.ItemStack((net.minecraft.world.level.ItemLike)icon.asItem()), title, description, Optional.<Identifier>ofNullable(background).map(net.minecraft.core.ClientAsset.ResourceTexture::new), frame, showToast, announceChat, hidden));
/*     */     }
/*     */     
/*     */     public Builder display(DisplayInfo display) {
/* 144 */       this.display = Optional.of(display);
/* 145 */       return this;
/*     */     }
/*     */     
/*     */     public Builder rewards(AdvancementRewards.Builder rewards) {
/* 149 */       return rewards(rewards.build());
/*     */     }
/*     */     
/*     */     public Builder rewards(AdvancementRewards rewards) {
/* 153 */       this.rewards = rewards;
/* 154 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addCriterion(String name, Criterion<?> criterion) {
/* 158 */       this.criteria.put(name, criterion);
/* 159 */       return this;
/*     */     }
/*     */     
/*     */     public Builder requirements(AdvancementRequirements.Strategy strategy) {
/* 163 */       this.requirementsStrategy = strategy;
/* 164 */       return this;
/*     */     }
/*     */     
/*     */     public Builder requirements(AdvancementRequirements requirements) {
/* 168 */       this.requirements = Optional.of(requirements);
/* 169 */       return this;
/*     */     }
/*     */     
/*     */     public Builder sendsTelemetryEvent() {
/* 173 */       this.sendsTelemetryEvent = true;
/* 174 */       return this;
/*     */     }
/*     */     
/*     */     public AdvancementHolder build(Identifier id) {
/* 178 */       com.google.common.collect.ImmutableMap immutableMap = this.criteria.buildOrThrow();
/* 179 */       AdvancementRequirements requirements = this.requirements.orElseGet(() -> this.requirementsStrategy.create(criteria.keySet()));
/* 180 */       return new AdvancementHolder(id, new Advancement(this.parent, this.display, this.rewards, (Map<String, Criterion<?>>)immutableMap, requirements, this.sendsTelemetryEvent));
/*     */     }
/*     */     
/*     */     public AdvancementHolder save(java.util.function.Consumer<AdvancementHolder> output, String name) {
/* 184 */       AdvancementHolder advancement = build(Identifier.parse(name));
/* 185 */       output.accept(advancement);
/* 186 */       return advancement;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/Advancement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
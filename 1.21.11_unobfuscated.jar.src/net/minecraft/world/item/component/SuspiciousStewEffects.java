/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.TooltipFlag;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ 
/*    */ public final class SuspiciousStewEffects extends Record implements ConsumableListener, TooltipProvider {
/*    */   private final List<Entry> effects;
/*    */   
/* 26 */   public SuspiciousStewEffects(List<Entry> effects) { this.effects = effects; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/SuspiciousStewEffects;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 26 */     //   0	7	0	this	Lnet/minecraft/world/item/component/SuspiciousStewEffects; } public List<Entry> effects() { return this.effects; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/SuspiciousStewEffects;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/SuspiciousStewEffects; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/SuspiciousStewEffects;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/SuspiciousStewEffects;
/* 27 */     //   0	8	1	o	Ljava/lang/Object; } public static final SuspiciousStewEffects EMPTY = new SuspiciousStewEffects(List.of());
/*    */   
/*    */   public static final int DEFAULT_DURATION = 160;
/* 30 */   public static final Codec<SuspiciousStewEffects> CODEC = Entry.CODEC.listOf().xmap(SuspiciousStewEffects::new, SuspiciousStewEffects::effects);
/*    */   
/* 32 */   public static final StreamCodec<RegistryFriendlyByteBuf, SuspiciousStewEffects> STREAM_CODEC = Entry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(SuspiciousStewEffects::new, SuspiciousStewEffects::effects);
/*    */   
/*    */   public SuspiciousStewEffects withEffectAdded(Entry entry) {
/* 35 */     return new SuspiciousStewEffects(net.minecraft.util.Util.copyAndAdd(this.effects, entry));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onConsume(net.minecraft.world.level.Level level, LivingEntity user, net.minecraft.world.item.ItemStack stack, Consumable consumable) {
/* 40 */     for (Entry effect : this.effects) {
/* 41 */       user.addEffect(effect.createEffectInstance());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, net.minecraft.core.component.DataComponentGetter components) {
/* 47 */     if (flag.isCreative()) {
/* 48 */       List<MobEffectInstance> effectInstances = new java.util.ArrayList<>();
/* 49 */       for (Entry effect : this.effects) {
/* 50 */         effectInstances.add(effect.createEffectInstance());
/*    */       }
/* 52 */       PotionContents.addPotionTooltip(effectInstances, consumer, 1.0F, context.tickRate());
/*    */     } 
/*    */   }
/*    */   public static final class Entry extends Record { private final Holder<MobEffect> effect; private final int duration; public static final Codec<Entry> CODEC;
/* 56 */     public Entry(Holder<MobEffect> effect, int duration) { this.effect = effect; this.duration = duration; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/SuspiciousStewEffects$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #56	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/component/SuspiciousStewEffects$Entry; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/SuspiciousStewEffects$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #56	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/component/SuspiciousStewEffects$Entry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/SuspiciousStewEffects$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #56	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/component/SuspiciousStewEffects$Entry;
/* 56 */       //   0	8	1	o	Ljava/lang/Object; } public Holder<MobEffect> effect() { return this.effect; } public int duration() { return this.duration; } static {
/* 57 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)MobEffect.CODEC.fieldOf("id").forGetter(Entry::effect), (App)Codec.INT.lenientOptionalFieldOf("duration", 160).forGetter(Entry::duration)).apply((Applicative)i, Entry::new));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 62 */     public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(MobEffect.STREAM_CODEC, Entry::effect, ByteBufCodecs.VAR_INT, Entry::duration, Entry::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public MobEffectInstance createEffectInstance() {
/* 69 */       return new MobEffectInstance(this.effect, this.duration);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/SuspiciousStewEffects.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
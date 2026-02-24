/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.EitherHolder;
/*    */ import net.minecraft.world.item.Instrument;
/*    */ import net.minecraft.world.item.Item;
/*    */ 
/*    */ public final class InstrumentComponent extends Record implements TooltipProvider {
/*    */   private final EitherHolder<Instrument> instrument;
/*    */   
/* 23 */   public InstrumentComponent(EitherHolder<Instrument> instrument) { this.instrument = instrument; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/InstrumentComponent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 23 */     //   0	7	0	this	Lnet/minecraft/world/item/component/InstrumentComponent; } public EitherHolder<Instrument> instrument() { return this.instrument; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/InstrumentComponent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/InstrumentComponent; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/InstrumentComponent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/InstrumentComponent;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 26 */   } public static final Codec<InstrumentComponent> CODEC = EitherHolder.codec(Registries.INSTRUMENT, Instrument.CODEC).xmap(InstrumentComponent::new, InstrumentComponent::instrument);
/* 27 */   public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, InstrumentComponent> STREAM_CODEC = EitherHolder.streamCodec(Registries.INSTRUMENT, Instrument.STREAM_CODEC).map(InstrumentComponent::new, InstrumentComponent::instrument);
/*    */   
/*    */   public InstrumentComponent(Holder<Instrument> instrument) {
/* 30 */     this(new EitherHolder(instrument));
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public InstrumentComponent(ResourceKey<Instrument> instrument) {
/* 35 */     this(new EitherHolder(instrument));
/*    */   }
/*    */ 
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, net.minecraft.world.item.TooltipFlag flag, DataComponentGetter components) {
/* 40 */     HolderLookup.Provider registries = context.registries();
/* 41 */     if (registries == null) {
/*    */       return;
/*    */     }
/* 44 */     unwrap(registries).ifPresent(instrument -> {
/*    */           Component description = ComponentUtils.mergeStyles(((Instrument)instrument.value()).description(), Style.EMPTY.withColor(ChatFormatting.GRAY));
/*    */           consumer.accept(description);
/*    */         });
/*    */   }
/*    */   
/*    */   public java.util.Optional<Holder<Instrument>> unwrap(HolderLookup.Provider registries) {
/* 51 */     return this.instrument.unwrap(registries);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/InstrumentComponent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
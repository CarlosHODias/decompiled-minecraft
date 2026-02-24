/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceSortedSets;
/*    */ import java.util.SequencedSet;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ 
/*    */ public final class TooltipDisplay extends Record {
/*    */   private final boolean hideTooltip;
/*    */   private final SequencedSet<DataComponentType<?>> hiddenComponents;
/*    */   
/* 15 */   public TooltipDisplay(boolean hideTooltip, SequencedSet<DataComponentType<?>> hiddenComponents) { this.hideTooltip = hideTooltip; this.hiddenComponents = hiddenComponents; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/TooltipDisplay;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/world/item/component/TooltipDisplay; } public boolean hideTooltip() { return this.hideTooltip; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/TooltipDisplay;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/TooltipDisplay; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/TooltipDisplay;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public SequencedSet<DataComponentType<?>> hiddenComponents() { return this.hiddenComponents; }
/*    */   
/* 17 */   private static final Codec<SequencedSet<DataComponentType<?>>> COMPONENT_SET_CODEC = DataComponentType.CODEC.listOf()
/* 18 */     .xmap(ReferenceLinkedOpenHashSet::new, java.util.List::copyOf); public static final Codec<TooltipDisplay> CODEC;
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.optionalFieldOf("hide_tooltip", false).forGetter(TooltipDisplay::hideTooltip), (App)COMPONENT_SET_CODEC.optionalFieldOf("hidden_components", ReferenceSortedSets.emptySet()).forGetter(TooltipDisplay::hiddenComponents)).apply((com.mojang.datafixers.kinds.Applicative)i, TooltipDisplay::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, TooltipDisplay> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.BOOL, TooltipDisplay::hideTooltip, 
/*    */       
/* 27 */       DataComponentType.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs.collection(ReferenceLinkedOpenHashSet::new)), TooltipDisplay::hiddenComponents, TooltipDisplay::new);
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static final TooltipDisplay DEFAULT = new TooltipDisplay(false, (SequencedSet<DataComponentType<?>>)ReferenceSortedSets.emptySet());
/*    */   
/*    */   public TooltipDisplay withHidden(DataComponentType<?> component, boolean hidden) {
/* 34 */     if (this.hiddenComponents.contains(component) == hidden) {
/* 35 */       return this;
/*    */     }
/* 37 */     ReferenceLinkedOpenHashSet<DataComponentType<?>> referenceLinkedOpenHashSet = new ReferenceLinkedOpenHashSet(this.hiddenComponents);
/* 38 */     if (hidden) {
/* 39 */       referenceLinkedOpenHashSet.add(component);
/*    */     } else {
/* 41 */       referenceLinkedOpenHashSet.remove(component);
/*    */     } 
/* 43 */     return new TooltipDisplay(this.hideTooltip, (SequencedSet<DataComponentType<?>>)referenceLinkedOpenHashSet);
/*    */   }
/*    */   
/*    */   public boolean shows(DataComponentType<?> component) {
/* 47 */     return (!this.hideTooltip && !this.hiddenComponents.contains(component));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/TooltipDisplay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
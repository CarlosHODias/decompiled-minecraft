/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Map;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public final class BlockItemStateProperties extends Record implements TooltipProvider {
/*    */   private final Map<String, String> properties;
/*    */   
/* 24 */   public BlockItemStateProperties(Map<String, String> properties) { this.properties = properties; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/BlockItemStateProperties;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 24 */     //   0	7	0	this	Lnet/minecraft/world/item/component/BlockItemStateProperties; } public Map<String, String> properties() { return this.properties; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/BlockItemStateProperties;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/BlockItemStateProperties; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/BlockItemStateProperties;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/BlockItemStateProperties;
/* 25 */     //   0	8	1	o	Ljava/lang/Object; } public static final BlockItemStateProperties EMPTY = new BlockItemStateProperties(Map.of());
/*    */   
/* 27 */   public static final Codec<BlockItemStateProperties> CODEC = Codec.unboundedMap((Codec)Codec.STRING, (Codec)Codec.STRING).xmap(BlockItemStateProperties::new, BlockItemStateProperties::properties);
/*    */   
/* 29 */   private static final StreamCodec<ByteBuf, Map<String, String>> PROPERTIES_STREAM_CODEC = ByteBufCodecs.map(it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.STRING_UTF8);
/* 30 */   public static final StreamCodec<ByteBuf, BlockItemStateProperties> STREAM_CODEC = PROPERTIES_STREAM_CODEC.map(BlockItemStateProperties::new, BlockItemStateProperties::properties);
/*    */   
/*    */   public <T extends Comparable<T>> BlockItemStateProperties with(Property<T> property, T value) {
/* 33 */     return new BlockItemStateProperties(Util.copyAndPut(this.properties, property.getName(), property.getName((Comparable)value)));
/*    */   }
/*    */   
/*    */   public <T extends Comparable<T>> BlockItemStateProperties with(Property<T> property, BlockState state) {
/* 37 */     return with(property, (T)state.getValue(property));
/*    */   }
/*    */   
/*    */   public <T extends Comparable<T>> T get(Property<T> property) {
/* 41 */     String value = this.properties.get(property.getName());
/* 42 */     if (value == null) {
/* 43 */       return null;
/*    */     }
/* 45 */     return (T)property.getValue(value).orElse(null);
/*    */   }
/*    */   
/*    */   public BlockState apply(BlockState state) {
/* 49 */     StateDefinition<Block, BlockState> stateDefinition = state.getBlock().getStateDefinition();
/* 50 */     for (Map.Entry<String, String> entry : this.properties.entrySet()) {
/* 51 */       Property<?> property = stateDefinition.getProperty(entry.getKey());
/* 52 */       if (property != null) {
/* 53 */         state = updateState(state, property, entry.getValue());
/*    */       }
/*    */     } 
/* 56 */     return state;
/*    */   }
/*    */   
/*    */   private static <T extends Comparable<T>> BlockState updateState(BlockState state, Property<T> property, String value) {
/* 60 */     return property.getValue(value).map(v -> (BlockState)state.setValue(property, v)).orElse(state);
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 64 */     return this.properties.isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, net.minecraft.world.item.TooltipFlag flag, DataComponentGetter components) {
/* 69 */     Integer honeyLevel = get((Property<Integer>)net.minecraft.world.level.block.BeehiveBlock.HONEY_LEVEL);
/* 70 */     if (honeyLevel != null)
/* 71 */       consumer.accept(Component.translatable("container.beehive.honey", new Object[] { honeyLevel, 5 }).withStyle(ChatFormatting.GRAY)); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/BlockItemStateProperties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
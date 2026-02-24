/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.ImmutableBiMap;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public interface WeatheringCopper extends ChangeOverTimeBlock<WeatheringCopper.WeatherState> {
/*  19 */   public static final Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = (Supplier<BiMap<Block, Block>>)Suppliers.memoize(() -> ImmutableBiMap.builder().put(Blocks.COPPER_BLOCK, Blocks.EXPOSED_COPPER).put(Blocks.EXPOSED_COPPER, Blocks.WEATHERED_COPPER).put(Blocks.WEATHERED_COPPER, Blocks.OXIDIZED_COPPER).put(Blocks.CUT_COPPER, Blocks.EXPOSED_CUT_COPPER).put(Blocks.EXPOSED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER).put(Blocks.WEATHERED_CUT_COPPER, Blocks.OXIDIZED_CUT_COPPER).put(Blocks.CHISELED_COPPER, Blocks.EXPOSED_CHISELED_COPPER).put(Blocks.EXPOSED_CHISELED_COPPER, Blocks.WEATHERED_CHISELED_COPPER).put(Blocks.WEATHERED_CHISELED_COPPER, Blocks.OXIDIZED_CHISELED_COPPER).put(Blocks.CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB).put(Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB).put(Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.OXIDIZED_CUT_COPPER_SLAB).put(Blocks.CUT_COPPER_STAIRS, Blocks.EXPOSED_CUT_COPPER_STAIRS).put(Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.WEATHERED_CUT_COPPER_STAIRS).put(Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.OXIDIZED_CUT_COPPER_STAIRS).put(Blocks.COPPER_DOOR, Blocks.EXPOSED_COPPER_DOOR).put(Blocks.EXPOSED_COPPER_DOOR, Blocks.WEATHERED_COPPER_DOOR).put(Blocks.WEATHERED_COPPER_DOOR, Blocks.OXIDIZED_COPPER_DOOR).put(Blocks.COPPER_TRAPDOOR, Blocks.EXPOSED_COPPER_TRAPDOOR).put(Blocks.EXPOSED_COPPER_TRAPDOOR, Blocks.WEATHERED_COPPER_TRAPDOOR).put(Blocks.WEATHERED_COPPER_TRAPDOOR, Blocks.OXIDIZED_COPPER_TRAPDOOR).putAll((Map)Blocks.COPPER_BARS.weatheringMapping()).put(Blocks.COPPER_GRATE, Blocks.EXPOSED_COPPER_GRATE).put(Blocks.EXPOSED_COPPER_GRATE, Blocks.WEATHERED_COPPER_GRATE).put(Blocks.WEATHERED_COPPER_GRATE, Blocks.OXIDIZED_COPPER_GRATE).put(Blocks.COPPER_BULB, Blocks.EXPOSED_COPPER_BULB).put(Blocks.EXPOSED_COPPER_BULB, Blocks.WEATHERED_COPPER_BULB).put(Blocks.WEATHERED_COPPER_BULB, Blocks.OXIDIZED_COPPER_BULB).putAll((Map)Blocks.COPPER_LANTERN.weatheringMapping()).put(Blocks.COPPER_CHEST, Blocks.EXPOSED_COPPER_CHEST).put(Blocks.EXPOSED_COPPER_CHEST, Blocks.WEATHERED_COPPER_CHEST).put(Blocks.WEATHERED_COPPER_CHEST, Blocks.OXIDIZED_COPPER_CHEST).put(Blocks.COPPER_GOLEM_STATUE, Blocks.EXPOSED_COPPER_GOLEM_STATUE).put(Blocks.EXPOSED_COPPER_GOLEM_STATUE, Blocks.WEATHERED_COPPER_GOLEM_STATUE).put(Blocks.WEATHERED_COPPER_GOLEM_STATUE, Blocks.OXIDIZED_COPPER_GOLEM_STATUE).put(Blocks.LIGHTNING_ROD, Blocks.EXPOSED_LIGHTNING_ROD).put(Blocks.EXPOSED_LIGHTNING_ROD, Blocks.WEATHERED_LIGHTNING_ROD).put(Blocks.WEATHERED_LIGHTNING_ROD, Blocks.OXIDIZED_LIGHTNING_ROD).putAll((Map)Blocks.COPPER_CHAIN.weatheringMapping()).build());
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
/*     */   
/*  76 */   public static final Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = (Supplier<BiMap<Block, Block>>)Suppliers.memoize(() -> ((BiMap)NEXT_BY_BLOCK.get()).inverse());
/*     */   
/*     */   static Optional<Block> getPrevious(Block block) {
/*  79 */     return Optional.ofNullable((Block)((BiMap)PREVIOUS_BY_BLOCK.get()).get(block));
/*     */   }
/*     */   
/*     */   static Block getFirst(Block block) {
/*  83 */     Block candiate = block;
/*  84 */     Block previous = (Block)((BiMap)PREVIOUS_BY_BLOCK.get()).get(candiate);
/*  85 */     while (previous != null) {
/*  86 */       candiate = previous;
/*  87 */       previous = (Block)((BiMap)PREVIOUS_BY_BLOCK.get()).get(candiate);
/*     */     } 
/*  89 */     return candiate;
/*     */   }
/*     */   
/*     */   static Optional<BlockState> getPrevious(BlockState state) {
/*  93 */     return getPrevious(state.getBlock()).map(s -> s.withPropertiesOf(state));
/*     */   }
/*     */   
/*     */   static Optional<Block> getNext(Block block) {
/*  97 */     return Optional.ofNullable((Block)((BiMap)NEXT_BY_BLOCK.get()).get(block));
/*     */   }
/*     */   
/*     */   static BlockState getFirst(BlockState state) {
/* 101 */     return getFirst(state.getBlock()).withPropertiesOf(state);
/*     */   }
/*     */ 
/*     */   
/*     */   default Optional<BlockState> getNext(BlockState state) {
/* 106 */     return getNext(state.getBlock()).map(s -> s.withPropertiesOf(state));
/*     */   }
/*     */ 
/*     */   
/*     */   default float getChanceModifier() {
/* 111 */     if (getAge() == WeatherState.UNAFFECTED) {
/* 112 */       return 0.75F;
/*     */     }
/* 114 */     return 1.0F;
/*     */   }
/*     */   
/*     */   public enum WeatherState
/*     */     implements StringRepresentable
/*     */   {
/* 120 */     UNAFFECTED("unaffected"),
/* 121 */     EXPOSED("exposed"),
/* 122 */     WEATHERED("weathered"),
/* 123 */     OXIDIZED("oxidized");
/*     */     
/* 125 */     public static final IntFunction<WeatherState> BY_ID = ByIdMap.continuous(Enum::ordinal, (Object[])values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/* 126 */     public static final Codec<WeatherState> CODEC = (Codec<WeatherState>)StringRepresentable.fromEnum(WeatherState::values);
/* 127 */     public static final StreamCodec<ByteBuf, WeatherState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     WeatherState(String name) {
/* 132 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 137 */       return this.name;
/*     */     }
/*     */     
/*     */     public WeatherState next() {
/* 141 */       return BY_ID.apply(ordinal() + 1);
/*     */     }
/*     */     
/*     */     public WeatherState previous() {
/* 145 */       return BY_ID.apply(ordinal() - 1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/WeatheringCopper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
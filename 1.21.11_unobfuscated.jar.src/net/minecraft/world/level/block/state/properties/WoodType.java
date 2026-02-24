/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ 
/*    */ public final class WoodType extends Record {
/*    */   private final String name;
/*    */   private final BlockSetType setType;
/*    */   private final net.minecraft.world.level.block.SoundType soundType;
/*    */   private final net.minecraft.world.level.block.SoundType hangingSignSoundType;
/*    */   private final net.minecraft.sounds.SoundEvent fenceGateClose;
/*    */   private final net.minecraft.sounds.SoundEvent fenceGateOpen;
/*    */   
/* 12 */   public WoodType(String name, BlockSetType setType, net.minecraft.world.level.block.SoundType soundType, net.minecraft.world.level.block.SoundType hangingSignSoundType, net.minecraft.sounds.SoundEvent fenceGateClose, net.minecraft.sounds.SoundEvent fenceGateOpen) { this.name = name; this.setType = setType; this.soundType = soundType; this.hangingSignSoundType = hangingSignSoundType; this.fenceGateClose = fenceGateClose; this.fenceGateOpen = fenceGateOpen; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/state/properties/WoodType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/properties/WoodType; } public String name() { return this.name; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/state/properties/WoodType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/properties/WoodType; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/state/properties/WoodType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/block/state/properties/WoodType;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public BlockSetType setType() { return this.setType; } public net.minecraft.world.level.block.SoundType soundType() { return this.soundType; } public net.minecraft.world.level.block.SoundType hangingSignSoundType() { return this.hangingSignSoundType; } public net.minecraft.sounds.SoundEvent fenceGateClose() { return this.fenceGateClose; } public net.minecraft.sounds.SoundEvent fenceGateOpen() { return this.fenceGateOpen; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   private static final java.util.Map<String, WoodType> TYPES = (java.util.Map<String, WoodType>)new it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap();
/* 21 */   public static final com.mojang.serialization.Codec<WoodType> CODEC = com.mojang.serialization.Codec.stringResolver(WoodType::name, TYPES::get); static { java.util.Objects.requireNonNull(TYPES); }
/*    */   
/* 23 */   public static final WoodType OAK = register(new WoodType("oak", BlockSetType.OAK));
/* 24 */   public static final WoodType SPRUCE = register(new WoodType("spruce", BlockSetType.SPRUCE));
/* 25 */   public static final WoodType BIRCH = register(new WoodType("birch", BlockSetType.BIRCH));
/* 26 */   public static final WoodType ACACIA = register(new WoodType("acacia", BlockSetType.ACACIA));
/* 27 */   public static final WoodType CHERRY = register(new WoodType("cherry", BlockSetType.CHERRY, net.minecraft.world.level.block.SoundType.CHERRY_WOOD, net.minecraft.world.level.block.SoundType.CHERRY_WOOD_HANGING_SIGN, net.minecraft.sounds.SoundEvents.CHERRY_WOOD_FENCE_GATE_CLOSE, net.minecraft.sounds.SoundEvents.CHERRY_WOOD_FENCE_GATE_OPEN));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public static final WoodType JUNGLE = register(new WoodType("jungle", BlockSetType.JUNGLE));
/* 36 */   public static final WoodType DARK_OAK = register(new WoodType("dark_oak", BlockSetType.DARK_OAK));
/* 37 */   public static final WoodType PALE_OAK = register(new WoodType("pale_oak", BlockSetType.PALE_OAK));
/* 38 */   public static final WoodType CRIMSON = register(new WoodType("crimson", BlockSetType.CRIMSON, net.minecraft.world.level.block.SoundType.NETHER_WOOD, net.minecraft.world.level.block.SoundType.NETHER_WOOD_HANGING_SIGN, net.minecraft.sounds.SoundEvents.NETHER_WOOD_FENCE_GATE_CLOSE, net.minecraft.sounds.SoundEvents.NETHER_WOOD_FENCE_GATE_OPEN));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public static final WoodType WARPED = register(new WoodType("warped", BlockSetType.WARPED, net.minecraft.world.level.block.SoundType.NETHER_WOOD, net.minecraft.world.level.block.SoundType.NETHER_WOOD_HANGING_SIGN, net.minecraft.sounds.SoundEvents.NETHER_WOOD_FENCE_GATE_CLOSE, net.minecraft.sounds.SoundEvents.NETHER_WOOD_FENCE_GATE_OPEN));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public static final WoodType MANGROVE = register(new WoodType("mangrove", BlockSetType.MANGROVE));
/* 55 */   public static final WoodType BAMBOO = register(new WoodType("bamboo", BlockSetType.BAMBOO, net.minecraft.world.level.block.SoundType.BAMBOO_WOOD, net.minecraft.world.level.block.SoundType.BAMBOO_WOOD_HANGING_SIGN, net.minecraft.sounds.SoundEvents.BAMBOO_WOOD_FENCE_GATE_CLOSE, net.minecraft.sounds.SoundEvents.BAMBOO_WOOD_FENCE_GATE_OPEN));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WoodType(String name, BlockSetType setType) {
/* 65 */     this(name, setType, net.minecraft.world.level.block.SoundType.WOOD, net.minecraft.world.level.block.SoundType.HANGING_SIGN, net.minecraft.sounds.SoundEvents.FENCE_GATE_CLOSE, net.minecraft.sounds.SoundEvents.FENCE_GATE_OPEN);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static WoodType register(WoodType type) {
/* 76 */     TYPES.put(type.name(), type);
/* 77 */     return type;
/*    */   }
/*    */   
/*    */   public static java.util.stream.Stream<WoodType> values() {
/* 81 */     return TYPES.values().stream();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/WoodType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
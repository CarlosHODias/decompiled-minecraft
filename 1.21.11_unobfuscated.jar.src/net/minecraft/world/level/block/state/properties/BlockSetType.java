/*     */ package net.minecraft.world.level.block.state.properties;
/*     */ 
/*     */ public final class BlockSetType extends Record {
/*     */   private final String name;
/*     */   private final boolean canOpenByHand;
/*     */   private final boolean canOpenByWindCharge;
/*     */   private final boolean canButtonBeActivatedByArrows;
/*     */   private final PressurePlateSensitivity pressurePlateSensitivity;
/*     */   private final net.minecraft.world.level.block.SoundType soundType;
/*     */   private final net.minecraft.sounds.SoundEvent doorClose;
/*     */   
/*  12 */   public BlockSetType(String name, boolean canOpenByHand, boolean canOpenByWindCharge, boolean canButtonBeActivatedByArrows, PressurePlateSensitivity pressurePlateSensitivity, net.minecraft.world.level.block.SoundType soundType, net.minecraft.sounds.SoundEvent doorClose, net.minecraft.sounds.SoundEvent doorOpen, net.minecraft.sounds.SoundEvent trapdoorClose, net.minecraft.sounds.SoundEvent trapdoorOpen, net.minecraft.sounds.SoundEvent pressurePlateClickOff, net.minecraft.sounds.SoundEvent pressurePlateClickOn, net.minecraft.sounds.SoundEvent buttonClickOff, net.minecraft.sounds.SoundEvent buttonClickOn) { this.name = name; this.canOpenByHand = canOpenByHand; this.canOpenByWindCharge = canOpenByWindCharge; this.canButtonBeActivatedByArrows = canButtonBeActivatedByArrows; this.pressurePlateSensitivity = pressurePlateSensitivity; this.soundType = soundType; this.doorClose = doorClose; this.doorOpen = doorOpen; this.trapdoorClose = trapdoorClose; this.trapdoorOpen = trapdoorOpen; this.pressurePlateClickOff = pressurePlateClickOff; this.pressurePlateClickOn = pressurePlateClickOn; this.buttonClickOff = buttonClickOff; this.buttonClickOn = buttonClickOn; } private final net.minecraft.sounds.SoundEvent doorOpen; private final net.minecraft.sounds.SoundEvent trapdoorClose; private final net.minecraft.sounds.SoundEvent trapdoorOpen; private final net.minecraft.sounds.SoundEvent pressurePlateClickOff; private final net.minecraft.sounds.SoundEvent pressurePlateClickOn; private final net.minecraft.sounds.SoundEvent buttonClickOff; private final net.minecraft.sounds.SoundEvent buttonClickOn; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/state/properties/BlockSetType;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #12	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/properties/BlockSetType; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/state/properties/BlockSetType;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #12	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/properties/BlockSetType; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/state/properties/BlockSetType;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #12	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/state/properties/BlockSetType;
/*  12 */     //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public boolean canOpenByHand() { return this.canOpenByHand; } public boolean canOpenByWindCharge() { return this.canOpenByWindCharge; } public boolean canButtonBeActivatedByArrows() { return this.canButtonBeActivatedByArrows; } public PressurePlateSensitivity pressurePlateSensitivity() { return this.pressurePlateSensitivity; } public net.minecraft.world.level.block.SoundType soundType() { return this.soundType; } public net.minecraft.sounds.SoundEvent doorClose() { return this.doorClose; } public net.minecraft.sounds.SoundEvent doorOpen() { return this.doorOpen; } public net.minecraft.sounds.SoundEvent trapdoorClose() { return this.trapdoorClose; } public net.minecraft.sounds.SoundEvent trapdoorOpen() { return this.trapdoorOpen; } public net.minecraft.sounds.SoundEvent pressurePlateClickOff() { return this.pressurePlateClickOff; } public net.minecraft.sounds.SoundEvent pressurePlateClickOn() { return this.pressurePlateClickOn; } public net.minecraft.sounds.SoundEvent buttonClickOff() { return this.buttonClickOff; } public net.minecraft.sounds.SoundEvent buttonClickOn() { return this.buttonClickOn; }
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
/*  28 */   private static final java.util.Map<String, BlockSetType> TYPES = (java.util.Map<String, BlockSetType>)new it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap();
/*  29 */   public static final com.mojang.serialization.Codec<BlockSetType> CODEC = com.mojang.serialization.Codec.stringResolver(BlockSetType::name, TYPES::get); static { java.util.Objects.requireNonNull(TYPES); }
/*     */   
/*  31 */   public static final BlockSetType IRON = register(new BlockSetType("iron", false, false, false, PressurePlateSensitivity.EVERYTHING, net.minecraft.world.level.block.SoundType.IRON, net.minecraft.sounds.SoundEvents.IRON_DOOR_CLOSE, net.minecraft.sounds.SoundEvents.IRON_DOOR_OPEN, net.minecraft.sounds.SoundEvents.IRON_TRAPDOOR_CLOSE, net.minecraft.sounds.SoundEvents.IRON_TRAPDOOR_OPEN, net.minecraft.sounds.SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, net.minecraft.sounds.SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, net.minecraft.sounds.SoundEvents.STONE_BUTTON_CLICK_OFF, net.minecraft.sounds.SoundEvents.STONE_BUTTON_CLICK_ON));
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
/*  50 */   public static final BlockSetType COPPER = register(new BlockSetType("copper", true, true, false, PressurePlateSensitivity.EVERYTHING, net.minecraft.world.level.block.SoundType.COPPER, net.minecraft.sounds.SoundEvents.COPPER_DOOR_CLOSE, net.minecraft.sounds.SoundEvents.COPPER_DOOR_OPEN, net.minecraft.sounds.SoundEvents.COPPER_TRAPDOOR_CLOSE, net.minecraft.sounds.SoundEvents.COPPER_TRAPDOOR_OPEN, net.minecraft.sounds.SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, net.minecraft.sounds.SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, net.minecraft.sounds.SoundEvents.STONE_BUTTON_CLICK_OFF, net.minecraft.sounds.SoundEvents.STONE_BUTTON_CLICK_ON));
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
/*  70 */   public static final BlockSetType GOLD = register(new BlockSetType("gold", false, true, false, PressurePlateSensitivity.EVERYTHING, net.minecraft.world.level.block.SoundType.METAL, net.minecraft.sounds.SoundEvents.IRON_DOOR_CLOSE, net.minecraft.sounds.SoundEvents.IRON_DOOR_OPEN, net.minecraft.sounds.SoundEvents.IRON_TRAPDOOR_CLOSE, net.minecraft.sounds.SoundEvents.IRON_TRAPDOOR_OPEN, net.minecraft.sounds.SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, net.minecraft.sounds.SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, net.minecraft.sounds.SoundEvents.STONE_BUTTON_CLICK_OFF, net.minecraft.sounds.SoundEvents.STONE_BUTTON_CLICK_ON));
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
/*  94 */   public static final BlockSetType STONE = register(new BlockSetType("stone", true, true, false, PressurePlateSensitivity.MOBS, net.minecraft.world.level.block.SoundType.STONE, net.minecraft.sounds.SoundEvents.IRON_DOOR_CLOSE, net.minecraft.sounds.SoundEvents.IRON_DOOR_OPEN, net.minecraft.sounds.SoundEvents.IRON_TRAPDOOR_CLOSE, net.minecraft.sounds.SoundEvents.IRON_TRAPDOOR_OPEN, net.minecraft.sounds.SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, net.minecraft.sounds.SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, net.minecraft.sounds.SoundEvents.STONE_BUTTON_CLICK_OFF, net.minecraft.sounds.SoundEvents.STONE_BUTTON_CLICK_ON));
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
/* 115 */   public static final BlockSetType POLISHED_BLACKSTONE = register(new BlockSetType("polished_blackstone", true, true, false, PressurePlateSensitivity.MOBS, net.minecraft.world.level.block.SoundType.STONE, net.minecraft.sounds.SoundEvents.IRON_DOOR_CLOSE, net.minecraft.sounds.SoundEvents.IRON_DOOR_OPEN, net.minecraft.sounds.SoundEvents.IRON_TRAPDOOR_CLOSE, net.minecraft.sounds.SoundEvents.IRON_TRAPDOOR_OPEN, net.minecraft.sounds.SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, net.minecraft.sounds.SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, net.minecraft.sounds.SoundEvents.STONE_BUTTON_CLICK_OFF, net.minecraft.sounds.SoundEvents.STONE_BUTTON_CLICK_ON));
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
/* 139 */   public static final BlockSetType OAK = register(new BlockSetType("oak"));
/* 140 */   public static final BlockSetType SPRUCE = register(new BlockSetType("spruce"));
/* 141 */   public static final BlockSetType BIRCH = register(new BlockSetType("birch"));
/* 142 */   public static final BlockSetType ACACIA = register(new BlockSetType("acacia"));
/* 143 */   public static final BlockSetType CHERRY = register(new BlockSetType("cherry", true, true, true, PressurePlateSensitivity.EVERYTHING, net.minecraft.world.level.block.SoundType.CHERRY_WOOD, net.minecraft.sounds.SoundEvents.CHERRY_WOOD_DOOR_CLOSE, net.minecraft.sounds.SoundEvents.CHERRY_WOOD_DOOR_OPEN, net.minecraft.sounds.SoundEvents.CHERRY_WOOD_TRAPDOOR_CLOSE, net.minecraft.sounds.SoundEvents.CHERRY_WOOD_TRAPDOOR_OPEN, net.minecraft.sounds.SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_OFF, net.minecraft.sounds.SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_ON, net.minecraft.sounds.SoundEvents.CHERRY_WOOD_BUTTON_CLICK_OFF, net.minecraft.sounds.SoundEvents.CHERRY_WOOD_BUTTON_CLICK_ON));
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
/* 159 */   public static final BlockSetType JUNGLE = register(new BlockSetType("jungle"));
/* 160 */   public static final BlockSetType DARK_OAK = register(new BlockSetType("dark_oak"));
/* 161 */   public static final BlockSetType PALE_OAK = register(new BlockSetType("pale_oak"));
/* 162 */   public static final BlockSetType CRIMSON = register(new BlockSetType("crimson", true, true, true, PressurePlateSensitivity.EVERYTHING, net.minecraft.world.level.block.SoundType.NETHER_WOOD, net.minecraft.sounds.SoundEvents.NETHER_WOOD_DOOR_CLOSE, net.minecraft.sounds.SoundEvents.NETHER_WOOD_DOOR_OPEN, net.minecraft.sounds.SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE, net.minecraft.sounds.SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN, net.minecraft.sounds.SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF, net.minecraft.sounds.SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON, net.minecraft.sounds.SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF, net.minecraft.sounds.SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON));
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
/* 178 */   public static final BlockSetType WARPED = register(new BlockSetType("warped", true, true, true, PressurePlateSensitivity.EVERYTHING, net.minecraft.world.level.block.SoundType.NETHER_WOOD, net.minecraft.sounds.SoundEvents.NETHER_WOOD_DOOR_CLOSE, net.minecraft.sounds.SoundEvents.NETHER_WOOD_DOOR_OPEN, net.minecraft.sounds.SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE, net.minecraft.sounds.SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN, net.minecraft.sounds.SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF, net.minecraft.sounds.SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON, net.minecraft.sounds.SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF, net.minecraft.sounds.SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON));
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
/* 194 */   public static final BlockSetType MANGROVE = register(new BlockSetType("mangrove"));
/* 195 */   public static final BlockSetType BAMBOO = register(new BlockSetType("bamboo", true, true, true, PressurePlateSensitivity.EVERYTHING, net.minecraft.world.level.block.SoundType.BAMBOO_WOOD, net.minecraft.sounds.SoundEvents.BAMBOO_WOOD_DOOR_CLOSE, net.minecraft.sounds.SoundEvents.BAMBOO_WOOD_DOOR_OPEN, net.minecraft.sounds.SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE, net.minecraft.sounds.SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN, net.minecraft.sounds.SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_OFF, net.minecraft.sounds.SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON, net.minecraft.sounds.SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_OFF, net.minecraft.sounds.SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON));
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
/*     */   public BlockSetType(String name) {
/* 213 */     this(name, true, true, true, PressurePlateSensitivity.EVERYTHING, net.minecraft.world.level.block.SoundType.WOOD, net.minecraft.sounds.SoundEvents.WOODEN_DOOR_CLOSE, net.minecraft.sounds.SoundEvents.WOODEN_DOOR_OPEN, net.minecraft.sounds.SoundEvents.WOODEN_TRAPDOOR_CLOSE, net.minecraft.sounds.SoundEvents.WOODEN_TRAPDOOR_OPEN, net.minecraft.sounds.SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, net.minecraft.sounds.SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, net.minecraft.sounds.SoundEvents.WOODEN_BUTTON_CLICK_OFF, net.minecraft.sounds.SoundEvents.WOODEN_BUTTON_CLICK_ON);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static BlockSetType register(BlockSetType type) {
/* 232 */     TYPES.put(type.name, type);
/* 233 */     return type;
/*     */   }
/*     */   
/*     */   public static java.util.stream.Stream<BlockSetType> values() {
/* 237 */     return TYPES.values().stream();
/*     */   }
/*     */   
/*     */   public enum PressurePlateSensitivity {
/* 241 */     EVERYTHING, MOBS;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/BlockSetType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
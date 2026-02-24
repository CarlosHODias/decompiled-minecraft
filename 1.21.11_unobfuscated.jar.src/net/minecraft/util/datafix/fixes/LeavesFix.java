/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.List;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntIterator;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.datafix.PackedBitStorage;
/*     */ 
/*     */ public class LeavesFix
/*     */   extends DataFix {
/*     */   private static final int NORTH_WEST_MASK = 128;
/*     */   private static final int WEST_MASK = 64;
/*     */   private static final int SOUTH_WEST_MASK = 32;
/*     */   private static final int SOUTH_MASK = 16;
/*     */   private static final int SOUTH_EAST_MASK = 8;
/*     */   private static final int EAST_MASK = 4;
/*     */   private static final int NORTH_EAST_MASK = 2;
/*     */   private static final int NORTH_MASK = 1;
/*  45 */   private static final int[][] DIRECTIONS = new int[][] { { -1, 0, 0 }, { 1, 0, 0 }, { 0, -1, 0 }, { 0, 1, 0 }, { 0, 0, -1 }, { 0, 0, 1 } };
/*     */ 
/*     */   
/*     */   private static final int DECAY_DISTANCE = 7;
/*     */   
/*     */   private static final int SIZE_BITS = 12;
/*     */   
/*     */   private static final int SIZE = 4096;
/*     */   
/*     */   private static final Object2IntMap<String> LEAVES;
/*     */ 
/*     */   
/*     */   static {
/*  58 */     LEAVES = (Object2IntMap<String>)DataFixUtils.make(new Object2IntOpenHashMap(), map -> {
/*     */           map.put("minecraft:acacia_leaves", 0);
/*     */           map.put("minecraft:birch_leaves", 1);
/*     */           map.put("minecraft:dark_oak_leaves", 2);
/*     */           map.put("minecraft:jungle_leaves", 3);
/*     */           map.put("minecraft:oak_leaves", 4);
/*     */           map.put("minecraft:spruce_leaves", 5);
/*     */         });
/*     */   }
/*  67 */   private static final Set<String> LOGS = (Set<String>)ImmutableSet.of("minecraft:acacia_bark", "minecraft:birch_bark", "minecraft:dark_oak_bark", "minecraft:jungle_bark", "minecraft:oak_bark", "minecraft:spruce_bark", (Object[])new String[] { "minecraft:acacia_log", "minecraft:birch_log", "minecraft:dark_oak_log", "minecraft:jungle_log", "minecraft:oak_log", "minecraft:spruce_log", "minecraft:stripped_acacia_log", "minecraft:stripped_birch_log", "minecraft:stripped_dark_oak_log", "minecraft:stripped_jungle_log", "minecraft:stripped_oak_log", "minecraft:stripped_spruce_log" });
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
/*     */   public LeavesFix(Schema outputSchema, boolean changesType) {
/*  89 */     super(outputSchema, changesType);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  94 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/*     */     
/*  96 */     OpticFinder<?> levelFinder = chunkType.findField("Level");
/*  97 */     OpticFinder<?> sectionsFinder = levelFinder.type().findField("Sections");
/*  98 */     Type<?> sectionsType = sectionsFinder.type();
/*  99 */     if (!(sectionsType instanceof List.ListType)) {
/* 100 */       throw new IllegalStateException("Expecting sections to be a list.");
/*     */     }
/* 102 */     Type<?> sectionType = ((List.ListType)sectionsType).getElement();
/* 103 */     OpticFinder<?> sectionFinder = DSL.typeFinder(sectionType);
/*     */     
/* 105 */     return fixTypeEverywhereTyped("Leaves fix", chunkType, chunk -> sectionFinder.updateTyped(levelFinder, ()));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class Section
/*     */   {
/*     */     protected static final String BLOCK_STATES_TAG = "BlockStates";
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
/*     */     protected static final String NAME_TAG = "Name";
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
/*     */     protected static final String PROPERTIES_TAG = "Properties";
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
/* 193 */     private final Type<Pair<String, Dynamic<?>>> blockStateType = DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType());
/* 194 */     protected final OpticFinder<java.util.List<Pair<String, Dynamic<?>>>> paletteFinder = DSL.fieldFinder("Palette", (Type)DSL.list(this.blockStateType));
/*     */     
/*     */     protected final java.util.List<Dynamic<?>> palette;
/*     */     protected final int index;
/*     */     protected PackedBitStorage storage;
/*     */     
/*     */     public Section(Typed<?> section, Schema inputSchema) {
/* 201 */       if (!Objects.equals(inputSchema.getType(References.BLOCK_STATE), this.blockStateType)) {
/* 202 */         throw new IllegalStateException("Block state type is not what was expected.");
/*     */       }
/*     */       
/* 205 */       Optional<java.util.List<Pair<String, Dynamic<?>>>> typedPalette = section.getOptional(this.paletteFinder);
/*     */       
/* 207 */       this.palette = (java.util.List<Dynamic<?>>)typedPalette.map(p -> (java.util.List)p.stream().map(Pair::getSecond).collect(Collectors.toList())).orElse(ImmutableList.of());
/*     */       
/* 209 */       Dynamic<?> tag = (Dynamic)section.get(DSL.remainderFinder());
/* 210 */       this.index = tag.get("Y").asInt(0);
/*     */       
/* 212 */       readStorage(tag);
/*     */     }
/*     */     
/*     */     protected void readStorage(Dynamic<?> tag) {
/* 216 */       if (skippable()) {
/* 217 */         this.storage = null;
/*     */       } else {
/* 219 */         long[] states = tag.get("BlockStates").asLongStream().toArray();
/* 220 */         int size = Math.max(4, DataFixUtils.ceillog2(this.palette.size()));
/* 221 */         this.storage = new PackedBitStorage(size, 4096, states);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Typed<?> write(Typed<?> section) {
/* 226 */       if (isSkippable()) {
/* 227 */         return section;
/*     */       }
/* 229 */       return 
/* 230 */         section.update(DSL.remainderFinder(), tag -> tag.set("BlockStates", tag.createLongList(Arrays.stream(this.storage.getRaw()))))
/* 231 */         .set(this.paletteFinder, this.palette.stream().map(b -> Pair.of(References.BLOCK_STATE.typeName(), b)).collect(Collectors.toList()));
/*     */     }
/*     */     
/*     */     public boolean isSkippable() {
/* 235 */       return (this.storage == null);
/*     */     }
/*     */     
/*     */     public int getBlock(int pos) {
/* 239 */       return this.storage.get(pos);
/*     */     }
/*     */     
/*     */     protected int getStateId(String blockName, boolean persistent, int distance) {
/* 243 */       return LeavesFix.LEAVES.get(blockName) << 5 | (persistent ? 16 : 0) | distance;
/*     */     }
/*     */     
/*     */     int getIndex() {
/* 247 */       return this.index;
/*     */     }
/*     */     
/*     */     protected abstract boolean skippable();
/*     */   }
/*     */   
/*     */   public static final class LeavesSection
/*     */     extends Section
/*     */   {
/*     */     private static final String PERSISTENT = "persistent";
/*     */     private static final String DECAYABLE = "decayable";
/*     */     private static final String DISTANCE = "distance";
/*     */     private IntSet leaveIds;
/*     */     private IntSet logIds;
/*     */     private Int2IntMap stateToIdMap;
/*     */     
/*     */     public LeavesSection(Typed<?> section, Schema inputSchema) {
/* 264 */       super(section, inputSchema);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean skippable() {
/* 269 */       this.leaveIds = (IntSet)new IntOpenHashSet();
/* 270 */       this.logIds = (IntSet)new IntOpenHashSet();
/* 271 */       this.stateToIdMap = (Int2IntMap)new Int2IntOpenHashMap();
/*     */       
/* 273 */       for (int i = 0; i < this.palette.size(); i++) {
/* 274 */         Dynamic<?> paletteTag = this.palette.get(i);
/* 275 */         String blockName = paletteTag.get("Name").asString("");
/* 276 */         if (LeavesFix.LEAVES.containsKey(blockName)) {
/* 277 */           boolean persistent = Objects.equals(paletteTag.get("Properties").get("decayable").asString(""), "false");
/* 278 */           this.leaveIds.add(i);
/* 279 */           this.stateToIdMap.put(getStateId(blockName, persistent, 7), i);
/* 280 */           this.palette.set(i, makeLeafTag(paletteTag, blockName, persistent, 7));
/*     */         } 
/* 282 */         if (LeavesFix.LOGS.contains(blockName)) {
/* 283 */           this.logIds.add(i);
/*     */         }
/*     */       } 
/*     */       
/* 287 */       return (this.leaveIds.isEmpty() && this.logIds.isEmpty());
/*     */     }
/*     */     
/*     */     private Dynamic<?> makeLeafTag(Dynamic<?> input, String blockName, boolean persistent, int distance) {
/* 291 */       Dynamic<?> properties = input.emptyMap();
/* 292 */       properties = properties.set("persistent", properties.createString(persistent ? "true" : "false"));
/* 293 */       properties = properties.set("distance", properties.createString(Integer.toString(distance)));
/*     */       
/* 295 */       Dynamic<?> tag = input.emptyMap();
/* 296 */       tag = tag.set("Properties", properties);
/* 297 */       tag = tag.set("Name", tag.createString(blockName));
/* 298 */       return tag;
/*     */     }
/*     */     
/*     */     public boolean isLog(int block) {
/* 302 */       return this.logIds.contains(block);
/*     */     }
/*     */     
/*     */     public boolean isLeaf(int block) {
/* 306 */       return this.leaveIds.contains(block);
/*     */     }
/*     */     
/*     */     private int getDistance(int block) {
/* 310 */       if (isLog(block)) {
/* 311 */         return 0;
/*     */       }
/* 313 */       return Integer.parseInt(((Dynamic)this.palette.get(block)).get("Properties").get("distance").asString(""));
/*     */     }
/*     */     
/*     */     private void setDistance(int pos, int block, int distance) {
/* 317 */       Dynamic<?> baseTag = this.palette.get(block);
/* 318 */       String blockName = baseTag.get("Name").asString("");
/* 319 */       boolean persistent = Objects.equals(baseTag.get("Properties").get("persistent").asString(""), "true");
/* 320 */       int stateId = getStateId(blockName, persistent, distance);
/*     */       
/* 322 */       if (!this.stateToIdMap.containsKey(stateId)) {
/* 323 */         int i = this.palette.size();
/* 324 */         this.leaveIds.add(i);
/* 325 */         this.stateToIdMap.put(stateId, i);
/* 326 */         this.palette.add(makeLeafTag(baseTag, blockName, persistent, distance));
/*     */       } 
/*     */       
/* 329 */       int id = this.stateToIdMap.get(stateId);
/* 330 */       if (1 << this.storage.getBits() <= id) {
/* 331 */         PackedBitStorage newStorage = new PackedBitStorage(this.storage.getBits() + 1, 4096);
/* 332 */         for (int i = 0; i < 4096; i++) {
/* 333 */           newStorage.set(i, this.storage.get(i));
/*     */         }
/* 335 */         this.storage = newStorage;
/*     */       } 
/* 337 */       this.storage.set(pos, id);
/*     */     }
/*     */   }
/*     */   
/*     */   public static int getIndex(int x, int y, int z) {
/* 342 */     return y << 8 | z << 4 | x;
/*     */   }
/*     */   
/*     */   private int getX(int index) {
/* 346 */     return index & 0xF;
/*     */   }
/*     */   
/*     */   private int getY(int index) {
/* 350 */     return index >> 8 & 0xFF;
/*     */   }
/*     */   
/*     */   private int getZ(int index) {
/* 354 */     return index >> 4 & 0xF;
/*     */   }
/*     */   
/*     */   public static int getSideMask(boolean west, boolean east, boolean north, boolean south) {
/* 358 */     int s = 0;
/* 359 */     if (north) {
/* 360 */       if (east) {
/* 361 */         s |= 0x2;
/* 362 */       } else if (west) {
/* 363 */         s |= 0x80;
/*     */       } else {
/* 365 */         s |= 0x1;
/*     */       } 
/* 367 */     } else if (south) {
/* 368 */       if (west) {
/* 369 */         s |= 0x20;
/* 370 */       } else if (east) {
/* 371 */         s |= 0x8;
/*     */       } else {
/* 373 */         s |= 0x10;
/*     */       } 
/* 375 */     } else if (east) {
/* 376 */       s |= 0x4;
/* 377 */     } else if (west) {
/* 378 */       s |= 0x40;
/*     */     } 
/* 380 */     return s;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/LeavesFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
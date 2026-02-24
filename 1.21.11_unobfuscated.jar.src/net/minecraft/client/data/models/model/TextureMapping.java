/*     */ package net.minecraft.client.data.models.model;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ public class TextureMapping
/*     */ {
/*  16 */   private final Map<TextureSlot, Identifier> slots = Maps.newHashMap();
/*  17 */   private final Set<TextureSlot> forcedSlots = Sets.newHashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextureMapping put(TextureSlot slot, Identifier id) {
/*  23 */     this.slots.put(slot, id);
/*  24 */     return this;
/*     */   }
/*     */   
/*     */   public TextureMapping putForced(TextureSlot slot, Identifier id) {
/*  28 */     this.slots.put(slot, id);
/*  29 */     this.forcedSlots.add(slot);
/*  30 */     return this;
/*     */   }
/*     */   
/*     */   public Stream<TextureSlot> getForced() {
/*  34 */     return this.forcedSlots.stream();
/*     */   }
/*     */   
/*     */   public TextureMapping copySlot(TextureSlot from, TextureSlot to) {
/*  38 */     this.slots.put(to, this.slots.get(from));
/*  39 */     return this;
/*     */   }
/*     */   
/*     */   public TextureMapping copyForced(TextureSlot from, TextureSlot to) {
/*  43 */     this.slots.put(to, this.slots.get(from));
/*  44 */     this.forcedSlots.add(to);
/*  45 */     return this;
/*     */   }
/*     */   
/*     */   public Identifier get(TextureSlot slot) {
/*  49 */     TextureSlot currentSlot = slot;
/*  50 */     while (currentSlot != null) {
/*  51 */       Identifier result = this.slots.get(currentSlot);
/*  52 */       if (result != null) {
/*  53 */         return result;
/*     */       }
/*  55 */       currentSlot = currentSlot.getParent();
/*     */     } 
/*  57 */     throw new IllegalStateException("Can't find texture for slot " + String.valueOf(slot));
/*     */   }
/*     */   
/*     */   public TextureMapping copyAndUpdate(TextureSlot slot, Identifier id) {
/*  61 */     TextureMapping result = new TextureMapping();
/*  62 */     result.slots.putAll(this.slots);
/*  63 */     result.forcedSlots.addAll(this.forcedSlots);
/*  64 */     result.put(slot, id);
/*  65 */     return result;
/*     */   }
/*     */   
/*     */   public static TextureMapping cube(Block block) {
/*  69 */     Identifier texture = getBlockTexture(block);
/*  70 */     return cube(texture);
/*     */   }
/*     */   
/*     */   public static TextureMapping defaultTexture(Block block) {
/*  74 */     Identifier texture = getBlockTexture(block);
/*  75 */     return defaultTexture(texture);
/*     */   }
/*     */   
/*     */   public static TextureMapping defaultTexture(Identifier texture) {
/*  79 */     return new TextureMapping().put(TextureSlot.TEXTURE, texture);
/*     */   }
/*     */   
/*     */   public static TextureMapping cube(Identifier all) {
/*  83 */     return new TextureMapping().put(TextureSlot.ALL, all);
/*     */   }
/*     */   
/*     */   public static TextureMapping cross(Block block) {
/*  87 */     return singleSlot(TextureSlot.CROSS, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping side(Block block) {
/*  91 */     return singleSlot(TextureSlot.SIDE, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping crossEmissive(Block block) {
/*  95 */     return new TextureMapping()
/*  96 */       .put(TextureSlot.CROSS, getBlockTexture(block))
/*  97 */       .put(TextureSlot.CROSS_EMISSIVE, getBlockTexture(block, "_emissive"));
/*     */   }
/*     */ 
/*     */   
/*     */   public static TextureMapping cross(Identifier cross) {
/* 102 */     return singleSlot(TextureSlot.CROSS, cross);
/*     */   }
/*     */   
/*     */   public static TextureMapping plant(Block block) {
/* 106 */     return singleSlot(TextureSlot.PLANT, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping plantEmissive(Block block) {
/* 110 */     return new TextureMapping()
/* 111 */       .put(TextureSlot.PLANT, getBlockTexture(block))
/* 112 */       .put(TextureSlot.CROSS_EMISSIVE, getBlockTexture(block, "_emissive"));
/*     */   }
/*     */ 
/*     */   
/*     */   public static TextureMapping plant(Identifier plant) {
/* 117 */     return singleSlot(TextureSlot.PLANT, plant);
/*     */   }
/*     */   
/*     */   public static TextureMapping rail(Block block) {
/* 121 */     return singleSlot(TextureSlot.RAIL, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping rail(Identifier rail) {
/* 125 */     return singleSlot(TextureSlot.RAIL, rail);
/*     */   }
/*     */   
/*     */   public static TextureMapping wool(Block block) {
/* 129 */     return singleSlot(TextureSlot.WOOL, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping flowerbed(Block block) {
/* 133 */     return new TextureMapping()
/* 134 */       .put(TextureSlot.FLOWERBED, getBlockTexture(block))
/* 135 */       .put(TextureSlot.STEM, getBlockTexture(block, "_stem"));
/*     */   }
/*     */ 
/*     */   
/*     */   public static TextureMapping wool(Identifier cross) {
/* 140 */     return singleSlot(TextureSlot.WOOL, cross);
/*     */   }
/*     */   
/*     */   public static TextureMapping stem(Block block) {
/* 144 */     return singleSlot(TextureSlot.STEM, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping attachedStem(Block stem, Block upperStem) {
/* 148 */     return new TextureMapping()
/* 149 */       .put(TextureSlot.STEM, getBlockTexture(stem))
/* 150 */       .put(TextureSlot.UPPER_STEM, getBlockTexture(upperStem));
/*     */   }
/*     */ 
/*     */   
/*     */   public static TextureMapping pattern(Block block) {
/* 155 */     return singleSlot(TextureSlot.PATTERN, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping fan(Block block) {
/* 159 */     return singleSlot(TextureSlot.FAN, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping crop(Identifier id) {
/* 163 */     return singleSlot(TextureSlot.CROP, id);
/*     */   }
/*     */   
/*     */   public static TextureMapping pane(Block body, Block edge) {
/* 167 */     return new TextureMapping().put(TextureSlot.PANE, getBlockTexture(body)).put(TextureSlot.EDGE, getBlockTexture(edge, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping singleSlot(TextureSlot slot, Identifier id) {
/* 171 */     return new TextureMapping().put(slot, id);
/*     */   }
/*     */   
/*     */   public static TextureMapping column(Block block) {
/* 175 */     return new TextureMapping()
/* 176 */       .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
/* 177 */       .put(TextureSlot.END, getBlockTexture(block, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping cubeTop(Block block) {
/* 181 */     return new TextureMapping()
/* 182 */       .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
/* 183 */       .put(TextureSlot.TOP, getBlockTexture(block, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping pottedAzalea(Block block) {
/* 187 */     return new TextureMapping()
/* 188 */       .put(TextureSlot.PLANT, getBlockTexture(block, "_plant"))
/* 189 */       .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
/* 190 */       .put(TextureSlot.TOP, getBlockTexture(block, "_top"));
/*     */   }
/*     */ 
/*     */   
/*     */   public static TextureMapping logColumn(Block block) {
/* 195 */     return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(block)).put(TextureSlot.END, getBlockTexture(block, "_top")).put(TextureSlot.PARTICLE, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping column(Identifier side, Identifier end) {
/* 199 */     return new TextureMapping().put(TextureSlot.SIDE, side).put(TextureSlot.END, end);
/*     */   }
/*     */   
/*     */   public static TextureMapping fence(Block block) {
/* 203 */     return new TextureMapping().put(TextureSlot.TEXTURE, getBlockTexture(block)).put(TextureSlot.SIDE, getBlockTexture(block, "_side")).put(TextureSlot.TOP, getBlockTexture(block, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping customParticle(Block block) {
/* 207 */     return new TextureMapping().put(TextureSlot.TEXTURE, getBlockTexture(block)).put(TextureSlot.PARTICLE, getBlockTexture(block, "_particle"));
/*     */   }
/*     */   
/*     */   public static TextureMapping cubeBottomTop(Block block) {
/* 211 */     return new TextureMapping()
/* 212 */       .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
/* 213 */       .put(TextureSlot.TOP, getBlockTexture(block, "_top"))
/* 214 */       .put(TextureSlot.BOTTOM, getBlockTexture(block, "_bottom"));
/*     */   }
/*     */   
/*     */   public static TextureMapping cubeBottomTopWithWall(Block block) {
/* 218 */     Identifier side = getBlockTexture(block);
/* 219 */     return new TextureMapping()
/* 220 */       .put(TextureSlot.WALL, side)
/* 221 */       .put(TextureSlot.SIDE, side)
/* 222 */       .put(TextureSlot.TOP, getBlockTexture(block, "_top"))
/* 223 */       .put(TextureSlot.BOTTOM, getBlockTexture(block, "_bottom"));
/*     */   }
/*     */   
/*     */   public static TextureMapping columnWithWall(Block block) {
/* 227 */     Identifier side = getBlockTexture(block);
/* 228 */     return new TextureMapping()
/* 229 */       .put(TextureSlot.TEXTURE, side)
/* 230 */       .put(TextureSlot.WALL, side)
/* 231 */       .put(TextureSlot.SIDE, side)
/* 232 */       .put(TextureSlot.END, getBlockTexture(block, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping door(Identifier top, Identifier bottom) {
/* 236 */     return new TextureMapping().put(TextureSlot.TOP, top).put(TextureSlot.BOTTOM, bottom);
/*     */   }
/*     */   
/*     */   public static TextureMapping door(Block block) {
/* 240 */     return new TextureMapping().put(TextureSlot.TOP, getBlockTexture(block, "_top")).put(TextureSlot.BOTTOM, getBlockTexture(block, "_bottom"));
/*     */   }
/*     */   
/*     */   public static TextureMapping particle(Block block) {
/* 244 */     return new TextureMapping().put(TextureSlot.PARTICLE, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping particle(Identifier id) {
/* 248 */     return new TextureMapping().put(TextureSlot.PARTICLE, id);
/*     */   }
/*     */   
/*     */   public static TextureMapping fire0(Block block) {
/* 252 */     return new TextureMapping().put(TextureSlot.FIRE, getBlockTexture(block, "_0"));
/*     */   }
/*     */   
/*     */   public static TextureMapping fire1(Block block) {
/* 256 */     return new TextureMapping().put(TextureSlot.FIRE, getBlockTexture(block, "_1"));
/*     */   }
/*     */   
/*     */   public static TextureMapping lantern(Block block) {
/* 260 */     return new TextureMapping().put(TextureSlot.LANTERN, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping torch(Block block) {
/* 264 */     return new TextureMapping().put(TextureSlot.TORCH, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping torch(Identifier id) {
/* 268 */     return new TextureMapping().put(TextureSlot.TORCH, id);
/*     */   }
/*     */   
/*     */   public static TextureMapping trialSpawner(Block block, String sideSuffix, String topSuffix) {
/* 272 */     return new TextureMapping()
/* 273 */       .put(TextureSlot.SIDE, getBlockTexture(block, sideSuffix))
/* 274 */       .put(TextureSlot.TOP, getBlockTexture(block, topSuffix))
/* 275 */       .put(TextureSlot.BOTTOM, getBlockTexture(block, "_bottom"));
/*     */   }
/*     */   
/*     */   public static TextureMapping vault(Block block, String frontSuffix, String sideSuffix, String topSuffix, String bottomSuffix) {
/* 279 */     return new TextureMapping()
/* 280 */       .put(TextureSlot.FRONT, getBlockTexture(block, frontSuffix))
/* 281 */       .put(TextureSlot.SIDE, getBlockTexture(block, sideSuffix))
/* 282 */       .put(TextureSlot.TOP, getBlockTexture(block, topSuffix))
/* 283 */       .put(TextureSlot.BOTTOM, getBlockTexture(block, bottomSuffix));
/*     */   }
/*     */   
/*     */   public static TextureMapping particleFromItem(Item item) {
/* 287 */     return new TextureMapping().put(TextureSlot.PARTICLE, getItemTexture(item));
/*     */   }
/*     */   
/*     */   public static TextureMapping commandBlock(Block block) {
/* 291 */     return new TextureMapping()
/* 292 */       .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
/* 293 */       .put(TextureSlot.FRONT, getBlockTexture(block, "_front"))
/* 294 */       .put(TextureSlot.BACK, getBlockTexture(block, "_back"));
/*     */   }
/*     */   
/*     */   public static TextureMapping orientableCube(Block block) {
/* 298 */     return new TextureMapping()
/* 299 */       .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
/* 300 */       .put(TextureSlot.FRONT, getBlockTexture(block, "_front"))
/* 301 */       .put(TextureSlot.TOP, getBlockTexture(block, "_top"))
/* 302 */       .put(TextureSlot.BOTTOM, getBlockTexture(block, "_bottom"));
/*     */   }
/*     */   
/*     */   public static TextureMapping orientableCubeOnlyTop(Block block) {
/* 306 */     return new TextureMapping()
/* 307 */       .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
/* 308 */       .put(TextureSlot.FRONT, getBlockTexture(block, "_front"))
/* 309 */       .put(TextureSlot.TOP, getBlockTexture(block, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping orientableCubeSameEnds(Block block) {
/* 313 */     return new TextureMapping()
/* 314 */       .put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
/* 315 */       .put(TextureSlot.FRONT, getBlockTexture(block, "_front"))
/* 316 */       .put(TextureSlot.END, getBlockTexture(block, "_end"));
/*     */   }
/*     */   
/*     */   public static TextureMapping top(Block block) {
/* 320 */     return new TextureMapping().put(TextureSlot.TOP, getBlockTexture(block, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping craftingTable(Block table, Block bottomWood) {
/* 324 */     return new TextureMapping()
/* 325 */       .put(TextureSlot.PARTICLE, getBlockTexture(table, "_front"))
/* 326 */       .put(TextureSlot.DOWN, getBlockTexture(bottomWood))
/* 327 */       .put(TextureSlot.UP, getBlockTexture(table, "_top"))
/* 328 */       .put(TextureSlot.NORTH, getBlockTexture(table, "_front"))
/* 329 */       .put(TextureSlot.EAST, getBlockTexture(table, "_side"))
/* 330 */       .put(TextureSlot.SOUTH, getBlockTexture(table, "_side"))
/* 331 */       .put(TextureSlot.WEST, getBlockTexture(table, "_front"));
/*     */   }
/*     */ 
/*     */   
/*     */   public static TextureMapping fletchingTable(Block table, Block bottomWood) {
/* 336 */     return new TextureMapping()
/* 337 */       .put(TextureSlot.PARTICLE, getBlockTexture(table, "_front"))
/* 338 */       .put(TextureSlot.DOWN, getBlockTexture(bottomWood))
/* 339 */       .put(TextureSlot.UP, getBlockTexture(table, "_top"))
/* 340 */       .put(TextureSlot.NORTH, getBlockTexture(table, "_front"))
/* 341 */       .put(TextureSlot.SOUTH, getBlockTexture(table, "_front"))
/* 342 */       .put(TextureSlot.EAST, getBlockTexture(table, "_side"))
/* 343 */       .put(TextureSlot.WEST, getBlockTexture(table, "_side"));
/*     */   }
/*     */   
/*     */   public static TextureMapping snifferEgg(String suffix) {
/* 347 */     return new TextureMapping()
/* 348 */       .put(TextureSlot.PARTICLE, getBlockTexture(Blocks.SNIFFER_EGG, suffix + "_north"))
/* 349 */       .put(TextureSlot.BOTTOM, getBlockTexture(Blocks.SNIFFER_EGG, suffix + "_bottom"))
/* 350 */       .put(TextureSlot.TOP, getBlockTexture(Blocks.SNIFFER_EGG, suffix + "_top"))
/* 351 */       .put(TextureSlot.NORTH, getBlockTexture(Blocks.SNIFFER_EGG, suffix + "_north"))
/* 352 */       .put(TextureSlot.SOUTH, getBlockTexture(Blocks.SNIFFER_EGG, suffix + "_south"))
/* 353 */       .put(TextureSlot.EAST, getBlockTexture(Blocks.SNIFFER_EGG, suffix + "_east"))
/* 354 */       .put(TextureSlot.WEST, getBlockTexture(Blocks.SNIFFER_EGG, suffix + "_west"));
/*     */   }
/*     */   
/*     */   public static TextureMapping driedGhast(String suffix) {
/* 358 */     return new TextureMapping()
/* 359 */       .put(TextureSlot.PARTICLE, getBlockTexture(Blocks.DRIED_GHAST, suffix + "_north"))
/* 360 */       .put(TextureSlot.BOTTOM, getBlockTexture(Blocks.DRIED_GHAST, suffix + "_bottom"))
/* 361 */       .put(TextureSlot.TOP, getBlockTexture(Blocks.DRIED_GHAST, suffix + "_top"))
/* 362 */       .put(TextureSlot.NORTH, getBlockTexture(Blocks.DRIED_GHAST, suffix + "_north"))
/* 363 */       .put(TextureSlot.SOUTH, getBlockTexture(Blocks.DRIED_GHAST, suffix + "_south"))
/* 364 */       .put(TextureSlot.EAST, getBlockTexture(Blocks.DRIED_GHAST, suffix + "_east"))
/* 365 */       .put(TextureSlot.WEST, getBlockTexture(Blocks.DRIED_GHAST, suffix + "_west"))
/* 366 */       .put(TextureSlot.TENTACLES, getBlockTexture(Blocks.DRIED_GHAST, suffix + "_tentacles"));
/*     */   }
/*     */   
/*     */   public static TextureMapping campfire(Block campfire) {
/* 370 */     return new TextureMapping()
/* 371 */       .put(TextureSlot.LIT_LOG, getBlockTexture(campfire, "_log_lit"))
/* 372 */       .put(TextureSlot.FIRE, getBlockTexture(campfire, "_fire"));
/*     */   }
/*     */ 
/*     */   
/*     */   public static TextureMapping candleCake(Block block, boolean lit) {
/* 377 */     return new TextureMapping()
/* 378 */       .put(TextureSlot.PARTICLE, getBlockTexture(Blocks.CAKE, "_side"))
/* 379 */       .put(TextureSlot.BOTTOM, getBlockTexture(Blocks.CAKE, "_bottom"))
/* 380 */       .put(TextureSlot.TOP, getBlockTexture(Blocks.CAKE, "_top"))
/* 381 */       .put(TextureSlot.SIDE, getBlockTexture(Blocks.CAKE, "_side"))
/* 382 */       .put(TextureSlot.CANDLE, getBlockTexture(block, lit ? "_lit" : ""));
/*     */   }
/*     */ 
/*     */   
/*     */   public static TextureMapping cauldron(Identifier contentTextureLoc) {
/* 387 */     return new TextureMapping()
/* 388 */       .put(TextureSlot.PARTICLE, getBlockTexture(Blocks.CAULDRON, "_side"))
/* 389 */       .put(TextureSlot.SIDE, getBlockTexture(Blocks.CAULDRON, "_side"))
/* 390 */       .put(TextureSlot.TOP, getBlockTexture(Blocks.CAULDRON, "_top"))
/* 391 */       .put(TextureSlot.BOTTOM, getBlockTexture(Blocks.CAULDRON, "_bottom"))
/* 392 */       .put(TextureSlot.INSIDE, getBlockTexture(Blocks.CAULDRON, "_inner"))
/* 393 */       .put(TextureSlot.CONTENT, contentTextureLoc);
/*     */   }
/*     */ 
/*     */   
/*     */   public static TextureMapping sculkShrieker(boolean canSummon) {
/* 398 */     String innerTopString = canSummon ? "_can_summon" : "";
/* 399 */     return new TextureMapping()
/* 400 */       .put(TextureSlot.PARTICLE, getBlockTexture(Blocks.SCULK_SHRIEKER, "_bottom"))
/* 401 */       .put(TextureSlot.SIDE, getBlockTexture(Blocks.SCULK_SHRIEKER, "_side"))
/* 402 */       .put(TextureSlot.TOP, getBlockTexture(Blocks.SCULK_SHRIEKER, "_top"))
/* 403 */       .put(TextureSlot.INNER_TOP, getBlockTexture(Blocks.SCULK_SHRIEKER, innerTopString + "_inner_top"))
/* 404 */       .put(TextureSlot.BOTTOM, getBlockTexture(Blocks.SCULK_SHRIEKER, "_bottom"));
/*     */   }
/*     */ 
/*     */   
/*     */   public static TextureMapping bars(Block block) {
/* 409 */     return new TextureMapping()
/* 410 */       .put(TextureSlot.BARS, getBlockTexture(block))
/* 411 */       .put(TextureSlot.EDGE, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping layer0(Item item) {
/* 415 */     return new TextureMapping().put(TextureSlot.LAYER0, getItemTexture(item));
/*     */   }
/*     */   
/*     */   public static TextureMapping layer0(Block block) {
/* 419 */     return new TextureMapping().put(TextureSlot.LAYER0, getBlockTexture(block));
/*     */   }
/*     */   
/*     */   public static TextureMapping layer0(Identifier id) {
/* 423 */     return new TextureMapping().put(TextureSlot.LAYER0, id);
/*     */   }
/*     */   
/*     */   public static TextureMapping layered(Identifier layer0, Identifier layer1) {
/* 427 */     return new TextureMapping().put(TextureSlot.LAYER0, layer0).put(TextureSlot.LAYER1, layer1);
/*     */   }
/*     */   
/*     */   public static TextureMapping layered(Identifier layer0, Identifier layer1, Identifier layer2) {
/* 431 */     return new TextureMapping().put(TextureSlot.LAYER0, layer0).put(TextureSlot.LAYER1, layer1).put(TextureSlot.LAYER2, layer2);
/*     */   }
/*     */   
/*     */   public static Identifier getBlockTexture(Block block) {
/* 435 */     Identifier id = BuiltInRegistries.BLOCK.getKey(block);
/* 436 */     return id.withPrefix("block/");
/*     */   }
/*     */   
/*     */   public static Identifier getBlockTexture(Block block, String suffix) {
/* 440 */     Identifier id = BuiltInRegistries.BLOCK.getKey(block);
/* 441 */     return id.withPath(path -> "block/" + path + suffix);
/*     */   }
/*     */   
/*     */   public static Identifier getItemTexture(Item block) {
/* 445 */     Identifier id = BuiltInRegistries.ITEM.getKey(block);
/* 446 */     return id.withPrefix("item/");
/*     */   }
/*     */   
/*     */   public static Identifier getItemTexture(Item item, String suffix) {
/* 450 */     Identifier id = BuiltInRegistries.ITEM.getKey(item);
/* 451 */     return id.withPath(path -> "item/" + path + suffix);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/model/TextureMapping.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
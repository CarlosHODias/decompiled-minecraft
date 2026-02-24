/*    */ package net.minecraft.client.data.models.model;
/*    */ 
/*    */ 
/*    */ public final class TextureSlot
/*    */ {
/*  6 */   public static final TextureSlot ALL = create("all");
/*  7 */   public static final TextureSlot TEXTURE = create("texture", ALL);
/*  8 */   public static final TextureSlot PARTICLE = create("particle", TEXTURE);
/*  9 */   public static final TextureSlot END = create("end", ALL);
/* 10 */   public static final TextureSlot BOTTOM = create("bottom", END);
/* 11 */   public static final TextureSlot TOP = create("top", END);
/* 12 */   public static final TextureSlot FRONT = create("front", ALL);
/* 13 */   public static final TextureSlot BACK = create("back", ALL);
/* 14 */   public static final TextureSlot SIDE = create("side", ALL);
/* 15 */   public static final TextureSlot NORTH = create("north", SIDE);
/* 16 */   public static final TextureSlot SOUTH = create("south", SIDE);
/* 17 */   public static final TextureSlot EAST = create("east", SIDE);
/* 18 */   public static final TextureSlot WEST = create("west", SIDE);
/* 19 */   public static final TextureSlot UP = create("up");
/* 20 */   public static final TextureSlot DOWN = create("down");
/* 21 */   public static final TextureSlot CROSS = create("cross");
/* 22 */   public static final TextureSlot CROSS_EMISSIVE = create("cross_emissive");
/* 23 */   public static final TextureSlot PLANT = create("plant");
/* 24 */   public static final TextureSlot WALL = create("wall", ALL);
/* 25 */   public static final TextureSlot RAIL = create("rail");
/* 26 */   public static final TextureSlot WOOL = create("wool");
/* 27 */   public static final TextureSlot PATTERN = create("pattern");
/* 28 */   public static final TextureSlot PANE = create("pane");
/* 29 */   public static final TextureSlot EDGE = create("edge");
/* 30 */   public static final TextureSlot FAN = create("fan");
/* 31 */   public static final TextureSlot STEM = create("stem");
/* 32 */   public static final TextureSlot UPPER_STEM = create("upperstem");
/* 33 */   public static final TextureSlot CROP = create("crop");
/* 34 */   public static final TextureSlot DIRT = create("dirt");
/* 35 */   public static final TextureSlot FIRE = create("fire");
/* 36 */   public static final TextureSlot LANTERN = create("lantern");
/* 37 */   public static final TextureSlot PLATFORM = create("platform");
/* 38 */   public static final TextureSlot UNSTICKY = create("unsticky");
/* 39 */   public static final TextureSlot TORCH = create("torch");
/* 40 */   public static final TextureSlot LAYER0 = create("layer0");
/* 41 */   public static final TextureSlot LAYER1 = create("layer1");
/* 42 */   public static final TextureSlot LAYER2 = create("layer2");
/* 43 */   public static final TextureSlot LIT_LOG = create("lit_log");
/* 44 */   public static final TextureSlot CANDLE = create("candle");
/* 45 */   public static final TextureSlot INSIDE = create("inside");
/* 46 */   public static final TextureSlot CONTENT = create("content");
/* 47 */   public static final TextureSlot INNER_TOP = create("inner_top");
/* 48 */   public static final TextureSlot FLOWERBED = create("flowerbed");
/* 49 */   public static final TextureSlot TENTACLES = create("tentacles");
/* 50 */   public static final TextureSlot BARS = create("bars");
/*    */   
/*    */   private final String id;
/*    */   
/*    */   private final TextureSlot parent;
/*    */   
/*    */   private static TextureSlot create(String id) {
/* 57 */     return new TextureSlot(id, null);
/*    */   }
/*    */   
/*    */   private static TextureSlot create(String id, TextureSlot parent) {
/* 61 */     return new TextureSlot(id, parent);
/*    */   }
/*    */   
/*    */   private TextureSlot(String id, TextureSlot parent) {
/* 65 */     this.id = id;
/* 66 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 70 */     return this.id;
/*    */   }
/*    */   
/*    */   public TextureSlot getParent() {
/* 74 */     return this.parent;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     return "#" + this.id;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/model/TextureSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
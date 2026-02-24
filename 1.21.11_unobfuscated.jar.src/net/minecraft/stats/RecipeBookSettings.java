/*     */ package net.minecraft.stats;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.inventory.RecipeBookType;
/*     */ 
/*     */ public final class RecipeBookSettings {
/*     */   public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, RecipeBookSettings> STREAM_CODEC;
/*     */   public static final MapCodec<RecipeBookSettings> MAP_CODEC;
/*     */   private TypeSettings crafting;
/*     */   private TypeSettings furnace;
/*     */   private TypeSettings blastFurnace;
/*     */   private TypeSettings smoker;
/*     */   
/*     */   static {
/*  18 */     STREAM_CODEC = StreamCodec.composite(TypeSettings.STREAM_CODEC, o -> o.crafting, TypeSettings.STREAM_CODEC, o -> o.furnace, TypeSettings.STREAM_CODEC, o -> o.blastFurnace, TypeSettings.STREAM_CODEC, o -> o.smoker, RecipeBookSettings::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  26 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TypeSettings.CRAFTING_MAP_CODEC.forGetter(()), (App)TypeSettings.FURNACE_MAP_CODEC.forGetter(()), (App)TypeSettings.BLAST_FURNACE_MAP_CODEC.forGetter(()), (App)TypeSettings.SMOKER_MAP_CODEC.forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, RecipeBookSettings::new));
/*     */   }
/*     */   public static final class TypeSettings extends Record {
/*     */     private final boolean open; private final boolean filtering;
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/stats/RecipeBookSettings$TypeSettings;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #33	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/stats/RecipeBookSettings$TypeSettings;
/*     */     }
/*  33 */     public TypeSettings(boolean open, boolean filtering) { this.open = open; this.filtering = filtering; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/stats/RecipeBookSettings$TypeSettings;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #33	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/stats/RecipeBookSettings$TypeSettings;
/*  33 */       //   0	8	1	o	Ljava/lang/Object; } public boolean open() { return this.open; } public boolean filtering() { return this.filtering; }
/*     */ 
/*     */ 
/*     */     
/*  37 */     public static final TypeSettings DEFAULT = new TypeSettings(false, false);
/*     */     
/*  39 */     public static final MapCodec<TypeSettings> CRAFTING_MAP_CODEC = codec("isGuiOpen", "isFilteringCraftable");
/*  40 */     public static final MapCodec<TypeSettings> FURNACE_MAP_CODEC = codec("isFurnaceGuiOpen", "isFurnaceFilteringCraftable");
/*  41 */     public static final MapCodec<TypeSettings> BLAST_FURNACE_MAP_CODEC = codec("isBlastingFurnaceGuiOpen", "isBlastingFurnaceFilteringCraftable");
/*  42 */     public static final MapCodec<TypeSettings> SMOKER_MAP_CODEC = codec("isSmokerGuiOpen", "isSmokerFilteringCraftable");
/*     */     
/*  44 */     public static final StreamCodec<io.netty.buffer.ByteBuf, TypeSettings> STREAM_CODEC = StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.BOOL, TypeSettings::open, net.minecraft.network.codec.ByteBufCodecs.BOOL, TypeSettings::filtering, TypeSettings::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/*  52 */       return "[open=" + this.open + ", filtering=" + this.filtering + "]";
/*     */     }
/*     */     
/*     */     public TypeSettings setOpen(boolean open) {
/*  56 */       return new TypeSettings(open, this.filtering);
/*     */     }
/*     */     
/*     */     public TypeSettings setFiltering(boolean filtering) {
/*  60 */       return new TypeSettings(this.open, filtering);
/*     */     }
/*     */     
/*     */     private static MapCodec<TypeSettings> codec(String openFieldName, String filteringFieldName) {
/*  64 */       return RecordCodecBuilder.mapCodec(i -> i.group((App)com.mojang.serialization.Codec.BOOL.optionalFieldOf(openFieldName, false).forGetter(TypeSettings::open), (App)com.mojang.serialization.Codec.BOOL.optionalFieldOf(filteringFieldName, false).forGetter(TypeSettings::filtering)).apply((com.mojang.datafixers.kinds.Applicative)i, TypeSettings::new));
/*     */     }
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
/*     */   public RecipeBookSettings() {
/*  77 */     this(TypeSettings.DEFAULT, TypeSettings.DEFAULT, TypeSettings.DEFAULT, TypeSettings.DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RecipeBookSettings(TypeSettings crafting, TypeSettings furnace, TypeSettings blastFurnace, TypeSettings smoker) {
/*  86 */     this.crafting = crafting;
/*  87 */     this.furnace = furnace;
/*  88 */     this.blastFurnace = blastFurnace;
/*  89 */     this.smoker = smoker;
/*     */   }
/*     */   
/*     */   @com.google.common.annotations.VisibleForTesting
/*     */   public TypeSettings getSettings(RecipeBookType type) {
/*  94 */     switch (type) { default: throw new MatchException(null, null);case CRAFTING: case FURNACE: case BLAST_FURNACE: case SMOKER: break; }  return 
/*     */ 
/*     */ 
/*     */       
/*  98 */       this.smoker;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateSettings(RecipeBookType recipeBookType, UnaryOperator<TypeSettings> operator) {
/* 103 */     switch (recipeBookType) { case CRAFTING:
/* 104 */         this.crafting = operator.apply(this.crafting); break;
/* 105 */       case FURNACE: this.furnace = operator.apply(this.furnace); break;
/* 106 */       case BLAST_FURNACE: this.blastFurnace = operator.apply(this.blastFurnace); break;
/* 107 */       case SMOKER: this.smoker = operator.apply(this.smoker);
/*     */         break; }
/*     */   
/*     */   }
/*     */   public boolean isOpen(RecipeBookType type) {
/* 112 */     return (getSettings(type)).open;
/*     */   }
/*     */   
/*     */   public void setOpen(RecipeBookType type, boolean open) {
/* 116 */     updateSettings(type, s -> s.setOpen(open));
/*     */   }
/*     */   
/*     */   public boolean isFiltering(RecipeBookType type) {
/* 120 */     return (getSettings(type)).filtering;
/*     */   }
/*     */   
/*     */   public void setFiltering(RecipeBookType type, boolean filtering) {
/* 124 */     updateSettings(type, s -> s.setFiltering(filtering));
/*     */   }
/*     */   
/*     */   public RecipeBookSettings copy() {
/* 128 */     return new RecipeBookSettings(this.crafting, this.furnace, this.blastFurnace, this.smoker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replaceFrom(RecipeBookSettings other) {
/* 137 */     this.crafting = other.crafting;
/* 138 */     this.furnace = other.furnace;
/* 139 */     this.blastFurnace = other.blastFurnace;
/* 140 */     this.smoker = other.smoker;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/stats/RecipeBookSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
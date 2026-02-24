/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class EquipmentClientInfo extends Record {
/*     */   private final Map<LayerType, List<Layer>> layers;
/*     */   
/*  18 */   public EquipmentClientInfo(Map<LayerType, List<Layer>> layers) { this.layers = layers; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/EquipmentClientInfo;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #18	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  18 */     //   0	7	0	this	Lnet/minecraft/client/resources/model/EquipmentClientInfo; } public Map<LayerType, List<Layer>> layers() { return this.layers; }
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/EquipmentClientInfo;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #18	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/EquipmentClientInfo; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/EquipmentClientInfo;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #18	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/resources/model/EquipmentClientInfo;
/*  19 */     //   0	8	1	o	Ljava/lang/Object; } private static final Codec<List<Layer>> LAYER_LIST_CODEC = ExtraCodecs.nonEmptyList(Layer.CODEC.listOf()); public static final Codec<EquipmentClientInfo> CODEC;
/*     */   static {
/*  21 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.nonEmptyMap((Codec)Codec.unboundedMap(LayerType.CODEC, LAYER_LIST_CODEC)).fieldOf("layers").forGetter(EquipmentClientInfo::layers)).apply((Applicative)i, EquipmentClientInfo::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Builder builder() {
/*  26 */     return new Builder();
/*     */   }
/*     */   
/*     */   public List<Layer> getLayers(LayerType type) {
/*  30 */     return this.layers.getOrDefault(type, List.of());
/*     */   }
/*     */   public static final class Layer extends Record { private final Identifier textureId; private final Optional<EquipmentClientInfo.Dyeable> dyeable; private final boolean usePlayerTexture; public static final Codec<Layer> CODEC;
/*  33 */     public Layer(Identifier textureId, Optional<EquipmentClientInfo.Dyeable> dyeable, boolean usePlayerTexture) { this.textureId = textureId; this.dyeable = dyeable; this.usePlayerTexture = usePlayerTexture; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/EquipmentClientInfo$Layer;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #33	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/EquipmentClientInfo$Layer; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/EquipmentClientInfo$Layer;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #33	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/EquipmentClientInfo$Layer; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/EquipmentClientInfo$Layer;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #33	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/EquipmentClientInfo$Layer;
/*  33 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier textureId() { return this.textureId; } public Optional<EquipmentClientInfo.Dyeable> dyeable() { return this.dyeable; } public boolean usePlayerTexture() { return this.usePlayerTexture; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*  39 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("texture").forGetter(Layer::textureId), (App)EquipmentClientInfo.Dyeable.CODEC.optionalFieldOf("dyeable").forGetter(Layer::dyeable), (App)Codec.BOOL.optionalFieldOf("use_player_texture", false).forGetter(Layer::usePlayerTexture)).apply((Applicative)i, Layer::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Layer(Identifier textureId) {
/*  46 */       this(textureId, Optional.empty(), false);
/*     */     }
/*     */     
/*     */     public static Layer leatherDyeable(Identifier textureId, boolean dyeable) {
/*  50 */       return new Layer(textureId, dyeable ? Optional.<EquipmentClientInfo.Dyeable>of(new EquipmentClientInfo.Dyeable(Optional.of(-6265536))) : Optional.<EquipmentClientInfo.Dyeable>empty(), false);
/*     */     }
/*     */     
/*     */     public static Layer onlyIfDyed(Identifier textureId, boolean dyeable) {
/*  54 */       return new Layer(textureId, dyeable ? Optional.<EquipmentClientInfo.Dyeable>of(new EquipmentClientInfo.Dyeable(Optional.empty())) : Optional.<EquipmentClientInfo.Dyeable>empty(), false);
/*     */     }
/*     */     
/*     */     public Identifier getTextureLocation(EquipmentClientInfo.LayerType type) {
/*  58 */       return this.textureId.withPath(path -> "textures/entity/equipment/" + type.getSerializedName() + "/" + path + ".png");
/*     */     } }
/*     */   public static final class Dyeable extends Record { private final Optional<Integer> colorWhenUndyed; public static final Codec<Dyeable> CODEC;
/*     */     
/*  62 */     public Dyeable(Optional<Integer> colorWhenUndyed) { this.colorWhenUndyed = colorWhenUndyed; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/EquipmentClientInfo$Dyeable;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #62	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/EquipmentClientInfo$Dyeable; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/EquipmentClientInfo$Dyeable;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #62	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/EquipmentClientInfo$Dyeable; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/EquipmentClientInfo$Dyeable;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #62	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/EquipmentClientInfo$Dyeable;
/*  62 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<Integer> colorWhenUndyed() { return this.colorWhenUndyed; }
/*     */     
/*     */     static {
/*  65 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color_when_undyed").forGetter(Dyeable::colorWhenUndyed)).apply((Applicative)i, Dyeable::new));
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*  71 */     private final Map<EquipmentClientInfo.LayerType, List<EquipmentClientInfo.Layer>> layersByType = new java.util.EnumMap<>(EquipmentClientInfo.LayerType.class);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addHumanoidLayers(Identifier textureId) {
/*  77 */       return addHumanoidLayers(textureId, false);
/*     */     }
/*     */     
/*     */     public Builder addHumanoidLayers(Identifier textureId, boolean dyeable) {
/*  81 */       addLayers(EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS, new EquipmentClientInfo.Layer[] { EquipmentClientInfo.Layer.leatherDyeable(textureId, dyeable) });
/*  82 */       addMainHumanoidLayer(textureId, dyeable);
/*  83 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addMainHumanoidLayer(Identifier textureId, boolean dyeable) {
/*  87 */       return addLayers(EquipmentClientInfo.LayerType.HUMANOID, new EquipmentClientInfo.Layer[] { EquipmentClientInfo.Layer.leatherDyeable(textureId, dyeable) });
/*     */     }
/*     */     
/*     */     public Builder addLayers(EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer... layers) {
/*  91 */       java.util.Collections.addAll(this.layersByType.computeIfAbsent(type, t -> new java.util.ArrayList()), layers);
/*  92 */       return this;
/*     */     }
/*     */     
/*     */     public EquipmentClientInfo build() {
/*  96 */       return new EquipmentClientInfo((Map<EquipmentClientInfo.LayerType, List<EquipmentClientInfo.Layer>>)
/*  97 */           this.layersByType.entrySet().stream().collect(com.google.common.collect.ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> List.copyOf((java.util.Collection)entry.getValue()))));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum LayerType
/*     */     implements net.minecraft.util.StringRepresentable
/*     */   {
/* 106 */     HUMANOID("humanoid"),
/* 107 */     HUMANOID_LEGGINGS("humanoid_leggings"),
/* 108 */     WINGS("wings"),
/* 109 */     WOLF_BODY("wolf_body"),
/* 110 */     HORSE_BODY("horse_body"),
/* 111 */     LLAMA_BODY("llama_body"),
/* 112 */     PIG_SADDLE("pig_saddle"),
/* 113 */     STRIDER_SADDLE("strider_saddle"),
/* 114 */     CAMEL_SADDLE("camel_saddle"),
/* 115 */     CAMEL_HUSK_SADDLE("camel_husk_saddle"),
/* 116 */     HORSE_SADDLE("horse_saddle"),
/* 117 */     DONKEY_SADDLE("donkey_saddle"),
/* 118 */     MULE_SADDLE("mule_saddle"),
/* 119 */     ZOMBIE_HORSE_SADDLE("zombie_horse_saddle"),
/* 120 */     SKELETON_HORSE_SADDLE("skeleton_horse_saddle"),
/* 121 */     HAPPY_GHAST_BODY("happy_ghast_body"),
/* 122 */     NAUTILUS_SADDLE("nautilus_saddle"),
/* 123 */     NAUTILUS_BODY("nautilus_body");
/*     */ 
/*     */     
/* 126 */     public static final Codec<LayerType> CODEC = (Codec<LayerType>)net.minecraft.util.StringRepresentable.fromEnum(LayerType::values);
/*     */     
/*     */     private final String id;
/*     */     
/*     */     LayerType(String id) {
/* 131 */       this.id = id;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 136 */       return this.id;
/*     */     }
/*     */     
/*     */     public String trimAssetPrefix() {
/* 140 */       return "trims/entity/" + this.id;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/EquipmentClientInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
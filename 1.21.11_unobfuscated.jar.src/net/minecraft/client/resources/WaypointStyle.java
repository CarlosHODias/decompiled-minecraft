/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ 
/*    */ public final class WaypointStyle extends Record {
/*    */   private final int nearDistance;
/*    */   private final int farDistance;
/*    */   private final java.util.List<net.minecraft.resources.Identifier> sprites;
/*    */   private final java.util.List<net.minecraft.resources.Identifier> spriteLocations;
/*    */   @com.google.common.annotations.VisibleForTesting
/*    */   public static final String ICON_LOCATION_PREFIX = "hud/locator_bar_dot/";
/*    */   public static final int DEFAULT_NEAR_DISTANCE = 128;
/*    */   public static final int DEFAULT_FAR_DISTANCE = 332;
/*    */   
/* 14 */   public WaypointStyle(int nearDistance, int farDistance, java.util.List<net.minecraft.resources.Identifier> sprites, java.util.List<net.minecraft.resources.Identifier> spriteLocations) { this.nearDistance = nearDistance; this.farDistance = farDistance; this.sprites = sprites; this.spriteLocations = spriteLocations; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/WaypointStyle;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/client/resources/WaypointStyle; } public int nearDistance() { return this.nearDistance; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/WaypointStyle;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/WaypointStyle; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/WaypointStyle;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/WaypointStyle;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public int farDistance() { return this.farDistance; } public java.util.List<net.minecraft.resources.Identifier> sprites() { return this.sprites; } public java.util.List<net.minecraft.resources.Identifier> spriteLocations() { return this.spriteLocations; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   private static final com.mojang.serialization.Codec<Integer> DISTANCE_CODEC = com.mojang.serialization.Codec.intRange(0, 60000000);
/*    */   
/*    */   public static final com.mojang.serialization.Codec<WaypointStyle> CODEC;
/*    */ 
/*    */   
/*    */   static {
/* 32 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)DISTANCE_CODEC.optionalFieldOf("near_distance", 128).forGetter(WaypointStyle::nearDistance), (com.mojang.datafixers.kinds.App)DISTANCE_CODEC.optionalFieldOf("far_distance", 332).forGetter(WaypointStyle::farDistance), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.nonEmptyList(net.minecraft.resources.Identifier.CODEC.listOf()).fieldOf("sprites").forGetter(WaypointStyle::sprites)).apply((com.mojang.datafixers.kinds.Applicative)i, WaypointStyle::new)).validate(WaypointStyle::validate);
/*    */   }
/*    */   @com.google.common.annotations.VisibleForTesting
/*    */   public com.mojang.serialization.DataResult<WaypointStyle> validate() {
/* 36 */     if (this.sprites.isEmpty()) {
/* 37 */       return com.mojang.serialization.DataResult.error(() -> "Must have at least one sprite icon");
/*    */     }
/* 39 */     if (this.nearDistance <= 0) {
/* 40 */       return com.mojang.serialization.DataResult.error(() -> "Near distance (" + this.nearDistance + ") must be greater than zero");
/*    */     }
/* 42 */     if (this.nearDistance >= this.farDistance) {
/* 43 */       return com.mojang.serialization.DataResult.error(() -> "Far distance (" + this.farDistance + ") cannot be closer or equal to near distance (" + this.nearDistance + ")");
/*    */     }
/* 45 */     return com.mojang.serialization.DataResult.success(this);
/*    */   }
/*    */   
/*    */   public WaypointStyle(int nearDistance, int farDistance, java.util.List<net.minecraft.resources.Identifier> sprites) {
/* 49 */     this(nearDistance, farDistance, sprites, 
/*    */         
/* 51 */         sprites.stream().map(sprite -> sprite.withPrefix("hud/locator_bar_dot/")).toList());
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.resources.Identifier sprite(float distance) {
/* 56 */     if (distance < this.nearDistance) {
/* 57 */       return this.spriteLocations.getFirst();
/*    */     }
/* 59 */     if (distance >= this.farDistance) {
/* 60 */       return this.spriteLocations.getLast();
/*    */     }
/* 62 */     if (this.spriteLocations.size() == 1) {
/* 63 */       return this.spriteLocations.getFirst();
/*    */     }
/*    */     
/* 66 */     if (this.spriteLocations.size() == 3) {
/* 67 */       return this.spriteLocations.get(1);
/*    */     }
/* 69 */     int index = net.minecraft.util.Mth.lerpInt((distance - this.nearDistance) / (this.farDistance - this.nearDistance), 1, this.spriteLocations.size() - 1);
/* 70 */     return this.spriteLocations.get(index);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/WaypointStyle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
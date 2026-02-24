/*    */ package net.minecraft.network.chat.contents.objects;
/*    */ 
/*    */ 
/*    */ public final class PlayerSprite extends Record implements ObjectInfo {
/*    */   private final net.minecraft.world.item.component.ResolvableProfile player;
/*    */   private final boolean hat;
/*    */   public static final com.mojang.serialization.MapCodec<PlayerSprite> MAP_CODEC;
/*    */   
/*  9 */   public PlayerSprite(net.minecraft.world.item.component.ResolvableProfile player, boolean hat) { this.player = player; this.hat = hat; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/contents/objects/PlayerSprite;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/objects/PlayerSprite; } public net.minecraft.world.item.component.ResolvableProfile player() { return this.player; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/objects/PlayerSprite;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/objects/PlayerSprite; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/objects/PlayerSprite;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/objects/PlayerSprite;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public boolean hat() { return this.hat; }
/*    */ 
/*    */   
/*    */   static {
/* 13 */     MAP_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.world.item.component.ResolvableProfile.CODEC.fieldOf("player").forGetter(PlayerSprite::player), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("hat", true).forGetter(PlayerSprite::hat)).apply((com.mojang.datafixers.kinds.Applicative)i, PlayerSprite::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.network.chat.FontDescription fontDescription() {
/* 20 */     return (net.minecraft.network.chat.FontDescription)new net.minecraft.network.chat.FontDescription.PlayerSprite(this.player, this.hat);
/*    */   }
/*    */ 
/*    */   
/*    */   public String description() {
/* 25 */     return this.player.name().map(name -> "[" + name + " head]").orElse("[unknown player head]");
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<PlayerSprite> codec() {
/* 30 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/contents/objects/PlayerSprite.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
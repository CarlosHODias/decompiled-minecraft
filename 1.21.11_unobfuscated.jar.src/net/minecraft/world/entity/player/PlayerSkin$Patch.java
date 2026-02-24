/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Patch
/*    */   extends Record
/*    */ {
/*    */   private final Optional<ClientAsset.ResourceTexture> body;
/*    */   private final Optional<ClientAsset.ResourceTexture> cape;
/*    */   private final Optional<ClientAsset.ResourceTexture> elytra;
/*    */   private final Optional<PlayerModelType> model;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/PlayerSkin$Patch;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/player/PlayerSkin$Patch;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/PlayerSkin$Patch;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/player/PlayerSkin$Patch;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/player/PlayerSkin$Patch;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/player/PlayerSkin$Patch;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Patch(Optional<ClientAsset.ResourceTexture> body, Optional<ClientAsset.ResourceTexture> cape, Optional<ClientAsset.ResourceTexture> elytra, Optional<PlayerModelType> model) {
/* 50 */     this.body = body; this.cape = cape; this.elytra = elytra; this.model = model; } public Optional<ClientAsset.ResourceTexture> body() { return this.body; } public Optional<ClientAsset.ResourceTexture> cape() { return this.cape; } public Optional<ClientAsset.ResourceTexture> elytra() { return this.elytra; } public Optional<PlayerModelType> model() { return this.model; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public static final Patch EMPTY = new Patch(
/* 57 */       Optional.empty(), 
/* 58 */       Optional.empty(), 
/* 59 */       Optional.empty(), 
/* 60 */       Optional.empty()); public static final MapCodec<Patch> MAP_CODEC;
/*    */   
/*    */   static {
/* 63 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ClientAsset.ResourceTexture.CODEC.optionalFieldOf("texture").forGetter(Patch::body), (App)ClientAsset.ResourceTexture.CODEC.optionalFieldOf("cape").forGetter(Patch::cape), (App)ClientAsset.ResourceTexture.CODEC.optionalFieldOf("elytra").forGetter(Patch::elytra), (App)PlayerModelType.CODEC.optionalFieldOf("model").forGetter(Patch::model)).apply((Applicative)i, Patch::create));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 70 */   public static final StreamCodec<ByteBuf, Patch> STREAM_CODEC = StreamCodec.composite(
/* 71 */       ClientAsset.ResourceTexture.STREAM_CODEC.apply(ByteBufCodecs::optional), Patch::body, 
/* 72 */       ClientAsset.ResourceTexture.STREAM_CODEC.apply(ByteBufCodecs::optional), Patch::cape, 
/* 73 */       ClientAsset.ResourceTexture.STREAM_CODEC.apply(ByteBufCodecs::optional), Patch::elytra, 
/* 74 */       PlayerModelType.STREAM_CODEC.apply(ByteBufCodecs::optional), Patch::model, Patch::create);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Patch create(Optional<ClientAsset.ResourceTexture> texture, Optional<ClientAsset.ResourceTexture> capeTexture, Optional<ClientAsset.ResourceTexture> elytraTexture, Optional<PlayerModelType> model) {
/* 84 */     if (texture.isEmpty() && capeTexture.isEmpty() && elytraTexture.isEmpty() && model.isEmpty()) {
/* 85 */       return EMPTY;
/*    */     }
/* 87 */     return new Patch(texture, capeTexture, elytraTexture, model);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/player/PlayerSkin$Patch.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
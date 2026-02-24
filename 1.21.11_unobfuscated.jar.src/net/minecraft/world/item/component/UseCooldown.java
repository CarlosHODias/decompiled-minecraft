/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public final class UseCooldown extends Record {
/*    */   private final float seconds;
/*    */   private final Optional<Identifier> cooldownGroup;
/*    */   public static final com.mojang.serialization.Codec<UseCooldown> CODEC;
/*    */   
/* 17 */   public UseCooldown(float seconds, Optional<Identifier> cooldownGroup) { this.seconds = seconds; this.cooldownGroup = cooldownGroup; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/UseCooldown;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 17 */     //   0	7	0	this	Lnet/minecraft/world/item/component/UseCooldown; } public float seconds() { return this.seconds; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/UseCooldown;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/UseCooldown; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/UseCooldown;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/UseCooldown;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Identifier> cooldownGroup() { return this.cooldownGroup; } static {
/* 18 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)net.minecraft.util.ExtraCodecs.POSITIVE_FLOAT.fieldOf("seconds").forGetter(UseCooldown::seconds), (App)Identifier.CODEC.optionalFieldOf("cooldown_group").forGetter(UseCooldown::cooldownGroup)).apply((com.mojang.datafixers.kinds.Applicative)i, UseCooldown::new));
/*    */   }
/*    */ 
/*    */   
/* 22 */   public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, UseCooldown> STREAM_CODEC = StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.FLOAT, UseCooldown::seconds, 
/*    */       
/* 24 */       Identifier.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs::optional), UseCooldown::cooldownGroup, UseCooldown::new);
/*    */ 
/*    */ 
/*    */   
/*    */   public UseCooldown(float seconds) {
/* 29 */     this(seconds, Optional.empty());
/*    */   }
/*    */   
/*    */   public int ticks() {
/* 33 */     return (int)(this.seconds * 20.0F);
/*    */   }
/*    */   
/*    */   public void apply(net.minecraft.world.item.ItemStack stack, LivingEntity user) {
/* 37 */     if (user instanceof Player) { Player player = (Player)user;
/* 38 */       player.getCooldowns().addCooldown(stack, ticks()); }
/*    */   
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/UseCooldown.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
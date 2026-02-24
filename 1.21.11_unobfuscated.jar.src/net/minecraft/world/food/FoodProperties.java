/*    */ package net.minecraft.world.food;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.component.Consumable;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class FoodProperties extends Record implements net.minecraft.world.item.component.ConsumableListener {
/*    */   private final int nutrition;
/*    */   private final float saturation;
/*    */   private final boolean canAlwaysEat;
/*    */   public static final Codec<FoodProperties> DIRECT_CODEC;
/*    */   
/* 20 */   public FoodProperties(int nutrition, float saturation, boolean canAlwaysEat) { this.nutrition = nutrition; this.saturation = saturation; this.canAlwaysEat = canAlwaysEat; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/food/FoodProperties;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 20 */     //   0	7	0	this	Lnet/minecraft/world/food/FoodProperties; } public int nutrition() { return this.nutrition; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/food/FoodProperties;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/food/FoodProperties; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/food/FoodProperties;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/food/FoodProperties;
/* 20 */     //   0	8	1	o	Ljava/lang/Object; } public float saturation() { return this.saturation; } public boolean canAlwaysEat() { return this.canAlwaysEat; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 26 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT.fieldOf("nutrition").forGetter(FoodProperties::nutrition), (App)Codec.FLOAT.fieldOf("saturation").forGetter(FoodProperties::saturation), (App)Codec.BOOL.optionalFieldOf("can_always_eat", false).forGetter(FoodProperties::canAlwaysEat)).apply((com.mojang.datafixers.kinds.Applicative)i, FoodProperties::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, FoodProperties> DIRECT_STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(ByteBufCodecs.VAR_INT, FoodProperties::nutrition, ByteBufCodecs.FLOAT, FoodProperties::saturation, ByteBufCodecs.BOOL, FoodProperties::canAlwaysEat, FoodProperties::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onConsume(Level level, LivingEntity user, net.minecraft.world.item.ItemStack stack, Consumable consumable) {
/* 41 */     RandomSource random = user.getRandom();
/* 42 */     level.playSound(null, user.getX(), user.getY(), user.getZ(), (net.minecraft.sounds.SoundEvent)consumable.sound().value(), net.minecraft.sounds.SoundSource.NEUTRAL, 1.0F, random.triangle(1.0F, 0.4F));
/*    */     
/* 44 */     if (user instanceof Player) { Player player = (Player)user;
/* 45 */       player.getFoodData().eat(this);
/* 46 */       level.playSound(null, player.getX(), player.getY(), player.getZ(), net.minecraft.sounds.SoundEvents.PLAYER_BURP, net.minecraft.sounds.SoundSource.PLAYERS, 0.5F, net.minecraft.util.Mth.randomBetween(random, 0.9F, 1.0F)); }
/*    */   
/*    */   }
/*    */   
/*    */   public static class Builder {
/*    */     private int nutrition;
/*    */     private float saturationModifier;
/*    */     private boolean canAlwaysEat;
/*    */     
/*    */     public Builder nutrition(int nutrition) {
/* 56 */       this.nutrition = nutrition;
/* 57 */       return this;
/*    */     }
/*    */     
/*    */     public Builder saturationModifier(float saturationModifier) {
/* 61 */       this.saturationModifier = saturationModifier;
/* 62 */       return this;
/*    */     }
/*    */     
/*    */     public Builder alwaysEdible() {
/* 66 */       this.canAlwaysEat = true;
/* 67 */       return this;
/*    */     }
/*    */     
/*    */     public FoodProperties build() {
/* 71 */       float saturation = FoodConstants.saturationByModifier(this.nutrition, this.saturationModifier);
/* 72 */       return new FoodProperties(this.nutrition, saturation, this.canAlwaysEat);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/food/FoodProperties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
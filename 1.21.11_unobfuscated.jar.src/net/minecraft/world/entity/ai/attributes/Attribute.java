/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class Attribute {
/* 13 */   public static final Codec<Holder<Attribute>> CODEC = BuiltInRegistries.ATTRIBUTE.holderByNameCodec();
/* 14 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Attribute>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.ATTRIBUTE);
/*    */   
/*    */   private final double defaultValue;
/*    */   private boolean syncable;
/*    */   private final String descriptionId;
/* 19 */   private Sentiment sentiment = Sentiment.POSITIVE;
/*    */   
/*    */   protected Attribute(String descriptionId, double defaultValue) {
/* 22 */     this.defaultValue = defaultValue;
/* 23 */     this.descriptionId = descriptionId;
/*    */   }
/*    */   
/*    */   public double getDefaultValue() {
/* 27 */     return this.defaultValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isClientSyncable() {
/* 32 */     return this.syncable;
/*    */   }
/*    */   
/*    */   public Attribute setSyncable(boolean syncable) {
/* 36 */     this.syncable = syncable;
/* 37 */     return this;
/*    */   }
/*    */   
/*    */   public Attribute setSentiment(Sentiment sentiment) {
/* 41 */     this.sentiment = sentiment;
/* 42 */     return this;
/*    */   }
/*    */   
/*    */   public double sanitizeValue(double value) {
/* 46 */     return value;
/*    */   }
/*    */   
/*    */   public String getDescriptionId() {
/* 50 */     return this.descriptionId;
/*    */   }
/*    */   
/*    */   public ChatFormatting getStyle(boolean valueIncrease) {
/* 54 */     return this.sentiment.getStyle(valueIncrease);
/*    */   }
/*    */   
/*    */   public enum Sentiment {
/* 58 */     POSITIVE,
/* 59 */     NEUTRAL,
/* 60 */     NEGATIVE;
/*    */ 
/*    */     
/*    */     public ChatFormatting getStyle(boolean valueIncrease) {
/* 64 */       switch (ordinal()) { default: throw new MatchException(null, null);
/* 65 */         case 0: if (valueIncrease);
/*    */         case 1: 
/* 67 */         case 2: if (valueIncrease); break; }  return ChatFormatting.BLUE;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/attributes/Attribute.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
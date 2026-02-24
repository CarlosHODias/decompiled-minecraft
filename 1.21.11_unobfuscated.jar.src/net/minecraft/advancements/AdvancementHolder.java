/*    */ package net.minecraft.advancements;
/*    */ 
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class AdvancementHolder extends Record {
/*    */   private final Identifier id;
/*    */   
/* 11 */   public AdvancementHolder(Identifier id, Advancement value) { this.id = id; this.value = value; } private final Advancement value; public Identifier id() { return this.id; } public Advancement value() { return this.value; }
/* 12 */    public static final StreamCodec<RegistryFriendlyByteBuf, AdvancementHolder> STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, AdvancementHolder::id, Advancement.STREAM_CODEC, AdvancementHolder::value, AdvancementHolder::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final StreamCodec<RegistryFriendlyByteBuf, java.util.List<AdvancementHolder>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 22 */     if (this == obj) {
/* 23 */       return true;
/*    */     }
/* 25 */     if (obj instanceof AdvancementHolder) { AdvancementHolder holder = (AdvancementHolder)obj; if (this.id.equals(holder.id)); }  return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 30 */     return this.id.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 35 */     return this.id.toString();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/AdvancementHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*    */ 
/*    */ public class Stat<T>
/*    */   extends ObjectiveCriteria
/*    */ {
/* 15 */   public static final StreamCodec<RegistryFriendlyByteBuf, Stat<?>> STREAM_CODEC = ByteBufCodecs.registry(Registries.STAT_TYPE).dispatch(Stat::getType, StatType::streamCodec);
/*    */   
/*    */   private final StatFormatter formatter;
/*    */   private final T value;
/*    */   private final StatType<T> type;
/*    */   
/*    */   protected Stat(StatType<T> type, T value, StatFormatter formatter) {
/* 22 */     super(buildName(type, value));
/* 23 */     this.type = type;
/* 24 */     this.formatter = formatter;
/* 25 */     this.value = value;
/*    */   }
/*    */   
/*    */   public static <T> String buildName(StatType<T> type, T value) {
/* 29 */     return locationToKey(BuiltInRegistries.STAT_TYPE.getKey(type)) + ":" + locationToKey(BuiltInRegistries.STAT_TYPE.getKey(type));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static String locationToKey(Identifier location) {
/* 35 */     return location.toString().replace(':', '.');
/*    */   }
/*    */   
/*    */   public StatType<T> getType() {
/* 39 */     return this.type;
/*    */   }
/*    */   
/*    */   public T getValue() {
/* 43 */     return this.value;
/*    */   }
/*    */   
/*    */   public String format(int value) {
/* 47 */     return this.formatter.format(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 52 */     return (this == o || (o instanceof Stat && Objects.equals(getName(), ((Stat)o).getName())));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 57 */     return getName().hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 63 */     return "Stat{name=" + getName() + ", formatter=" + String.valueOf(this.formatter) + "}";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/stats/Stat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
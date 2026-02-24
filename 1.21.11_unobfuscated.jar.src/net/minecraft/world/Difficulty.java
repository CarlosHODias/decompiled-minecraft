/*    */ package net.minecraft.world;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Difficulty
/*    */   implements StringRepresentable
/*    */ {
/* 14 */   PEACEFUL(0, "peaceful"),
/* 15 */   EASY(1, "easy"),
/* 16 */   NORMAL(2, "normal"),
/* 17 */   HARD(3, "hard");
/*    */ 
/*    */   
/* 20 */   public static final StringRepresentable.EnumCodec<Difficulty> CODEC = StringRepresentable.fromEnum(Difficulty::values);
/*    */   
/* 22 */   private static final IntFunction<Difficulty> BY_ID = ByIdMap.continuous(Difficulty::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/* 23 */   public static final StreamCodec<ByteBuf, Difficulty> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Difficulty::getId);
/*    */   
/*    */   private final int id;
/*    */   private final String key;
/*    */   
/*    */   Difficulty(int id, String key) {
/* 29 */     this.id = id;
/* 30 */     this.key = key;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 34 */     return this.id;
/*    */   }
/*    */   
/*    */   public Component getDisplayName() {
/* 38 */     return (Component)Component.translatable("options.difficulty." + this.key);
/*    */   }
/*    */   
/*    */   public Component getInfo() {
/* 42 */     return (Component)Component.translatable("options.difficulty." + this.key + ".info");
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public static Difficulty byId(int id) {
/* 47 */     return BY_ID.apply(id);
/*    */   }
/*    */   
/*    */   public static Difficulty byName(String name) {
/* 51 */     return (Difficulty)CODEC.byName(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 58 */     return this.key;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 63 */     return this.key;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/Difficulty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
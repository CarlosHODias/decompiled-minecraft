/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum PermissionLevel
/*    */   implements StringRepresentable {
/* 11 */   ALL("all", 0),
/* 12 */   MODERATORS("moderators", 1),
/* 13 */   GAMEMASTERS("gamemasters", 2),
/* 14 */   ADMINS("admins", 3),
/* 15 */   OWNERS("owners", 4);
/*    */   private static final IntFunction<PermissionLevel> BY_ID;
/*    */   public static final Codec<PermissionLevel> INT_CODEC;
/* 18 */   public static final Codec<PermissionLevel> CODEC = (Codec<PermissionLevel>)StringRepresentable.fromEnum(PermissionLevel::values);
/*    */   static {
/* 20 */     BY_ID = ByIdMap.continuous(level -> level.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/* 21 */     Objects.requireNonNull(BY_ID); INT_CODEC = Codec.INT.xmap(BY_ID::apply, level -> level.id);
/*    */   }
/*    */   private final String name;
/*    */   private final int id;
/*    */   
/*    */   PermissionLevel(String name, int id) {
/* 27 */     this.name = name;
/* 28 */     this.id = id;
/*    */   }
/*    */   
/*    */   public boolean isEqualOrHigherThan(PermissionLevel other) {
/* 32 */     return (this.id >= other.id);
/*    */   }
/*    */   
/*    */   public static PermissionLevel byId(int level) {
/* 36 */     return BY_ID.apply(level);
/*    */   }
/*    */   
/*    */   public int id() {
/* 40 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 45 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/permissions/PermissionLevel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
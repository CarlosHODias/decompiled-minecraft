/*    */ package net.minecraft.server.permissions;
/*    */ public interface PermissionCheck {
/*    */   public static final com.mojang.serialization.Codec<PermissionCheck> CODEC;
/*    */   
/*    */   boolean check(PermissionSet paramPermissionSet);
/*    */   
/*    */   com.mojang.serialization.MapCodec<? extends PermissionCheck> codec();
/*    */   
/*    */   static {
/* 10 */     CODEC = net.minecraft.core.registries.BuiltInRegistries.PERMISSION_CHECK_TYPE.byNameCodec().dispatch(PermissionCheck::codec, c -> c);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class AlwaysPass
/*    */     implements PermissionCheck
/*    */   {
/* 21 */     public static final AlwaysPass INSTANCE = new AlwaysPass();
/*    */     
/* 23 */     public static final com.mojang.serialization.MapCodec<AlwaysPass> MAP_CODEC = com.mojang.serialization.MapCodec.unit(INSTANCE);
/*    */ 
/*    */     
/*    */     public boolean check(PermissionSet source) {
/* 27 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public com.mojang.serialization.MapCodec<AlwaysPass> codec() {
/* 32 */       return MAP_CODEC;
/*    */     } }
/*    */   public static final class Require extends Record implements PermissionCheck { private final Permission permission; public static final com.mojang.serialization.MapCodec<Require> MAP_CODEC;
/*    */     
/* 36 */     public Require(Permission permission) { this.permission = permission; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/permissions/PermissionCheck$Require;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 36 */       //   0	7	0	this	Lnet/minecraft/server/permissions/PermissionCheck$Require; } public Permission permission() { return this.permission; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/permissions/PermissionCheck$Require;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/permissions/PermissionCheck$Require; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/permissions/PermissionCheck$Require;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/permissions/PermissionCheck$Require;
/*    */       //   0	8	1	o	Ljava/lang/Object; } static {
/* 39 */       MAP_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)Permission.CODEC.fieldOf("permission").forGetter(Require::permission)).apply((com.mojang.datafixers.kinds.Applicative)i, Require::new));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public com.mojang.serialization.MapCodec<Require> codec() {
/* 45 */       return MAP_CODEC;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean check(PermissionSet source) {
/* 50 */       return source.hasPermission(this.permission);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/permissions/PermissionCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
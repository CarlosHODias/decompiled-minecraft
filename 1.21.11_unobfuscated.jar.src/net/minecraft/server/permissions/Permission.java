/*    */ package net.minecraft.server.permissions;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public interface Permission {
/*    */   public static final com.mojang.serialization.Codec<Permission> FULL_CODEC;
/*    */   public static final com.mojang.serialization.Codec<Permission> CODEC;
/*    */   
/*    */   static {
/* 12 */     FULL_CODEC = net.minecraft.core.registries.BuiltInRegistries.PERMISSION_TYPE.byNameCodec().dispatch(Permission::codec, c -> c);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 17 */     CODEC = com.mojang.serialization.Codec.either(FULL_CODEC, Identifier.CODEC).xmap(e -> (Permission)e.map((), Atom::create), permission -> {
/*    */           Atom atom = (Atom)permission;
/*    */           return (permission instanceof Atom) ? Either.right(atom.id()) : Either.left(permission);
/*    */         });
/*    */   }
/*    */   MapCodec<? extends Permission> codec();
/*    */   public static final class Atom extends Record implements Permission { private final Identifier id; public static final MapCodec<Atom> MAP_CODEC;
/*    */     
/* 25 */     public Atom(Identifier id) { this.id = id; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/permissions/Permission$Atom;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 25 */       //   0	7	0	this	Lnet/minecraft/server/permissions/Permission$Atom; } public Identifier id() { return this.id; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/permissions/Permission$Atom;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/permissions/Permission$Atom; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/permissions/Permission$Atom;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/permissions/Permission$Atom;
/* 26 */       //   0	8	1	o	Ljava/lang/Object; } static { MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)Identifier.CODEC.fieldOf("id").forGetter(Atom::id)).apply((com.mojang.datafixers.kinds.Applicative)i, Atom::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public MapCodec<Atom> codec() {
/* 32 */       return MAP_CODEC;
/*    */     }
/*    */     
/*    */     public static Atom create(String name) {
/* 36 */       return create(Identifier.withDefaultNamespace(name));
/*    */     }
/*    */     
/*    */     public static Atom create(Identifier id) {
/* 40 */       return new Atom(id);
/*    */     } }
/*    */   public static final class HasCommandLevel extends Record implements Permission { private final PermissionLevel level; public static final MapCodec<HasCommandLevel> MAP_CODEC;
/*    */     
/* 44 */     public HasCommandLevel(PermissionLevel level) { this.level = level; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/permissions/Permission$HasCommandLevel;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/permissions/Permission$HasCommandLevel; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/permissions/Permission$HasCommandLevel;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/permissions/Permission$HasCommandLevel; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/permissions/Permission$HasCommandLevel;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/permissions/Permission$HasCommandLevel;
/* 44 */       //   0	8	1	o	Ljava/lang/Object; } public PermissionLevel level() { return this.level; } static {
/* 45 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)PermissionLevel.CODEC.fieldOf("level").forGetter(HasCommandLevel::level)).apply((com.mojang.datafixers.kinds.Applicative)i, HasCommandLevel::new));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public MapCodec<HasCommandLevel> codec() {
/* 51 */       return MAP_CODEC;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/permissions/Permission.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum MipmapStrategy implements StringRepresentable {
/*  7 */   AUTO("auto"),
/*  8 */   MEAN("mean"),
/*  9 */   CUTOUT("cutout"),
/* 10 */   STRICT_CUTOUT("strict_cutout"),
/* 11 */   DARK_CUTOUT("dark_cutout");
/*    */   
/* 13 */   public static final Codec<MipmapStrategy> CODEC = StringRepresentable.fromValues(MipmapStrategy::values);
/*    */   
/*    */   private final String name;
/*    */   
/*    */   MipmapStrategy(String name) {
/* 18 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 23 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/MipmapStrategy.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
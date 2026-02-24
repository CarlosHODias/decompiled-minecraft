/*    */ package net.minecraft.client.gui.font.providers;
/*    */ 
/*    */ import com.mojang.blaze3d.font.SpaceProvider;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum GlyphProviderType implements StringRepresentable {
/*  9 */   BITMAP("bitmap", (MapCodec)BitmapProvider.Definition.CODEC),
/* 10 */   TTF("ttf", (MapCodec)TrueTypeGlyphProviderDefinition.CODEC),
/* 11 */   SPACE("space", SpaceProvider.Definition.CODEC),
/* 12 */   UNIHEX("unihex", (MapCodec)UnihexProvider.Definition.CODEC),
/* 13 */   REFERENCE("reference", (MapCodec)ProviderReferenceDefinition.CODEC);
/*    */ 
/*    */   
/* 16 */   public static final Codec<GlyphProviderType> CODEC = (Codec<GlyphProviderType>)StringRepresentable.fromEnum(GlyphProviderType::values);
/*    */   
/*    */   private final String name;
/*    */   private final MapCodec<? extends GlyphProviderDefinition> codec;
/*    */   
/*    */   GlyphProviderType(String name, MapCodec<? extends GlyphProviderDefinition> codec) {
/* 22 */     this.name = name;
/* 23 */     this.codec = codec;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 28 */     return this.name;
/*    */   }
/*    */   
/*    */   public MapCodec<? extends GlyphProviderDefinition> mapCodec() {
/* 32 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/providers/GlyphProviderType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
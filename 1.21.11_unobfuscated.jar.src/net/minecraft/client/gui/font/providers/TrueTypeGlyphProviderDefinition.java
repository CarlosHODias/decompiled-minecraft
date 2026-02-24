/*    */ package net.minecraft.client.gui.font.providers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.io.InputStream;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import org.lwjgl.PointerBuffer;
/*    */ import org.lwjgl.system.MemoryStack;
/*    */ import org.lwjgl.util.freetype.FT_Face;
/*    */ import org.lwjgl.util.freetype.FreeType;
/*    */ 
/*    */ public final class TrueTypeGlyphProviderDefinition extends Record implements GlyphProviderDefinition {
/*    */   private final Identifier location;
/*    */   private final float size;
/*    */   private final float oversample;
/*    */   private final Shift shift;
/*    */   private final String skip;
/*    */   private static final Codec<String> SKIP_LIST_CODEC;
/*    */   public static final com.mojang.serialization.MapCodec<TrueTypeGlyphProviderDefinition> CODEC;
/*    */   
/* 24 */   public TrueTypeGlyphProviderDefinition(Identifier location, float size, float oversample, Shift shift, String skip) { this.location = location; this.size = size; this.oversample = oversample; this.shift = shift; this.skip = skip; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 24 */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition; } public Identifier location() { return this.location; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition;
/* 24 */     //   0	8	1	o	Ljava/lang/Object; } public float size() { return this.size; } public float oversample() { return this.oversample; } public Shift shift() { return this.shift; } public String skip() { return this.skip; }
/*    */ 
/*    */   
/*    */   public static final class Shift extends Record {
/*    */     private final float x;
/*    */     private final float y;
/*    */     
/* 31 */     public Shift(float x, float y) { this.x = x; this.y = y; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition$Shift;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition$Shift; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition$Shift;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition$Shift; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition$Shift;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition$Shift;
/* 31 */       //   0	8	1	o	Ljava/lang/Object; } public float x() { return this.x; } public float y() { return this.y; }
/* 32 */      public static final Shift NONE = new Shift(0.0F, 0.0F); public static final Codec<Shift> CODEC;
/*    */     static {
/* 34 */       CODEC = Codec.floatRange(-512.0F, 512.0F).listOf().comapFlatMap(input -> net.minecraft.util.Util.fixedSize(input, 2).map(()), shift -> List.of(shift.x, shift.y));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 41 */     SKIP_LIST_CODEC = Codec.withAlternative((Codec)Codec.STRING, 
/*    */         
/* 43 */         Codec.STRING.listOf(), list -> String.join("", list));
/*    */ 
/*    */ 
/*    */     
/* 47 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("file").forGetter(TrueTypeGlyphProviderDefinition::location), (App)Codec.FLOAT.optionalFieldOf("size", 11.0F).forGetter(TrueTypeGlyphProviderDefinition::size), (App)Codec.FLOAT.optionalFieldOf("oversample", 1.0F).forGetter(TrueTypeGlyphProviderDefinition::oversample), (App)Shift.CODEC.optionalFieldOf("shift", Shift.NONE).forGetter(TrueTypeGlyphProviderDefinition::shift), (App)SKIP_LIST_CODEC.optionalFieldOf("skip", "").forGetter(TrueTypeGlyphProviderDefinition::skip)).apply((com.mojang.datafixers.kinds.Applicative)i, TrueTypeGlyphProviderDefinition::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GlyphProviderType type() {
/* 57 */     return GlyphProviderType.TTF;
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.datafixers.util.Either<GlyphProviderDefinition.Loader, GlyphProviderDefinition.Reference> unpack() {
/* 62 */     return com.mojang.datafixers.util.Either.left(this::load);
/*    */   }
/*    */   
/*    */   private com.mojang.blaze3d.font.GlyphProvider load(net.minecraft.server.packs.resources.ResourceManager resourceManager) throws java.io.IOException {
/* 66 */     FT_Face face = null;
/* 67 */     ByteBuffer fontData = null; 
/* 68 */     try { InputStream resource = resourceManager.open(this.location.withPrefix("font/")); 
/* 69 */       try { fontData = com.mojang.blaze3d.platform.TextureUtil.readResource(resource);
/*    */         
/* 71 */         synchronized (FreeTypeUtil.LIBRARY_LOCK)
/* 72 */         { MemoryStack stack = MemoryStack.stackPush(); 
/* 73 */           try { PointerBuffer faceBuffer = stack.mallocPointer(1);
/* 74 */             FreeTypeUtil.assertError(FreeType.FT_New_Memory_Face(FreeTypeUtil.getLibrary(), fontData, 0L, faceBuffer), "Initializing font face");
/* 75 */             face = FT_Face.create(faceBuffer.get());
/* 76 */             if (stack != null) stack.close();  } catch (Throwable throwable) { if (stack != null)
/*    */               try { stack.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 78 */            String format = FreeType.FT_Get_Font_Format(face);
/* 79 */           if (!"TrueType".equals(format)) {
/* 80 */             throw new java.io.IOException("Font is not in TTF format, was " + format);
/*    */           }
/*    */           
/* 83 */           FreeTypeUtil.assertError(FreeType.FT_Select_Charmap(face, FreeType.FT_ENCODING_UNICODE), "Find unicode charmap");
/*    */           
/* 85 */           com.mojang.blaze3d.font.TrueTypeGlyphProvider trueTypeGlyphProvider = new com.mojang.blaze3d.font.TrueTypeGlyphProvider(fontData, face, this.size, this.oversample, this.shift.x, this.shift.y, this.skip);
/*    */           
/* 87 */           if (resource != null) resource.close();  return (com.mojang.blaze3d.font.GlyphProvider)trueTypeGlyphProvider; }  } catch (Throwable throwable) { if (resource != null) try { resource.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception ex)
/* 88 */     { synchronized (FreeTypeUtil.LIBRARY_LOCK) {
/* 89 */         if (face != null) {
/* 90 */           FreeType.FT_Done_Face(face);
/*    */         }
/*    */       } 
/* 93 */       org.lwjgl.system.MemoryUtil.memFree(fontData);
/* 94 */       throw ex; }
/*    */   
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
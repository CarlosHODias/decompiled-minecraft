/*    */ package net.minecraft.client.gui.font.providers;
/*    */ import com.mojang.blaze3d.font.GlyphProvider;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.io.IOException;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.client.gui.font.FontOption;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public interface GlyphProviderDefinition {
/* 15 */   public static final MapCodec<GlyphProviderDefinition> MAP_CODEC = GlyphProviderType.CODEC.dispatchMap(GlyphProviderDefinition::type, GlyphProviderType::mapCodec); GlyphProviderType type(); Either<Loader, Reference> unpack(); public static interface Loader {
/*    */     GlyphProvider load(net.minecraft.server.packs.resources.ResourceManager param1ResourceManager) throws IOException;
/*    */   }
/*    */   public static final class Reference extends Record { private final Identifier id;
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/GlyphProviderDefinition$Reference;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/GlyphProviderDefinition$Reference;
/*    */     }
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/GlyphProviderDefinition$Reference;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/GlyphProviderDefinition$Reference;
/*    */     }
/* 25 */     public Reference(Identifier id) { this.id = id; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/GlyphProviderDefinition$Reference;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/GlyphProviderDefinition$Reference;
/* 25 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier id() { return this.id; }
/*    */      }
/*    */   public static final class Conditional extends Record { private final GlyphProviderDefinition definition; private final FontOption.Filter filter; public static final Codec<Conditional> CODEC;
/* 28 */     public Conditional(GlyphProviderDefinition definition, FontOption.Filter filter) { this.definition = definition; this.filter = filter; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/GlyphProviderDefinition$Conditional;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/GlyphProviderDefinition$Conditional; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/GlyphProviderDefinition$Conditional;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/GlyphProviderDefinition$Conditional; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/GlyphProviderDefinition$Conditional;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/GlyphProviderDefinition$Conditional;
/* 28 */       //   0	8	1	o	Ljava/lang/Object; } public GlyphProviderDefinition definition() { return this.definition; } public FontOption.Filter filter() { return this.filter; }
/*    */ 
/*    */     
/*    */     static {
/* 32 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)GlyphProviderDefinition.MAP_CODEC.forGetter(Conditional::definition), (App)FontOption.Filter.CODEC.optionalFieldOf("filter", FontOption.Filter.ALWAYS_PASS).forGetter(Conditional::filter)).apply((Applicative)i, Conditional::new));
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/providers/GlyphProviderDefinition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
/*     */ package net.minecraft.client.renderer.block.model;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.resources.model.Material;
/*     */ import net.minecraft.client.resources.model.ModelDebugName;
/*     */ import net.minecraft.client.resources.model.ModelManager;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class TextureSlots
/*     */ {
/*  26 */   public static final TextureSlots EMPTY = new TextureSlots(Map.of());
/*     */   
/*     */   private static final char REFERENCE_CHAR = '#';
/*     */   private final Map<String, Material> resolvedValues;
/*     */   
/*     */   private TextureSlots(Map<String, Material> resolvedValues) {
/*  32 */     this.resolvedValues = resolvedValues;
/*     */   }
/*     */ 
/*     */   
/*     */   public Material getMaterial(String reference) {
/*  37 */     if (isTextureReference(reference)) {
/*  38 */       reference = reference.substring(1);
/*     */     }
/*     */     
/*  41 */     return this.resolvedValues.get(reference);
/*     */   }
/*     */   
/*     */   private static boolean isTextureReference(String texture) {
/*  45 */     return (texture.charAt(0) == '#');
/*     */   }
/*     */   
/*     */   public static Data parseTextureMap(JsonObject texturesObject) {
/*  49 */     Data.Builder builder = new Data.Builder();
/*  50 */     for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)texturesObject.entrySet()) {
/*  51 */       parseEntry(entry.getKey(), ((JsonElement)entry.getValue()).getAsString(), builder);
/*     */     }
/*  53 */     return builder.build();
/*     */   }
/*     */   
/*     */   private static void parseEntry(String slot, String value, Data.Builder output) {
/*  57 */     if (isTextureReference(value)) {
/*  58 */       output.addReference(slot, value.substring(1));
/*     */     } else {
/*  60 */       Identifier location = Identifier.tryParse(value);
/*  61 */       if (location == null) {
/*  62 */         throw new JsonParseException(value + " is not valid resource location");
/*     */       }
/*  64 */       output.addTexture(slot, new Material(ModelManager.BLOCK_OR_ITEM, location));
/*     */     } 
/*     */   } private static final class Value extends Record implements SlotContents {
/*     */     private final Material material; public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/TextureSlots$Value;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/TextureSlots$Value;
/*     */     } public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/TextureSlots$Value;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/TextureSlots$Value;
/*  71 */     } private Value(Material material) { this.material = material; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/TextureSlots$Value;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/TextureSlots$Value;
/*  71 */       //   0	8	1	o	Ljava/lang/Object; } public Material material() { return this.material; }
/*     */      }
/*     */   private static final class Reference extends Record implements SlotContents { private final String target;
/*  74 */     private Reference(String target) { this.target = target; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/TextureSlots$Reference;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #74	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/TextureSlots$Reference; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/TextureSlots$Reference;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #74	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/TextureSlots$Reference; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/TextureSlots$Reference;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #74	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/TextureSlots$Reference;
/*  74 */       //   0	8	1	o	Ljava/lang/Object; } public String target() { return this.target; }
/*     */      }
/*     */   public static final class Data extends Record { private final Map<String, TextureSlots.SlotContents> values;
/*  77 */     public Data(Map<String, TextureSlots.SlotContents> values) { this.values = values; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/TextureSlots$Data;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #77	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/TextureSlots$Data; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/TextureSlots$Data;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #77	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/TextureSlots$Data; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/TextureSlots$Data;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #77	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/TextureSlots$Data;
/*  77 */       //   0	8	1	o	Ljava/lang/Object; } public Map<String, TextureSlots.SlotContents> values() { return this.values; }
/*  78 */      public static final Data EMPTY = new Data(Map.of());
/*     */     
/*     */     public static class Builder {
/*  81 */       private final Map<String, TextureSlots.SlotContents> textureMap = new HashMap<>();
/*     */       
/*     */       public Builder addReference(String slot, String reference) {
/*  84 */         this.textureMap.put(slot, new TextureSlots.Reference(reference));
/*  85 */         return this;
/*     */       }
/*     */       
/*     */       public Builder addTexture(String slot, Material material) {
/*  89 */         this.textureMap.put(slot, new TextureSlots.Value(material));
/*  90 */         return this;
/*     */       }
/*     */       
/*     */       public TextureSlots.Data build() {
/*  94 */         if (this.textureMap.isEmpty()) {
/*  95 */           return TextureSlots.Data.EMPTY;
/*     */         }
/*     */         
/*  98 */         return new TextureSlots.Data(Map.copyOf(this.textureMap)); } } } public static class Builder { private final Map<String, TextureSlots.SlotContents> textureMap; public Builder() { this.textureMap = new HashMap<>(); } public TextureSlots.Data build() { if (this.textureMap.isEmpty()) return TextureSlots.Data.EMPTY;  return new TextureSlots.Data(Map.copyOf(this.textureMap)); }
/*     */      public Builder addReference(String slot, String reference) {
/*     */       this.textureMap.put(slot, new TextureSlots.Reference(reference));
/*     */       return this;
/*     */     } public Builder addTexture(String slot, Material material) {
/*     */       this.textureMap.put(slot, new TextureSlots.Value(material));
/*     */       return this;
/*     */     } }
/*     */   public static class Resolver { public Resolver() {
/* 107 */       this.entries = new ArrayList<>();
/*     */     } private static final Logger LOGGER = LogUtils.getLogger(); private final List<TextureSlots.Data> entries;
/*     */     public Resolver addLast(TextureSlots.Data data) {
/* 110 */       this.entries.addLast(data);
/* 111 */       return this;
/*     */     }
/*     */     
/*     */     public Resolver addFirst(TextureSlots.Data data) {
/* 115 */       this.entries.addFirst(data);
/* 116 */       return this;
/*     */     }
/*     */     
/*     */     public TextureSlots resolve(ModelDebugName debugNameProvider) {
/* 120 */       if (this.entries.isEmpty()) {
/* 121 */         return TextureSlots.EMPTY;
/*     */       }
/*     */       
/* 124 */       Object2ObjectArrayMap object2ObjectArrayMap1 = new Object2ObjectArrayMap();
/* 125 */       Object2ObjectArrayMap object2ObjectArrayMap2 = new Object2ObjectArrayMap();
/*     */       
/* 127 */       for (TextureSlots.Data data : (Iterable<TextureSlots.Data>)Lists.reverse(this.entries)) {
/* 128 */         data.values.forEach((slot, contents) -> {
/*     */               // Byte code:
/*     */               //   0: aload_3
/*     */               //   1: dup
/*     */               //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */               //   5: pop
/*     */               //   6: astore #4
/*     */               //   8: iconst_0
/*     */               //   9: istore #5
/*     */               //   11: aload #4
/*     */               //   13: iload #5
/*     */               //   15: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */               //   20: lookupswitch default -> 48, 0 -> 58, 1 -> 89
/*     */               //   48: new java/lang/MatchException
/*     */               //   51: dup
/*     */               //   52: aconst_null
/*     */               //   53: aconst_null
/*     */               //   54: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */               //   57: athrow
/*     */               //   58: aload #4
/*     */               //   60: checkcast net/minecraft/client/renderer/block/model/TextureSlots$Value
/*     */               //   63: astore #6
/*     */               //   65: aload_0
/*     */               //   66: aload_2
/*     */               //   67: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */               //   72: pop
/*     */               //   73: aload_1
/*     */               //   74: aload_2
/*     */               //   75: aload #6
/*     */               //   77: invokevirtual material : ()Lnet/minecraft/client/resources/model/Material;
/*     */               //   80: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */               //   85: pop
/*     */               //   86: goto -> 114
/*     */               //   89: aload #4
/*     */               //   91: checkcast net/minecraft/client/renderer/block/model/TextureSlots$Reference
/*     */               //   94: astore #7
/*     */               //   96: aload_1
/*     */               //   97: aload_2
/*     */               //   98: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */               //   103: pop
/*     */               //   104: aload_0
/*     */               //   105: aload_2
/*     */               //   106: aload #7
/*     */               //   108: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */               //   113: pop
/*     */               //   114: return
/*     */               // Line number table:
/*     */               //   Java source line number -> byte code offset
/*     */               //   #129	-> 0
/*     */               //   #130	-> 58
/*     */               //   #131	-> 65
/*     */               //   #132	-> 73
/*     */               //   #133	-> 86
/*     */               //   #134	-> 89
/*     */               //   #135	-> 96
/*     */               //   #136	-> 104
/*     */               //   #139	-> 114
/*     */               // Local variable table:
/*     */               //   start	length	slot	name	descriptor
/*     */               //   65	24	6	value	Lnet/minecraft/client/renderer/block/model/TextureSlots$Value;
/*     */               //   96	18	7	reference	Lnet/minecraft/client/renderer/block/model/TextureSlots$Reference;
/*     */               //   8	106	4	selector0$temp	Lnet/minecraft/client/renderer/block/model/TextureSlots$SlotContents;
/*     */               //   11	103	5	index$1	I
/*     */               //   0	115	0	unresolved	Lit/unimi/dsi/fastutil/objects/Object2ObjectMap;
/*     */               //   0	115	1	resolved	Lit/unimi/dsi/fastutil/objects/Object2ObjectMap;
/*     */               //   0	115	2	slot	Ljava/lang/String;
/*     */               //   0	115	3	contents	Lnet/minecraft/client/renderer/block/model/TextureSlots$SlotContents;
/*     */             });
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 142 */       if (object2ObjectArrayMap2.isEmpty()) {
/* 143 */         return new TextureSlots((Map<String, Material>)object2ObjectArrayMap1);
/*     */       }
/*     */       
/*     */       boolean hasChanges = true;
/*     */       
/* 148 */       while (hasChanges) {
/* 149 */         hasChanges = false;
/* 150 */         ObjectIterator<Object2ObjectMap.Entry<String, TextureSlots.Reference>> iterator = Object2ObjectMaps.fastIterator((Object2ObjectMap)object2ObjectArrayMap2);
/* 151 */         while (iterator.hasNext()) {
/* 152 */           Object2ObjectMap.Entry<String, TextureSlots.Reference> entry = (Object2ObjectMap.Entry<String, TextureSlots.Reference>)iterator.next();
/* 153 */           Material maybeResolved = (Material)object2ObjectArrayMap1.get(((TextureSlots.Reference)entry.getValue()).target);
/* 154 */           if (maybeResolved != null) {
/* 155 */             object2ObjectArrayMap1.put(entry.getKey(), maybeResolved);
/* 156 */             iterator.remove();
/* 157 */             hasChanges = true;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 162 */       if (!object2ObjectArrayMap2.isEmpty())
/*     */       {
/*     */         
/* 165 */         LOGGER.warn("Unresolved texture references in {}:\n{}", debugNameProvider.debugName(), object2ObjectArrayMap2.entrySet().stream().map(e -> "\t#" + (String)e.getKey() + "-> #" + ((TextureSlots.Reference)e.getValue()).target + "\n").collect(Collectors.joining()));
/*     */       }
/*     */       
/* 168 */       return new TextureSlots((Map<String, Material>)object2ObjectArrayMap1);
/*     */     } }
/*     */ 
/*     */   
/*     */   public static interface SlotContents {}
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/TextureSlots.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
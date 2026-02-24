/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.Streams;
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public class ParticleDescription {
/*    */   private final List<Identifier> textures;
/*    */   
/*    */   private ParticleDescription(List<Identifier> textures) {
/* 16 */     this.textures = textures;
/*    */   }
/*    */   
/*    */   public List<Identifier> getTextures() {
/* 20 */     return this.textures;
/*    */   }
/*    */   
/*    */   public static ParticleDescription fromJson(JsonObject data) {
/* 24 */     JsonArray texturesData = GsonHelper.getAsJsonArray(data, "textures", null);
/* 25 */     if (texturesData == null) {
/* 26 */       return new ParticleDescription(List.of());
/*    */     }
/*    */     
/* 29 */     List<Identifier> textures = (List<Identifier>)Streams.stream((Iterable)texturesData)
/* 30 */       .map(element -> GsonHelper.convertToString(element, "texture"))
/* 31 */       .map(Identifier::parse)
/* 32 */       .collect(ImmutableList.toImmutableList());
/*    */     
/* 34 */     return new ParticleDescription(textures);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/ParticleDescription.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
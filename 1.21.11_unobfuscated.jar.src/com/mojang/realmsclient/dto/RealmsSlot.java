/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.annotations.JsonAdapter;
/*    */ import com.google.gson.annotations.SerializedName;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public final class RealmsSlot
/*    */   implements ReflectionBasedSerialization
/*    */ {
/*    */   @SerializedName("slotId")
/*    */   public int slotId;
/*    */   @SerializedName("options")
/*    */   @JsonAdapter(RealmsWorldOptionsJsonAdapter.class)
/*    */   public RealmsWorldOptions options;
/*    */   @SerializedName("settings")
/*    */   public List<RealmsSetting> settings;
/*    */   
/*    */   public RealmsSlot(int slotId, RealmsWorldOptions options, List<RealmsSetting> settings) {
/* 25 */     this.slotId = slotId;
/* 26 */     this.options = options;
/* 27 */     this.settings = settings;
/*    */   }
/*    */   
/*    */   public static RealmsSlot defaults(int slotId) {
/* 31 */     return new RealmsSlot(slotId, RealmsWorldOptions.createEmptyDefaults(), List.of(RealmsSetting.hardcoreSetting(false)));
/*    */   }
/*    */   
/*    */   public RealmsSlot copy() {
/* 35 */     return new RealmsSlot(this.slotId, this.options.copy(), new ArrayList<>(this.settings));
/*    */   }
/*    */   
/*    */   public boolean isHardcore() {
/* 39 */     return RealmsSetting.isHardcore(this.settings);
/*    */   }
/*    */   
/*    */   private static class RealmsWorldOptionsJsonAdapter
/*    */     extends TypeAdapter<RealmsWorldOptions>
/*    */   {
/*    */     public void write(JsonWriter jsonWriter, RealmsWorldOptions realmsSlotOptions) throws IOException {
/* 46 */       jsonWriter.jsonValue(new GuardedSerializer().toJson(realmsSlotOptions));
/*    */     }
/*    */ 
/*    */     
/*    */     public RealmsWorldOptions read(JsonReader jsonReader) throws IOException {
/* 51 */       String json = jsonReader.nextString();
/* 52 */       return RealmsWorldOptions.parse(new GuardedSerializer(), json);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RealmsSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
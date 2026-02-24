/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.annotations.JsonAdapter;
/*    */ import com.google.gson.annotations.SerializedName;
/*    */ 
/*    */ public class RegionSelectionPreferenceDto
/*    */   implements ReflectionBasedSerialization {
/*  8 */   public static final RegionSelectionPreferenceDto DEFAULT = new RegionSelectionPreferenceDto(RegionSelectionPreference.AUTOMATIC_OWNER, null);
/*    */   
/*    */   @SerializedName("regionSelectionPreference")
/*    */   @JsonAdapter(RegionSelectionPreference.RegionSelectionPreferenceJsonAdapter.class)
/*    */   public final RegionSelectionPreference regionSelectionPreference;
/*    */   
/*    */   @SerializedName("preferredRegion")
/*    */   @JsonAdapter(RealmsRegion.RealmsRegionJsonAdapter.class)
/*    */   public RealmsRegion preferredRegion;
/*    */   
/*    */   public RegionSelectionPreferenceDto(RegionSelectionPreference regionSelectionPreference, RealmsRegion preferredRegion) {
/* 19 */     this.regionSelectionPreference = regionSelectionPreference;
/* 20 */     this.preferredRegion = preferredRegion;
/*    */   }
/*    */   
/*    */   public RegionSelectionPreferenceDto copy() {
/* 24 */     return new RegionSelectionPreferenceDto(this.regionSelectionPreference, this.preferredRegion);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RegionSelectionPreferenceDto.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
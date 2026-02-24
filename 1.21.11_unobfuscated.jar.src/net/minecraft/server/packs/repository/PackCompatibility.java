/*    */ package net.minecraft.server.packs.repository;
/*    */ 
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.packs.metadata.pack.PackFormat;
/*    */ import net.minecraft.util.InclusiveRange;
/*    */ 
/*    */ public enum PackCompatibility {
/*  9 */   TOO_OLD("old"),
/* 10 */   TOO_NEW("new"),
/* 11 */   UNKNOWN("unknown"),
/* 12 */   COMPATIBLE("compatible");
/*    */   
/*    */   public static final int UNKNOWN_VERSION = 2147483647;
/*    */   
/*    */   private final Component description;
/*    */   
/*    */   private final Component confirmation;
/*    */   
/*    */   PackCompatibility(String key) {
/* 21 */     this.description = (Component)Component.translatable("pack.incompatible." + key).withStyle(ChatFormatting.GRAY);
/* 22 */     this.confirmation = (Component)Component.translatable("pack.incompatible.confirm." + key);
/*    */   }
/*    */   
/*    */   public boolean isCompatible() {
/* 26 */     return (this == COMPATIBLE);
/*    */   }
/*    */   
/*    */   public static PackCompatibility forVersion(InclusiveRange<PackFormat> packDeclaredVersions, PackFormat gameSupportedVersion) {
/* 30 */     if (((PackFormat)packDeclaredVersions.minInclusive()).major() == Integer.MAX_VALUE) {
/* 31 */       return UNKNOWN;
/*    */     }
/* 33 */     if (((PackFormat)packDeclaredVersions.maxInclusive()).compareTo(gameSupportedVersion) < 0) {
/* 34 */       return TOO_OLD;
/*    */     }
/* 36 */     if (gameSupportedVersion.compareTo((PackFormat)packDeclaredVersions.minInclusive()) < 0) {
/* 37 */       return TOO_NEW;
/*    */     }
/* 39 */     return COMPATIBLE;
/*    */   }
/*    */   
/*    */   public Component getDescription() {
/* 43 */     return this.description;
/*    */   }
/*    */   
/*    */   public Component getConfirmation() {
/* 47 */     return this.confirmation;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/repository/PackCompatibility.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
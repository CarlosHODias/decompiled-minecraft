/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.PlayerModelType;
/*    */ import net.minecraft.world.entity.player.PlayerSkin;
/*    */ 
/*    */ public class DefaultPlayerSkin
/*    */ {
/* 12 */   private static final PlayerSkin[] DEFAULT_SKINS = new PlayerSkin[] { 
/* 13 */       create("entity/player/slim/alex", PlayerModelType.SLIM), 
/* 14 */       create("entity/player/slim/ari", PlayerModelType.SLIM), 
/* 15 */       create("entity/player/slim/efe", PlayerModelType.SLIM), 
/* 16 */       create("entity/player/slim/kai", PlayerModelType.SLIM), 
/* 17 */       create("entity/player/slim/makena", PlayerModelType.SLIM), 
/* 18 */       create("entity/player/slim/noor", PlayerModelType.SLIM), 
/* 19 */       create("entity/player/slim/steve", PlayerModelType.SLIM), 
/* 20 */       create("entity/player/slim/sunny", PlayerModelType.SLIM), 
/* 21 */       create("entity/player/slim/zuri", PlayerModelType.SLIM), 
/*    */       
/* 23 */       create("entity/player/wide/alex", PlayerModelType.WIDE), 
/* 24 */       create("entity/player/wide/ari", PlayerModelType.WIDE), 
/* 25 */       create("entity/player/wide/efe", PlayerModelType.WIDE), 
/* 26 */       create("entity/player/wide/kai", PlayerModelType.WIDE), 
/* 27 */       create("entity/player/wide/makena", PlayerModelType.WIDE), 
/* 28 */       create("entity/player/wide/noor", PlayerModelType.WIDE), 
/* 29 */       create("entity/player/wide/steve", PlayerModelType.WIDE), 
/* 30 */       create("entity/player/wide/sunny", PlayerModelType.WIDE), 
/* 31 */       create("entity/player/wide/zuri", PlayerModelType.WIDE) };
/*    */ 
/*    */   
/*    */   public static Identifier getDefaultTexture() {
/* 35 */     return getDefaultSkin().body().texturePath();
/*    */   }
/*    */ 
/*    */   
/*    */   public static PlayerSkin getDefaultSkin() {
/* 40 */     return DEFAULT_SKINS[6];
/*    */   }
/*    */   
/*    */   public static PlayerSkin get(UUID profileId) {
/* 44 */     return DEFAULT_SKINS[Math.floorMod(profileId.hashCode(), DEFAULT_SKINS.length)];
/*    */   }
/*    */   
/*    */   public static PlayerSkin get(GameProfile profile) {
/* 48 */     return get(profile.id());
/*    */   }
/*    */   
/*    */   private static PlayerSkin create(String body, PlayerModelType model) {
/* 52 */     return new PlayerSkin((ClientAsset.Texture)new ClientAsset.ResourceTexture(Identifier.withDefaultNamespace(body)), null, null, model, true);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/DefaultPlayerSkin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
/*     */ package com.mojang.realmsclient.dto;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.util.JsonUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.PopupScreen;
/*     */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.LenientJsonParser;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class RealmsNotification
/*     */ {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String NOTIFICATION_UUID = "notificationUuid";
/*     */   
/*     */   private static final String DISMISSABLE = "dismissable";
/*     */   private static final String SEEN = "seen";
/*     */   private static final String TYPE = "type";
/*     */   private static final String VISIT_URL = "visitUrl";
/*     */   private static final String INFO_POPUP = "infoPopup";
/*  36 */   private static final Component BUTTON_TEXT_FALLBACK = (Component)Component.translatable("mco.notification.visitUrl.buttonText.default");
/*     */   
/*     */   private final UUID uuid;
/*     */   private final boolean dismissable;
/*     */   private final boolean seen;
/*     */   private final String type;
/*     */   
/*     */   private RealmsNotification(UUID uuid, boolean dismissable, boolean seen, String type) {
/*  44 */     this.uuid = uuid;
/*  45 */     this.dismissable = dismissable;
/*  46 */     this.seen = seen;
/*  47 */     this.type = type;
/*     */   }
/*     */   
/*     */   public boolean seen() {
/*  51 */     return this.seen;
/*     */   }
/*     */   
/*     */   public boolean dismissable() {
/*  55 */     return this.dismissable;
/*     */   }
/*     */   
/*     */   public UUID uuid() {
/*  59 */     return this.uuid;
/*     */   }
/*     */   
/*     */   public static List<RealmsNotification> parseList(String json) {
/*  63 */     List<RealmsNotification> result = new ArrayList<>();
/*     */     try {
/*  65 */       JsonArray array = LenientJsonParser.parse(json).getAsJsonObject().get("notifications").getAsJsonArray();
/*  66 */       for (JsonElement element : (Iterable<JsonElement>)array) {
/*  67 */         result.add(parse(element.getAsJsonObject()));
/*     */       }
/*  69 */     } catch (Exception e) {
/*  70 */       LOGGER.error("Could not parse list of RealmsNotifications", e);
/*     */     } 
/*  72 */     return result;
/*     */   }
/*     */   
/*     */   private static RealmsNotification parse(JsonObject jsonObject) {
/*  76 */     UUID uuid = JsonUtils.getUuidOr("notificationUuid", jsonObject, null);
/*  77 */     if (uuid == null) {
/*  78 */       throw new IllegalStateException("Missing required property notificationUuid");
/*     */     }
/*  80 */     boolean dismissable = JsonUtils.getBooleanOr("dismissable", jsonObject, true);
/*  81 */     boolean seen = JsonUtils.getBooleanOr("seen", jsonObject, false);
/*  82 */     String type = JsonUtils.getRequiredString("type", jsonObject);
/*     */     
/*  84 */     RealmsNotification base = new RealmsNotification(uuid, dismissable, seen, type);
/*  85 */     switch (type) { case "visitUrl": case "infoPopup": default: break; }  return 
/*     */ 
/*     */       
/*  88 */       base;
/*     */   }
/*     */   
/*     */   public static class VisitUrl
/*     */     extends RealmsNotification
/*     */   {
/*     */     private static final String URL = "url";
/*     */     private static final String BUTTON_TEXT = "buttonText";
/*     */     private static final String MESSAGE = "message";
/*     */     private final String url;
/*     */     private final RealmsText buttonText;
/*     */     private final RealmsText message;
/*     */     
/*     */     private VisitUrl(RealmsNotification base, String url, RealmsText buttonText, RealmsText message) {
/* 102 */       super(base.uuid, base.dismissable, base.seen, base.type);
/* 103 */       this.url = url;
/* 104 */       this.buttonText = buttonText;
/* 105 */       this.message = message;
/*     */     }
/*     */     
/*     */     public static VisitUrl parse(RealmsNotification base, JsonObject jsonObject) {
/* 109 */       String url = JsonUtils.getRequiredString("url", jsonObject);
/* 110 */       RealmsText buttonText = (RealmsText)JsonUtils.getRequired("buttonText", jsonObject, RealmsText::parse);
/* 111 */       RealmsText message = (RealmsText)JsonUtils.getRequired("message", jsonObject, RealmsText::parse);
/* 112 */       return new VisitUrl(base, url, buttonText, message);
/*     */     }
/*     */     
/*     */     public Component getMessage() {
/* 116 */       return this.message.createComponent((Component)Component.translatable("mco.notification.visitUrl.message.default"));
/*     */     }
/*     */     
/*     */     public Button buildOpenLinkButton(Screen parentScreen) {
/* 120 */       Component buttonLabel = this.buttonText.createComponent(RealmsNotification.BUTTON_TEXT_FALLBACK);
/* 121 */       return Button.builder(buttonLabel, ConfirmLinkScreen.confirmLink(parentScreen, this.url)).build();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class InfoPopup
/*     */     extends RealmsNotification
/*     */   {
/*     */     private static final String TITLE = "title";
/*     */     private static final String MESSAGE = "message";
/*     */     private static final String IMAGE = "image";
/*     */     private static final String URL_BUTTON = "urlButton";
/*     */     private final RealmsText title;
/*     */     private final RealmsText message;
/*     */     private final Identifier image;
/*     */     private final RealmsNotification.UrlButton urlButton;
/*     */     
/*     */     private InfoPopup(RealmsNotification base, RealmsText title, RealmsText message, Identifier image, RealmsNotification.UrlButton urlButton) {
/* 138 */       super(base.uuid, base.dismissable, base.seen, base.type);
/* 139 */       this.title = title;
/* 140 */       this.message = message;
/* 141 */       this.image = image;
/* 142 */       this.urlButton = urlButton;
/*     */     }
/*     */     
/*     */     public static InfoPopup parse(RealmsNotification base, JsonObject object) {
/* 146 */       RealmsText title = (RealmsText)JsonUtils.getRequired("title", object, RealmsText::parse);
/* 147 */       RealmsText message = (RealmsText)JsonUtils.getRequired("message", object, RealmsText::parse);
/* 148 */       Identifier image = Identifier.parse(JsonUtils.getRequiredString("image", object));
/* 149 */       RealmsNotification.UrlButton urlButton = (RealmsNotification.UrlButton)JsonUtils.getOptional("urlButton", object, RealmsNotification.UrlButton::parse);
/* 150 */       return new InfoPopup(base, title, message, image, urlButton);
/*     */     }
/*     */     
/*     */     public PopupScreen buildScreen(Screen parentScreen, Consumer<UUID> dismiss) {
/* 154 */       Component title = this.title.createComponent();
/* 155 */       if (title == null) {
/* 156 */         RealmsNotification.LOGGER.warn("Realms info popup had title with no available translation: {}", this.title);
/* 157 */         return null;
/*     */       } 
/* 159 */       PopupScreen.Builder builder = new PopupScreen.Builder(parentScreen, title)
/* 160 */         .setImage(this.image)
/* 161 */         .setMessage(this.message.createComponent(CommonComponents.EMPTY));
/* 162 */       if (this.urlButton != null) {
/* 163 */         builder.addButton(this.urlButton.urlText.createComponent(RealmsNotification.BUTTON_TEXT_FALLBACK), popup -> {
/*     */               Minecraft minecraft = Minecraft.getInstance();
/*     */ 
/*     */ 
/*     */               
/*     */               minecraft.setScreen((Screen)new ConfirmLinkScreen((), this.urlButton.url, true));
/*     */ 
/*     */               
/*     */               parentScreen.accept(uuid());
/*     */             });
/*     */       }
/*     */ 
/*     */       
/* 176 */       builder.addButton(CommonComponents.GUI_OK, popup -> {
/*     */             dismiss.onClose();
/*     */             dismiss.accept(uuid());
/*     */           });
/* 180 */       builder.onClose(() -> dismiss.accept(uuid()));
/* 181 */       return builder.build();
/*     */     } }
/*     */   private static final class UrlButton extends Record { private final String url; private final RealmsText urlText; private static final String URL = "url"; private static final String URL_TEXT = "urlText";
/*     */     
/* 185 */     private UrlButton(String url, RealmsText urlText) { this.url = url; this.urlText = urlText; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsNotification$UrlButton;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #185	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 185 */       //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsNotification$UrlButton; } public String url() { return this.url; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsNotification$UrlButton;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #185	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsNotification$UrlButton; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsNotification$UrlButton;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #185	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsNotification$UrlButton;
/* 185 */       //   0	8	1	o	Ljava/lang/Object; } public RealmsText urlText() { return this.urlText; }
/*     */ 
/*     */ 
/*     */     
/*     */     public static UrlButton parse(JsonObject jsonObject) {
/* 190 */       String url = JsonUtils.getRequiredString("url", jsonObject);
/* 191 */       RealmsText urlText = (RealmsText)JsonUtils.getRequired("urlText", jsonObject, RealmsText::parse);
/* 192 */       return new UrlButton(url, urlText);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RealmsNotification.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
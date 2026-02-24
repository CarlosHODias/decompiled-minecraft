/*    */ package com.mojang.realmsclient.util.task;
/*    */ 
/*    */ import com.mojang.realmsclient.client.RealmsClient;
/*    */ import com.mojang.realmsclient.dto.WorldTemplate;
/*    */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class ResettingTemplateWorldTask extends ResettingWorldTask {
/*    */   private final WorldTemplate template;
/*    */   
/*    */   public ResettingTemplateWorldTask(WorldTemplate template, long serverId, Component title, Runnable callback) {
/* 12 */     super(serverId, title, callback);
/* 13 */     this.template = template;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void sendResetRequest(RealmsClient client, long serverId) throws RealmsServiceException {
/* 18 */     client.resetWorldWithTemplate(serverId, this.template.id());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/util/task/ResettingTemplateWorldTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
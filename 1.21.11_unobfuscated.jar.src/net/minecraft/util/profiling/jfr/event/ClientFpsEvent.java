/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import jdk.jfr.Category;
/*    */ import jdk.jfr.Event;
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ import jdk.jfr.Period;
/*    */ import jdk.jfr.StackTrace;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Name("minecraft.ClientFps")
/*    */ @Label("Client fps")
/*    */ @Category({"Minecraft", "Ticking"})
/*    */ @StackTrace(false)
/*    */ @Period("1 s")
/*    */ public class ClientFpsEvent
/*    */   extends Event
/*    */ {
/*    */   public static final String EVENT_NAME = "minecraft.ClientFps";
/* 22 */   public static final EventType TYPE = EventType.getEventType((Class)ClientFpsEvent.class);
/*    */   
/*    */   @Name("fps")
/*    */   @Label("Client fps")
/*    */   public final int fps;
/*    */   
/*    */   public ClientFpsEvent(int fps) {
/* 29 */     this.fps = fps;
/*    */   }
/*    */   
/*    */   public static class Fields {
/*    */     public static final String FPS = "fps";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/jfr/event/ClientFpsEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
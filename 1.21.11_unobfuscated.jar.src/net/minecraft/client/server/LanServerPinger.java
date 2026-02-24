/*     */ package net.minecraft.client.server;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class LanServerPinger
/*     */   extends Thread
/*     */ {
/*  16 */   private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
/*  17 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final String MULTICAST_GROUP = "224.0.2.60";
/*     */   
/*     */   public static final int PING_PORT = 4445;
/*     */   private static final long PING_INTERVAL = 1500L;
/*     */   private final String motd;
/*     */   private final DatagramSocket socket;
/*     */   private boolean isRunning = true;
/*     */   private final String serverAddress;
/*     */   
/*     */   public LanServerPinger(String motd, String serverAddress) throws IOException {
/*  29 */     super("LanServerPinger #" + UNIQUE_THREAD_ID.incrementAndGet());
/*  30 */     this.motd = motd;
/*  31 */     this.serverAddress = serverAddress;
/*  32 */     setDaemon(true);
/*  33 */     setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(LOGGER));
/*     */     
/*  35 */     this.socket = new DatagramSocket();
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  40 */     String pingString = createPingString(this.motd, this.serverAddress);
/*  41 */     byte[] ping = pingString.getBytes(StandardCharsets.UTF_8);
/*     */     
/*  43 */     while (!isInterrupted() && this.isRunning) {
/*     */       try {
/*  45 */         InetAddress group = InetAddress.getByName("224.0.2.60");
/*     */         
/*  47 */         DatagramPacket packet = new DatagramPacket(ping, ping.length, group, 4445);
/*  48 */         this.socket.send(packet);
/*  49 */       } catch (IOException e) {
/*  50 */         LOGGER.warn("LanServerPinger: {}", e.getMessage());
/*     */         
/*     */         break;
/*     */       } 
/*     */       try {
/*  55 */         sleep(1500L);
/*  56 */       } catch (InterruptedException interruptedException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void interrupt() {
/*  63 */     super.interrupt();
/*     */ 
/*     */     
/*  66 */     this.isRunning = false;
/*     */   }
/*     */   
/*     */   public static String createPingString(String motd, String address) {
/*  70 */     return "[MOTD]" + motd + "[/MOTD][AD]" + address + "[/AD]";
/*     */   }
/*     */   
/*     */   public static String parseMotd(String pingString) {
/*  74 */     int startIndex = pingString.indexOf("[MOTD]");
/*  75 */     if (startIndex < 0) {
/*  76 */       return "missing no";
/*     */     }
/*  78 */     int endIndex = pingString.indexOf("[/MOTD]", startIndex + "[MOTD]".length());
/*  79 */     if (endIndex < startIndex) {
/*  80 */       return "missing no";
/*     */     }
/*  82 */     return pingString.substring(startIndex + "[MOTD]".length(), endIndex);
/*     */   }
/*     */   
/*     */   public static String parseAddress(String pingString) {
/*  86 */     int endMotdIndex = pingString.indexOf("[/MOTD]");
/*  87 */     if (endMotdIndex < 0) {
/*  88 */       return null;
/*     */     }
/*     */     
/*  91 */     int secondEndMotdIndex = pingString.indexOf("[/MOTD]", endMotdIndex + "[/MOTD]".length());
/*  92 */     if (secondEndMotdIndex >= 0)
/*     */     {
/*  94 */       return null;
/*     */     }
/*     */     
/*  97 */     int startIndex = pingString.indexOf("[AD]", endMotdIndex + "[/MOTD]".length());
/*  98 */     if (startIndex < 0) {
/*  99 */       return null;
/*     */     }
/* 101 */     int endIndex = pingString.indexOf("[/AD]", startIndex + "[AD]".length());
/* 102 */     if (endIndex < startIndex) {
/* 103 */       return null;
/*     */     }
/* 105 */     return pingString.substring(startIndex + "[AD]".length(), endIndex);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/server/LanServerPinger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
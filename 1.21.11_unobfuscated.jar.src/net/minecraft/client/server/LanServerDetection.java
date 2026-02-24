/*     */ package net.minecraft.client.server;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.MulticastSocket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class LanServerDetection
/*     */ {
/*  19 */   private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
/*  20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static class LanServerList {
/*  23 */     private final List<LanServer> servers = Lists.newArrayList();
/*     */     private boolean isDirty;
/*     */     
/*     */     public synchronized List<LanServer> takeDirtyServers() {
/*  27 */       if (this.isDirty) {
/*  28 */         List<LanServer> newServers = List.copyOf(this.servers);
/*  29 */         this.isDirty = false;
/*  30 */         return newServers;
/*     */       } 
/*  32 */       return null;
/*     */     }
/*     */     
/*     */     public synchronized void addServer(String pingData, InetAddress socketAddress) {
/*  36 */       String motd = LanServerPinger.parseMotd(pingData);
/*  37 */       String address = LanServerPinger.parseAddress(pingData);
/*  38 */       if (address == null) {
/*     */         return;
/*     */       }
/*     */       
/*  42 */       address = socketAddress.getHostAddress() + ":" + socketAddress.getHostAddress();
/*     */       
/*     */       boolean found = false;
/*  45 */       for (LanServer server : this.servers) {
/*  46 */         if (server.getAddress().equals(address)) {
/*  47 */           server.updatePingTime();
/*  48 */           found = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*  53 */       if (!found) {
/*  54 */         this.servers.add(new LanServer(motd, address));
/*  55 */         this.isDirty = true;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LanServerDetector
/*     */     extends Thread {
/*     */     private final LanServerDetection.LanServerList serverList;
/*     */     private final InetAddress pingGroup;
/*     */     private final MulticastSocket socket;
/*     */     
/*     */     public LanServerDetector(LanServerDetection.LanServerList serverList) throws IOException {
/*  67 */       super("LanServerDetector #" + LanServerDetection.UNIQUE_THREAD_ID.incrementAndGet());
/*  68 */       this.serverList = serverList;
/*  69 */       setDaemon(true);
/*  70 */       setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(LanServerDetection.LOGGER));
/*     */       
/*  72 */       this.socket = new MulticastSocket(4445);
/*  73 */       this.pingGroup = InetAddress.getByName("224.0.2.60");
/*  74 */       this.socket.setSoTimeout(5000);
/*  75 */       this.socket.joinGroup(this.pingGroup);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*  81 */       byte[] buf = new byte[1024];
/*     */       
/*  83 */       while (!isInterrupted()) {
/*  84 */         DatagramPacket packet = new DatagramPacket(buf, buf.length);
/*     */         try {
/*  86 */           this.socket.receive(packet);
/*  87 */         } catch (SocketTimeoutException ignored) {
/*     */           continue;
/*  89 */         } catch (IOException e) {
/*  90 */           LanServerDetection.LOGGER.error("Couldn't ping server", e);
/*     */           
/*     */           break;
/*     */         } 
/*  94 */         String received = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
/*  95 */         LanServerDetection.LOGGER.debug("{}: {}", packet.getAddress(), received);
/*  96 */         this.serverList.addServer(received, packet.getAddress());
/*     */       } 
/*     */       
/*     */       try {
/* 100 */         this.socket.leaveGroup(this.pingGroup);
/* 101 */       } catch (IOException iOException) {}
/*     */       
/* 103 */       this.socket.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/server/LanServerDetection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
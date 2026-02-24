/*    */ package com.mojang.realmsclient.client;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.realmsclient.dto.RegionPingResult;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Socket;
/*    */ import java.net.SocketAddress;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.Util;
/*    */ import org.apache.commons.io.IOUtils;
/*    */ 
/*    */ 
/*    */ public class Ping
/*    */ {
/*    */   public static List<RegionPingResult> ping(Region... regions) {
/* 17 */     for (Region region : regions) {
/* 18 */       ping(region.endpoint);
/*    */     }
/*    */ 
/*    */     
/* 22 */     List<RegionPingResult> results = Lists.newArrayList();
/* 23 */     for (Region region : regions) {
/* 24 */       results.add(new RegionPingResult(region.name, ping(region.endpoint)));
/*    */     }
/*    */ 
/*    */     
/* 28 */     results.sort(Comparator.comparingInt(RegionPingResult::ping));
/* 29 */     return results;
/*    */   }
/*    */   
/*    */   private static int ping(String host) {
/* 33 */     int timeout = 700;
/* 34 */     long sum = 0L;
/* 35 */     Socket socket = null;
/* 36 */     for (int i = 0; i < 5; i++) {
/*    */       
/* 38 */       try { SocketAddress sockAddr = new InetSocketAddress(host, 80);
/* 39 */         socket = new Socket();
/* 40 */         long t1 = now();
/* 41 */         socket.connect(sockAddr, 700);
/* 42 */         sum += now() - t1;
/*    */ 
/*    */ 
/*    */         
/* 46 */         IOUtils.closeQuietly(socket); } catch (Exception ignored) { sum += 700L; } finally { IOUtils.closeQuietly(socket); }
/*    */     
/*    */     } 
/* 49 */     return (int)(sum / 5.0D);
/*    */   }
/*    */   
/*    */   private static long now() {
/* 53 */     return Util.getMillis();
/*    */   }
/*    */   
/*    */   public static List<RegionPingResult> pingAllRegions() {
/* 57 */     return ping(Region.values());
/*    */   }
/*    */   
/*    */   enum Region {
/* 61 */     US_EAST_1("us-east-1", "ec2.us-east-1.amazonaws.com"),
/* 62 */     US_WEST_2("us-west-2", "ec2.us-west-2.amazonaws.com"),
/* 63 */     US_WEST_1("us-west-1", "ec2.us-west-1.amazonaws.com"),
/* 64 */     EU_WEST_1("eu-west-1", "ec2.eu-west-1.amazonaws.com"),
/* 65 */     AP_SOUTHEAST_1("ap-southeast-1", "ec2.ap-southeast-1.amazonaws.com"),
/* 66 */     AP_SOUTHEAST_2("ap-southeast-2", "ec2.ap-southeast-2.amazonaws.com"),
/* 67 */     AP_NORTHEAST_1("ap-northeast-1", "ec2.ap-northeast-1.amazonaws.com"),
/* 68 */     SA_EAST_1("sa-east-1", "ec2.sa-east-1.amazonaws.com"); private final String name; private final String endpoint;
/*    */     
/*    */     Region(String name, String endpoint) {
/* 71 */       this.name = name;
/* 72 */       this.endpoint = endpoint;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/client/Ping.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
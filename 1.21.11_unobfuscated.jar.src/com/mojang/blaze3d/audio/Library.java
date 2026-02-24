/*     */ package com.mojang.blaze3d.audio;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.OptionalLong;
/*     */ import java.util.Set;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.lwjgl.openal.AL;
/*     */ import org.lwjgl.openal.AL10;
/*     */ import org.lwjgl.openal.ALC;
/*     */ import org.lwjgl.openal.ALC10;
/*     */ import org.lwjgl.openal.ALC11;
/*     */ import org.lwjgl.openal.ALCCapabilities;
/*     */ import org.lwjgl.openal.ALCapabilities;
/*     */ import org.lwjgl.openal.ALUtil;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Library
/*     */ {
/*     */   public enum Pool
/*     */   {
/*  35 */     STATIC,
/*  36 */     STREAMING;
/*     */   }
/*     */ 
/*     */   
/*  40 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int NO_DEVICE = 0;
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_CHANNEL_COUNT = 30;
/*     */ 
/*     */   
/*     */   private long currentDevice;
/*     */ 
/*     */   
/*     */   private long context;
/*     */ 
/*     */   
/*     */   private boolean supportsDisconnections;
/*     */ 
/*     */   
/*     */   private String defaultDeviceName;
/*     */ 
/*     */ 
/*     */   
/*  63 */   private static final ChannelPool EMPTY = new ChannelPool()
/*     */     {
/*     */       public Channel acquire() {
/*  66 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean release(Channel channel) {
/*  71 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void cleanup() {}
/*     */ 
/*     */       
/*     */       public int getMaxCount() {
/*  80 */         return 0;
/*     */       }
/*     */ 
/*     */       
/*     */       public int getUsedCount() {
/*  85 */         return 0;
/*     */       }
/*     */     };
/*     */   
/*     */   private static class CountingChannelPool implements ChannelPool {
/*     */     private final int limit;
/*  91 */     private final Set<Channel> activeChannels = Sets.newIdentityHashSet();
/*     */     
/*     */     public CountingChannelPool(int limit) {
/*  94 */       this.limit = limit;
/*     */     }
/*     */ 
/*     */     
/*     */     public Channel acquire() {
/*  99 */       if (this.activeChannels.size() >= this.limit) {
/* 100 */         if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 101 */           Library.LOGGER.warn("Maximum sound pool size {} reached", this.limit);
/*     */         }
/* 103 */         return null;
/*     */       } 
/*     */       
/* 106 */       Channel channel = Channel.create();
/* 107 */       if (channel != null) {
/* 108 */         this.activeChannels.add(channel);
/*     */       }
/*     */       
/* 111 */       return channel;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean release(Channel channel) {
/* 116 */       if (!this.activeChannels.remove(channel)) {
/* 117 */         return false;
/*     */       }
/* 119 */       channel.destroy();
/* 120 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void cleanup() {
/* 125 */       this.activeChannels.forEach(Channel::destroy);
/* 126 */       this.activeChannels.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getMaxCount() {
/* 131 */       return this.limit;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getUsedCount() {
/* 136 */       return this.activeChannels.size();
/*     */     }
/*     */   }
/*     */   
/* 140 */   private ChannelPool staticChannels = EMPTY;
/* 141 */   private ChannelPool streamingChannels = EMPTY;
/*     */   
/* 143 */   private final Listener listener = new Listener();
/*     */   
/*     */   public Library() {
/* 146 */     this.defaultDeviceName = getDefaultDeviceName();
/*     */   }
/*     */   
/*     */   public void init(String preferredDevice, boolean useHrtf) {
/* 150 */     this.currentDevice = openDeviceOrFallback(preferredDevice);
/* 151 */     this.supportsDisconnections = false;
/*     */     
/* 153 */     ALCCapabilities alcCapabilities = ALC.createCapabilities(this.currentDevice);
/* 154 */     if (OpenAlUtil.checkALCError(this.currentDevice, "Get capabilities")) {
/* 155 */       throw new IllegalStateException("Failed to get OpenAL capabilities");
/*     */     }
/*     */     
/* 158 */     if (!alcCapabilities.OpenALC11) {
/* 159 */       throw new IllegalStateException("OpenAL 1.1 not supported");
/*     */     }
/*     */     
/* 162 */     MemoryStack stack = MemoryStack.stackPush(); 
/* 163 */     try { IntBuffer attr = createAttributes(stack, (alcCapabilities.ALC_SOFT_HRTF && useHrtf));
/* 164 */       this.context = ALC10.alcCreateContext(this.currentDevice, attr);
/* 165 */       if (stack != null) stack.close();  } catch (Throwable throwable) { if (stack != null)
/*     */         try { stack.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 167 */      if (OpenAlUtil.checkALCError(this.currentDevice, "Create context")) {
/* 168 */       throw new IllegalStateException("Unable to create OpenAL context");
/*     */     }
/*     */     
/* 171 */     ALC10.alcMakeContextCurrent(this.context);
/*     */     
/* 173 */     int totalChannelCount = getChannelCount();
/* 174 */     int streamingChannelCount = Mth.clamp((int)Mth.sqrt(totalChannelCount), 2, 8);
/* 175 */     int staticChannelCount = Mth.clamp(totalChannelCount - streamingChannelCount, 8, 255);
/*     */     
/* 177 */     this.staticChannels = new CountingChannelPool(staticChannelCount);
/* 178 */     this.streamingChannels = new CountingChannelPool(streamingChannelCount);
/*     */     
/* 180 */     ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
/* 181 */     OpenAlUtil.checkALError("Initialization");
/*     */     
/* 183 */     if (!alCapabilities.AL_EXT_source_distance_model)
/*     */     {
/* 185 */       throw new IllegalStateException("AL_EXT_source_distance_model is not supported");
/*     */     }
/* 187 */     AL10.alEnable(512);
/*     */     
/* 189 */     if (!alCapabilities.AL_EXT_LINEAR_DISTANCE) {
/* 190 */       throw new IllegalStateException("AL_EXT_LINEAR_DISTANCE is not supported");
/*     */     }
/* 192 */     OpenAlUtil.checkALError("Enable per-source distance models");
/* 193 */     LOGGER.info("OpenAL initialized on device {}", getCurrentDeviceName());
/*     */ 
/*     */ 
/*     */     
/* 197 */     this.supportsDisconnections = ALC10.alcIsExtensionPresent(this.currentDevice, "ALC_EXT_disconnect");
/*     */   }
/*     */   
/*     */   private IntBuffer createAttributes(MemoryStack stack, boolean enableHrtf) {
/* 201 */     int maxAttributes = 5;
/* 202 */     IntBuffer attr = stack.callocInt(11);
/*     */     
/* 204 */     int numHrtf = ALC10.alcGetInteger(this.currentDevice, 6548);
/* 205 */     if (numHrtf > 0) {
/* 206 */       attr.put(6546).put(enableHrtf ? 1 : 0);
/*     */       
/* 208 */       attr.put(6550).put(0);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 213 */     attr.put(6554).put(1);
/*     */     
/* 215 */     return attr.put(0).flip();
/*     */   }
/*     */   
/*     */   private int getChannelCount() {
/* 219 */     MemoryStack stack = MemoryStack.stackPush(); 
/* 220 */     try { int size = ALC10.alcGetInteger(this.currentDevice, 4098);
/* 221 */       if (OpenAlUtil.checkALCError(this.currentDevice, "Get attributes size")) {
/* 222 */         throw new IllegalStateException("Failed to get OpenAL attributes");
/*     */       }
/*     */       
/* 225 */       IntBuffer attributes = stack.mallocInt(size);
/* 226 */       ALC10.alcGetIntegerv(this.currentDevice, 4099, attributes);
/* 227 */       if (OpenAlUtil.checkALCError(this.currentDevice, "Get attributes")) {
/* 228 */         throw new IllegalStateException("Failed to get OpenAL attributes");
/*     */       }
/*     */       
/* 231 */       int pos = 0;
/* 232 */       while (pos < size)
/* 233 */       { int attribute = attributes.get(pos++);
/* 234 */         if (attribute == 0) {
/*     */           break;
/*     */         }
/* 237 */         int attributeValue = attributes.get(pos++);
/*     */         
/* 239 */         if (attribute == 4112)
/* 240 */         { int i = attributeValue;
/*     */ 
/*     */           
/* 243 */           if (stack != null) stack.close();  return i; }  }  if (stack != null) stack.close();  } catch (Throwable throwable) { if (stack != null)
/* 244 */         try { stack.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  return 30;
/*     */   }
/*     */   
/*     */   public static String getDefaultDeviceName() {
/* 248 */     if (!ALC10.alcIsExtensionPresent(0L, "ALC_ENUMERATE_ALL_EXT")) {
/* 249 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 254 */     ALUtil.getStringList(0L, 4115);
/*     */     
/* 256 */     return ALC10.alcGetString(0L, 4114);
/*     */   }
/*     */   
/*     */   public String getCurrentDeviceName() {
/* 260 */     String name = ALC10.alcGetString(this.currentDevice, 4115);
/* 261 */     if (name == null) {
/* 262 */       name = ALC10.alcGetString(this.currentDevice, 4101);
/*     */     }
/* 264 */     if (name == null) {
/* 265 */       name = "Unknown";
/*     */     }
/* 267 */     return name;
/*     */   }
/*     */   
/*     */   public synchronized boolean hasDefaultDeviceChanged() {
/* 271 */     String name = getDefaultDeviceName();
/* 272 */     if (Objects.equals(this.defaultDeviceName, name)) {
/* 273 */       return false;
/*     */     }
/* 275 */     this.defaultDeviceName = name;
/* 276 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static long openDeviceOrFallback(String preferredDevice) {
/* 281 */     OptionalLong device = OptionalLong.empty();
/* 282 */     if (preferredDevice != null) {
/* 283 */       device = tryOpenDevice(preferredDevice);
/*     */     }
/* 285 */     if (device.isEmpty()) {
/* 286 */       device = tryOpenDevice(getDefaultDeviceName());
/*     */     }
/* 288 */     if (device.isEmpty()) {
/* 289 */       device = tryOpenDevice(null);
/*     */     }
/* 291 */     if (device.isEmpty()) {
/* 292 */       throw new IllegalStateException("Failed to open OpenAL device");
/*     */     }
/*     */     
/* 295 */     return device.getAsLong();
/*     */   }
/*     */   
/*     */   private static OptionalLong tryOpenDevice(String name) {
/* 299 */     long device = ALC10.alcOpenDevice(name);
/*     */     
/* 301 */     if (device != 0L && !OpenAlUtil.checkALCError(device, "Open device")) {
/* 302 */       return OptionalLong.of(device);
/*     */     }
/*     */     
/* 305 */     return OptionalLong.empty();
/*     */   }
/*     */   
/*     */   public void cleanup() {
/* 309 */     this.staticChannels.cleanup();
/* 310 */     this.streamingChannels.cleanup();
/*     */     
/* 312 */     ALC10.alcDestroyContext(this.context);
/* 313 */     if (this.currentDevice != 0L) {
/* 314 */       ALC10.alcCloseDevice(this.currentDevice);
/*     */     }
/*     */   }
/*     */   
/*     */   public Listener getListener() {
/* 319 */     return this.listener;
/*     */   }
/*     */   
/*     */   public Channel acquireChannel(Pool pool) {
/* 323 */     return ((pool == Pool.STREAMING) ? this.streamingChannels : this.staticChannels).acquire();
/*     */   }
/*     */   
/*     */   public void releaseChannel(Channel channel) {
/* 327 */     if (!this.staticChannels.release(channel) && !this.streamingChannels.release(channel)) {
/* 328 */       throw new IllegalStateException("Tried to release unknown channel");
/*     */     }
/*     */   }
/*     */   
/*     */   public String getDebugString() {
/* 333 */     return String.format(Locale.ROOT, "Sounds: %d/%d + %d/%d", new Object[] { this.staticChannels.getUsedCount(), this.staticChannels.getMaxCount(), this.streamingChannels.getUsedCount(), this.streamingChannels.getMaxCount() });
/*     */   }
/*     */   
/*     */   public List<String> getAvailableSoundDevices() {
/* 337 */     List<String> result = ALUtil.getStringList(0L, 4115);
/* 338 */     if (result == null) {
/* 339 */       return Collections.emptyList();
/*     */     }
/* 341 */     return result;
/*     */   }
/*     */   
/*     */   public boolean isCurrentDeviceDisconnected() {
/* 345 */     return (this.supportsDisconnections && ALC11.alcGetInteger(this.currentDevice, 787) == 0);
/*     */   }
/*     */   
/*     */   private static interface ChannelPool {
/*     */     Channel acquire();
/*     */     
/*     */     boolean release(Channel param1Channel);
/*     */     
/*     */     void cleanup();
/*     */     
/*     */     int getMaxCount();
/*     */     
/*     */     int getUsedCount();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/audio/Library.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
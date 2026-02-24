/*     */ package net.minecraft.client.sounds;
/*     */ 
/*     */ import com.jcraft.jogg.Packet;
/*     */ import com.jcraft.jogg.Page;
/*     */ import com.jcraft.jogg.StreamState;
/*     */ import com.jcraft.jogg.SyncState;
/*     */ import com.jcraft.jorbis.Block;
/*     */ import com.jcraft.jorbis.Comment;
/*     */ import com.jcraft.jorbis.DspState;
/*     */ import com.jcraft.jorbis.Info;
/*     */ import it.unimi.dsi.fastutil.floats.FloatConsumer;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JOrbisAudioStream
/*     */   implements FloatSampleSource
/*     */ {
/*     */   private static final int BUFSIZE = 8192;
/*     */   private static final int PAGEOUT_RECAPTURE = -1;
/*     */   private static final int PAGEOUT_NEED_MORE_DATA = 0;
/*     */   private static final int PAGEOUT_OK = 1;
/*     */   private static final int PACKETOUT_ERROR = -1;
/*     */   private static final int PACKETOUT_NEED_MORE_DATA = 0;
/*     */   private static final int PACKETOUT_OK = 1;
/*  32 */   private final SyncState syncState = new SyncState();
/*  33 */   private final Page page = new Page();
/*     */ 
/*     */ 
/*     */   
/*  37 */   private final StreamState streamState = new StreamState();
/*  38 */   private final Packet packet = new Packet();
/*     */   
/*  40 */   private final Info info = new Info();
/*  41 */   private final DspState dspState = new DspState();
/*  42 */   private final Block block = new Block(this.dspState);
/*     */   
/*     */   private final AudioFormat audioFormat;
/*     */   
/*     */   private final InputStream input;
/*     */   private long samplesWritten;
/*  48 */   private long totalSamplesInStream = Long.MAX_VALUE;
/*     */   
/*     */   public JOrbisAudioStream(InputStream input) throws IOException {
/*  51 */     this.input = input;
/*     */     
/*  53 */     Comment comment = new Comment();
/*     */     
/*  55 */     Page firstPage = readPage();
/*  56 */     if (firstPage == null) {
/*  57 */       throw new IOException("Invalid Ogg file - can't find first page");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  62 */     Packet firstPacket = readIdentificationPacket(firstPage);
/*  63 */     if (isError(this.info.synthesis_headerin(comment, firstPacket))) {
/*  64 */       throw new IOException("Invalid Ogg identification packet");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  69 */     int headerPacketCount = 0;
/*     */ 
/*     */ 
/*     */     
/*  73 */     while (headerPacketCount < 2) {
/*  74 */       Packet packet = readPacket();
/*  75 */       if (packet == null) {
/*  76 */         throw new IOException("Unexpected end of Ogg stream");
/*     */       }
/*  78 */       if (isError(this.info.synthesis_headerin(comment, packet))) {
/*  79 */         throw new IOException("Invalid Ogg header packet " + headerPacketCount);
/*     */       }
/*  81 */       headerPacketCount++;
/*     */     } 
/*     */ 
/*     */     
/*  85 */     this.dspState.synthesis_init(this.info);
/*  86 */     this.block.init(this.dspState);
/*     */     
/*  88 */     this.audioFormat = new AudioFormat(this.info.rate, 16, this.info.channels, true, false);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isError(int value) {
/*  93 */     return (value < 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public AudioFormat getFormat() {
/*  98 */     return this.audioFormat;
/*     */   }
/*     */   
/*     */   private boolean readToBuffer() throws IOException {
/* 102 */     int offset = this.syncState.buffer(8192);
/* 103 */     byte[] buffer = this.syncState.data;
/* 104 */     int bytes = this.input.read(buffer, offset, 8192);
/* 105 */     if (bytes == -1) {
/* 106 */       return false;
/*     */     }
/*     */     
/* 109 */     this.syncState.wrote(bytes);
/* 110 */     return true;
/*     */   }
/*     */   private Page readPage() throws IOException {
/*     */     int pageOutResult;
/*     */     while (true) {
/* 115 */       pageOutResult = this.syncState.pageout(this.page);
/* 116 */       switch (pageOutResult)
/*     */       { case 1:
/* 118 */           if (this.page.eos() != 0) {
/* 119 */             this.totalSamplesInStream = this.page.granulepos();
/*     */           }
/* 121 */           return this.page;
/*     */         
/*     */         case 0:
/* 124 */           if (!readToBuffer())
/* 125 */             return null; 
/*     */           continue;
/*     */         case -1:
/* 128 */           throw new IOException("Corrupt or missing data in bitstream"); }  break;
/* 129 */     }  throw new IllegalStateException("Unknown page decode result: " + pageOutResult);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Packet readIdentificationPacket(Page firstPage) throws IOException {
/* 135 */     this.streamState.init(firstPage.serialno());
/* 136 */     if (isError(this.streamState.pagein(firstPage))) {
/* 137 */       throw new IOException("Failed to parse page");
/*     */     }
/* 139 */     int result = this.streamState.packetout(this.packet);
/*     */     
/* 141 */     if (result != 1) {
/* 142 */       throw new IOException("Failed to read identification packet: " + result);
/*     */     }
/* 144 */     return this.packet;
/*     */   } private Packet readPacket() throws IOException {
/*     */     int packetOutResult;
/*     */     while (true) {
/*     */       Page page;
/* 149 */       packetOutResult = this.streamState.packetout(this.packet);
/* 150 */       switch (packetOutResult)
/*     */       { case 1:
/* 152 */           return this.packet;
/*     */         
/*     */         case 0:
/* 155 */           page = readPage();
/* 156 */           if (page == null) {
/* 157 */             return null;
/*     */           }
/*     */           
/* 160 */           if (isError(this.streamState.pagein(page)))
/* 161 */             throw new IOException("Failed to parse page"); 
/*     */           continue;
/*     */         case -1:
/* 164 */           throw new IOException("Failed to parse packet"); }  break;
/* 165 */     }  throw new IllegalStateException("Unknown packet decode result: " + packetOutResult);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private long getSamplesToWrite(int samples) {
/*     */     long samplesToWrite;
/* 172 */     long samplesAfterWrite = this.samplesWritten + samples;
/*     */     
/* 174 */     if (samplesAfterWrite > this.totalSamplesInStream) {
/* 175 */       samplesToWrite = this.totalSamplesInStream - this.samplesWritten;
/* 176 */       this.samplesWritten = this.totalSamplesInStream;
/*     */     } else {
/* 178 */       this.samplesWritten = samplesAfterWrite;
/* 179 */       samplesToWrite = samples;
/*     */     } 
/* 181 */     return samplesToWrite;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean readChunk(FloatConsumer consumer) throws IOException {
/* 187 */     float[][][] pcmSampleOutput = new float[1][][];
/* 188 */     int[] pcmOffsetOutput = new int[this.info.channels];
/*     */     
/* 190 */     Packet packet = readPacket();
/* 191 */     if (packet == null) {
/* 192 */       return false;
/*     */     }
/*     */     
/* 195 */     if (isError(this.block.synthesis(packet))) {
/* 196 */       throw new IOException("Can't decode audio packet");
/*     */     }
/*     */     
/* 199 */     this.dspState.synthesis_blockin(this.block);
/*     */     int samples;
/* 201 */     while ((samples = this.dspState.synthesis_pcmout(pcmSampleOutput, pcmOffsetOutput)) > 0) {
/* 202 */       float[][] channelSamples = pcmSampleOutput[0];
/*     */       
/* 204 */       long samplesToWrite = getSamplesToWrite(samples);
/* 205 */       switch (this.info.channels) { case 1:
/* 206 */           copyMono(channelSamples[0], pcmOffsetOutput[0], samplesToWrite, consumer); break;
/* 207 */         case 2: copyStereo(channelSamples[0], pcmOffsetOutput[0], channelSamples[1], pcmOffsetOutput[1], samplesToWrite, consumer); break;
/* 208 */         default: copyAnyChannels(channelSamples, this.info.channels, pcmOffsetOutput, samplesToWrite, consumer); break; }
/*     */       
/* 210 */       this.dspState.synthesis_read(samples);
/*     */     } 
/*     */     
/* 213 */     return true;
/*     */   }
/*     */   
/*     */   private static void copyAnyChannels(float[][] samples, int channelCount, int[] offsets, long count, FloatConsumer output) {
/* 217 */     for (int j = 0; j < count; j++) {
/* 218 */       for (int channel = 0; channel < channelCount; channel++) {
/* 219 */         int offset = offsets[channel];
/* 220 */         float val = samples[channel][offset + j];
/* 221 */         output.accept(val);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void copyMono(float[] samples, int offset, long count, FloatConsumer output) {
/* 227 */     for (int i = offset; i < offset + count; i++) {
/* 228 */       output.accept(samples[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void copyStereo(float[] samples1, int offset1, float[] samples2, int offset2, long count, FloatConsumer output) {
/* 233 */     for (int i = 0; i < count; i++) {
/* 234 */       output.accept(samples1[offset1 + i]);
/* 235 */       output.accept(samples2[offset2 + i]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 241 */     this.input.close();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/sounds/JOrbisAudioStream.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
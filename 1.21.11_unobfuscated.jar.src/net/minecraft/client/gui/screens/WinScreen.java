/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.List;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.GameNarrator;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.LogoRenderer;
/*     */ import net.minecraft.client.gui.render.TextureSetup;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.sounds.Music;
/*     */ import net.minecraft.sounds.Musics;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class WinScreen
/*     */   extends Screen {
/*  40 */   private static final Identifier VIGNETTE_LOCATION = Identifier.withDefaultNamespace("textures/misc/credits_vignette.png");
/*  41 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  42 */   private static final Component SECTION_HEADING = (Component)Component.literal("============").withStyle(ChatFormatting.WHITE);
/*     */   private static final String NAME_PREFIX = "           ";
/*  44 */   private static final String OBFUSCATE_TOKEN = String.valueOf(ChatFormatting.WHITE) + String.valueOf(ChatFormatting.WHITE) + String.valueOf(ChatFormatting.OBFUSCATED) + String.valueOf(ChatFormatting.GREEN);
/*     */   
/*     */   private static final float SPEEDUP_FACTOR = 5.0F;
/*     */   private static final float SPEEDUP_FACTOR_FAST = 15.0F;
/*  48 */   private static final Identifier END_POEM_LOCATION = Identifier.withDefaultNamespace("texts/end.txt");
/*  49 */   private static final Identifier CREDITS_LOCATION = Identifier.withDefaultNamespace("texts/credits.json");
/*  50 */   private static final Identifier POSTCREDITS_LOCATION = Identifier.withDefaultNamespace("texts/postcredits.txt");
/*     */   
/*     */   private final boolean poem;
/*     */   
/*     */   private final Runnable onFinished;
/*     */   private float scroll;
/*     */   private List<FormattedCharSequence> lines;
/*     */   private List<Component> narratorComponents;
/*     */   private IntSet centeredLines;
/*     */   private int totalScrollLength;
/*     */   private boolean speedupActive;
/*  61 */   private final IntSet speedupModifiers = (IntSet)new IntOpenHashSet();
/*     */   
/*     */   private float scrollSpeed;
/*     */   private final float unmodifiedScrollSpeed;
/*     */   private int direction;
/*  66 */   private final LogoRenderer logoRenderer = new LogoRenderer(false);
/*     */   
/*     */   public WinScreen(boolean poem, Runnable onFinished) {
/*  69 */     super(GameNarrator.NO_TITLE);
/*  70 */     this.poem = poem;
/*  71 */     this.onFinished = onFinished;
/*  72 */     if (!poem) {
/*  73 */       this.unmodifiedScrollSpeed = 0.75F;
/*     */     } else {
/*  75 */       this.unmodifiedScrollSpeed = 0.5F;
/*     */     } 
/*  77 */     this.direction = 1;
/*     */     
/*  79 */     this.scrollSpeed = this.unmodifiedScrollSpeed;
/*     */   }
/*     */   
/*     */   private float calculateScrollSpeed() {
/*  83 */     if (this.speedupActive) {
/*  84 */       return this.unmodifiedScrollSpeed * (5.0F + this.speedupModifiers.size() * 15.0F) * this.direction;
/*     */     }
/*  86 */     return this.unmodifiedScrollSpeed * this.direction;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  91 */     this.minecraft.getMusicManager().tick();
/*  92 */     this.minecraft.getSoundManager().tick(false);
/*  93 */     float maxScroll = (this.totalScrollLength + this.height + this.height + 24);
/*  94 */     if (this.scroll > maxScroll) {
/*  95 */       respawn();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 101 */     if (event.isUp()) {
/* 102 */       this.direction = -1;
/* 103 */     } else if (event.key() == 341 || event.key() == 345) {
/* 104 */       this.speedupModifiers.add(event.key());
/* 105 */     } else if (event.key() == 32) {
/* 106 */       this.speedupActive = true;
/*     */     } 
/* 108 */     this.scrollSpeed = calculateScrollSpeed();
/*     */     
/* 110 */     return super.keyPressed(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyReleased(KeyEvent event) {
/* 115 */     if (event.isUp()) {
/* 116 */       this.direction = 1;
/*     */     }
/* 118 */     if (event.key() == 32) {
/* 119 */       this.speedupActive = false;
/* 120 */     } else if (event.key() == 341 || event.key() == 345) {
/* 121 */       this.speedupModifiers.remove(event.key());
/*     */     } 
/* 123 */     this.scrollSpeed = calculateScrollSpeed();
/*     */     
/* 125 */     return super.keyReleased(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 130 */     respawn();
/*     */   }
/*     */   
/*     */   private void respawn() {
/* 134 */     this.onFinished.run();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/* 139 */     if (this.lines != null) {
/*     */       return;
/*     */     }
/*     */     
/* 143 */     this.lines = Lists.newArrayList();
/* 144 */     this.narratorComponents = Lists.newArrayList();
/* 145 */     this.centeredLines = (IntSet)new IntOpenHashSet();
/* 146 */     if (this.poem) {
/* 147 */       wrapCreditsIO(END_POEM_LOCATION, this::addPoemFile);
/*     */     }
/* 149 */     wrapCreditsIO(CREDITS_LOCATION, this::addCreditsFile);
/* 150 */     if (this.poem) {
/* 151 */       wrapCreditsIO(POSTCREDITS_LOCATION, this::addPoemFile);
/*     */     }
/*     */     
/* 154 */     this.totalScrollLength = this.lines.size() * 12;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getNarrationMessage() {
/* 159 */     return (Component)CommonComponents.joinForNarration((Component[])this.narratorComponents.toArray(x$0 -> new Component[x$0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void wrapCreditsIO(Identifier file, CreditsReader creditsReader) {
/*     */     
/* 168 */     try { Reader resource = this.minecraft.getResourceManager().openAsReader(file); 
/* 169 */       try { creditsReader.read(resource);
/* 170 */         if (resource != null) resource.close();  } catch (Throwable throwable) { if (resource != null) try { resource.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/* 171 */     { LOGGER.error("Couldn't load credits from file {}", file, e); }
/*     */   
/*     */   }
/*     */   
/*     */   private void addPoemFile(Reader inputReader) throws IOException {
/* 176 */     BufferedReader reader = new BufferedReader(inputReader);
/* 177 */     RandomSource random = RandomSource.create(8124371L);
/*     */     
/*     */     String line;
/* 180 */     while ((line = reader.readLine()) != null) {
/* 181 */       line = line.replaceAll("PLAYERNAME", this.minecraft.getUser().getName());
/*     */       
/*     */       int pos;
/* 184 */       while ((pos = line.indexOf(OBFUSCATE_TOKEN)) != -1) {
/* 185 */         String before = line.substring(0, pos);
/* 186 */         String after = line.substring(pos + OBFUSCATE_TOKEN.length());
/* 187 */         line = before + before + String.valueOf(ChatFormatting.WHITE) + String.valueOf(ChatFormatting.OBFUSCATED) + "XXXXXXXX".substring(0, random.nextInt(4) + 3);
/*     */       } 
/* 189 */       addPoemLines(line);
/* 190 */       addEmptyLine();
/*     */     } 
/*     */     
/* 193 */     for (int i = 0; i < 8; i++) {
/* 194 */       addEmptyLine();
/*     */     }
/*     */   }
/*     */   
/*     */   private void addCreditsFile(Reader inputReader) {
/* 199 */     JsonArray root = GsonHelper.parseArray(inputReader);
/* 200 */     for (JsonElement sectionElement : (Iterable<JsonElement>)root) {
/* 201 */       JsonObject section = sectionElement.getAsJsonObject();
/* 202 */       String sectionName = section.get("section").getAsString();
/* 203 */       addCreditsLine(SECTION_HEADING, true, false);
/* 204 */       addCreditsLine((Component)Component.literal(sectionName).withStyle(ChatFormatting.YELLOW), true, true);
/* 205 */       addCreditsLine(SECTION_HEADING, true, false);
/* 206 */       addEmptyLine();
/* 207 */       addEmptyLine();
/*     */       
/* 209 */       JsonArray disciplines = section.getAsJsonArray("disciplines");
/* 210 */       for (JsonElement disciplineElement : (Iterable<JsonElement>)disciplines) {
/* 211 */         JsonObject discipline = disciplineElement.getAsJsonObject();
/* 212 */         String disciplineName = discipline.get("discipline").getAsString();
/* 213 */         if (StringUtils.isNotEmpty(disciplineName)) {
/* 214 */           addCreditsLine((Component)Component.literal(disciplineName).withStyle(ChatFormatting.YELLOW), true, true);
/* 215 */           addEmptyLine();
/* 216 */           addEmptyLine();
/*     */         } 
/*     */         
/* 219 */         JsonArray titles = discipline.getAsJsonArray("titles");
/* 220 */         for (JsonElement titleElement : (Iterable<JsonElement>)titles) {
/* 221 */           JsonObject title = titleElement.getAsJsonObject();
/* 222 */           String titleName = title.get("title").getAsString();
/* 223 */           JsonArray names = title.getAsJsonArray("names");
/* 224 */           addCreditsLine((Component)Component.literal(titleName).withStyle(ChatFormatting.GRAY), false, true);
/* 225 */           for (JsonElement nameElement : (Iterable<JsonElement>)names) {
/* 226 */             String name = nameElement.getAsString();
/* 227 */             addCreditsLine((Component)Component.literal("           ").append(name).withStyle(ChatFormatting.WHITE), false, true);
/*     */           } 
/* 229 */           addEmptyLine();
/* 230 */           addEmptyLine();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addEmptyLine() {
/* 237 */     this.lines.add(FormattedCharSequence.EMPTY);
/* 238 */     this.narratorComponents.add(CommonComponents.EMPTY);
/*     */   }
/*     */   
/*     */   private void addPoemLines(String line) {
/* 242 */     MutableComponent mutableComponent = Component.literal(line);
/* 243 */     this.lines.addAll(this.minecraft.font.split((FormattedText)mutableComponent, 256));
/* 244 */     this.narratorComponents.add(mutableComponent);
/*     */   }
/*     */   
/*     */   private void addCreditsLine(Component line, boolean centered, boolean narrated) {
/* 248 */     if (centered) {
/* 249 */       this.centeredLines.add(this.lines.size());
/*     */     }
/* 251 */     this.lines.add(line.getVisualOrderText());
/* 252 */     if (narrated) {
/* 253 */       this.narratorComponents.add(line);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 259 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/* 261 */     renderVignette(graphics);
/*     */     
/* 263 */     this.scroll = Math.max(0.0F, this.scroll + a * this.scrollSpeed);
/*     */     
/* 265 */     int logoX = this.width / 2 - 128;
/* 266 */     int logoY = this.height + 50;
/*     */     
/* 268 */     float yOffs = -this.scroll;
/*     */     
/* 270 */     graphics.pose().pushMatrix();
/* 271 */     graphics.pose().translate(0.0F, yOffs);
/*     */     
/* 273 */     graphics.nextStratum();
/* 274 */     this.logoRenderer.renderLogo(graphics, this.width, 1.0F, logoY);
/*     */     
/* 276 */     int yPos = logoY + 100;
/*     */     
/* 278 */     for (int i = 0; i < this.lines.size(); i++) {
/* 279 */       if (i == this.lines.size() - 1) {
/* 280 */         float diff = yPos + yOffs - (this.height / 2 - 6);
/* 281 */         if (diff < 0.0F) {
/* 282 */           graphics.pose().translate(0.0F, -diff);
/*     */         }
/*     */       } 
/* 285 */       if (yPos + yOffs + 12.0F + 8.0F > 0.0F && yPos + yOffs < this.height) {
/* 286 */         FormattedCharSequence line = this.lines.get(i);
/* 287 */         if (this.centeredLines.contains(i)) {
/* 288 */           graphics.drawCenteredString(this.font, line, logoX + 128, yPos, -1);
/*     */         } else {
/* 290 */           graphics.drawString(this.font, line, logoX, yPos, -1);
/*     */         } 
/*     */       } 
/* 293 */       yPos += 12;
/*     */     } 
/* 295 */     graphics.pose().popMatrix();
/*     */   }
/*     */   
/*     */   private void renderVignette(GuiGraphics graphics) {
/* 299 */     graphics.blit(RenderPipelines.VIGNETTE, VIGNETTE_LOCATION, 0, 0, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 304 */     if (this.poem) {
/* 305 */       TextureManager textureManager = Minecraft.getInstance().getTextureManager();
/* 306 */       AbstractTexture skyTexture = textureManager.getTexture(AbstractEndPortalRenderer.END_SKY_LOCATION);
/* 307 */       AbstractTexture portalTexture = textureManager.getTexture(AbstractEndPortalRenderer.END_PORTAL_LOCATION);
/* 308 */       TextureSetup textureSetup = TextureSetup.doubleTexture(skyTexture.getTextureView(), skyTexture.getSampler(), portalTexture.getTextureView(), portalTexture.getSampler());
/* 309 */       graphics.fill(RenderPipelines.END_PORTAL, textureSetup, 0, 0, this.width, this.height);
/*     */     } else {
/* 311 */       super.renderBackground(graphics, mouseX, mouseY, a);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderMenuBackground(GuiGraphics graphics, int x, int y, int width, int height) {
/* 317 */     float v = this.scroll * 0.5F;
/* 318 */     Screen.renderMenuBackgroundTexture(graphics, Screen.MENU_BACKGROUND, 0, 0, 0.0F, v, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPauseScreen() {
/* 323 */     return !this.poem;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAllowedInPortal() {
/* 328 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed() {
/* 333 */     this.minecraft.getMusicManager().stopPlaying(Musics.CREDITS);
/*     */   }
/*     */ 
/*     */   
/*     */   public Music getBackgroundMusic() {
/* 338 */     return Musics.CREDITS;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface CreditsReader {
/*     */     void read(Reader param1Reader) throws IOException;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/WinScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
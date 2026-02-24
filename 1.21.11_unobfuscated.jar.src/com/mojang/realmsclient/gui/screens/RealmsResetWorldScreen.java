/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.client.worldupload.RealmsCreateWorldFlow;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.WorldTemplate;
/*     */ import com.mojang.realmsclient.dto.WorldTemplatePaginatedList;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*     */ import com.mojang.realmsclient.util.task.RealmCreationTask;
/*     */ import com.mojang.realmsclient.util.task.ResettingTemplateWorldTask;
/*     */ import com.mojang.realmsclient.util.task.SwitchSlotTask;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.layouts.SpacerElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsResetWorldScreen extends RealmsScreen {
/*  39 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  41 */   private static final Component CREATE_REALM_TITLE = (Component)Component.translatable("mco.selectServer.create");
/*  42 */   private static final Component CREATE_REALM_SUBTITLE = (Component)Component.translatable("mco.selectServer.create.subtitle").withColor(-6250336);
/*     */   
/*  44 */   private static final Component CREATE_WORLD_TITLE = (Component)Component.translatable("mco.configure.world.switch.slot");
/*  45 */   private static final Component CREATE_WORLD_SUBTITLE = (Component)Component.translatable("mco.configure.world.switch.slot.subtitle").withColor(-6250336);
/*     */   
/*  47 */   private static final Component GENERATE_NEW_WORLD = (Component)Component.translatable("mco.reset.world.generate");
/*  48 */   private static final Component RESET_WORLD_TITLE = (Component)Component.translatable("mco.reset.world.title");
/*  49 */   private static final Component RESET_WORLD_SUBTITLE = (Component)Component.translatable("mco.reset.world.warning").withColor(-65536);
/*     */   
/*  51 */   public static final Component CREATE_WORLD_RESET_TASK_TITLE = (Component)Component.translatable("mco.create.world.reset.title");
/*  52 */   private static final Component RESET_WORLD_RESET_TASK_TITLE = (Component)Component.translatable("mco.reset.world.resetting.screen.title");
/*     */   
/*  54 */   private static final Component WORLD_TEMPLATES_TITLE = (Component)Component.translatable("mco.reset.world.template");
/*  55 */   private static final Component ADVENTURES_TITLE = (Component)Component.translatable("mco.reset.world.adventure");
/*  56 */   private static final Component EXPERIENCES_TITLE = (Component)Component.translatable("mco.reset.world.experience");
/*  57 */   private static final Component INSPIRATION_TITLE = (Component)Component.translatable("mco.reset.world.inspiration");
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*     */   private final RealmsServer serverData;
/*     */   private final Component subtitle;
/*     */   private final Component resetTaskTitle;
/*  64 */   private static final Identifier UPLOAD_LOCATION = Identifier.withDefaultNamespace("textures/gui/realms/upload.png");
/*  65 */   private static final Identifier ADVENTURE_MAP_LOCATION = Identifier.withDefaultNamespace("textures/gui/realms/adventure.png");
/*  66 */   private static final Identifier SURVIVAL_SPAWN_LOCATION = Identifier.withDefaultNamespace("textures/gui/realms/survival_spawn.png");
/*  67 */   private static final Identifier NEW_WORLD_LOCATION = Identifier.withDefaultNamespace("textures/gui/realms/new_world.png");
/*  68 */   private static final Identifier EXPERIENCE_LOCATION = Identifier.withDefaultNamespace("textures/gui/realms/experience.png");
/*  69 */   private static final Identifier INSPIRATION_LOCATION = Identifier.withDefaultNamespace("textures/gui/realms/inspiration.png");
/*     */   
/*     */   private WorldTemplatePaginatedList templates;
/*     */   
/*     */   private WorldTemplatePaginatedList adventuremaps;
/*     */   
/*     */   private WorldTemplatePaginatedList experiences;
/*     */   private WorldTemplatePaginatedList inspirations;
/*     */   public final int slot;
/*     */   private final RealmCreationTask realmCreationTask;
/*     */   private final Runnable resetWorldRunnable;
/*  80 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout((Screen)this);
/*     */   
/*     */   private RealmsResetWorldScreen(Screen lastScreen, RealmsServer serverData, int slot, Component title, Component subtitle, Component resetTaskTitle, Runnable resetWorldRunnable) {
/*  83 */     this(lastScreen, serverData, slot, title, subtitle, resetTaskTitle, null, resetWorldRunnable);
/*     */   }
/*     */   
/*     */   public RealmsResetWorldScreen(Screen lastScreen, RealmsServer serverData, int slot, Component title, Component subtitle, Component resetTaskTitle, RealmCreationTask realmCreationTask, Runnable resetWorldRunnable) {
/*  87 */     super(title);
/*  88 */     this.lastScreen = lastScreen;
/*  89 */     this.serverData = serverData;
/*  90 */     this.slot = slot;
/*  91 */     this.subtitle = subtitle;
/*  92 */     this.resetTaskTitle = resetTaskTitle;
/*  93 */     this.realmCreationTask = realmCreationTask;
/*  94 */     this.resetWorldRunnable = resetWorldRunnable;
/*     */   }
/*     */   
/*     */   public static RealmsResetWorldScreen forNewRealm(Screen lastScreen, RealmsServer serverData, RealmCreationTask realmCreationTask, Runnable resetWorldRunnable) {
/*  98 */     return new RealmsResetWorldScreen(lastScreen, serverData, serverData.activeSlot, CREATE_REALM_TITLE, CREATE_REALM_SUBTITLE, CREATE_WORLD_RESET_TASK_TITLE, realmCreationTask, resetWorldRunnable);
/*     */   }
/*     */   
/*     */   public static RealmsResetWorldScreen forEmptySlot(Screen lastScreen, int slot, RealmsServer serverData, Runnable resetWorldRunnable) {
/* 102 */     return new RealmsResetWorldScreen(lastScreen, serverData, slot, CREATE_WORLD_TITLE, CREATE_WORLD_SUBTITLE, CREATE_WORLD_RESET_TASK_TITLE, resetWorldRunnable);
/*     */   }
/*     */   
/*     */   public static RealmsResetWorldScreen forResetSlot(Screen lastScreen, RealmsServer serverData, Runnable resetWorldRunnable) {
/* 106 */     return new RealmsResetWorldScreen(lastScreen, serverData, serverData.activeSlot, RESET_WORLD_TITLE, RESET_WORLD_SUBTITLE, RESET_WORLD_RESET_TASK_TITLE, resetWorldRunnable);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/* 111 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical());
/* 112 */     java.util.Objects.requireNonNull(this.font); header.defaultCellSetting().padding(9 / 3);
/* 113 */     header.addChild((LayoutElement)new StringWidget(this.title, this.font), LayoutSettings::alignHorizontallyCenter);
/* 114 */     header.addChild((LayoutElement)new StringWidget(this.subtitle, this.font), LayoutSettings::alignHorizontallyCenter);
/*     */     
/* 116 */     new Thread("Realms-reset-world-fetcher")
/*     */       {
/*     */         public void run() {
/* 119 */           RealmsClient client = RealmsClient.getOrCreate();
/*     */           try {
/* 121 */             WorldTemplatePaginatedList templates = client.fetchWorldTemplates(1, 10, RealmsServer.WorldType.NORMAL);
/* 122 */             WorldTemplatePaginatedList adventuremaps = client.fetchWorldTemplates(1, 10, RealmsServer.WorldType.ADVENTUREMAP);
/* 123 */             WorldTemplatePaginatedList experiences = client.fetchWorldTemplates(1, 10, RealmsServer.WorldType.EXPERIENCE);
/* 124 */             WorldTemplatePaginatedList inspirations = client.fetchWorldTemplates(1, 10, RealmsServer.WorldType.INSPIRATION);
/* 125 */             RealmsResetWorldScreen.this.minecraft.execute(() -> {
/*     */                   RealmsResetWorldScreen.this.templates = templates;
/*     */                   RealmsResetWorldScreen.this.adventuremaps = adventuremaps;
/*     */                   RealmsResetWorldScreen.this.experiences = experiences;
/*     */                   RealmsResetWorldScreen.this.inspirations = inspirations;
/*     */                 });
/* 131 */           } catch (RealmsServiceException e) {
/* 132 */             RealmsResetWorldScreen.LOGGER.error("Couldn't fetch templates in reset world", (Throwable)e);
/*     */           } 
/*     */         }
/* 135 */       }.start();
/*     */     
/* 137 */     GridLayout grid = (GridLayout)this.layout.addToContents((LayoutElement)new GridLayout());
/* 138 */     GridLayout.RowHelper helper = grid.createRowHelper(3);
/* 139 */     helper.defaultCellSetting().paddingHorizontal(16);
/* 140 */     helper.addChild((LayoutElement)new FrameButton(this.minecraft.font, GENERATE_NEW_WORLD, NEW_WORLD_LOCATION, button -> RealmsCreateWorldFlow.createWorld(this.minecraft, this.lastScreen, (Screen)this, this.slot, this.serverData, this.realmCreationTask)));
/*     */ 
/*     */     
/* 143 */     helper.addChild((LayoutElement)new FrameButton(this.minecraft.font, RealmsSelectFileToUploadScreen.TITLE, UPLOAD_LOCATION, button -> this.minecraft.setScreen((Screen)new RealmsSelectFileToUploadScreen(this.realmCreationTask, this.serverData.id, this.slot, this))));
/*     */ 
/*     */     
/* 146 */     helper.addChild((LayoutElement)new FrameButton(this.minecraft.font, WORLD_TEMPLATES_TITLE, SURVIVAL_SPAWN_LOCATION, button -> this.minecraft.setScreen((Screen)new RealmsSelectWorldTemplateScreen(WORLD_TEMPLATES_TITLE, this::templateSelectionCallback, RealmsServer.WorldType.NORMAL, this.templates))));
/*     */ 
/*     */ 
/*     */     
/* 150 */     helper.addChild((LayoutElement)SpacerElement.height(16), 3);
/*     */     
/* 152 */     helper.addChild((LayoutElement)new FrameButton(this.minecraft.font, ADVENTURES_TITLE, ADVENTURE_MAP_LOCATION, button -> this.minecraft.setScreen((Screen)new RealmsSelectWorldTemplateScreen(ADVENTURES_TITLE, this::templateSelectionCallback, RealmsServer.WorldType.ADVENTUREMAP, this.adventuremaps))));
/*     */ 
/*     */     
/* 155 */     helper.addChild((LayoutElement)new FrameButton(this.minecraft.font, EXPERIENCES_TITLE, EXPERIENCE_LOCATION, button -> this.minecraft.setScreen((Screen)new RealmsSelectWorldTemplateScreen(EXPERIENCES_TITLE, this::templateSelectionCallback, RealmsServer.WorldType.EXPERIENCE, this.experiences))));
/*     */ 
/*     */     
/* 158 */     helper.addChild((LayoutElement)new FrameButton(this.minecraft.font, INSPIRATION_TITLE, INSPIRATION_LOCATION, button -> this.minecraft.setScreen((Screen)new RealmsSelectWorldTemplateScreen(INSPIRATION_TITLE, this::templateSelectionCallback, RealmsServer.WorldType.INSPIRATION, this.inspirations))));
/*     */ 
/*     */ 
/*     */     
/* 162 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose()).build());
/*     */     
/* 164 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/* 165 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 170 */     this.layout.arrangeElements();
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getNarrationMessage() {
/* 175 */     return (Component)CommonComponents.joinForNarration(new Component[] { getTitle(), this.subtitle });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 180 */     this.minecraft.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   private void templateSelectionCallback(WorldTemplate template) {
/* 184 */     this.minecraft.setScreen((Screen)this);
/* 185 */     if (template != null) {
/* 186 */       runResetTasks((LongRunningTask)new ResettingTemplateWorldTask(template, this.serverData.id, this.resetTaskTitle, this.resetWorldRunnable));
/*     */     }
/* 188 */     RealmsMainScreen.refreshServerList();
/*     */   }
/*     */   
/*     */   private void runResetTasks(LongRunningTask resetTask) {
/* 192 */     List<LongRunningTask> tasks = new ArrayList<>();
/* 193 */     if (this.realmCreationTask != null) {
/* 194 */       tasks.add(this.realmCreationTask);
/*     */     }
/* 196 */     if (this.slot != this.serverData.activeSlot)
/* 197 */       tasks.add(new SwitchSlotTask(this.serverData.id, this.slot, () -> {
/*     */             
/* 199 */             }));  tasks.add(resetTask);
/* 200 */     this.minecraft.setScreen((Screen)new RealmsLongRunningMcoTaskScreen(this.lastScreen, tasks.<LongRunningTask>toArray(new LongRunningTask[0])));
/*     */   }
/*     */   
/*     */   private class FrameButton extends Button {
/* 204 */     private static final Identifier SLOT_FRAME_SPRITE = Identifier.withDefaultNamespace("widget/slot_frame");
/*     */     private static final int FRAME_SIZE = 60;
/*     */     private static final int FRAME_WIDTH = 2;
/*     */     private static final int IMAGE_SIZE = 56;
/*     */     private final Identifier image;
/*     */     
/*     */     private FrameButton(Font font, Component text, Identifier image, Button.OnPress onPress) {
/* 211 */       super(0, 0, 60, 60 + 9, text, onPress, DEFAULT_NARRATION);
/* 212 */       this.image = image;
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 217 */       boolean hoveredOrFocused = isHoveredOrFocused();
/* 218 */       int color = -1;
/* 219 */       if (hoveredOrFocused) {
/* 220 */         color = ARGB.colorFromFloat(1.0F, 0.56F, 0.56F, 0.56F);
/*     */       }
/* 222 */       int x = getX();
/* 223 */       int y = getY();
/* 224 */       graphics.blit(RenderPipelines.GUI_TEXTURED, this.image, x + 2, y + 2, 0.0F, 0.0F, 56, 56, 56, 56, 56, 56, color);
/* 225 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_FRAME_SPRITE, x, y, 60, 60, color);
/*     */       
/* 227 */       int textColor = hoveredOrFocused ? -6250336 : -1;
/* 228 */       graphics.drawCenteredString(RealmsResetWorldScreen.this.font, getMessage(), x + 28, y - 14, textColor);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/RealmsResetWorldScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
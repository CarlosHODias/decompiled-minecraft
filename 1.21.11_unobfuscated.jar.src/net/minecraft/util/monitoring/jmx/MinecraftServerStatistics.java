/*     */ package net.minecraft.util.monitoring.jmx;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import javax.management.Attribute;
/*     */ import javax.management.AttributeList;
/*     */ import javax.management.DynamicMBean;
/*     */ import javax.management.JMException;
/*     */ import javax.management.MBeanAttributeInfo;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MinecraftServerStatistics
/*     */   implements DynamicMBean
/*     */ {
/*  30 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private final MinecraftServer server;
/*     */   
/*     */   private MinecraftServerStatistics(MinecraftServer server) {
/*  34 */     this
/*     */ 
/*     */       
/*  37 */       .attributeDescriptionByName = (Map<String, AttributeDescription>)Stream.<AttributeDescription>of(new AttributeDescription[] { new AttributeDescription("tickTimes", this::getTickTimes, "Historical tick times (ms)", long[].class), new AttributeDescription("averageTickTime", this::getAverageTickTime, "Current average tick time (ms)", long.class) }).collect(Collectors.toMap(attributeDescription -> attributeDescription.name, Function.identity()));
/*     */ 
/*     */     
/*  40 */     this.server = server;
/*     */     
/*  42 */     MBeanAttributeInfo[] mBeanAttributeInfos = (MBeanAttributeInfo[])this.attributeDescriptionByName.values().stream()
/*  43 */       .map(AttributeDescription::asMBeanAttributeInfo)
/*  44 */       .toArray(x$0 -> new MBeanAttributeInfo[x$0]);
/*     */     
/*  46 */     this.mBeanInfo = new MBeanInfo(MinecraftServerStatistics.class.getSimpleName(), "metrics for dedicated server", mBeanAttributeInfos, null, null, new javax.management.MBeanNotificationInfo[0]);
/*     */   }
/*     */   private final MBeanInfo mBeanInfo; private final Map<String, AttributeDescription> attributeDescriptionByName;
/*     */   public static void registerJmxMonitoring(MinecraftServer server) {
/*     */     try {
/*  51 */       ManagementFactory.getPlatformMBeanServer().registerMBean(new MinecraftServerStatistics(server), new ObjectName("net.minecraft.server:type=Server"));
/*     */ 
/*     */     
/*     */     }
/*  55 */     catch (MalformedObjectNameException|javax.management.InstanceAlreadyExistsException|javax.management.MBeanRegistrationException|javax.management.NotCompliantMBeanException e) {
/*  56 */       LOGGER.warn("Failed to initialise server as JMX bean", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private float getAverageTickTime() {
/*  61 */     return this.server.getCurrentSmoothedTickTime();
/*     */   }
/*     */   
/*     */   private long[] getTickTimes() {
/*  65 */     return this.server.getTickTimesNanos();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getAttribute(String attribute) {
/*  70 */     AttributeDescription attributeDescription = this.attributeDescriptionByName.get(attribute);
/*  71 */     return (attributeDescription == null) ? 
/*  72 */       null : 
/*  73 */       attributeDescription.getter.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(Attribute attribute) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeList getAttributes(String[] attributes) {
/*  84 */     Objects.requireNonNull(this.attributeDescriptionByName); List<Attribute> attributeList = (List<Attribute>)Arrays.<String>stream(attributes).map(this.attributeDescriptionByName::get)
/*  85 */       .filter(Objects::nonNull)
/*  86 */       .map(attributeDescription -> new Attribute(attributeDescription.name, attributeDescription.getter.get()))
/*  87 */       .collect(Collectors.toList());
/*  88 */     return new AttributeList(attributeList);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeList setAttributes(AttributeList attributes) {
/*  94 */     return new AttributeList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(String actionName, Object[] params, String[] signature) {
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public MBeanInfo getMBeanInfo() {
/* 105 */     return this.mBeanInfo;
/*     */   }
/*     */   
/*     */   private static final class AttributeDescription {
/*     */     private final String name;
/*     */     private final Supplier<Object> getter;
/*     */     private final String description;
/*     */     private final Class<?> type;
/*     */     
/*     */     private AttributeDescription(String name, Supplier<Object> getter, String description, Class<?> type) {
/* 115 */       this.name = name;
/* 116 */       this.getter = getter;
/* 117 */       this.description = description;
/* 118 */       this.type = type;
/*     */     }
/*     */     
/*     */     private MBeanAttributeInfo asMBeanAttributeInfo() {
/* 122 */       return new MBeanAttributeInfo(this.name, this.type.getSimpleName(), this.description, true, false, false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/monitoring/jmx/MinecraftServerStatistics.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */
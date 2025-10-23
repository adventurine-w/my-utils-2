package org.adv.clickerflex.ultimate_utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.adv.clickerflex.Clickerflex;
import org.adv.clickerflex.ultimate_utils.better_classes.BetterBukkitSchedulerTask;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockType;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

import static org.adv.clickerflex.item.lore.LoreWidthHandler.centerComponent;
import static org.adv.clickerflex.utils.generic.ColorUtils.mmColored;

public class Common {

    public static void runMainThread(Runnable runnable){
        Bukkit.getScheduler().runTaskLater(Clickerflex.getInstance(), runnable, 0L);
    }

    public static void runAsync(Runnable runnable){
        Bukkit.getScheduler().runTaskAsynchronously(Clickerflex.getInstance(), runnable);
    }

    public static BetterBukkitSchedulerTask runTaskLater(Runnable runnable, long ticks){
        if(ticks == 0){
            runnable.run();
            return null;
        }
        return new BetterBukkitSchedulerTask(Bukkit.getScheduler().runTaskLater(Clickerflex.getInstance(), runnable, ticks), ticks);
    }
    public static BetterBukkitSchedulerTask runTaskLaterS(Runnable runnable, float seconds) {
        long delayTicks = Math.round(seconds * 20);
        if(seconds>0){
            delayTicks = Math.max(delayTicks, 1);
        }
        return runTaskLater(runnable, delayTicks);
    }

    public static BukkitTask runTaskTimer(Runnable runnable, long delayTicks, long periodTicks){
        return Bukkit.getScheduler().runTaskTimer(Clickerflex.getInstance(), runnable, delayTicks, periodTicks);
    }

    public static void fileMKDIRS(File file){
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        if(!file.exists()){
            try {
                file.createNewFile();
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
    }


    public static double dSquared(Location loc1, Location loc2) {
        return loc1.distanceSquared(loc2);
    }
    public static double dSquared(Location loc1, LivingEntity entity2) {
        return loc1.distanceSquared(getCenterLocation(entity2));
    }
    public static double dSquared(LivingEntity entity1, Location loc2) {
        return getCenterLocation(entity1).distanceSquared(loc2);
    }
    public static double dSquared(LivingEntity entity1, LivingEntity entity2) {
        return getCenterLocation(entity1).distanceSquared(getCenterLocation(entity2));
    }
    public static Location getCenterLocation(LivingEntity entity){
        return entity.getLocation().add(0,entity.getEyeHeight()/2,0);
    }

    public static Vector getDirection(LivingEntity entity){
        return entity.getLocation().getDirection().normalize();
    }
    public static boolean teleport(Entity entity, Location location) {
        List<Entity> passengers = new ArrayList<>(entity.getPassengers());
        for (Entity passenger : passengers) {
            entity.removePassenger(passenger);
        }

        if (entity.getVehicle() != null) {
            entity.getVehicle().removePassenger(entity);
        }

        boolean success = entity.teleport(location);

        for (Entity passenger : passengers) {
            entity.addPassenger(passenger);
        }

        return success;
    }
    public static void setScale(LivingEntity entity, double scale){
        AttributeInstance instance = entity.getAttribute(Attribute.SCALE);
        if(instance==null) return;
        instance.setBaseValue(scale);
    }
    public static void setScale(Display display, float scale){
        setScale(display,scale,scale,scale);
    }
    public static void setScale(Display display, float x, float y, float z){
        var t = display.getTransformation();
        var newT = new Transformation(t.getTranslation(), t.getLeftRotation(), new Vector3f(x,y,z), t.getRightRotation());
        display.setTransformation(newT);
    }
    public static double getScale(Entity entity){
        if(!(entity instanceof LivingEntity lv)) return 1d;
        AttributeInstance instance = lv.getAttribute(Attribute.SCALE);
        if(instance==null) return 1d;
        return instance.getValue();
    }
    public static void applyPotionEffect(LivingEntity entity, PotionEffectType type, int amplifier, int durationTicks) {
        if (entity == null || type == null) return;

        PotionEffect effect = new PotionEffect(type, durationTicks, amplifier, false, false);
        entity.addPotionEffect(effect);
    }
    public static void push(LivingEntity beingPushed, Vector vector, double force) {
        if (beingPushed == null || vector == null) return;

        Vector normalized = vector.clone().normalize().multiply(force);
        beingPushed.setVelocity(beingPushed.getVelocity().add(normalized));
    }
    public static void pushForwards(LivingEntity beingPushed, double force) {
        if (beingPushed == null) return;
        beingPushed.setVelocity(beingPushed.getVelocity().add(beingPushed.getLocation().getDirection().multiply(force)));
    }
    public static void pushBackwards(LivingEntity beingPushed, double force) {
        if (beingPushed == null) return;
        beingPushed.setVelocity(beingPushed.getVelocity().add(beingPushed.getLocation().getDirection().multiply(force*-1)));
    }

    public static void pushUpwards(LivingEntity beingPushed, double force) {
        if (beingPushed == null) return;
        beingPushed.setVelocity(beingPushed.getVelocity().add(new Vector(0, force, 0)));
    }

    public static void pushDownwards(LivingEntity beingPushed, double force) {
        if (beingPushed == null) return;
        beingPushed.setVelocity(beingPushed.getVelocity().add(new Vector(0, -force, 0)));
    }

    public static Location getLocationInFront(Location origin, double distance) {
        return offsetInDirection(origin, 0, distance);
    }

    public static Location getLocationBehind(Location origin, double distance) {
        return offsetInDirection(origin, 180, distance);
    }

    public static Location getLocationToLeft(Location origin, double distance) {
        return offsetInDirection(origin, 90, distance);
    }

    public static Location getLocationToRight(Location origin, double distance) {
        return offsetInDirection(origin, -90, distance);
    }

    private static Location offsetInDirection(Location origin, float yawOffset, double distance) {
        Location clone = origin.clone();

        float yaw = (clone.getYaw() + yawOffset) % 360;
        double radians = Math.toRadians(yaw);

        double dx = -Math.sin(radians) * distance;
        double dz =  Math.cos(radians) * distance;

        clone.add(dx, 0, dz);
        return clone;
    }
    public static Location getLocationInFrontOf(Location origin, Vector direction, double distance) {
        return offsetInDirection(origin, direction, distance);
    }

    public static Location getLocationBehind(Location origin, Vector direction, double distance) {
        return offsetInDirection(origin, direction.clone().multiply(-1), distance);
    }

    public static Location getLocationToLeft(Location origin, Vector direction, double distance) {
        // Rotate direction vector 90° left (XZ plane)
        Vector left = new Vector(-direction.getZ(), 0, direction.getX()).normalize();
        return offsetInDirection(origin, left, distance);
    }

    public static Location getLocationToRight(Location origin, Vector direction, double distance) {
        // Rotate direction vector 90° right (XZ plane)
        Vector right = new Vector(direction.getZ(), 0, -direction.getX()).normalize();
        return offsetInDirection(origin, right, distance);
    }

    private static Location offsetInDirection(Location origin, Vector direction, double distance) {
        Location clone = origin.clone();
        Vector offset = direction.clone().normalize().multiply(distance);
        clone.add(offset);
        return clone;
    }
    public static void showTitle(Player player, String title, String subtitle, float seconds){
        player.showTitle(Title.title(mmColored(title), mmColored(subtitle), Title.Times.times(Duration.ofMillis(500), Duration.ofMillis((long) (seconds*1000)), Duration.ofMillis(1000))));
    }

    public static Component centerChat(String component) {
        return centerComponent(mmColored(component), 322);
    }
    public static Component centerChat(Component component) {
        return centerComponent(component, 322);
    }
    public static void setMetadata(Entity entity, String key, Object value) {
        entity.setMetadata(key, new FixedMetadataValue(Clickerflex.getInstance(), value));
    }

    @SuppressWarnings("unchecked")
    public static <T> T getMetadata(Entity entity, String key) {
        if (!entity.hasMetadata(key)) {
            return null;
        }
        List<MetadataValue> metadata = entity.getMetadata(key);
        if (metadata.isEmpty()) {
            return null;
        }
        return (T) metadata.get(0).value(); // just grab the first one
    }
    public static void loadChunk(Chunk chunk){
        if(!chunk.isLoaded()) {
            chunk.load();
            Common.runTaskLater(() -> {
                chunk.getWorld().refreshChunk(chunk.getX(), chunk.getZ());
            }, 1L);
        }
    }

    public static boolean hasMetadata(Entity entity, String key) {
        return entity.hasMetadata(key);
    }

    public static void removeMetadata(Entity entity, String key) {
        entity.removeMetadata(key, Clickerflex.getInstance());
    }

    private static final Set<Material> UNSOLID_BLOCKS = EnumSet.of(
            Material.AIR,
            Material.LIGHT,
            Material.SHORT_GRASS,
            Material.TALL_GRASS,
            Material.FERN,
            Material.DEAD_BUSH
    );
    static {
        for (Material mat : Material.values()) {
            if (mat.name().endsWith("_CARPET")) {
                UNSOLID_BLOCKS.add(mat);
            }
            if (mat.name().endsWith("_BUTTON")) {
                UNSOLID_BLOCKS.add(mat);
            }
        }
    }

    public static boolean isSolid(Material material) {
        return !UNSOLID_BLOCKS.contains(material);
    }
    public static boolean isSolid(BlockType blockType) {
        return isSolid(blockType);
    }
    public static boolean isSolid(Block block) {
        return isSolid(block.getType());
    }
    public static boolean isSolid(Location location) {
        return isSolid(location.getBlock());
    }
    private static final List<EntityType> UNDEAD_TYPES = List.of(
            EntityType.ZOMBIE, EntityType.SKELETON, EntityType.WITHER_SKELETON, EntityType.STRAY,
            EntityType.DROWNED, EntityType.HUSK, EntityType.ZOMBIFIED_PIGLIN, EntityType.GHAST,
            EntityType.PHANTOM, EntityType.VEX, EntityType.WITCH, EntityType.ZOGLIN,
            EntityType.SKELETON_HORSE, EntityType.ZOMBIE_HORSE, EntityType.WITHER
    );
    public static boolean isUndead(EntityType type) {
        return UNDEAD_TYPES.contains(type);
    }


    public static boolean isDay(World world) {
        long time = world.getTime();
        return time < 12000;
    }
    public static boolean isNight(World world) {
        return !isDay(world);
    }

    private static final Set<EntityType> END_MOBS = new HashSet<>(Set.of(
            EntityType.ENDERMAN,
            EntityType.ENDERMITE,
            EntityType.SHULKER,
            EntityType.ENDER_DRAGON
    ));
    public static boolean isEndMob(EntityType type){
        return END_MOBS.contains(type);
    }

    public static boolean isOnGround(Player player){
        Location loc = player.getLocation();
        Block under = loc.getBlock().getRelative(BlockFace.DOWN);

        if (under.getType().isSolid()) return true;

        double y = loc.getY();
        double blockTop = under.getY() + 1.0;
        return y <= blockTop + 0.001 && y >= blockTop - 1.1;
    }
}

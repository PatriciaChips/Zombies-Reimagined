package org.pat.zombiesReimagined.Commands;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.HangingSign;
import org.bukkit.block.data.type.Light;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.pat.pattyEssentialsV3.ColoredText;
import org.pat.pattyEssentialsV3.Utils;
import org.pat.zombiesReimagined.Listeners.Interact;
import org.pat.zombiesReimagined.Utility.ItemUtils.Guns;
import org.pat.zombiesReimagined.Utility.ItemUtils.Item;
import org.pat.zombiesReimagined.Utility.ItemUtils.UseType;
import org.pat.zombiesReimagined.Utility.MapUtils.FeatureType;
import org.pat.zombiesReimagined.Utility.MapUtils.IdentifiedStructures;
import org.pat.zombiesReimagined.Utility.MapUtils.MapFeature;
import org.pat.zombiesReimagined.Utility.MapUtils.StructUtils.SpawnStructure;
import org.pat.zombiesReimagined.Utility.ZUtils;
import org.pat.zombiesReimagined.Zombies;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.*;

public class Test implements TabExecutor {

    public static boolean useModels = true;

    public static List<Entity> bulletHoles = new ArrayList<>();
    public static int maxEntities = 200 * 2; //200

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {

        if (sender instanceof Player p) {

            /**
             useModels = !useModels;
             p.sendMessage(ColoredText.t("&7&oUse Models set to: " + useModels));
             for (var v : MapFeature.storedStructures.entrySet()) {
             if (v.getKey().getType() == FeatureType.GUN) {
             v.getKey().statusVisibilitySwap(false);
             } else if (v.getKey().getType() == FeatureType.CHEST) {
             v.getKey().statusVisibilitySwap(true);
             }
             }
             */

            /**
             p.setInvulnerable(false);
             p.setAllowFlight(false);
             p.setGameMode(GameMode.SURVIVAL);
             p.setLevel(0);
             p.setFoodLevel(20);
             p.setSaturation(6);
             p.setHealth(20);
             p.setArrowsInBody(0);
             p.setCollidable(true);
             p.setExpCooldown(0);
             */

            for (var mapFeature : MapFeature.storedStructures.entrySet()) {
                MapFeature feature = mapFeature.getKey();
                Location loc = mapFeature.getValue();
                if (feature.getType() == FeatureType.WINDOW) {
                    if (feature.getLoc() != null) {
                        for (int i = 0; i <= 5; i++) {
                            Zombie zombie = feature.getLoc().getWorld().spawn(feature.getLoc(), Zombie.class);
                            zombie.setRemoveWhenFarAway(false);
                            zombie.setPersistent(true);
                            zombie.registerAttribute(Attribute.FOLLOW_RANGE);
                            zombie.registerAttribute(Attribute.TEMPT_RANGE);
                            zombie.getAttribute(Attribute.FOLLOW_RANGE).setBaseValue(1000);
                            zombie.getAttribute(Attribute.TEMPT_RANGE).setBaseValue(1000);
                            zombie.setTarget(p);
                        }
                    }
                }
            }

        }

        return false;
    }

    public static void textDisplaySmokeTest1(Player p) {
        int i = 0;
        for (float f : new float[]{-45F, 45F, -45F, 45F}) {

            float scaleWidth = 0.1F;
            float scaleLength = 100F;

            Location loc = p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(0.5));
            loc.setYaw(((loc.getYaw() - 90) + 180) % 360 - 180);
            loc.setPitch(f);

            if (i >= 2)
                loc.setYaw(((loc.getYaw() - 180) + 180) % 360 - 180);

            loc.add(loc.getDirection().normalize().multiply(scaleWidth));

            TextDisplay textDisplayFront = p.getWorld().spawn(loc, TextDisplay.class);
            textDisplayFront.setBackgroundColor(Color.fromARGB(100, 0, 0, 0));
            textDisplayFront.setText(" ");
            textDisplayFront.setAlignment(TextDisplay.TextAlignment.CENTER);
            textDisplayFront.setBrightness(new Display.Brightness(5, 5));
            textDisplayFront.setBillboard(Display.Billboard.FIXED);
            textDisplayFront.setTransformationMatrix(new Matrix4f().scaleLocal(1));
            textDisplayFront.setInterpolationDuration(20);
            textDisplayFront.setInterpolationDelay(2);

            int finalI = i;

            float yScale = scaleWidth * 8F;
            textDisplayFront.setTransformationMatrix(new Matrix4f().scale(scaleLength * 8, yScale, 0)
                    .translateLocal((finalI >= 2) ? (scaleLength / 2.5188F) + 0.2999F : -(scaleLength / 1.675F) - 0.2999F, -yScale * 0.125F, 0));

            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                //textDisplayFront.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                    //textDisplayFront.remove();
                }, 25);
            }, 3);

            i++;
        }
    }

    public static Location getBlockFaceDirection(Location hitLocation) {
        double distanceNorth = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(0, 0, -0.5)); // North face
        double distanceSouth = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(0, 0, 0.5)); // South face
        double distanceWest = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(-0.5, 0, 0)); // West face
        double distanceEast = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(0.5, 0, 0)); // East face
        double distanceUp = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(0, 0.5, 0)); // Up face
        double distanceDown = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(0, -0.5, 0)); // Down face

        // Find the face with the smallest distance
        double minDistance = Math.min(Math.min(Math.min(distanceNorth, distanceSouth), Math.min(distanceWest, distanceEast)), Math.min(distanceUp, distanceDown));

        Location faceLocation = null;
        float pitch = 0;
        float yaw = 0;

        // Return the corresponding face based on the smallest distance
        if (minDistance == distanceNorth) {
            faceLocation = hitLocation.clone().add(0, 0, -0.5);
            yaw = -180;
            pitch = 0;
        } else if (minDistance == distanceSouth) {
            faceLocation = hitLocation.clone().add(0, 0, 0.5);
            yaw = 0;
            pitch = 0;
        } else if (minDistance == distanceWest) {
            faceLocation = hitLocation.clone().add(-0.5, 0, 0);
            yaw = 90;
            pitch = 0;
        } else if (minDistance == distanceEast) {
            faceLocation = hitLocation.clone().add(0.5, 0, 0);
            yaw = -90;
            pitch = 0;
        } else if (minDistance == distanceUp) {
            faceLocation = hitLocation.clone().add(0, 0.5, 0);
            yaw = 0;
            pitch = -90;
        } else {
            faceLocation = hitLocation.clone().add(0, -0.5, 0);
            yaw = 0;
            pitch = 90;
        }
        Vector holeVec = hitLocation.clone().getBlock().getLocation().add(0.5, 0.5, 0.5).toVector().subtract(faceLocation.toVector()).normalize().multiply(-1);

        holeVec = holeVec.multiply(0.01);

        Location holeLocation = hitLocation.clone().add(holeVec);
        holeLocation.setPitch(pitch);
        holeLocation.setYaw(yaw);

        return holeLocation;
    }

    public static void runColourArray(BlockDisplay blockDisplay1) {
        blockDisplay1.setBrightness(new Display.Brightness(15, 15));
        blockDisplay1.setBlock(Material.OCHRE_FROGLIGHT.createBlockData());
        int c = 1;
        for (Material colours : new Material[]{Material.YELLOW_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.RED_STAINED_GLASS, Material.GRAY_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS}) {
            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                /**
                 if (colours == Material.LIGHT_GRAY_STAINED_GLASS) {
                 for (int b = 5; b <= 15; b++) {
                 int brightness = b;
                 Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                 if (!blockDisplay1.isDead())
                 blockDisplay1.setBrightness(new Display.Brightness(brightness, brightness));
                 }, b);
                 }
                 } else {
                 for (int b = 15; b >= 5; b--) {
                 int brightness = b;
                 Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                 if (!blockDisplay1.isDead())
                 blockDisplay1.setBrightness(new Display.Brightness(brightness, brightness));
                 }, 15 - b);
                 }
                 }
                 */
                blockDisplay1.setBlock(colours.createBlockData());
            }, c);
            c++;
        }
    }

    public static boolean createTrail(Location adjustedLoc, boolean hasColour, Material customTrail, float scale, int duration, float length) {
        return createTrail(adjustedLoc, hasColour, customTrail, scale, duration, length, null);
    }

    public static boolean createTrail(Location adjustedLoc, boolean hasColour, Material customTrail, float scale, int duration, float length, @Nullable Player p) {
        if (!MapFeature.isMaterialSafe(adjustedLoc.getBlock().getType()))
            return false;

        BlockDisplay blockDisplay1 = adjustedLoc.getWorld().spawn(adjustedLoc, BlockDisplay.class);
        float ranScale = (new Random().nextInt((int) (scale * 150F)) / 100F);
        blockDisplay1.setTransformationMatrix(new Matrix4f()
                .scale(scale + ranScale, scale + ranScale, (float) length)
                .translate(-0.5F, -0.5F, 0)
        );

        if (p != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player != p)
                    player.hideEntity(ZUtils.plugin, blockDisplay1);
            }
        }

        int ran = new Random().nextInt(2);
        int ran1 = new Random().nextInt(duration * 2);

        if (hasColour) {
            runColourArray(blockDisplay1);
        } else {
            blockDisplay1.setBrightness(new Display.Brightness(15, 15));
            if (customTrail == null) {
                blockDisplay1.setBlock(Material.LIGHT_GRAY_STAINED_GLASS.createBlockData());
            } else {
                blockDisplay1.setBlock(customTrail.createBlockData());
            }
        }

        blockDisplay1.setInterpolationDelay(-1);
        blockDisplay1.setInterpolationDuration(duration + ran1);

        Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
            blockDisplay1.setTransformationMatrix(new Matrix4f()
                    .scale(0f, 0F, ((float) length / 2))
                    .translate(-0.5F, -0.5F, 0)
                    .rotateLocalZ((float) Math.toRadians(new Random().nextInt(91)))
            );
            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                blockDisplay1.remove();
            }, 1 + ran + blockDisplay1.getInterpolationDuration()); //2
        }, 1 + ran);

        return true;
    }

    public static BlockFace getBlockFaceFromNormal(Vector normalVec) {
        // Get the absolute values of the vector components
        double absX = Math.abs(normalVec.getX());
        double absY = Math.abs(normalVec.getY());
        double absZ = Math.abs(normalVec.getZ());

        // Check which component has the largest absolute value
        if (absX > absY && absX > absZ) {
            // If the X component is the largest, it's either EAST or WEST
            return (normalVec.getX() > 0) ? BlockFace.EAST : BlockFace.WEST;
        } else if (absY > absX && absY > absZ) {
            // If the Y component is the largest, it's either UP or DOWN
            return (normalVec.getY() > 0) ? BlockFace.UP : BlockFace.DOWN;
        } else {
            // If the Z component is the largest, it's either NORTH or SOUTH
            return (normalVec.getZ() > 0) ? BlockFace.SOUTH : BlockFace.NORTH;
        }
    }

    public static boolean shootRay1(Player p, ItemStack item, boolean initialShot, boolean useBullet) {
        Item gun = Item.getItem(item);
        float bloom = gun.getBloom();

        boolean doReturn = useGunLogic(item, gun, initialShot, useBullet);
        if (doReturn)
            return true;

        Location loc = createWeaponLocation(p, bloom, false, gun.getRange());
        Vector dir = loc.getDirection();

        loc.getWorld().spawnParticle(Particle.DUST, loc, 3, 0, 0 ,0, new Particle.DustOptions(Color.fromRGB(140, 240, 70), 0.8F));

        Object[] variables = getLookingAtBlockSpot(loc, p, gun.getRange(), 0);
        float distance = (variables.length > 0 ? (float) variables[0] : gun.getRange());
        Location hitLocation = (Location) (variables.length > 1 ? variables[1] : null);

        if (MapFeature.isMaterialSafe(hitLocation.getBlock().getType()) && variables.length < 3)
            hitLocation = null;

        //BlockDisplay blockDisplay = loc.getWorld().spawn(loc, BlockDisplay.class);
        //blockDisplay.setBrightness(new Display.Brightness(15, 15));
        //blockDisplay.setInterpolationDelay(-1);
        //blockDisplay.setInterpolationDuration(4);
        //blockDisplay.setBlock(Material.GREEN_STAINED_GLASS.createBlockData());

        createDisplayRings(loc, distance, 0.08F, 0.1F, 1, 0.15F, 0, 91, Material.LIME_STAINED_GLASS, 15, 8, 0.8, 3.3);

        float randomValue = 0.05f + (0.09f - 0.05f) * new Random().nextFloat();
        float randomRotation = new Random().nextInt(361);

        //blockDisplay.setTransformationMatrix(new Matrix4f().scale(randomValue, randomValue, (float) distance).transpose().translate(-0.5F + (randomValue / 4), -0.5F + (randomValue / 4), 0).rotateLocal(randomRotation, 0, 0, 1));
        //Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
        //     blockDisplay.setTransformationMatrix(new Matrix4f().scale(randomValue / 1.5F, randomValue / 1.5F, (float) distance).transpose().translate(-0.5F + (randomValue / 4), -0.5F + (randomValue / 4), 0).rotateLocal(randomRotation + new Random().nextInt(361), 0, 0, 1));
        //    Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
        //        blockDisplay.remove();
        //    }, blockDisplay.getInterpolationDuration());
        //}, 1);

        if (hitLocation != null) {
            int ranRot = new Random().nextInt(91);
            Location ploomLoc = hitLocation.clone();
            ploomLoc.setDirection(createHoleVector(hitLocation));

            double dmg = 3;

            if (variables.length == 3) {
                LivingEntity entity = (LivingEntity) variables[2];
                entity.damage(0);
                entity.setHealth(entity.getHealth() - (dmg/3) < 0 ? 0 : entity.getHealth() - (dmg/3));
                Vector vec = entity.getLocation().toVector().subtract(ploomLoc.toVector()).normalize().multiply(0.3);
                if (entity.getHealth() > 0)
                    entity.setVelocity(vec);
            } else {
                for (LivingEntity entity : ploomLoc.getNearbyLivingEntities(2)) {
                    Player player = (Player) ((entity instanceof Player) ? entity : null);
                    if (player == p || player == null) {
                        entity.damage(0);
                        entity.setHealth(entity.getHealth() - dmg < 0 ? 0 : entity.getHealth() - dmg);
                        Vector vec = entity.getLocation().toVector().subtract(ploomLoc.toVector()).normalize().multiply(0.3);
                        if (entity.getHealth() > 0)
                            entity.setVelocity(vec);
                    }
                }
            }

            createSpark(ploomLoc.clone(), ploomLoc.getDirection(), 180, 0.05F, 13, 3, 0.9F, 3, Material.LIME_STAINED_GLASS);

            if (variables.length != 3) {
                for (int rot : new int[]{0, 45}) {
                    BlockDisplay ploomDisplay = loc.getWorld().spawn(ploomLoc, BlockDisplay.class);
                    ploomDisplay.setBrightness(new Display.Brightness(14, 14));
                    ploomDisplay.setBlock(Material.YELLOW_STAINED_GLASS.createBlockData());
                    ploomDisplay.setTransformationMatrix(new Matrix4f().scale(1.5F, 1.5F, 0.1F).translate(-0.5F, -0.5F, 0).rotateLocalZ((float) Math.toRadians(ranRot + rot)));
                    ploomDisplay.setInterpolationDelay(-1);
                    ploomDisplay.setInterpolationDuration(3);
                    Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                        ploomDisplay.setBrightness(new Display.Brightness(11, 11));
                        ploomDisplay.setBlock(Material.GREEN_STAINED_GLASS.createBlockData());
                        ploomDisplay.setTransformationMatrix(new Matrix4f().scale(2.5F, 2.5F, 0.01F).translate(-0.5F, -0.5F, 0).rotateLocalZ((float) Math.toRadians(ranRot + rot)));
                        Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                            ploomDisplay.remove();
                        }, ploomDisplay.getInterpolationDuration());
                    }, 2);
                    ploomLoc.add(0, 0.001, 0);
                }
            }
        }

        return false;
    }

    public static void createDisplayRings(Location location, float distance, float ringLength1, float ringLength2, float gapLength, float ringSize1, float ringSize2, int ranRotMax, Material mat, int brightness, int ringDuration) {
        createDisplayRings(location, distance, ringLength1, ringLength2, gapLength, ringSize1, ringSize2, ranRotMax, mat, brightness, ringDuration, 0, 0);
    }

    public static void createDisplayRings(Location location, float distance, float ringLength1, float ringLength2, float gapLength, float ringSize1
            , float ringSize2, int ranRotMax, Material mat, int brightness, int ringDuration, double eclipseMin, double eclipseMax) {

        Location loc = location.clone();

        double sizeMin = eclipseMin;
        double sizeMax = eclipseMax;

        for (float i = 0; i <= distance; i += ringLength1 + gapLength) {

            float pSize = (float) (sizeMin + (sizeMax - sizeMin) * (1 + Math.cos(2 * Math.PI * i / distance)) / 2);

            if (sizeMin == 0 && sizeMax == 0)
                pSize = 1;

            float size = pSize;

            int ranRot = new Random().nextInt(ranRotMax);

            BlockDisplay ringDisplay = loc.getWorld().spawn(loc, BlockDisplay.class);
            ringDisplay.setBlock(mat.createBlockData());
            ringDisplay.setBrightness(new Display.Brightness(brightness, brightness));
            ringDisplay.setTransformationMatrix(new Matrix4f()
                    .scale(size * ringSize1, size * ringSize1, ringLength1)
                    .translate(-0.5F, -0.5F, 0)
            );
            ringDisplay.setInterpolationDelay(-1);
            ringDisplay.setInterpolationDuration(ringDuration);

            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                ringDisplay.setTransformationMatrix(new Matrix4f()
                        .scale(size * ringSize2, size * ringSize2, ringLength2)
                        .translate(-0.5F, -0.5F, 0)
                );
                Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                    ringDisplay.remove();
                }, ringDuration);
            }, 2);

            loc.add(loc.getDirection().normalize().multiply(ringLength1).add(loc.getDirection().normalize().multiply(gapLength)));

        }

    }

    public static boolean shootExplosive1(Player p, ItemStack item, boolean hasGravity, boolean hasTrail, boolean hasSmoke, boolean hasColour,
                                          boolean hasPropulsion, boolean canBounce, boolean pitchCheck, Material customTrail, float dmgRadius, float dmg, boolean hasSparks, boolean initialShot, boolean useBullet) {
        Item gun = Item.getItem(item);
        float bloom = gun.getBloom();

        boolean doReturn = useGunLogic(item, gun, initialShot, useBullet);
        if (doReturn)
            return true;

        Location loc = createWeaponLocation(p, bloom, false, gun.getRange());

        if (pitchCheck) {
            if (p.getPitch() >= 80 || p.getPitch() <= -80)
                loc.setDirection(p.getEyeLocation().getDirection());
        }

        Location adjustedLoc = loc.clone();
        adjustedLoc.setDirection(adjustedLoc.getDirection().normalize().multiply(3));

        Vector dir = adjustedLoc.getDirection();

        float grav = -0.08f;
        if (!hasGravity)
            grav = 0;

        float dep = 0.99F;
        if (hasPropulsion)
            dep = 1;

        float depletionRate = dep;
        float gravity = grav;

        new BukkitRunnable() {
            int i = 0;
            Location prevLoc = null;

            public void run() {

                Object[] variables1 = getLookingAtBlockSpot(adjustedLoc.clone(), p, (float) dir.length(), 0.001F);
                float distance = (float) variables1[0];
                Location hitLocation = (Location) (variables1.length > 1 ? variables1[1] : null);

                if (hitLocation != null && !MapFeature.isMaterialSafe(hitLocation.getBlock().getType()) || variables1.length == 3) {

                    double dirStr = dir.length();

                    dir.normalize();

                    Vector blockNormalVec = getBlockFaceDirection(hitLocation).getDirection();

                    hitLocation.getWorld().playSound(hitLocation, Sound.ITEM_SHIELD_BLOCK, 0.2F, 1.4F);
                    hitLocation.getWorld().playSound(hitLocation, Sound.ITEM_SHIELD_BLOCK, 0.1F, 1.8F);

                    if (canBounce && variables1.length != 3) {
                        if (variables1.length == 3) {
                            Location tL = ((Entity) variables1[2]).getLocation();
                            tL.setY(hitLocation.getY());
                            blockNormalVec = hitLocation.toVector().subtract(tL.toVector());
                        }
                        blockNormalVec.normalize();

                        double dotProduct = dir.dot(blockNormalVec);

                        Vector reflected = dir.subtract(blockNormalVec.clone().multiply(2 * dotProduct));

                        reflected.setX(reflected.getX() / 2);
                        reflected.setZ(reflected.getZ() / 2);

                        adjustedLoc.setDirection(reflected.multiply(dirStr).multiply(depletionRate));
                        adjustedLoc.set(hitLocation.getX(), hitLocation.getY(), hitLocation.getZ());
                        adjustedLoc.add(blockNormalVec.multiply(0.01));

                        dir.setY(dir.getY() + gravity);
                        dir.multiply(depletionRate);

                    } else {
                        boolean showPloom = !(variables1.length == 3);
                        createExplosion(adjustedLoc.clone(), blockNormalVec, 150, 20, showPloom, dmgRadius, dmg); //(getBlockFaceFromNormal(blockNormalVec) == BlockFace.DOWN || getBlockFaceFromNormal(blockNormalVec) == BlockFace.UP) ? true:false
                        cancel();
                    }

                } else {
                    if (i != 0) {
                        dir.setY(dir.getY() + gravity);
                        dir.multiply(depletionRate);
                        if (MapFeature.isMaterialSafe(adjustedLoc.clone().add(dir).getBlock().getType())) {
                            adjustedLoc.setDirection(dir);
                            adjustedLoc.add(dir);
                        } else {
                            dir.setY(dir.getY() - gravity);
                            dir.multiply(1.01);
                        }
                    }
                }

                if (hasSparks) {
                    createSpark(adjustedLoc.clone(), dir.clone().multiply(-1), 50, 0.02F, 1, 2, 0.2F, 5, Material.YELLOW_STAINED_GLASS);
                }

                if (hasTrail) {
                    if (prevLoc != null) {
                        Vector vector = adjustedLoc.toVector().subtract(prevLoc.toVector());
                        vector.normalize();
                        vector.multiply(adjustedLoc.getDirection().length());
                        prevLoc.setDirection(vector);
                        createTrail(prevLoc.clone(), hasColour, customTrail, 0.05F, 7, (float) dir.length());
                    } else {
                        createTrail(adjustedLoc.clone(), hasColour, customTrail, 0.05F, 7, (float) dir.length());
                    }
                    prevLoc = adjustedLoc.clone();
                }

                if (hasSmoke) {
                    for (float i = 0; i <= dir.length(); i += 0.5) {
                        Location particleLoc = adjustedLoc.clone().add(adjustedLoc.clone().getDirection().multiply(i));
                        //particleLoc.setYaw(new Random().nextInt(361));
                        //particleLoc.setPitch(new Random().nextInt(181));

                        BlockDisplay blockDisplay2 = particleLoc.getWorld().spawn(particleLoc, BlockDisplay.class);
                        blockDisplay2.setInterpolationDelay(-1);
                        int ranDura = 10 + new Random().nextInt(35);
                        blockDisplay2.setInterpolationDuration(ranDura);

                        blockDisplay2.setBrightness(new Display.Brightness(15, 15));

                        float ranScale = 0.1F + (float) new Random().nextInt(10) / (float) 100;

                        blockDisplay2.setTransformationMatrix(new Matrix4f()
                                .scale(ranScale, ranScale, ranScale)
                                .translate(-0.5F, -0.5F, -0.5F)
                                .rotateLocalX((float) Math.toRadians(new Random().nextInt(91)))
                                .rotateLocalY((float) Math.toRadians(new Random().nextInt(91)))
                                .rotateLocalZ((float) Math.toRadians(new Random().nextInt(91))));

                        int ran2 = new Random().nextInt(6);

                        Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                            float ranDir = ((float) new Random().nextInt(10)) / 10F;
                            blockDisplay2.setTransformationMatrix(new Matrix4f()
                                    .scaleLocal(0F, 0F, 0F)
                                    .translateLocal(-ranDir / 2 + ranDir, -ranDir / 2 + ranDir, -ranDir / 2 + ranDir)
                                    .rotateLocalX((float) Math.toRadians(new Random().nextInt(91)))
                                    .rotateLocalY((float) Math.toRadians(new Random().nextInt(91)))
                                    .rotateLocalZ((float) Math.toRadians(new Random().nextInt(91))));
                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                blockDisplay2.remove();
                            }, ranDura);
                        }, 1);

                        if (hasColour) {
                            blockDisplay2.setBrightness(new Display.Brightness(15, 15));
                            runColourArray(blockDisplay2);
                        } else {
                            blockDisplay2.setBrightness(new Display.Brightness(15, 15));
                            blockDisplay2.setBlock(Material.LIGHT_GRAY_STAINED_GLASS.createBlockData());
                        }

                    }
                }

                i++;

                if (i == gun.getRange() || dir.length() <= 0.01 || !MapFeature.isMaterialSafe(adjustedLoc.getBlock().getType())) {
                    boolean showPloom = false;
                    if (!MapFeature.isMaterialSafe(adjustedLoc.getBlock().getType())) {
                        adjustedLoc.setY(adjustedLoc.getBlock().getRelative(BlockFace.UP).getLocation().getY());
                        dir.setY(1);
                        dir.setX(0);
                        dir.setZ(0);
                        adjustedLoc.setDirection(dir);
                        showPloom = true;
                        dir.normalize();
                    }
                    createExplosion(adjustedLoc.clone(), adjustedLoc.getDirection(), hasPropulsion ? 140 : 500, 25, showPloom, dmgRadius, dmg);
                    cancel();
                }

            }
        }.runTaskTimer(ZUtils.plugin, 0L, 0L);

        return false;

        // USE RANGE TO DETERMINE WHEN TO EXPLODE

    }

    public static void createSpark(Location location, Vector dir, float bloom, float scale, int amount, int duration, float length, int lengthAmount, Material trail) {
        createSpark(location, dir, bloom, scale, amount, duration, length, lengthAmount, trail, null);
    }

    public static void createSpark(Location location, Vector dir, float bloom, float scale, int amount, int duration, float length, int lengthAmount, Material trail, @Nullable Player p) {

        for (int a = 1; a <= amount; a++) {
            Location loc = location.clone();

            loc.setDirection(dir);
            setBloom(loc, bloom);

            for (int l = 1; l <= lengthAmount; l++) {
                Location tLoc = loc.clone();
                Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                    createTrail(tLoc, false, trail, scale, duration, (float) loc.getDirection().normalize().multiply(length).length(), p);
                }, l);
                Vector vec = loc.getDirection();
                vec.setY(vec.getY() - 0.008);
                vec.normalize().multiply(length);
                loc.setDirection(vec);
                loc.add(vec);
            }
        }

    }

    public static void createExplosion(Location location, Vector dirVec, float bloom, int particleAmount, boolean showPloom, float dmgRadius, float dmg) {

        if (!MapFeature.isMaterialSafe(location.clone().add(0, -0.1, 0).getBlock().getType())) {
            dirVec.multiply(1);
        }

        createSpark(location.clone(), dirVec.clone(), 120, 0.04F, 15, 2, 2F, 3, Material.OCHRE_FROGLIGHT);

        location.getWorld().playSound(location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.3F, 0);
        location.getWorld().playSound(location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.7F, 0.5F);

        for (LivingEntity entity : location.getNearbyLivingEntities(dmgRadius)) {
            if (!(entity instanceof Player)) {
                Vector explosionVel = entity.getLocation().toVector().setY(location.getY()).subtract(location.toVector());
                explosionVel.add(new Vector(0, 1.5, 0));
                explosionVel.normalize();
                explosionVel.multiply(0.8F);
                entity.setVelocity(explosionVel);
                entity.getWorld().spawnParticle(Particle.BLOCK, entity.getLocation().add(0, 1, 0), 10, 0.3, 1, 0.3, Material.REDSTONE_BLOCK.createBlockData());
                new BukkitRunnable() {
                    int iteration = 0;

                    public void run() {

                        if (!entity.isDead())
                            entity.getWorld().spawnParticle(Particle.BLOCK, entity.getLocation().add(0, 1, 0), 2, 0.1, 0.1, 0.1, Material.REDSTONE_BLOCK.createBlockData());

                        if (iteration == 5 || entity.isDead())
                            cancel();

                        iteration++;
                    }
                }.runTaskTimer(ZUtils.plugin, 0L, 0L);
                entity.damage(0);
                entity.setHealth(entity.getHealth() - dmg >= 0 ? entity.getHealth() - dmg : 0);
            }
        }

        if (showPloom) {
            int ranRot = new Random().nextInt(91);
            Location ploomLoc = location.clone();
            ploomLoc.setDirection(dirVec);
            for (int rot : new int[]{0, 45}) {
                BlockDisplay blockDisplay = location.getWorld().spawn(ploomLoc, BlockDisplay.class);
                blockDisplay.setBrightness(new Display.Brightness(7, 7));
                blockDisplay.setBlock(Material.WHITE_STAINED_GLASS.createBlockData());
                blockDisplay.setTransformationMatrix(new Matrix4f().scale(1.5F, 1.5F, 0.1F).translate(-0.5F, -0.5F, 0).rotateLocalZ((float) Math.toRadians(ranRot + rot)));
                blockDisplay.setInterpolationDelay(-1);
                blockDisplay.setInterpolationDuration(3);
                Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                    blockDisplay.setBlock(Material.LIGHT_GRAY_STAINED_GLASS.createBlockData());
                    blockDisplay.setTransformationMatrix(new Matrix4f().scale(6, 6, 0.01F).translate(-0.5F, -0.5F, 0).rotateLocalZ((float) Math.toRadians(ranRot + rot)));
                    Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                        blockDisplay.remove();
                    }, blockDisplay.getInterpolationDuration());
                }, 1);
                ploomLoc.add(0, 0.001, 0);
            }
        }

        for (int i = 0; i <= particleAmount; i++) {
            Location loc = location.clone();
            loc.setDirection(dirVec);

            setBloom(loc, bloom);

            new BukkitRunnable() {
                Location loca = loc.clone();
                Vector vector = loc.getDirection().clone();
                int length = 0;

                public void run() {
                    boolean tC = createTrail(loca, true, null, 0.1F, 7, (float) loca.getDirection().length());

                    vector.multiply(0.96);
                    vector.setY(vector.getY() - 0.08);
                    loca.add(vector);
                    loca.setDirection(vector);

                    length++;
                    if (length == 5 || !tC)
                        cancel();
                }
            }.runTaskTimer(ZUtils.plugin, 0L, 0L);
        }

    }

    public static void setBloom(Location loc, float bloom) {

        Random rand = new Random();

        //float radius = bloom * (rand.nextFloat() * 0.5f + 0.5f);
        float radius = bloom * (float) Math.pow(rand.nextFloat(), 0.75f);

        float theta = rand.nextFloat() * 2 * (float) Math.PI;
        float phi = (float) Math.acos(2 * rand.nextFloat() - 1);

        float offsetX = radius * (float) Math.sin(phi) * (float) Math.cos(theta); // X offset for yaw
        float offsetY = radius * (float) Math.sin(phi) * (float) Math.sin(theta); // Y offset for yaw
        float offsetZ = radius * (float) Math.cos(phi); // Z offset for pitch

        Location placeholderLoc = loc.clone();

        placeholderLoc.setPitch(0);
        placeholderLoc.setYaw(placeholderLoc.getYaw() + 90);

        Location placeholderLoc1 = loc.clone();

        placeholderLoc1.setPitch(90 - loc.getPitch());
        placeholderLoc1.setYaw(placeholderLoc1.getYaw() + 180);

        Location placeholderLoc2 = loc.clone();

        placeholderLoc2.setPitch(180 - loc.getPitch());
        placeholderLoc2.setYaw(placeholderLoc1.getYaw() + 360);

        Location offsetLoc = loc.clone().add(loc.getDirection().multiply(100)).add(placeholderLoc1.getDirection().multiply(offsetY)).add(placeholderLoc.getDirection().multiply(offsetZ)).add(placeholderLoc2.getDirection().multiply(offsetX)); //.add(gunLoc1.getDirection()

        Vector offsetVec = offsetLoc.toVector().subtract(loc.toVector());

        offsetVec.normalize();
        offsetVec.multiply(1);
        loc.setDirection(offsetVec);

    }

    public static boolean addCooldown(ItemStack item, Item gun, boolean initialShot) {
        boolean addCooldown = false;

        if (initialShot) {
            if (Guns.shootCooldown.contains(item)) {
                return true;
            } else {
                addCooldown = true;
            }
        }

        if (addCooldown) {
            //gun.setDurability(item);
            Guns.shootCooldown.add(item);
            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                Guns.shootCooldown.remove(item);
            }, gun.getFireRate());
        }
        return false;
    }

    public static void useAmmo(ItemStack item, Item gun) {
        //if (showMuzzle) {
        //p.setCooldown(item, gun.getFireRate());
        //p.sendMessage(gun.getRange() + "");
        //p.sendMessage(gun.getBloom() + "");
        //p.sendMessage(gun.getMagSize() + "");
        //p.sendMessage(gun.getAmmo() + "");
        //p.sendMessage(gun.getKey() + "");
        //}
        gun.setAmmo((gun.getAmmo() - 1 < 0) ? 0 : gun.getAmmo() - 1);
        Guns.registeredGuns.remove(item);
        //gun.setItemMetaData(UseType.GUN, item, item.getItemMeta().getDisplayName());
        Guns.registeredGuns.put(item, gun);
    }

    public static boolean useGunLogic(ItemStack item, Item gun, boolean initialShot, boolean useBullet) {
        boolean doReturn = addCooldown(item, gun, initialShot);

        if (doReturn)
            return true;

        if (useBullet)
            useAmmo(item, gun);

        return false;
    }

    public static boolean shootGunTest1(Player p, ItemStack item, boolean useBullet, boolean showMuzzle, boolean initialShot) {

        Item gun = Item.getItem(item);
        float bloom = gun.getBloom();

        boolean doReturn = useGunLogic(item, gun, initialShot, useBullet);
        if (doReturn)
            return true;

        //float rF = 0.9f + (1.15f - 0.9f) * new Random().nextFloat();

        Location gunLoc = p.getEyeLocation().clone();

        gunLoc.setPitch(0);
        gunLoc.setYaw(gunLoc.getYaw() + 90);

        Location gunLoc1 = p.getEyeLocation().clone();

        gunLoc1.setPitch(90 - p.getEyeLocation().getPitch());
        gunLoc1.setYaw(gunLoc1.getYaw() + 180);

        boolean rightHand = p.getMainHand() == MainHand.RIGHT;

        //Location flashLoc = p.getEyeLocation().clone().add(gunLoc1.getDirection().multiply(0.212)).add(p.getEyeLocation().getDirection().multiply(0.95)).add(gunLoc.getDirection().multiply((rightHand) ? 0.465 : -0.465)); //.add(gunLoc1.getDirection().multiply(0.26) //.add(gunLoc1.getDirection().multiply(0.26)).add(p.getEyeLocation().getDirection().multiply(0.95)).add(gunLoc.getDirection().multiply(0.6).add(rotatedVec.multiply(0.2)

        Location flashLoc = createWeaponLocation(p, 0, true, 0);

        Location loc = createWeaponLocation(p, bloom, false, 100);
        Location loc1 = loc.clone();

        loc.getWorld().spawnParticle(Particle.SMOKE, loc.clone().add(p.getEyeLocation().getDirection().multiply(0.2)), 1, 0, 0, 0, 0.01);

        if (showMuzzle) {
            loc.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, loc, 2, 0, 0, 0, new Particle.DustTransition(Color.fromRGB(234, 255, 0), Color.fromRGB(199, 207, 114), 1F));

            createSpark(loc, loc.getDirection(), 100, 0.03F, 2, 2, 0.2F, 3, Material.YELLOW_STAINED_GLASS);

            int rotateFlash = new Random().nextInt(91);

            BlockDisplay muzzleFlash = loc.getWorld().spawn(flashLoc.clone().add(loc.getDirection().multiply(-0.2)), BlockDisplay.class);
            muzzleFlash.setBrightness(new Display.Brightness(15, 15));
            muzzleFlash.setBlock(Material.WHITE_STAINED_GLASS.createBlockData());
            muzzleFlash.setTransformationMatrix(new Matrix4f().translate(-0.09f, -0.09F, 0).scale(0.18F, 0.18F, 0.05F).rotateLocalZ(rotateFlash));

            BlockDisplay muzzleFlash1 = loc.getWorld().spawn(flashLoc.clone().add(loc.getDirection().multiply(-0.2)), BlockDisplay.class);
            muzzleFlash1.setBrightness(new Display.Brightness(15, 15));
            muzzleFlash1.setBlock(Material.WHITE_STAINED_GLASS.createBlockData());
            muzzleFlash1.setTransformationMatrix(new Matrix4f().translate(-0.09f, -0.09F, 0).scale(0.18F, 0.18F, 0.05F).rotateLocalZ(rotateFlash + 45));

            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                muzzleFlash.setBlock(Material.YELLOW_STAINED_GLASS.createBlockData());
                muzzleFlash1.setBlock(Material.YELLOW_STAINED_GLASS.createBlockData());
            }, 3);

            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                muzzleFlash.remove();
                muzzleFlash1.remove();
            }, 4);

            // LIGHT FLASH
            Location lightFlashLoc = p.getEyeLocation();
            if (lightFlashLoc.getBlock().getType() == Material.AIR) {
                lightFlashLoc.getBlock().setType(Material.LIGHT);
                Light light = (Light) lightFlashLoc.getBlock().getBlockData();
                light.setLevel(7);
                lightFlashLoc.getBlock().setBlockData(light);
                Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                    lightFlashLoc.getBlock().setType(Material.AIR);
                }, 2);
            }
        }

        Object[] variables = getLookingAtBlockSpot(loc, p, gun.getRange(), 0);
        float distance = (variables.length > 0 ? (float) variables[0] : gun.getRange());
        Location hitLocation = (Location) (variables.length > 1 ? variables[1] : null);

        if (MapFeature.isMaterialSafe(hitLocation.getBlock().getType()) && variables.length < 3)
            hitLocation = null;

        BlockDisplay display = (BlockDisplay) p.getWorld().spawnEntity(loc, EntityType.BLOCK_DISPLAY);
        BlockDisplay display1 = (BlockDisplay) p.getWorld().spawnEntity(loc1, EntityType.BLOCK_DISPLAY);

        display1.setBlock(Material.OCHRE_FROGLIGHT.createBlockData());
        display.setViewRange(256);
        display1.setViewRange(256);
        display.setBillboard(Display.Billboard.FIXED);
        display1.setBillboard(Display.Billboard.FIXED);
        display.setBrightness(new Display.Brightness(0, 0));
        display1.setBrightness(new Display.Brightness(15, 15));

        for (int b = 0; b <= 15; b++) {
            int brightness = b;
            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                if (!display.isDead())
                    display.setBrightness(new Display.Brightness(brightness, brightness));
            }, b);
        }

        //display.setTransformationMatrix(new Matrix4f().scale(0.1F, 0.1F, 0.1F).translate(-.5F, -.5F, 0).translate(0, 0, 0.1F));

        display.setInterpolationDuration(10); // 20 ticks = 1 second
        display1.setInterpolationDuration(2); // 20 ticks = 1 second
        display.setInterpolationDelay(1);  // 40

        float randomValue = 0.05f + (0.09f - 0.05f) * new Random().nextFloat();
        float randomRotation = new Random().nextInt(361);

        createDisplayRings(loc, distance, 1, 0.8F, 0.3F, randomValue + 0.03F, 0, 91, Material.LIGHT_GRAY_STAINED_GLASS, 10, 7 + new Random().nextInt(5), 1, 2);

        AxisAngle4f angle = new AxisAngle4f(new Random().nextInt(361), 0, 0, 1);

        display.setTransformationMatrix(new Matrix4f().scale(randomValue, randomValue, (float) distance).transpose().translate(-0.5F + (randomValue / 4), -0.5F + (randomValue / 4), 0).rotateLocal(randomRotation, 0, 0, 1));
        display1.setTransformationMatrix(new Matrix4f().scale(randomValue / 2, randomValue / 2, (float) distance).transpose().translate(-0.5F + (randomValue / 4), -0.5F + (randomValue / 4), 0));
        //if (e.getValue() != null)
        //p.getWorld().createExplosion(e.getValue(), Double.valueOf(args[0]).floatValue());

        Utils.scheduler.runTaskLater(Utils.plugin, () -> {
            display.setTransformationMatrix(new Matrix4f().scale(randomValue + 0.03F, randomValue + 0.03F, (float) distance).transpose().translate(-0.5F + (randomValue / 4), -0.5F + (randomValue / 4), 0).rotateLocal(randomRotation, 0, 0, 1));
        }, 3);

        BukkitTask task = null;

        if (variables.length == 3) {
            LivingEntity entity = (LivingEntity) variables[2];
            if (p != entity && entity.getHealth() > 0) {
                p.playSound(p, Sound.ENTITY_ARROW_HIT_PLAYER, 0.3F, 1.5F);
                entity.damage(0);
                entity.setHealth(entity.getHealth() > 5 ? entity.getHealth() - 5 : 0);
                p.getWorld().spawnParticle(Particle.BLOCK, hitLocation, 10, 0.2, 0.2, 0.2, Material.REDSTONE_BLOCK.createBlockData());
                loc.getWorld().spawnParticle(Particle.DUST_PILLAR, hitLocation, 1, 0.1, 0.1, 0.1, Material.REDSTONE_BLOCK.createBlockData());
            }
        }

        if (hitLocation != null && variables.length == 2) {
            Vector holeVec = createHoleVector(hitLocation);

            holeVec = holeVec.multiply(0.01);

            Location holeLocation = hitLocation.clone().add(holeVec);
            holeLocation.setDirection(holeVec);
            //holeLocation.setDirection(holeVec);

            Location lightFlashLoc = holeLocation;
            if (lightFlashLoc.getBlock().getType() == Material.AIR) {
                lightFlashLoc.getBlock().setType(Material.LIGHT);
                Light light = (Light) lightFlashLoc.getBlock().getBlockData();
                light.setLevel(4);
                lightFlashLoc.getBlock().setBlockData(light);
                Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                    lightFlashLoc.getBlock().setType(Material.AIR);
                }, 2);
            }

            p.getWorld().playSound(holeLocation, hitLocation.getBlock().getBlockSoundGroup().getBreakSound(), 0.02F, 0.9F);
            if (new Random().nextInt(30) == 9) {
                createSpark(holeLocation, holeVec, 100, 0.05F, 1, 2, 1.5F, 3 + new Random().nextInt(2), Material.OCHRE_FROGLIGHT);

                p.getWorld().playSound(hitLocation, Sound.ITEM_TRIDENT_RETURN, 1F, 1.7F);
                p.getWorld().playSound(hitLocation, Sound.ITEM_TRIDENT_RETURN, 1F, 2F);
                p.getWorld().playSound(hitLocation, Sound.ITEM_TRIDENT_HIT_GROUND, 1F, 2F);
            }

            p.getWorld().spawnParticle(Particle.BLOCK, holeLocation, 3, 0.1, 0.1, 0.1, hitLocation.getBlock().getType().createBlockData());
            loc.getWorld().spawnParticle(Particle.DUST_PILLAR, holeLocation, 1, 0.1, 0.1, 0.1, hitLocation.getBlock().getType().createBlockData());
            loc.getWorld().spawnParticle(Particle.FALLING_DUST, holeLocation, 2, 0.2, 0.2, 0.2, hitLocation.getBlock().getType().createBlockData());

            BlockDisplay holedisplay = (BlockDisplay) p.getWorld().spawnEntity(holeLocation, EntityType.BLOCK_DISPLAY);
            BlockDisplay holedisplay1 = (BlockDisplay) p.getWorld().spawnEntity(holeLocation.clone().add(holeVec.clone().multiply(0.99)), EntityType.BLOCK_DISPLAY);
            BlockDisplay holeDisplayPuff = (BlockDisplay) p.getWorld().spawnEntity(holeLocation, EntityType.BLOCK_DISPLAY);

            bulletHoles.add(holedisplay);
            bulletHoles.add(holedisplay1);

            holedisplay.setBlock(Material.GRAY_STAINED_GLASS.createBlockData());
            holedisplay1.setBlock(Material.BLACK_STAINED_GLASS.createBlockData());
            holedisplay.setViewRange(256);
            holedisplay1.setViewRange(256);
            holedisplay.setBillboard(Display.Billboard.FIXED);
            holedisplay1.setBillboard(Display.Billboard.FIXED);
            holedisplay.setBrightness(new Display.Brightness(2, 2));
            holedisplay1.setBrightness(new Display.Brightness(0, 0));
            holeDisplayPuff.setBrightness(new Display.Brightness(0, 0));

            int randomPuffRotation = new Random().nextInt(91);
            float randomSize = new Random().nextFloat(11) / 100; //0.25
            holedisplay.setTransformationMatrix(new Matrix4f().scale(0.16F + randomSize, 0.16F + randomSize, 0.01F).transpose().translate(-0.5F, -0.5F, 0).rotateLocalZ((float) Math.toRadians(randomPuffRotation)));
            holeDisplayPuff.setTransformationMatrix(new Matrix4f().scale(0).transpose().translate(-0.5F, -0.5F, 0));
            holedisplay1.setTransformationMatrix(new Matrix4f().scale(0.04F + randomSize, 0.04F + randomSize, 0.01F).transpose().translate(-0.5F, -0.5F, 0).rotateLocalZ((float) Math.toRadians(randomPuffRotation + new Random().nextInt(26))));

            holeDisplayPuff.setBlock(Material.GRAY_STAINED_GLASS.createBlockData());

            holeDisplayPuff.setInterpolationDuration(5); // 20 ticks = 1 second
            holeDisplayPuff.setInterpolationDelay(1);  // 40

            if (bulletHoles.size() > maxEntities) {
                for (Entity entity : bulletHoles) {
                    entity.remove();
                }
                bulletHoles.clear();
            }

            Utils.scheduler.runTaskLater(Utils.plugin, () -> {
                display.setBlock(Material.GRAY_STAINED_GLASS.createBlockData());
            }, 3);

            Utils.scheduler.runTaskLater(Utils.plugin, () -> {
                holeDisplayPuff.setTransformationMatrix(new Matrix4f().scale(1F, 1F, 0.6F).transpose().translate(-0.5F, -0.5F, 0));
                holeDisplayPuff.setBrightness(new Display.Brightness(15, 15));
            }, 3);

            Utils.scheduler.runTaskLater(Utils.plugin, () -> {
                holeDisplayPuff.remove();
            }, 6);

            Utils.scheduler.runTaskLater(Utils.plugin, () -> {
                bulletHoles.remove(holedisplay);
                bulletHoles.remove(holedisplay1);
                holedisplay.remove();
                holedisplay1.remove();
            }, 180 + new Random().nextInt(60)); //25
        }

        Utils.scheduler.runTaskLater(Utils.plugin, () -> {
            display.setBlock(Material.LIGHT_GRAY_STAINED_GLASS.createBlockData());
        }, 3);

        Utils.scheduler.runTaskLater(Utils.plugin, () -> {
            //display.setBlock(Material.WHITE_STAINED_GLASS.createBlockData());
        }, 7);

        Utils.scheduler.runTaskLater(Utils.plugin, () -> {
            display1.remove();
        }, 4);

        BukkitTask finalTask = task;
        Utils.scheduler.runTaskLater(Utils.plugin, () -> {
            display.remove();
            if (finalTask != null)
                finalTask.cancel();
        }, 8);

        return false;

    }

    public static BlockDisplay createBlock(Material mat, Location loc) {

        BlockDisplay blockDisplay = loc.getWorld().spawn(loc, BlockDisplay.class);
        blockDisplay.setBlock(mat.createBlockData());
        blockDisplay.setInterpolationDelay(1);
        blockDisplay.setBillboard(Display.Billboard.FIXED);
        blockDisplay.setBrightness(new Display.Brightness(15, 15));

        return blockDisplay;

    }

    public static Location createWeaponLocation(Player p, float bloom, boolean isFlash, float searchDistance) {
        Location gunLoc = p.getEyeLocation().clone();

        gunLoc.setPitch(0);
        gunLoc.setYaw(gunLoc.getYaw() + 90);

        Location gunLoc1 = p.getEyeLocation().clone();

        gunLoc1.setPitch(90 - p.getEyeLocation().getPitch());
        gunLoc1.setYaw(gunLoc1.getYaw() + 180);

        boolean rightHand = p.getMainHand() == MainHand.RIGHT;

        Location loc;

        loc = p.getEyeLocation().clone().add(gunLoc1.getDirection().multiply(0.26)).add(p.getEyeLocation().getDirection().multiply(0.95)).add(gunLoc.getDirection().multiply((rightHand) ? 0.6 : -0.6)); //.add(gunLoc1.getDirection().multiply(0.26) //.add(gunLoc1.getDirection().multiply(0.26)).add(p.getEyeLocation().getDirection().multiply(0.95)).add(gunLoc.getDirection().multiply(0.6).add(rotatedVec.multiply(0.2)

        if (isFlash)
            loc = p.getEyeLocation().clone().add(gunLoc1.getDirection().multiply(0.212)).add(p.getEyeLocation().getDirection().multiply(0.95)).add(gunLoc.getDirection().multiply((rightHand) ? 0.465 : -0.465)); //.add(gunLoc1.getDirection().multiply(0.26) //.add(gunLoc1.getDirection().multiply(0.26)).add(p.getEyeLocation().getDirection().multiply(0.95)).add(gunLoc.getDirection().multiply(0.6).add(rotatedVec.multiply(0.2)

        Location hitLocation = null;

        Object[] variables1 = getLookingAtBlockSpot(p.getEyeLocation(), p, searchDistance, 0);
        hitLocation = (Location) (variables1.length > 1 ? variables1[1] : null);

        if (hitLocation != null) { // run check
            Vector newVec = hitLocation.clone().toVector().subtract(loc.clone().toVector());
            loc.setDirection(newVec);
        }

        if (bloom > 0) {
            /**
             if (new Random().nextInt(5) == 0) {
             float pitch = loc.getPitch() - bloom / 2 + new Random().nextInt(bloom + 1);
             float yaw = loc.getYaw() - bloom / 2 + new Random().nextInt(bloom + 1);
             loc.setPitch(pitch);
             loc.setYaw(yaw);
             loc1.setPitch(pitch);
             loc1.setYaw(yaw);
             } else {
             float pitch = loc.getPitch() - bloom / 4 + new Random().nextInt(bloom / 2 + 1);
             float yaw = loc.getYaw() - bloom / 4 + new Random().nextInt(bloom / 2 + 1);
             loc.setPitch(pitch);
             loc.setYaw(yaw);
             loc1.setPitch(pitch);
             loc1.setYaw(yaw);
             }
             */
            Random rand = new Random();

            //float radius = bloom * (rand.nextFloat() * 0.5f + 0.5f);
            float radius = bloom * (float) Math.pow(rand.nextFloat(), 0.75f);

            float theta = rand.nextFloat() * 2 * (float) Math.PI;
            float phi = (float) Math.acos(2 * rand.nextFloat() - 1);

            //float offsetX = radius * (float)Math.sin(phi) * (float)Math.cos(theta); // X offset for yaw
            float offsetY = radius * (float) Math.sin(phi) * (float) Math.sin(theta); // Y offset for yaw
            float offsetZ = radius * (float) Math.cos(phi); // Z offset for pitch

            Location placeholderLoc = p.getEyeLocation().clone();

            placeholderLoc.setPitch(0);
            placeholderLoc.setYaw(placeholderLoc.getYaw() + 90);

            Location placeholderLoc1 = p.getEyeLocation().clone();

            placeholderLoc1.setPitch(90 - p.getEyeLocation().getPitch());
            placeholderLoc1.setYaw(placeholderLoc1.getYaw() + 180);

            Location offsetLoc = loc.clone().add(loc.getDirection().multiply(100)).add(placeholderLoc1.getDirection().multiply(offsetY)).add(placeholderLoc.getDirection().multiply(offsetZ)); //.add(gunLoc1.getDirection()

            Vector offsetVec = offsetLoc.toVector().subtract(loc.toVector());

            loc.setDirection(offsetVec);
        }

        return loc;
    }

    private static double getDistanceToFace(Location targetLocation, Location faceLocation) {
        return targetLocation.distance(faceLocation);
    }

    public static Object[] getLookingAtBlockSpot(Location loc, Player p, float range, float iterationAmount) {
        // Step 1: Get player's eye location and the direction they are looking
        Location eyeLocation = loc.clone();
        Vector direction = eyeLocation.getDirection().clone();

        DecimalFormat df = new DecimalFormat("0.00");

        // Step 2: Set maximum distance for the raytrace (you can adjust this as needed)
        float maxDistance = range; // Maximum distance you want to trace

        if (iterationAmount == 0)
            iterationAmount = 0.01F;

        // Step 4: Trace the line of sight and find the first non-air block with 0.01 increments
        for (float distance = iterationAmount; distance <= maxDistance; distance += iterationAmount) {
            // Calculate the current location the player is looking at with the smaller increment
            Location currentLocation = eyeLocation.clone().add(direction.clone().multiply(distance));

            for (LivingEntity entity : currentLocation.getNearbyLivingEntities(0.1, 0.1, 0.1)) {
                if (entity.getBoundingBox().contains(currentLocation.toVector())) {
                    if (entity != p && entity.getHealth() > 0) {
                        return new Object[]{distance, currentLocation, entity};
                    }
                }
            }

            // Step 5: Check if the block is solid (not air)
            if ((!MapFeature.isMaterialSafe(currentLocation.getBlock().getType())) || Double.valueOf(df.format(distance)) == maxDistance - 0.01) {
                // Add the exact distance and location to the HashMap
                return new Object[]{distance, currentLocation};
            }
        }

        // If no block is hit within the maximum distance, return an empty map
        return new Object[]{range};
    }

    public static Vector createHoleVector(Location hitLocation) {
        double distanceNorth = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(0, 0, -0.5)); // North face
        double distanceSouth = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(0, 0, 0.5)); // South face
        double distanceWest = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(-0.5, 0, 0)); // West face
        double distanceEast = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(0.5, 0, 0)); // East face
        double distanceUp = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(0, 0.5, 0)); // Up face
        double distanceDown = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(0, -0.5, 0)); // Down face

        // Find the face with the smallest distance
        double minDistance = Math.min(Math.min(Math.min(distanceNorth, distanceSouth), Math.min(distanceWest, distanceEast)), Math.min(distanceUp, distanceDown));

        Location faceLocation = null;
        float pitch = 0;
        float yaw = 0;

        // Return the corresponding face based on the smallest distance
        if (minDistance == distanceNorth) {
            faceLocation = hitLocation.clone().add(0, 0, -0.5);
            yaw = -180;
            pitch = 0;
        } else if (minDistance == distanceSouth) {
            faceLocation = hitLocation.clone().add(0, 0, 0.5);
            yaw = 0;
            pitch = 0;
        } else if (minDistance == distanceWest) {
            faceLocation = hitLocation.clone().add(-0.5, 0, 0);
            yaw = 90;
            pitch = 0;
        } else if (minDistance == distanceEast) {
            faceLocation = hitLocation.clone().add(0.5, 0, 0);
            yaw = -90;
            pitch = 0;
        } else if (minDistance == distanceUp) {
            faceLocation = hitLocation.clone().add(0, 0.5, 0);
            yaw = 0;
            pitch = -90;
        } else {
            faceLocation = hitLocation.clone().add(0, -0.5, 0);
            yaw = 0;
            pitch = 90;
        }

        hitLocation.setYaw(yaw);
        hitLocation.setPitch(pitch);
        Vector holeVec = hitLocation.getDirection();

        return holeVec;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();

        return arguments;
    }
}

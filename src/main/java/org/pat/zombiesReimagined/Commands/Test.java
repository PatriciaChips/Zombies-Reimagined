package org.pat.zombiesReimagined.Commands;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
import org.bukkit.*;
import org.bukkit.block.Block;
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

            p.sendMessage(Guns.registeredGuns.size() + "");

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

    public static void shootGunTest2(Player p) {

        float randomIncrease = 0.5f + (0.75f - 0.5f) * new Random().nextFloat();
        Location bulletLoc = p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().multiply(randomIncrease));

    }

    public static void shootExplosive1(Player p, ItemStack item, boolean isRocket) {
        Item gun = Item.getItem(item);
        float bloom = gun.getBloom();

        boolean addCooldown = false;

        if (Guns.shootCooldown.contains(item)) {
            return;
        } else {
            addCooldown = true;
        }

        gun.setAmmo((gun.getAmmo() - 1 < 0) ? 0 : gun.getAmmo() - 1);
        Guns.registeredGuns.remove(item);
        //gun.setItemMetaData(UseType.GUN, item, item.getItemMeta().getDisplayName());
        if (addCooldown) {
            //gun.setDurability(item);
            Guns.shootCooldown.add(item);
            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                Guns.shootCooldown.remove(item);
            }, gun.getFireRate());
        }
        Guns.registeredGuns.put(item, gun);

        Location loc = createWeaponLocation(p, bloom, false, gun.getRange());

        // GRENADE METHOD AKA REWRITE FOR BELOW METHOD USING NO SNOWBALLS
        // GRENADE TO BOUNCE OFF BLOCKS
        // EXPLODE ON ENTITY SETTING
        // PASS-THRU ENTITY SETTING
        // BOUNCE OFF ENTITY SETTING
        // USE RANGE TO DETERMINE WHEN TO EXPLODE
        // USE FIRST BLOCK DISPLAY TRAIL METHOD FROM BELOW METHOD (TO BEING WITH)
        // CREATE METHOD FOR EXPLOSION PARTICLES AKA QUICK PARTICLES WITH TRAIL DISPLAY AND A GLOWING BALL
        // ABOVE COMMENT TO BE CONSIDERED FOR BULLET RICOCHETS

    }

    public static void shootExplosive(Player p, ItemStack item, boolean isRocket) {

        Item gun = Item.getItem(item);
        float bloom = gun.getBloom();

        boolean addCooldown = false;

        if (Guns.shootCooldown.contains(item)) {
            return;
        } else {
            addCooldown = true;
        }

        gun.setAmmo((gun.getAmmo() - 1 < 0) ? 0 : gun.getAmmo() - 1);
        Guns.registeredGuns.remove(item);
        //gun.setItemMetaData(UseType.GUN, item, item.getItemMeta().getDisplayName());
        if (addCooldown) {
            //gun.setDurability(item);
            Guns.shootCooldown.add(item);
            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                Guns.shootCooldown.remove(item);
            }, gun.getFireRate());
        }
        Guns.registeredGuns.put(item, gun);

        Location loc = createWeaponLocation(p, bloom, false, gun.getRange());

        Snowball ball = loc.getWorld().spawn(loc, Snowball.class);
        ball.setVelocity(loc.getDirection().normalize().multiply(1.2));

        if (isRocket)
            ball.setGravity(false);

        ball.setInvulnerable(true);

        List<Entity> particles = new ArrayList<>();

        new BukkitRunnable() {
            public void run() {

                if (loc.distance(ball.getLocation()) >= gun.getRange())
                    ball.remove();

                Location dirLoc = ball.getLocation();
                dirLoc.setDirection(ball.getVelocity());
                dirLoc.add(0, 0.15, 0);

                /**
                 BlockDisplay blockDisplay1 = loc.getWorld().spawn(dirLoc, BlockDisplay.class);
                 blockDisplay1.setBlock(Material.LIGHT_GRAY_STAINED_GLASS.createBlockData());
                 blockDisplay1.setTransformationMatrix(new Matrix4f().scale(0.25f + (new Random().nextInt(15) / 100), 0.25F + (new Random().nextInt(15) / 100), (float) dirLoc.distance(dirLoc.clone().add(ball.getVelocity()))).translate(-0.5F, -0.5F, -(float) (dirLoc.distance(dirLoc.clone().add(ball.getVelocity())) / 2)));

                 int ran = new Random().nextInt(2);
                 int ran1 = new Random().nextInt(25);
                 */


                if (isRocket) {
                    float dis = (float) dirLoc.distance(dirLoc.clone().add(dirLoc.getDirection()));

                    for (float i = 0; i <= dis; i += 0.3) {
                        Location particleLoc = dirLoc.clone().add(dirLoc.getDirection().multiply(i));
                        //particleLoc.setYaw(new Random().nextInt(361));
                        //particleLoc.setPitch(new Random().nextInt(181));

                        BlockDisplay blockDisplay2 = particleLoc.getWorld().spawn(particleLoc, BlockDisplay.class);
                        particles.add(blockDisplay2);
                        blockDisplay2.setBlock(Material.OCHRE_FROGLIGHT.createBlockData());
                        blockDisplay2.setInterpolationDelay(-1);
                        int ranDura = 10 + new Random().nextInt(35);
                        blockDisplay2.setInterpolationDuration(ranDura);

                        blockDisplay2.setBrightness(new Display.Brightness(15, 15));

                        int c = 1;
                        for (Material colours : new Material[]{Material.YELLOW_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.RED_STAINED_GLASS, Material.GRAY_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS}) {
                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                if (colours == Material.LIGHT_GRAY_STAINED_GLASS) {
                                    for (int b = 0; b <= 15; b++) {
                                        int brightness = b;
                                        Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                            if (!blockDisplay2.isDead())
                                                blockDisplay2.setBrightness(new Display.Brightness(brightness, brightness));
                                        }, b);
                                    }
                                } else {
                                    for (int b = 15; b >= 0; b--) {
                                        int brightness = b;
                                        Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                            if (!blockDisplay2.isDead())
                                                blockDisplay2.setBrightness(new Display.Brightness(brightness, brightness));
                                        }, 15 - b);
                                    }
                                }
                                blockDisplay2.setBlock(colours.createBlockData());
                            }, c);
                            c++;
                        }

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
                                    .scale(0F, 0F, 0F)
                                    .translateLocal(-ranDir / 2 + ranDir, -ranDir / 2 + ranDir, -ranDir / 2 + ranDir)
                                    .rotateLocalX((float) Math.toRadians(new Random().nextInt(91)))
                                    .rotateLocalY((float) Math.toRadians(new Random().nextInt(91)))
                                    .rotateLocalZ((float) Math.toRadians(new Random().nextInt(91))));
                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                blockDisplay2.remove();
                            }, ranDura / 4);
                        }, 1);
                    }

                    //blockDisplay1.setInterpolationDelay(-1);
                    //blockDisplay1.setInterpolationDuration(7 + ran1);

                    //Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                    //    blockDisplay1.setTransformationMatrix(new Matrix4f().scale(0f, 0F, ((float) dirLoc.distance(dirLoc.clone().add(ball.getVelocity())) / 2)).translate(-0.5F, -0.5F, -(float) (dirLoc.distance(dirLoc.clone().add(ball.getVelocity())) / 4)));
                    //    Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                    //        blockDisplay1.remove();
                    //    }, 7 + ran1);
                    //}, 1 + ran);

                    if (ball.isDead()) {
                        loc.getWorld().createExplosion(dirLoc, 2F);
                        cancel();
                        for (Entity entity : particles) {
                            entity.remove();
                        }
                    }
                }

            }
        }.runTaskTimer(ZUtils.plugin, 0L, 0L);

    }

    public static boolean shootGunTest1(Player p, ItemStack item, boolean useBullet, boolean showMuzzle, boolean initialShot) {

        Item gun = Item.getItem(item);
        float bloom = gun.getBloom();

        boolean addCooldown = false;

        if (initialShot) {
            if (Guns.shootCooldown.contains(item)) {
                return true;
            } else {
                addCooldown = true;
            }
        }

        int ammo = gun.getAmmo();

        if (useBullet) {
            if (showMuzzle) {
                //p.setCooldown(item, gun.getFireRate());
                //p.sendMessage(gun.getRange() + "");
                //p.sendMessage(gun.getBloom() + "");
                //p.sendMessage(gun.getMagSize() + "");
                //p.sendMessage(gun.getAmmo() + "");
                //p.sendMessage(gun.getKey() + "");
            }
            gun.setAmmo((gun.getAmmo() - 1 < 0) ? 0 : gun.getAmmo() - 1);
            Guns.registeredGuns.remove(item);
            //gun.setItemMetaData(UseType.GUN, item, item.getItemMeta().getDisplayName());
            if (addCooldown) {
                //gun.setDurability(item);
                Guns.shootCooldown.add(item);
                Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                    Guns.shootCooldown.remove(item);
                }, gun.getFireRate());
            }
            Guns.registeredGuns.put(item, gun);
        }

        //float rF = 0.9f + (1.15f - 0.9f) * new Random().nextFloat();

        Location gunLoc = p.getEyeLocation().clone();

        gunLoc.setPitch(0);
        gunLoc.setYaw(gunLoc.getYaw() + 90);

        Location gunLoc1 = p.getEyeLocation().clone();

        gunLoc1.setPitch(90 - p.getEyeLocation().getPitch());
        gunLoc1.setYaw(gunLoc1.getYaw() + 180);

        boolean rightHand = p.getMainHand() == MainHand.RIGHT;

        Location flashLoc = p.getEyeLocation().clone().add(gunLoc1.getDirection().multiply(0.212)).add(p.getEyeLocation().getDirection().multiply(0.95)).add(gunLoc.getDirection().multiply((rightHand) ? 0.465 : -0.465)); //.add(gunLoc1.getDirection().multiply(0.26) //.add(gunLoc1.getDirection().multiply(0.26)).add(p.getEyeLocation().getDirection().multiply(0.95)).add(gunLoc.getDirection().multiply(0.6).add(rotatedVec.multiply(0.2)

        Location loc = createWeaponLocation(p, bloom, false, 100);
        Location loc1 = loc.clone();

        loc.getWorld().spawnParticle(Particle.SMOKE, loc.clone().add(p.getEyeLocation().getDirection().multiply(0.2)), 1, 0, 0, 0, 0.01);

        p.playSound(p, Sound.BLOCK_BAMBOO_WOOD_PLACE, 1, 1);
        p.playSound(p, Sound.BLOCK_STONE_PLACE, 1, 2);

        if (showMuzzle) {
            loc.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, loc, 2, 0, 0, 0, new Particle.DustTransition(Color.fromRGB(234, 255, 0), Color.fromRGB(199, 207, 114), 1F));

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

        Object[] variables = getLookingAtBlockSpot(loc, p, gun.getRange());
        float distance = (variables.length > 0 ? (float) variables[0] : gun.getRange());
        Location hitLocation = (Location) (variables.length > 1 ? variables[1] : null);

        if (hitLocation.getBlock().getType() == Material.AIR)
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

        AxisAngle4f angle = new AxisAngle4f(new Random().nextInt(361), 0, 0, 1);

        display.setTransformationMatrix(new Matrix4f().scale(randomValue, randomValue, (float) distance).transpose().translate(-0.5F + (randomValue / 4), -0.5F + (randomValue / 4), 0).rotateLocal(randomRotation, 0, 0, 1));
        display1.setTransformationMatrix(new Matrix4f().scale(randomValue / 2, randomValue / 2, (float) distance).transpose().translate(-0.5F + (randomValue / 4), -0.5F + (randomValue / 4), 0));
        //if (e.getValue() != null)
        //p.getWorld().createExplosion(e.getValue(), Double.valueOf(args[0]).floatValue());

        Utils.scheduler.runTaskLater(Utils.plugin, () -> {
            display.setTransformationMatrix(new Matrix4f().scale(randomValue + 0.03F, randomValue + 0.03F, (float) distance).transpose().translate(-0.5F + (randomValue / 4), -0.5F + (randomValue / 4), 0).rotateLocal(randomRotation, 0, 0, 1));
        }, 2);

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
            holedisplay.setBrightness(new Display.Brightness(15, 15));
            holedisplay1.setBrightness(new Display.Brightness(15, 15));
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
            }, 2);

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

        Object[] variables1 = getLookingAtBlockSpot(p.getEyeLocation(), p, searchDistance);
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

    public static Object[] getLookingAtBlockSpot(Location loc, Player p, float range) {
        // Step 1: Get player's eye location and the direction they are looking
        Location eyeLocation = loc;
        Vector direction = eyeLocation.getDirection();

        DecimalFormat df = new DecimalFormat("0.00");

        // Step 2: Set maximum distance for the raytrace (you can adjust this as needed)
        float maxDistance = range; // Maximum distance you want to trace

        // Step 4: Trace the line of sight and find the first non-air block with 0.01 increments
        for (float distance = 0.01F; distance <= maxDistance; distance += 0.01) {
            // Calculate the current location the player is looking at with the smaller increment
            Location currentLocation = eyeLocation.clone().add(direction.clone().multiply(distance));

            for (LivingEntity entity : currentLocation.getNearbyLivingEntities(0.5, 0.5, 0.5)) {
                if (entity.getBoundingBox().contains(currentLocation.toVector())) {
                    if (entity != p && entity.getHealth() > 0) {
                        return new Object[]{distance, currentLocation, entity};
                    }
                }
            }

            // Step 5: Check if the block is solid (not air)
            if ((currentLocation.getBlock().getType() != Material.AIR && currentLocation.getBlock().getType() != Material.LIGHT) || Double.valueOf(df.format(distance)) == maxDistance - 0.01) {
                // Add the exact distance and location to the HashMap
                return new Object[]{distance, currentLocation};
            }
        }

        // If no block is hit within the maximum distance, return an empty map
        return new Object[]{range};
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();

        return arguments;
    }
}

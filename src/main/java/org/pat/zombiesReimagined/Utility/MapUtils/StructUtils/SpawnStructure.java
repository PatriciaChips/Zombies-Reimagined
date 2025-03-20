package org.pat.zombiesReimagined.Utility.MapUtils.StructUtils;

import net.minecraft.util.Brightness;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.pat.pattyEssentialsV3.ColoredText;
import org.pat.zombiesReimagined.Utility.ItemUtils.Item;
import org.pat.zombiesReimagined.Utility.ItemUtils.UseType;
import org.pat.zombiesReimagined.Utility.MapUtils.IdentifiedStructures;
import org.pat.zombiesReimagined.Utility.MapUtils.MapFeature;
import org.pat.zombiesReimagined.Utility.ZUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpawnStructure {

    public static Display.Brightness textBrightness = new Display.Brightness(10, 10);

    public static void spawnStructures(PlayerInteractEvent e, Player p) {
        for (var featureMap : MapFeature.storedStructures.entrySet()) {
            MapFeature feature = featureMap.getKey();
            Location loc = featureMap.getValue().clone();

            switch (feature.getType()) {
                case UNLOCK_DOOR: {
                    loc.add(0, 3.3, 0);

                    TextDisplay nameText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(0.501)), TextDisplay.class);
                    nameText.setBrightness(textBrightness);
                    nameText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    nameText.setShadowed(true);
                    nameText.setBillboard(Display.Billboard.FIXED);
                    nameText.setText(feature.getName());
                    nameText.setAlignment(TextDisplay.TextAlignment.CENTER);
                    nameText.setTransformationMatrix(new Matrix4f().scaleLocal(2.5F, 2.5F, 2.5F));

                    TextDisplay nameText1 = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), TextDisplay.class);
                    nameText1.setBrightness(textBrightness);
                    nameText1.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    nameText1.setShadowed(true);
                    nameText1.setBillboard(Display.Billboard.FIXED);
                    nameText1.setText(feature.getName1());
                    nameText1.setAlignment(TextDisplay.TextAlignment.CENTER);
                    nameText1.setTransformationMatrix(new Matrix4f().scaleLocal(-2.5F, 2.5F, -2.5F));  // Flip horizontally

                    loc.add(0, -0.2, 0);
                    String underText = ColoredText.t("&7Click to purchase!");

                    TextDisplay nameUnderText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(0.501)), TextDisplay.class);
                    nameUnderText.setBrightness(textBrightness);
                    nameUnderText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    nameUnderText.setShadowed(true);
                    nameUnderText.setBillboard(Display.Billboard.FIXED);
                    nameUnderText.setText(underText);
                    nameUnderText.setAlignment(TextDisplay.TextAlignment.CENTER);
                    nameUnderText.setTransformationMatrix(new Matrix4f().scaleLocal(1F));

                    TextDisplay nameUnderText1 = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), TextDisplay.class);
                    nameUnderText1.setBrightness(textBrightness);
                    nameUnderText1.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    nameUnderText1.setShadowed(true);
                    nameUnderText1.setBillboard(Display.Billboard.FIXED);
                    nameUnderText1.setText(underText);
                    nameUnderText1.setAlignment(TextDisplay.TextAlignment.CENTER);
                    nameUnderText1.setTransformationMatrix(new Matrix4f().scaleLocal(-1F, 1F, -1F));  // Flip horizontally

                    loc.add(0, -0.5, 0);

                    TextDisplay costText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(0.501)), TextDisplay.class);
                    costText.setBrightness(textBrightness);
                    costText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    costText.setShadowed(true);
                    costText.setBillboard(Display.Billboard.FIXED);
                    costText.setText(ColoredText.t("&#29ab34$" + feature.getCost()));
                    costText.setAlignment(TextDisplay.TextAlignment.CENTER);
                    costText.setTransformationMatrix(new Matrix4f().scaleLocal(1.3F).translate(-0.021F, 0, 0));

                    TextDisplay costText1 = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), TextDisplay.class);
                    costText1.setBrightness(textBrightness);
                    costText1.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    costText1.setShadowed(true);
                    costText1.setBillboard(Display.Billboard.FIXED);
                    costText1.setText(ColoredText.t("&#29ab34$" + feature.getCost1()));
                    costText1.setAlignment(TextDisplay.TextAlignment.CENTER);
                    costText1.setTransformationMatrix(new Matrix4f().scaleLocal(-1.3F, 1.3F, -1.3F).translate(-0.021F, 0, 0));  // Flip horizontally

                    feature.setStructureEntities(List.of(new Entity[]{nameText, nameText1, nameUnderText, nameUnderText1, costText, costText1}));

                }
                break;
                case GUN: {
                    loc.add(0, 4, 0);

                    TextDisplay nameText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), TextDisplay.class);
                    nameText.setBrightness(textBrightness);
                    nameText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    nameText.setShadowed(true);
                    nameText.setBillboard(Display.Billboard.FIXED);
                    nameText.setText(feature.getName());
                    nameText.setAlignment(TextDisplay.TextAlignment.CENTER);
                    nameText.setTransformationMatrix(new Matrix4f().scale(-1.5F, 1.5F, -1.5F));

                    loc.add(0, -0.2, 0);

                    String underText = ColoredText.t("&7Click to purchase!");

                    TextDisplay nameUnderText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), TextDisplay.class);
                    nameUnderText.setBrightness(textBrightness);
                    nameUnderText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    nameUnderText.setShadowed(true);
                    nameUnderText.setBillboard(Display.Billboard.FIXED);
                    nameUnderText.setText(underText);
                    nameUnderText.setAlignment(TextDisplay.TextAlignment.CENTER);
                    nameUnderText.setTransformationMatrix(new Matrix4f().scale(-0.7F, 0.7F, -0.7F));

                    underText = ColoredText.t("&7Click to buy ammo!");

                    TextDisplay nameUnderTextBrought = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), TextDisplay.class);
                    nameUnderTextBrought.setBrightness(textBrightness);
                    nameUnderTextBrought.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    nameUnderTextBrought.setShadowed(true);
                    nameUnderTextBrought.setBillboard(Display.Billboard.FIXED);
                    nameUnderTextBrought.setText(underText);
                    nameUnderTextBrought.setAlignment(TextDisplay.TextAlignment.CENTER);
                    nameUnderTextBrought.setTransformationMatrix(new Matrix4f().scale(-0.7F, 0.7F, -0.7F));


                    loc.add(0, -0.5, 0);

                    ItemDisplay ammoModelPackless = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), ItemDisplay.class);
                    ammoModelPackless.setItemStack(new ItemStack(Material.PRISMARINE_CRYSTALS));
                    ammoModelPackless.setTransformationMatrix(new Matrix4f().scaleLocal(0.85F).translate(-0.5F, 0, 0));

                    ItemDisplay ammoModelPack = loc.getWorld().spawn(loc.clone().add(0, -0.45, 0).add(loc.getDirection().multiply(-0.501)), ItemDisplay.class);
                    ammoModelPack.setItemStack(new ItemStack(Material.PRISMARINE_CRYSTALS));
                    ammoModelPack.setTransformationMatrix(new Matrix4f().scaleLocal(0.75F));

                    ItemDisplay smallerModelPackless = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), ItemDisplay.class);
                    smallerModelPackless.setItemStack(new Item(UseType.GUN, feature.getName(), feature.getMaterial(), feature.getKey(), false).getItemStack());
                    smallerModelPackless.setTransformationMatrix(new Matrix4f().scaleLocal(0.85F).translate(0.5F, 0, 0));

                    ItemDisplay smallerModelPack = loc.getWorld().spawn(loc.clone().add(0, 0, 0).add(loc.getDirection().multiply(-0.501)), ItemDisplay.class);
                    smallerModelPack.setItemStack(new Item(UseType.GUN, feature.getName(), feature.getMaterial(), feature.getKey(), true).getItemStack());
                    smallerModelPack.setTransformationMatrix(new Matrix4f().scaleLocal(1.2F).translate(0, 0.4F, 0).rotateY((float) Math.toRadians(90)));

                    ItemDisplay gunItemPackless = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), ItemDisplay.class);
                    gunItemPackless.setItemStack(new Item(UseType.GUN, feature.getName(), feature.getMaterial(), feature.getKey(), false).getItemStack());

                    ItemDisplay gunItemPack = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), ItemDisplay.class);
                    gunItemPack.setItemStack(new Item(UseType.GUN, feature.getName(), feature.getMaterial(), feature.getKey(), true).getItemStack());
                    gunItemPack.setTransformationMatrix(new Matrix4f().scaleLocal(1.4F).translate(0, 0.2F, 0).rotateY((float) Math.toRadians(90)));

                    loc.add(0, -1.1, 0);

                    TextDisplay gunCostText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), TextDisplay.class);
                    gunCostText.setBrightness(textBrightness);
                    gunCostText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    gunCostText.setShadowed(true);
                    gunCostText.setBillboard(Display.Billboard.FIXED);
                    gunCostText.setText(ColoredText.t("&#29ab34$" + feature.getCost()));
                    gunCostText.setAlignment(TextDisplay.TextAlignment.CENTER);
                    gunCostText.setTransformationMatrix(new Matrix4f().scale(-1.3F, 1.3F, -1.3F).translate(-0.021F, 0, 0));

                    TextDisplay ammoCostText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), TextDisplay.class);
                    ammoCostText.setBrightness(textBrightness);
                    ammoCostText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    ammoCostText.setShadowed(true);
                    ammoCostText.setBillboard(Display.Billboard.FIXED);
                    ammoCostText.setText(ColoredText.t("&#29ab34$" + feature.getCost1()));
                    ammoCostText.setAlignment(TextDisplay.TextAlignment.CENTER);
                    ammoCostText.setTransformationMatrix(new Matrix4f().scale(-1.3F, 1.3F, -1.3F).translate(-0.021F, 0, 0));

                    feature.setStructureEntities(List.of(new Entity[]{gunItemPack, gunItemPackless, nameText, nameUnderText, nameUnderTextBrought, smallerModelPack, gunCostText, ammoCostText, ammoModelPackless, ammoModelPack, smallerModelPackless}));
                    feature.setPreHiddenEntities(List.of(new Entity[]{nameUnderTextBrought, ammoCostText, ammoModelPackless, smallerModelPackless, ammoModelPack, smallerModelPack}));
                    feature.setToBEHiddenEntities(List.of(new Entity[]{nameUnderText, gunCostText, gunItemPackless, gunItemPack}));
                    feature.setPackEnabledEntities(List.of(new Entity[]{gunItemPack, ammoModelPack, smallerModelPack}));

                    feature.statusVisibilitySwap(false);

                    break;
                }
                case ARMOR: {
                    loc.add(0, 4, 0);

                    TextDisplay nameText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), TextDisplay.class);
                    nameText.setBrightness(textBrightness);
                    nameText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    nameText.setShadowed(true);
                    nameText.setBillboard(Display.Billboard.FIXED);
                    nameText.setText(feature.getName());
                    nameText.setAlignment(TextDisplay.TextAlignment.CENTER);
                    nameText.setTransformationMatrix(new Matrix4f().scale(-1.5F, 1.5F, -1.5F));

                    loc.add(0, -0.2, 0);

                    String underText = ColoredText.t("&7Click to purchase!");

                    TextDisplay nameUnderText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), TextDisplay.class);
                    nameUnderText.setBrightness(textBrightness);
                    nameUnderText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    nameUnderText.setShadowed(true);
                    nameUnderText.setBillboard(Display.Billboard.FIXED);
                    nameUnderText.setText(underText);
                    nameUnderText.setAlignment(TextDisplay.TextAlignment.CENTER);
                    nameUnderText.setTransformationMatrix(new Matrix4f().scale(-0.7F, 0.7F, -0.7F));

                    underText = ColoredText.t("&7Already purchased!");

                    TextDisplay nameUnderTextBrought = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), TextDisplay.class);
                    nameUnderTextBrought.setBrightness(textBrightness);
                    nameUnderTextBrought.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    nameUnderTextBrought.setShadowed(true);
                    nameUnderTextBrought.setBillboard(Display.Billboard.FIXED);
                    nameUnderTextBrought.setText(underText);
                    nameUnderTextBrought.setAlignment(TextDisplay.TextAlignment.CENTER);
                    nameUnderTextBrought.setTransformationMatrix(new Matrix4f().scale(-0.7F, 0.7F, -0.7F));

                    loc.add(0, -0.5, 0);

                    ItemStack[] itemModels = IdentifiedStructures.convertMaterialToArmour(feature.getMaterial());

                    ItemDisplay topModel = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), ItemDisplay.class);
                    topModel.setItemStack(((feature.isBottomHalf()) ? itemModels[1] : itemModels[3]));
                    topModel.setTransformationMatrix(new Matrix4f().scaleLocal(0.85F).translate(-0.5F, 0, 0));

                    ItemDisplay bottomModel = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), ItemDisplay.class);
                    bottomModel.setItemStack(((feature.isBottomHalf()) ? itemModels[0] : itemModels[2]));
                    bottomModel.setTransformationMatrix(new Matrix4f().scaleLocal(0.85F).translate(0.5F, 0, 0));

                    loc.add(0, -1.1, 0);

                    TextDisplay costText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), TextDisplay.class);
                    costText.setBrightness(textBrightness);
                    costText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    costText.setShadowed(true);
                    costText.setBillboard(Display.Billboard.FIXED);
                    costText.setText(ColoredText.t("&#29ab34$" + feature.getCost()));
                    costText.setAlignment(TextDisplay.TextAlignment.CENTER);
                    costText.setTransformationMatrix(new Matrix4f().scale(-1.3F, 1.3F, -1.3F).translate(-0.021F, 0, 0));

                    TextDisplay purchasedText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), TextDisplay.class);
                    purchasedText.setBrightness(textBrightness);
                    purchasedText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    purchasedText.setShadowed(true);
                    purchasedText.setBillboard(Display.Billboard.FIXED);
                    purchasedText.setText(ColoredText.t("&#b52424" + "Purchased!"));
                    purchasedText.setAlignment(TextDisplay.TextAlignment.CENTER);
                    purchasedText.setTransformationMatrix(new Matrix4f().scale(-1.3F, 1.3F, -1.3F).translate(-0.021F, 0, 0));

                    feature.setStructureEntities(List.of(new Entity[]{topModel, bottomModel, nameUnderText, nameText, costText, nameUnderTextBrought, purchasedText}));
                    feature.setPreHiddenEntities(List.of(new Entity[]{purchasedText, nameUnderTextBrought}));
                    feature.setToBEHiddenEntities(List.of(new Entity[]{costText, nameUnderText}));

                    feature.statusVisibilitySwap(false);

                }
                break;
                case CHEST: {
                    loc.add(0, 2.8, 0);

                    float f = 0.9F;

                    TextDisplay nameText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(f)), TextDisplay.class);
                    nameText.setBrightness(new Display.Brightness(15, 15));
                    nameText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    nameText.setShadowed(true);
                    nameText.setBillboard(Display.Billboard.FIXED);
                    //nameText.setText();

                    String[] animatedText = new String[]{ //#FFDF86 #FFC115 //#FFCD42
                            "&#FFC115&lL&#FFC115&lu&#FFC115&lc&#FFC115&lk&#FFC115&ly &#FFC115&lC&#FFC115&lh&#FFC115&le&#FFC115&ls&#FFC115&lt",
                            "&#FFC115&lL&#FFC115&lu&#FFC115&lc&#FFC115&lk&#FFC115&ly &#FFC115&lC&#FFC115&lh&#FFC115&le&#FFC115&ls&#FFC115&lt",
                            "&#FFC115&lL&#FFC115&lu&#FFC115&lc&#FFC115&lk&#FFC115&ly &#FFC115&lC&#FFC115&lh&#FFC115&le&#FFC115&ls&#FFC115&lt",
                            "&#FFCD42&lL&#FFC933&lu&#FFC524&lc&#FFC115&lk&#FFC115&ly &#FFC115&lC&#FFC115&lh&#FFC115&le&#FFC115&ls&#FFC115&lt",
                            "&#FFDF86&lL&#FFD560&lu&#FFCB3B&lc&#FFC115&lk&#FFC115&ly &#FFC115&lC&#FFC115&lh&#FFC115&le&#FFC115&ls&#FFC115&lt",
                            "&#FFC115&lL&#FFCB3B&lu&#FFD560&lc&#FFDF86&lk&#FFD04E&ly &#FFC115&lC&#FFC115&lh&#FFC115&le&#FFC115&ls&#FFC115&lt",
                            "&#FFC115&lL&#FFC115&lu&#FFC115&lc&#FFC115&lk&#FFD04E&ly &#FFD04E&lC&#FFC115&lh&#FFC115&le&#FFC115&ls&#FFC115&lt",
                            "&#FFC115&lL&#FFC115&lu&#FFC115&lc&#FFC115&lk&#FFC115&ly &#FFD04E&lC&#FFDF86&lh&#FFD560&le&#FFCB3B&ls&#FFC115&lt",
                            "&#FFC115&lL&#FFC115&lu&#FFC115&lc&#FFC115&lk&#FFC115&ly &#FFC115&lC&#FFC115&lh&#FFCB3B&le&#FFD560&ls&#FFDF86&lt",
                            "&#FFC115&lL&#FFC115&lu&#FFC115&lc&#FFC115&lk&#FFC115&ly &#FFC115&lC&#FFC115&lh&#FFC524&le&#FFC933&ls&#FFCD42&lt"
                    };

                    new BukkitRunnable() {
                        int i = 0;

                        public void run() {
                            if (nameText == null)
                                cancel();

                            nameText.setText(ColoredText.t(animatedText[i]));
                            i++;
                            if (i > animatedText.length - 1)
                                i = 0;
                        }
                    }.runTaskTimer(ZUtils.plugin, 0L, 5L);
                    nameText.setAlignment(TextDisplay.TextAlignment.CENTER);
                    nameText.setTransformationMatrix(new Matrix4f().scaleLocal(-1.5F, 1.5F, -1.5F).rotateLocalX(100.4F).translate(0, 0.07F, 0));

                    TextDisplay nameUnderText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(f)), TextDisplay.class);
                    nameUnderText.setBrightness(textBrightness);
                    nameUnderText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    nameUnderText.setShadowed(true);
                    nameUnderText.setText(ColoredText.t("&7Click to roll for a gun!"));
                    nameUnderText.setBillboard(Display.Billboard.FIXED);
                    nameUnderText.setAlignment(TextDisplay.TextAlignment.CENTER);
                    nameUnderText.setTransformationMatrix(new Matrix4f().scaleLocal(-0.8F, 0.8F, -0.8F).rotateLocalX(100.4F).translate(0, -0.14F, 0));

                    TextDisplay nameUnderTextRolling = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(f)), TextDisplay.class);
                    nameUnderTextRolling.setBrightness(textBrightness);
                    nameUnderTextRolling.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    nameUnderTextRolling.setShadowed(true);
                    nameUnderTextRolling.setText(ColoredText.t("&7 "));
                    nameUnderTextRolling.setBillboard(Display.Billboard.FIXED);
                    nameUnderTextRolling.setAlignment(TextDisplay.TextAlignment.CENTER);
                    nameUnderTextRolling.setTransformationMatrix(new Matrix4f().scaleLocal(-0.8F, 0.8F, -0.8F).rotateLocalX(100.4F).translate(0, -0.14F, 0));

                    BlockDisplay nameBackboardleft = p.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(f + 0.01)), BlockDisplay.class);
                    nameBackboardleft.setBlock(Material.TUFF_BRICKS.createBlockData());
                    nameBackboardleft.setTransformationMatrix(new Matrix4f().scaleLocal(1.5F, 0.7F, 0.3F).translate(-1F, -0.25F, 0).rotateLocalX(100.4F));

                    BlockDisplay nameBackboardright = p.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(f + 0.01)), BlockDisplay.class);
                    nameBackboardright.setBlock(Material.TUFF_BRICKS.createBlockData());
                    nameBackboardright.setTransformationMatrix(new Matrix4f().scaleLocal(1.5F, 0.7F, 0.3F).translate(0, -0.25F, 0).rotateLocalX(100.4F));

                    BlockDisplay nameBackboardStandleft = p.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(f + 0.01)), BlockDisplay.class);
                    nameBackboardStandleft.setBlock(Material.TUFF_BRICKS.createBlockData());
                    nameBackboardStandleft.setTransformationMatrix(new Matrix4f().scaleLocal(0.4F, 0.55F, 0.7F).translate(-2.51F, -0.1F, 0.1F).rotateLocalX(100.6F));

                    BlockDisplay nameBackboardStandright = p.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(f + 0.01)), BlockDisplay.class);
                    nameBackboardStandright.setBlock(Material.TUFF_BRICKS.createBlockData());
                    nameBackboardStandright.setTransformationMatrix(new Matrix4f().scaleLocal(0.4F, 0.55F, 0.7F).translate(1.51F, -0.1F, 0.1F).rotateLocalX(100.6F));

                    BlockDisplay nameBackboardStandChainleft = p.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(f + 0.01)), BlockDisplay.class);
                    nameBackboardStandChainleft.setBlock(Material.CHAIN.createBlockData());
                    nameBackboardStandChainleft.setTransformationMatrix(new Matrix4f().scaleLocal(0.8F).translate(0.15F, -0.7F, -0.3F).rotateLocalX(0F));

                    BlockDisplay nameBackboardStandChainright = p.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(f + 0.01)), BlockDisplay.class);
                    nameBackboardStandChainright.setBlock(Material.CHAIN.createBlockData());
                    nameBackboardStandChainright.setTransformationMatrix(new Matrix4f().scaleLocal(0.8F).translate(-1.15F, -0.7F, -0.3F).rotateLocalX(0F));

                    BlockDisplay hangingSign = p.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(f + 0.01)), BlockDisplay.class);
                    hangingSign.setBlock(Material.STRIPPED_OAK_WOOD.createBlockData());
                    hangingSign.setTransformationMatrix(new Matrix4f().scaleLocal(1.5F, 0.45F, 0.13F).translate(-0.5F, -1.7F, 0.7F).rotateLocalX(0F));

                    ItemDisplay itemDisplayPackless = p.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(f + 0.01)), ItemDisplay.class);
                    itemDisplayPackless.setItemStack(new Item(UseType.GUN, "minigun", Material.IRON_HOE, "deagle", false).getItemStack());
                    itemDisplayPackless.setTransformationMatrix(new Matrix4f().scale(0.3F).translate(0.08F, -4.4F, 0.13F).rotateZ(79.318F));
                    //itemDisplayPackless.setTransformationMatrix(new Matrix4f().scale(1.3F).translate(0.08F, -0.4F, 0.13F).rotateZ(79.318F));

                    ItemDisplay itemDisplayPack = p.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(f + 0.01)), ItemDisplay.class);
                    itemDisplayPack.setItemStack(new Item(UseType.GUN, "minigun", Material.IRON_HOE, "minigun", true).getItemStack());
                    itemDisplayPack.setTransformationMatrix(new Matrix4f().scaleLocal(0.3F).translate(-0.13F, -4.3F, 0F).rotateLocalY((float) Math.toRadians(90)));
                    //itemDisplayPack.setTransformationMatrix(new Matrix4f().scaleLocal(1.5F).translate(-0.13F, -0.15F, 0F).rotateLocalY((float) Math.toRadians(90)));

                    loc.add(0, -0.7, 0);

                    TextDisplay costText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(f + 0.1)), TextDisplay.class);
                    costText.setBrightness(textBrightness);
                    costText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    costText.setShadowed(true);
                    costText.setText(ColoredText.t("&#29ab34$" + feature.getCost()));
                    costText.setBillboard(Display.Billboard.FIXED);
                    costText.setAlignment(TextDisplay.TextAlignment.CENTER);
                    costText.setTransformationMatrix(new Matrix4f().scaleLocal(-1.3F, 1.3F, -1.3F).translate(-0.021F, 0, 0));

                    feature.setStructureEntities(List.of(new Entity[]{itemDisplayPack, nameUnderTextRolling, itemDisplayPackless, costText, hangingSign, nameUnderText, nameText, nameBackboardleft, nameBackboardright, nameBackboardStandleft, nameBackboardStandright, nameBackboardStandChainleft, nameBackboardStandChainright}));
                    feature.setPreHiddenEntities(List.of(new Entity[]{itemDisplayPackless, itemDisplayPack, nameUnderTextRolling}));
                    feature.setToBEHiddenEntities(List.of(new Entity[]{hangingSign, costText, nameUnderText}));
                    feature.setPackEnabledEntities(List.of(new Entity[]{itemDisplayPack}));

                    feature.statusVisibilitySwap(false);

                }
                break;
                case WINDOW: {
                    List<Entity> fogEntities = new ArrayList<>();
                    BlockDisplay smogDisplay = loc.getWorld().spawn(loc.clone().add(loc.getDirection().normalize()), BlockDisplay.class);
                    smogDisplay.setBlock(Material.GREEN_STAINED_GLASS.createBlockData());
                    smogDisplay.setBrightness(new Display.Brightness(12, 12));
                    smogDisplay.setTransformationMatrix(new Matrix4f()
                            .scale(3.1F, 0.4F, 6.5F)
                            .translate(-0.5F, 3.75F, -0.05F));
                    fogEntities.add(smogDisplay);
                    for (Block block : feature.getExtraBlocks()) {
                        if (block.getType() == Material.BLACK_CONCRETE_POWDER) { // SPAWN LOCATION

                        }
                    }
                    for (double i = 2; i <= 6; i += 0.7F) {
                        BlockDisplay blockDisplay = loc.getWorld().spawn(loc.clone().add(loc.getDirection().normalize().multiply(i)), BlockDisplay.class);
                        blockDisplay.setBlock(Material.BLACK_STAINED_GLASS.createBlockData());
                        blockDisplay.setTransformationMatrix(new Matrix4f()
                                .scale(3.1F, 3.5F, 0.1F)
                                .translate(-0.5F, 0.25F, 0));
                        fogEntities.add(blockDisplay);
                    }
                    feature.setStructureEntities(fogEntities);
                }
                break;
                case POWERUP: {
                    for (Block block : feature.getExtraBlocks()) {
                        if (Tag.BUTTONS.isTagged(block.getType())) {
                            Switch button = (Switch) block.getBlockData();
                            Vector vec = blockFaceToVector(button.getFacing(), button.getAttachedFace());
                            Location location = block.getLocation();
                            location.setDirection(vec);
                            vec.normalize().multiply(-0.55);

                            BlockDisplay offButton = block.getWorld().spawn(location.clone().add(0.5, 0.5, 0.5).add(vec), BlockDisplay.class);
                            offButton.setBrightness(new Display.Brightness(15, 15));
                            offButton.setTransformationMatrix(new Matrix4f()
                                    .scale(0.51F, 0.38F, 0.1F)
                                    .translate(-0.5F, -0.5F, 0));
                            offButton.setBlock(Material.RED_STAINED_GLASS.createBlockData());

                            BlockDisplay onButton = block.getWorld().spawn(location.clone().add(0.5, 0.5, 0.5).add(vec), BlockDisplay.class);
                            onButton.setBrightness(new Display.Brightness(15, 15));
                            onButton.setTransformationMatrix(new Matrix4f()
                                    .scale(0.51F, 0.38F, 0.1F)
                                    .translate(-0.5F, -0.5F, 0));
                            onButton.setBlock(Material.LIME_STAINED_GLASS.createBlockData());

                            feature.setStructureEntities(List.of(new Entity[]{onButton, offButton}));
                            feature.setPreHiddenEntities(onButton);
                            feature.setToBEHiddenEntities(offButton);
                        }
                    }

                    loc.add(0, 2.5, 0).add(loc.getDirection().normalize().multiply(-0.7));

                    boolean isClear = MapFeature.isMaterialSafe(featureMap.getValue().clone().add(0, 1, 0).getBlock().getType());

                    if (!isClear)
                        loc.add(0, 0.5, 0);

                    BlockDisplay nameBackboardStandChainleft = p.getWorld().spawn(loc.clone().add(loc.getDirection()), BlockDisplay.class);
                    nameBackboardStandChainleft.setBlock(Material.CHAIN.createBlockData());
                    nameBackboardStandChainleft.setTransformationMatrix(new Matrix4f()
                            .scale(0.8F)
                            .translate((isClear) ? 0.1F: 0.05F, (isClear) ? -0.3F:-0.3F, (isClear) ? -0.25F:0.02F)
                            .rotateLocalX((float) Math.toRadians((isClear) ? 18:45))
                            .rotateLocalZ((float) Math.toRadians((isClear) ? -15L:13)));

                    BlockDisplay nameBackboardStandChainright = p.getWorld().spawn(loc.clone().add(loc.getDirection()), BlockDisplay.class);
                    nameBackboardStandChainright.setBlock(Material.CHAIN.createBlockData());
                    nameBackboardStandChainright.setTransformationMatrix(new Matrix4f()
                            .scale(0.8F)
                            .translate((isClear) ? -1.1F: -1.05F, (isClear) ? -0.3F:-0.3F, (isClear) ? -0.25F:0.02F)
                            .rotateLocalX((float) Math.toRadians((isClear) ? 18:45))
                            .rotateLocalZ((float) Math.toRadians((isClear) ? 15:-13)));

                    BlockDisplay hangingSign = p.getWorld().spawn(loc.clone().add(loc.getDirection()), BlockDisplay.class);
                    hangingSign.setBlock(Material.STRIPPED_OAK_WOOD.createBlockData());
                    hangingSign.setTransformationMatrix(new Matrix4f()
                            .scale(1.15F, 0.45F, 0.13F)
                            .translate(-0.5F, -1.7F, 0.7F)
                            .rotateLocalX((float) Math.toRadians(4)));

                    loc.setYaw(loc.getYaw() + 180);

                    TextDisplay costText = loc.getWorld().spawn(loc.clone(), TextDisplay.class);
                    costText.setBrightness(textBrightness);
                    costText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    costText.setShadowed(true);
                    costText.setText(ColoredText.t("&#29ab34$" + feature.getCost()));
                    costText.setBillboard(Display.Billboard.FIXED);
                    costText.setAlignment(TextDisplay.TextAlignment.CENTER);
                    costText.setTransformationMatrix(new Matrix4f()
                            .scale(1.5F)
                            .translate(-0.021F, -0.44375F, -0.725F)
                            .rotateLocalX((float) Math.toRadians(-4)));

                    loc.add(0, (isClear) ? 2.4:1.4, 0);

                    BlockDisplay lanternOn = p.getWorld().spawn(loc.clone().add(loc.getDirection()), BlockDisplay.class);
                    Lantern lanternOnData = (Lantern) Material.LANTERN.createBlockData();
                    lanternOnData.setHanging(true);
                    lanternOn.setBlock(lanternOnData);
                    lanternOn.setBrightness(new Display.Brightness(15, 15));
                    lanternOn.setTransformationMatrix(new Matrix4f()
                            .scale(1)
                            .translate(-0.5F, 0, -3.2F)
                            .rotateLocalZ((float) Math.toRadians(180)));

                    BlockDisplay lanternOff = p.getWorld().spawn(loc.clone().add(loc.getDirection()), BlockDisplay.class);
                    Lantern lanternOffData = (Lantern) Material.SOUL_LANTERN.createBlockData();
                    lanternOffData.setHanging(true);
                    lanternOff.setBlock(lanternOffData);
                    lanternOff.setTransformationMatrix(new Matrix4f()
                            .scale(1)
                            .translate(-0.5F, 0, -3.2F)
                            .rotateLocalZ((float) Math.toRadians(180)));

                    BlockDisplay lanternGlass = p.getWorld().spawn(loc.clone().add(loc.getDirection()), BlockDisplay.class);
                    lanternGlass.setBlock(Material.GLASS.createBlockData());
                    lanternGlass.setTransformationMatrix(new Matrix4f()
                            .scale(0.5F, 0.6F, 0.5F)
                            .translateLocal(-0.25f, -0.59f, -2.95F));

                    feature.setStructureEntities(List.of(new Entity[]{costText, nameBackboardStandChainleft, nameBackboardStandChainright, hangingSign, lanternOn, lanternOff, lanternGlass}));
                    feature.setPreHiddenEntities(List.of(new Entity[]{lanternOn}));
                    feature.setToBEHiddenEntities(List.of(new Entity[]{lanternOn}));

                    feature.statusVisibilitySwap(false);
                }
                break;
            }
        }
    }

    public static void reset(Player p) {
        for (var v : MapFeature.storedStructures.entrySet()) {
            for (Entity entity : v.getKey().getStructureEntities()) {
                entity.remove();
            }
            v.getKey().clearContainedPlayers();
        }
        MapFeature.storedStructures.clear();
        if (p != null) {
            MapFeature.iteratedStructures.remove(p);
            p.sendMessage(ColoredText.t("&7&oCleared stored structures map.."));
        }
    }

    public static Vector blockFaceToVector(BlockFace face) {
        return blockFaceToVector(face, null);
    }

    public static Vector blockFaceToVector(BlockFace face, @Nullable FaceAttachable.AttachedFace attachedFace) {
        if (attachedFace != null && attachedFace.equals(FaceAttachable.AttachedFace.CEILING))
            face = BlockFace.DOWN;

        if (attachedFace != null && attachedFace.equals(FaceAttachable.AttachedFace.FLOOR))
            face = BlockFace.UP;

        switch (face) {
            case NORTH:
                return new Vector(0, 0, -1);
            case SOUTH:
                return new Vector(0, 0, 1);
            case EAST:
                return new Vector(1, 0, 0);
            case WEST:
                return new Vector(-1, 0, 0);
            case UP:
                return new Vector(0, 1, 0);
            case DOWN:
                return new Vector(0, -1, 0);
            default:
                return new Vector(0, 0, 0); // Default case for unknown BlockFace
        }
    }

}

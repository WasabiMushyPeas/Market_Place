package com.mcdomainname.marketplace;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class Posting implements Serializable {
    private String itemStackString;
    private String owner;
    private String materialDesired;
    private int quantityOfDesiredItem;

    public Posting(ItemStack itemStackToPost, String owner, String materialDesired, int quantityOfDesiredItem) {
        this.itemStackString = itemToStringBlob(itemStackToPost);
        this.owner = owner;
        this.materialDesired = materialDesired.toUpperCase();
        this.quantityOfDesiredItem = quantityOfDesiredItem;
    }

    public ItemStack getItemStack() {
        return stringBlobToItem(itemStackString);
    }

    public String getOwner() {
        return owner;
    }

    public String getMaterialDesired() {
        return materialDesired;
    }

    public int getQuantityOfDesiredItem() {
        return quantityOfDesiredItem;
    }

    @Override
    public String toString() {
        return "Posting [Owner=" + owner + " selling=" + stringBlobToItem(itemStackString).getType().name() +
                ", material desired=" + materialDesired + ", desired QTY=" + quantityOfDesiredItem + "]";
    }

    public static ItemStack stringBlobToItem(String stringBlob) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(stringBlob);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return config.getItemStack("i", null);
    }
    public static String itemToStringBlob(ItemStack itemStack) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("i", itemStack);
        return config.saveToString();
    }
}

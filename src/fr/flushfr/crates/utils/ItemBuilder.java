package fr.flushfr.crates.utils;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(ItemStack item) {
        this.item = Objects.requireNonNull(item, "item");
        this.meta = item.getItemMeta();

        if (meta == null) {
            throw new IllegalArgumentException("The type " + item.getType() + " don't support item meta");
        }
    }

    public ItemBuilder(ItemStack item, ItemMeta meta) {
        this.item = Objects.requireNonNull(item, "item");
        this.meta = meta;

        if (meta == null) {
            throw new IllegalArgumentException("The type " + item.getType() + " don't support item meta");
        }
    }

    public ItemBuilder type(Material material) {
        item.setType(material);
        return this;
    }

    public ItemBuilder data(int data) {
        return durability((short) data);
    }

    public int getData() {
        return item.getDurability();
    }

    public ItemBuilder durability(short durability) {
        item.setDurability(durability);
        return this;
    }

    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment) {
        return enchant(enchantment, 1);
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder enchant(List<String> enchantment) {
        if (!(enchantment==null)) {
            for (String e:enchantment) {
                String[] parts = e.split(":");
                String enchantement = parts[0];
                String level = parts[1];
                meta.addEnchant(Enchantment.getByName(enchantement), Integer.parseInt(level), true);
            }
        }
        return this;
    }

    public void setHead(String player) {
        SkullMeta skull = (SkullMeta) meta;
        skull.setOwner(player);
        item.setItemMeta(skull);
    }

    public ItemBuilder removeEnchant(Enchantment enchantment) {
        meta.removeEnchant(enchantment);
        return this;
    }

    public void setGlowing(boolean paramBoolean) {
        if (paramBoolean) {
            this.meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
            this.meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
    }

    public ItemBuilder removeEnchants() {
        meta.getEnchants().keySet().forEach(meta::removeEnchant);
        return this;
    }

    public ItemBuilder meta(Consumer<ItemMeta> metaConsumer) {
        metaConsumer.accept(meta);
        return this;
    }

    public <T extends ItemMeta> ItemBuilder meta(Class<T> metaClass, Consumer<T> metaConsumer) {
        if (metaClass.isInstance(meta)) {
            metaConsumer.accept(metaClass.cast(meta));
        }
        return this;
    }

    public void name(String name) {
        meta.setDisplayName(name);
    }

    public String getName() {
        return meta.getDisplayName();
    }

    public ItemBuilder lore(String lore) {
        return lore(Collections.singletonList(lore));
    }

    public ItemBuilder lore(String... lore) {
        return lore(Arrays.asList(lore));
    }

    public ItemBuilder lore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }

    public ItemBuilder copy() {
        return new ItemBuilder(item);
    }

    public ItemBuilder addLore(String line) {
        List<String> lore = meta.getLore();

        if (lore == null) {
            return lore(line);
        }

        lore.add(line);
        return lore(lore);
    }

    public ItemBuilder addLore(String... lines) {
        return addLore(Arrays.asList(lines));
    }

    public ItemBuilder addLore(List<String> lines) {
        List<String> lore = meta.getLore();

        if (lore == null) {
            return lore(lines);
        }

        lore.addAll(lines);
        return lore(lore);
    }

    public ItemBuilder flags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder flags() {
        return flags(ItemFlag.values());
    }

    public ItemBuilder removeFlags(ItemFlag... flags) {
        meta.removeItemFlags(flags);
        return this;
    }

    public ItemBuilder removeFlags() {
        return removeFlags(ItemFlag.values());
    }

    public ItemBuilder armorColor(Color color) {
        return meta(LeatherArmorMeta.class, armorMeta -> armorMeta.setColor(color));
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}


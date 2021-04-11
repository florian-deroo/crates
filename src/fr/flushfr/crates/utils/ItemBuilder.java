package fr.flushfr.crates.utils;

import fr.flushfr.crates.managers.ErrorManager;
import fr.flushfr.crates.objects.Error;
import fr.flushfr.crates.objects.ErrorCategory;
import fr.flushfr.crates.objects.ErrorType;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
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

    private ItemStack item;
    private ItemMeta meta;

    public ItemBuilder copy() {
        ItemStack i = item.clone();
        i.setItemMeta(meta);
        return new ItemBuilder(i);
    }

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


    public ItemBuilder enchant(FileConfiguration f, String path, String fileName) {
        List<String> enchantment = ErrorManager.getInstance().getStringList(f, path + ".display.enchantment");
        if (!(enchantment==null)) {
            for (String e:enchantment) {
                String[] parts = e.split(":");
                Enchantment enchantement = Enchantment.getByName(parts[0]);
                String levelString = parts[1];
                int levelId = 1;
                try {
                    levelId = Integer.parseInt(levelString);
                } catch (NumberFormatException exception) {
                    ErrorManager.getInstance().addError(new Error(ErrorCategory.ITEM, fileName, path, "level enchantment", ErrorType.INCORRECT_INTEGER));
                }
                if (enchantement == null) {
                    enchantement = Enchantment.DURABILITY;
                    ErrorManager.getInstance().addError(new Error(ErrorCategory.ITEM, fileName, path, "enchantment", ErrorType.INCORRECT));
                }
                meta.addEnchant(enchantement, levelId, true);
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


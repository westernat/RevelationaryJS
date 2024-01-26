package org.mesdag.revjs.register;

import de.dafuqs.revelationary.api.revelations.CloakedItem;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

public class ExtendedClockedItem extends CloakedItem {
    private final Identifier cloakAdvancement;
    private final Item cloakItem;
    private final OnCloakCallback onCloakCallback;
    private final OnUnCloakCallback onUnCloakCallback;
    private final MutableText cloakedItemTranslation;

    public ExtendedClockedItem(Builder builder) {
        super(builder.createItemProperties(), builder.cloakAdvancement, builder.cloakItem);
        this.cloakAdvancement = builder.cloakAdvancement;
        this.cloakItem = builder.cloakItem;
        this.onCloakCallback = builder.onCloakCallback;
        this.onUnCloakCallback = builder.onUnCloakCallback;
        this.cloakedItemTranslation=builder.cloakedItemTranslation;
    }

    @Override
    public void onCloak() {
        if (onCloakCallback != null && getItemCloak() != null) {
            onCloakCallback.onCloakItem(this, cloakAdvancement, cloakItem);
        }
    }

    @Override
    public void onUncloak() {
        if (onUnCloakCallback != null && getItemCloak() != null) {
            onUnCloakCallback.onUnCloakItem(this, cloakAdvancement, cloakItem);
        }
    }

    @Override
    public @Nullable Pair<Item, MutableText> getCloakedItemTranslation() {
        return new Pair<>(this, cloakedItemTranslation);
    }

    public static class Builder extends ItemBuilder {
        Identifier cloakAdvancement = new Identifier("adventure/root");
        Item cloakItem = Items.GRASS;
        OnCloakCallback onCloakCallback;
        OnUnCloakCallback onUnCloakCallback;
        MutableText cloakedItemTranslation;

        public Builder(Identifier identifier) {
            super(identifier);
        }

        public Builder cloakAdvancement(Identifier identifier) {
            this.cloakAdvancement = identifier;
            return this;
        }

        public Builder cloakItem(Item item) {
            this.cloakItem = item;
            return this;
        }

        public Builder onCloak(OnCloakCallback callback) {
            this.onCloakCallback = callback;
            return this;
        }

        public Builder onUnCloak(OnUnCloakCallback callback) {
            this.onUnCloakCallback = callback;
            return this;
        }

        public Builder cloakedItemTranslation(Text text) {
            this.cloakedItemTranslation = (MutableText) text;
            return this;
        }

        @Override
        public Item createObject() {
            return new ExtendedClockedItem(this);
        }
    }

    @FunctionalInterface
    public interface OnCloakCallback {
        void onCloakItem(ExtendedClockedItem self, Identifier cloakAdvancement, Item cloakItem);
    }

    @FunctionalInterface
    public interface OnUnCloakCallback {
        void onUnCloakItem(ExtendedClockedItem self, Identifier cloakAdvancement, Item cloakItem);
    }
}

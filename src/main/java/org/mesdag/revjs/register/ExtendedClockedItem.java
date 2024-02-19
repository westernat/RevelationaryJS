package org.mesdag.revjs.register;

import de.dafuqs.revelationary.api.revelations.CloakedItem;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class ExtendedClockedItem extends CloakedItem {
    private final ResourceLocation cloakAdvancement;
    private final Item cloakItem;
    private final OnCloakCallback onCloakCallback;
    private final OnUnCloakCallback onUnCloakCallback;
    private final MutableComponent cloakedItemTranslation;

    public ExtendedClockedItem(Builder builder) {
        super(builder.createItemProperties(), builder.cloakAdvancement, builder.cloakItem);
        this.cloakAdvancement = builder.cloakAdvancement;
        this.cloakItem = builder.cloakItem;
        this.onCloakCallback = builder.onCloakCallback;
        this.onUnCloakCallback = builder.onUnCloakCallback;
        this.cloakedItemTranslation = builder.cloakedItemTranslation;
    }

    @Override
    public void onCloak() {
        if (onCloakCallback != null) {
            onCloakCallback.onCloakItem(this, cloakAdvancement, cloakItem);
        }
    }

    @Override
    public void onUncloak() {
        if (onUnCloakCallback != null) {
            onUnCloakCallback.onUnCloakItem(this, cloakAdvancement, cloakItem);
        }
    }

    @Override
    public @Nullable Tuple<Item, MutableComponent> getCloakedItemTranslation() {
        return cloakedItemTranslation == null ? null : new Tuple<>(this, cloakedItemTranslation);
    }

    public static class Builder extends ItemBuilder {
        ResourceLocation cloakAdvancement = new ResourceLocation("adventure/root");
        Item cloakItem = Items.GRASS;
        OnCloakCallback onCloakCallback;
        OnUnCloakCallback onUnCloakCallback;
        MutableComponent cloakedItemTranslation;

        public Builder(ResourceLocation identifier) {
            super(identifier);
        }

        @Info(params = @Param(name = "advancementId"))
        public Builder cloakAdvancement(ResourceLocation advancementId) {
            this.cloakAdvancement = advancementId;
            return this;
        }

        public Builder cloakItem(Item item) {
            this.cloakItem = item;
            return this;
        }

        @Info("Implement custom logic when the block is getting hidden from the player")
        public Builder onCloak(OnCloakCallback callback) {
            this.onCloakCallback = callback;
            return this;
        }

        @Info("Implement custom logic when the block is made visible to the player")
        public Builder onUnCloak(OnUnCloakCallback callback) {
            this.onUnCloakCallback = callback;
            return this;
        }

        public Builder cloakedItemTranslation(Component text) {
            this.cloakedItemTranslation = (MutableComponent) text;
            return this;
        }

        @Override
        public Item createObject() {
            return new ExtendedClockedItem(this);
        }
    }

    @FunctionalInterface
    public interface OnCloakCallback {
        void onCloakItem(ExtendedClockedItem self, ResourceLocation cloakAdvancement, Item cloakItem);
    }

    @FunctionalInterface
    public interface OnUnCloakCallback {
        void onUnCloakItem(ExtendedClockedItem self, ResourceLocation cloakAdvancement, Item cloakItem);
    }
}

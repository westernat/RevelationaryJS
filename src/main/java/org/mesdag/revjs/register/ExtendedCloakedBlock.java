package org.mesdag.revjs.register;

import de.dafuqs.revelationary.api.revelations.CloakedBlock;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ExtendedCloakedBlock extends CloakedBlock {
    private final Identifier cloakAdvancement;
    private final Block cloakedBlock;
    private final OnCloakCallback onCloakCallback;
    private final OnUnCloakCallback onUnCloakCallback;
    private final Map<BlockState, BlockState> map;
    private final MutableText cloakedBlockTranslation;

    public ExtendedCloakedBlock(Builder builder) {
        super(builder.createProperties(), builder.cloakedBlock);
        this.cloakAdvancement = builder.cloakAdvancement;
        this.cloakedBlock = builder.cloakedBlock;
        this.onCloakCallback = builder.onCloakCallback;
        this.onUnCloakCallback = builder.onUnCloakCallback;
        this.map = builder.map;
        this.cloakedBlockTranslation = builder.cloakedBlockTranslation;
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        return cloakAdvancement;
    }

    @Override
    public void onCloak() {
        if (onCloakCallback != null) {
            onCloakCallback.onCloakBlock(this, cloakAdvancement, cloakedBlock);
        }
    }

    @Override
    public void onUncloak() {
        if (onUnCloakCallback != null) {
            onUnCloakCallback.onUnCloakBlock(this, cloakAdvancement, cloakedBlock);
        }
    }

    @Override
    public Map<BlockState, BlockState> getBlockStateCloaks() {
        return map == null ? super.getBlockStateCloaks() : map;
    }

    @Override
    public @Nullable Pair<Item, MutableText> getCloakedItemTranslation() {
        return new Pair<>(this.asItem(), cloakedBlockTranslation);
    }

    @Override
    public @Nullable Pair<Block, MutableText> getCloakedBlockTranslation() {
        return new Pair<>(this, cloakedBlockTranslation);
    }

    public static class Builder extends BlockBuilder {
        Identifier cloakAdvancement = new Identifier("adventure/story");
        Block cloakedBlock = Blocks.BEACON;
        OnCloakCallback onCloakCallback;
        OnUnCloakCallback onUnCloakCallback;
        Map<BlockState, BlockState> map;
        MutableText cloakedBlockTranslation;

        public Builder(Identifier identifier) {
            super(identifier);
        }

        public Builder cloakAdvancement(Identifier identifier) {
            this.cloakAdvancement = identifier;
            return this;
        }

        public Builder cloakItem(Block item) {
            this.cloakedBlock = item;
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

        public Builder blockStateCloaks(Map<BlockState, BlockState> map) {
            this.map = map;
            return this;
        }

        public Builder cloakedBlockTranslation(Text text) {
            this.cloakedBlockTranslation = (MutableText) text;
            return this;
        }

        @Override
        public Block createObject() {
            return new ExtendedCloakedBlock(this);
        }

        @Override
        protected BlockItemBuilder getOrCreateItemBuilder() {
            return itemBuilder == null ? (itemBuilder = new CustomCloakedBlockItem.Builder(id, cloakAdvancement, cloakedBlock, map)) : itemBuilder;
        }
    }

    @FunctionalInterface
    public interface OnCloakCallback {
        void onCloakBlock(ExtendedCloakedBlock self, Identifier cloakAdvancement, Block cloakBlock);
    }

    @FunctionalInterface
    public interface OnUnCloakCallback {
        void onUnCloakBlock(ExtendedCloakedBlock self, Identifier cloakAdvancement, Block cloakBlock);
    }
}

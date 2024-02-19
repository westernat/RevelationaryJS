package org.mesdag.revjs.register;

import de.dafuqs.revelationary.api.revelations.CloakedBlock;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;
import org.mesdag.revjs.revelation.RevBuilder;

import java.util.Hashtable;
import java.util.Map;
import java.util.function.Consumer;

public class ExtendedCloakedBlock extends CloakedBlock {
    private final Identifier cloakAdvancement;
    private final Item cloakItem;
    private final Block cloakBlock;
    private final OnCloakCallback onCloakCallback;
    private final OnUnCloakCallback onUnCloakCallback;
    private final Map<Object, Object> objectMap;
    private Map<BlockState, BlockState> blockStateMap;
    private final MutableText cloakedItemTranslation;
    private final MutableText cloakedBlockTranslation;

    public ExtendedCloakedBlock(Builder builder) {
        super(builder.createProperties(), builder.cloakBlock);
        this.cloakAdvancement = builder.cloakAdvancement;
        this.cloakItem = builder.cloakItem;
        this.cloakBlock = builder.cloakBlock;
        this.onCloakCallback = builder.onCloakCallback;
        this.onUnCloakCallback = builder.onUnCloakCallback;
        this.objectMap = builder.objectMap;
        this.cloakedItemTranslation = builder.cloakedItemTranslation;
        this.cloakedBlockTranslation = builder.cloakedBlockTranslation;
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        return cloakAdvancement;
    }

    @Override
    public void onCloak() {
        if (onCloakCallback != null) {
            onCloakCallback.onCloakBlock(this, cloakAdvancement, cloakBlock);
        }
    }

    @Override
    public void onUncloak() {
        if (onUnCloakCallback != null) {
            onUnCloakCallback.onUnCloakBlock(this, cloakAdvancement, cloakBlock);
        }
    }

    @Override
    public Map<BlockState, BlockState> getBlockStateCloaks() {
        if (objectMap == null) {
            return super.getBlockStateCloaks();
        } else if (blockStateMap == null) {
            blockStateMap = new Hashtable<>();
            objectMap.forEach((key, value) -> blockStateMap.put(RevBuilder.getState(key), RevBuilder.getState(value)));
        }
        return blockStateMap;
    }

    @Override
    public Pair<Item, Item> getItemCloak() {
        return cloakItem == null ? null : new Pair<>(this.asItem(), cloakItem);
    }

    @Override
    public @Nullable Pair<Item, MutableText> getCloakedItemTranslation() {
        if (cloakedItemTranslation == null) {
            if (cloakedBlockTranslation == null) {
                return null;
            }
            return new Pair<>(this.asItem(), cloakedBlockTranslation);
        }
        return new Pair<>(this.asItem(), cloakedItemTranslation);
    }

    @Override
    public @Nullable Pair<Block, MutableText> getCloakedBlockTranslation() {
        return cloakedBlockTranslation == null ? null : new Pair<>(this, cloakedBlockTranslation);
    }

    public static class Builder extends BlockBuilder {
        Identifier cloakAdvancement = new Identifier("adventure/story");
        Item cloakItem;
        Block cloakBlock = Blocks.BEACON;
        OnCloakCallback onCloakCallback;
        OnUnCloakCallback onUnCloakCallback;
        Map<Object, Object> objectMap;
        MutableText cloakedItemTranslation;
        MutableText cloakedBlockTranslation;

        public Builder(Identifier identifier) {
            super(identifier);
        }

        @Info(params = @Param(name = "advancementId"))
        public Builder cloakAdvancement(Identifier advancementId) {
            this.cloakAdvancement = advancementId;
            return this;
        }

        public Builder cloakItem(Item item) {
            this.cloakItem = item;
            return this;
        }

        @Info("This method is same as ```blockStateCloak(this, cloakBlock)```")
        public Builder cloakBlock(Block block) {
            this.cloakBlock = block;
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

        @Info(value = """
            Accepts

                BlockState,

                BlockIDPredicate: Block.id()

                Stringify blockState: 'blockId[state=value,]'

                Block

                BlockContainerJS
            """,
                params = {
                        @Param(name = "sourceBlock"),
                        @Param(name = "targetBlock")
                })
        public Builder blockStateCloak(Object sourceBlock, Object targetBlock) {
            if (objectMap == null) {
                objectMap = new Hashtable<>();
            }
            objectMap.put(sourceBlock, targetBlock);
            return this;
        }

        public Builder cloakedItemTranslation(Text text) {
            this.cloakedItemTranslation = (MutableText) text;
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
            return itemBuilder == null ? (itemBuilder = new CustomCloakedBlockItem.Builder(id)) : itemBuilder;
        }

        @Override
        public void createAdditionalObjects() {
            if (itemBuilder != null) {
                ((CustomCloakedBlockItem.Builder) itemBuilder).syncData(cloakAdvancement, cloakBlock, objectMap);
            }
            super.createAdditionalObjects();
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

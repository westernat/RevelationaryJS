package org.mesdag.revjs.register;

import de.dafuqs.revelationary.api.revelations.CloakedBlock;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.mesdag.revjs.revelation.RevBuilder;

import java.util.Hashtable;
import java.util.Map;

public class ExtendedCloakedBlock extends CloakedBlock {
    private final ResourceLocation cloakAdvancement;
    private final Item cloakItem;
    private final Block cloakBlock;
    private final OnCloakCallback onCloakCallback;
    private final OnUnCloakCallback onUnCloakCallback;
    private final Map<Object, Object> objectMap;
    private Map<BlockState, BlockState> blockStateMap;
    private final MutableComponent cloakedItemTranslation;
    private final MutableComponent cloakedBlockTranslation;

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
    public ResourceLocation getCloakAdvancementIdentifier() {
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
    public Tuple<Item, Item> getItemCloak() {
        return cloakItem == null ? null : new Tuple<>(this.asItem(), cloakItem);
    }

    @Override
    public @Nullable Tuple<Item, MutableComponent> getCloakedItemTranslation() {
        if (cloakedItemTranslation == null) {
            if (cloakedBlockTranslation == null) {
                return null;
            }
            return new Tuple<>(this.asItem(), cloakedBlockTranslation);
        }
        return new Tuple<>(this.asItem(), cloakedItemTranslation);
    }

    @Override
    public @Nullable Tuple<Block, MutableComponent> getCloakedBlockTranslation() {
        return cloakedBlockTranslation == null ? null : new Tuple<>(this, cloakedBlockTranslation);
    }

    public static class Builder extends BlockBuilder {
        ResourceLocation cloakAdvancement = new ResourceLocation("adventure/root");
        Item cloakItem;
        Block cloakBlock = Blocks.BEACON;
        OnCloakCallback onCloakCallback;
        OnUnCloakCallback onUnCloakCallback;
        Map<Object, Object> objectMap;
        MutableComponent cloakedItemTranslation;
        MutableComponent cloakedBlockTranslation;

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
            Available parameters type:

                BlockState

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

        public Builder cloakedItemTranslation(Component text) {
            this.cloakedItemTranslation = (MutableComponent) text;
            return this;
        }

        public Builder cloakedBlockTranslation(Component text) {
            this.cloakedBlockTranslation = (MutableComponent) text;
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
        void onCloakBlock(ExtendedCloakedBlock self, ResourceLocation cloakAdvancement, Block cloakBlock);
    }

    @FunctionalInterface
    public interface OnUnCloakCallback {
        void onUnCloakBlock(ExtendedCloakedBlock self, ResourceLocation cloakAdvancement, Block cloakBlock);
    }
}

package org.mesdag.revjs.register;

import de.dafuqs.revelationary.api.revelations.RevelationAware;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CustomCloakedBlockItem extends BlockItem implements RevelationAware {
    private final Identifier cloakAdvancement;
    private final Block cloakBlock;
    private final Map<BlockState, BlockState> map;

    public CustomCloakedBlockItem(Block block, Item.Settings settings, Identifier cloakAdvancement, Block cloakBlock, Map<BlockState, BlockState> map) {
        super(block, settings);
        this.cloakAdvancement = cloakAdvancement;
        this.cloakBlock = cloakBlock;
        this.map = map;
        RevelationAware.register(this);
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        return cloakAdvancement;
    }

    @Override
    public Map<BlockState, BlockState> getBlockStateCloaks() {
        return map == null ? Map.of(getBlock().getDefaultState(), cloakBlock.getDefaultState()) : map;
    }

    @Override
    public @Nullable Pair<Item, Item> getItemCloak() {
        return new Pair<>(this, cloakBlock.asItem());
    }

    public static class Builder extends BlockItemBuilder {
        private final Identifier cloakAdvancement;
        private final Block cloakBlock;
        private final Map<BlockState, BlockState> map;

        public Builder(Identifier identifier, Identifier cloakAdvancement, Block cloakBlock, Map<BlockState, BlockState> map) {
            super(identifier);
            this.cloakAdvancement = cloakAdvancement;
            this.cloakBlock = cloakBlock;
            this.map = map;
        }

        @Override
        public Item createObject() {
            return new CustomCloakedBlockItem(blockBuilder.get(), createItemProperties(), cloakAdvancement, cloakBlock, map);
        }
    }
}

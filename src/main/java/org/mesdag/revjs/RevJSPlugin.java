package org.mesdag.revjs;

import de.dafuqs.revelationary.RevelationRegistry;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.ScriptType;
import org.mesdag.revjs.register.ExtendedCloakedBlock;
import org.mesdag.revjs.register.ExtendedClockedItem;
import org.mesdag.revjs.revelation.RevelationEventJS;

public class RevJSPlugin extends KubeJSPlugin {
    @Override
    public void init() {
        RegistryInfo.ITEM.addType("cloaked", ExtendedClockedItem.Builder.class, ExtendedClockedItem.Builder::new);
        RegistryInfo.BLOCK.addType("cloaked", ExtendedCloakedBlock.Builder.class, ExtendedCloakedBlock.Builder::new);
    }

    @Override
    public void onServerReload() {
        RevelationRegistry.clear();
        RevJS.REVELATION.post(ScriptType.SERVER, RevelationEventJS.INSTANCE);
    }
}

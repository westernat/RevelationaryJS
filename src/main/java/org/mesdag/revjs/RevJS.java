package org.mesdag.revjs;

import dev.latvian.mods.kubejs.bindings.event.ServerEvents;
import dev.latvian.mods.kubejs.event.EventHandler;
import net.minecraftforge.fml.common.Mod;
import org.mesdag.revjs.revelation.RevelationEventJS;

@Mod("revjs")
public class RevJS {
    public static final EventHandler REVELATION = ServerEvents.GROUP.server("revelation", () -> RevelationEventJS.class);

    public RevJS() {
    }
}

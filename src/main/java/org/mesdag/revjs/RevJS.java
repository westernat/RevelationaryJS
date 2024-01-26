package org.mesdag.revjs;

import dev.latvian.mods.kubejs.bindings.event.ServerEvents;
import dev.latvian.mods.kubejs.event.EventHandler;
import net.fabricmc.api.ModInitializer;
import org.mesdag.revjs.revelation.RevelationEventJS;

public class RevJS implements ModInitializer {
	public static final EventHandler REVELATION = ServerEvents.GROUP.server("revelation", () -> RevelationEventJS.class);

	@Override
	public void onInitialize() {
	}
}
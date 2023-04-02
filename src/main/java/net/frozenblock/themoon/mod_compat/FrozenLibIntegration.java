package net.frozenblock.themoon.mod_compat;

import net.frozenblock.lib.integration.api.ModIntegration;
import net.frozenblock.themoon.util.TheMoonSharedConstants;

public class FrozenLibIntegration extends ModIntegration {
	public FrozenLibIntegration() {
		super("frozenlib");
	}

	@Override
	public void init() {
		TheMoonSharedConstants.log("FrozenLib integration ran!", TheMoonSharedConstants.UNSTABLE_LOGGING);
	}
}

/***************************************************************************
 *                     Copyright © 2020 - Arianne                          *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.core.rp.achievement;

import static games.stendhal.server.core.rp.achievement.factory.FightingAchievementFactory.COUNT_DEEPSEA;
import static games.stendhal.server.core.rp.achievement.factory.FightingAchievementFactory.ENEMIES_DEEPSEA;
import static games.stendhal.server.core.rp.achievement.factory.FightingAchievementFactory.ID_DEEPSEA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.maps.MockStendlRPWorld;
import marauroa.server.game.db.DatabaseFactory;
import utilities.AchievementTestHelper;
import utilities.PlayerTestHelper;
import utilities.ZoneAndPlayerTestImpl;

public class DeepSeaFishermanTest extends ZoneAndPlayerTestImpl {

	@BeforeClass
	public static void setUpBeforeClass() {
		new DatabaseFactory().initializeDatabase();
		// initialize world
		MockStendlRPWorld.get();
	}

	@Override
	@Before
	public void setUp() throws Exception {
		zone = setupZone("testzone");
		//super.setUp();
	}

	@Test
	public void init() {
		final int totalRequiredKills = COUNT_DEEPSEA * ENEMIES_DEEPSEA.length;

		// solo kills
		resetPlayer();
		int killCount = 0;
		for (final String enemyName: ENEMIES_DEEPSEA) {
			for (int idx = 0; idx < COUNT_DEEPSEA; idx++) {
				player.setSoloKillCount(enemyName, player.getSoloKill(enemyName) + 1);
				AchievementNotifier.get().onKill(player);
				killCount++;

				if (killCount < totalRequiredKills) {
					assertFalse(achievementReached());
				} else {
					assertTrue(achievementReached());
				}
			}

			assertEquals(COUNT_DEEPSEA, player.getSoloKill(enemyName));
		}

		// team kills
		resetPlayer();
		killCount = 0;
		for (final String enemyName: ENEMIES_DEEPSEA) {
			for (int idx = 0; idx < COUNT_DEEPSEA; idx++) {
				player.setSharedKillCount(enemyName, player.getSharedKill(enemyName) + 1);
				AchievementNotifier.get().onKill(player);
				killCount++;

				if (killCount < totalRequiredKills) {
					assertFalse(achievementReached());
				} else {
					assertTrue(achievementReached());
				}
			}

			assertEquals(COUNT_DEEPSEA, player.getSharedKill(enemyName));
		}

		if (ENEMIES_DEEPSEA.length > 1) {
			for (final String enemyName: ENEMIES_DEEPSEA) {
				resetPlayer();
				for (int idx = 0; idx < totalRequiredKills; idx++) {
					player.setSoloKillCount(enemyName, player.getSoloKill(enemyName) + 1);
					AchievementNotifier.get().onKill(player);
				}

				assertFalse(achievementReached());
			}
		}
	}

	private void resetPlayer() {
		if (player != null) {
			PlayerTestHelper.removePlayer(player.getName(), "testzone");
		}
		player = PlayerTestHelper.createPlayer("player");
		player.setPosition(0, 0);
		zone.add(player);
		assertNotNull(player);

		player.setLevel(597);
		player.setAtk(597);
		player.setDef(597);

		for (final String stat: Arrays.asList("level", "atk", "def")) {
			assertEquals(597, player.getInt(stat));
		}

		equip("chaos axe", "rhand");
		equip("mithril shield", "lhand");
		equip("mithril helmet", "head");
		equip("mithril armor", "armor");
		equip("mithril legs", "legs");
		equip("mithril boots", "feet");
		equip("mithril cloak", "cloak");
		equip("imperial ring", "finger");

		for (final String enemy: ENEMIES_DEEPSEA) {
			assertFalse(player.hasKilled(enemy));
		}

		AchievementTestHelper.init(player);
		assertFalse(achievementReached());
	}

	private void equip(final String item, final String slot) {
		PlayerTestHelper.equipWithItemToSlot(player, item, slot);
	}

	/**
	 * Checks if the player has reached the achievement.
	 *
	 * @return
	 * 		<code>true</player> if the player has the achievement.
	 */
	private boolean achievementReached() {
		return AchievementTestHelper.achievementReached(player, ID_DEEPSEA);
	}
}

/***************************************************************************
 *                      (C) Copyright 2019 - Stendhal                      *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.maps.deniran.cityinterior.weaponsshop;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.mapstuff.sign.ShopSign;
import games.stendhal.server.entity.npc.ShopList;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.BuyerAdder;
import games.stendhal.server.entity.npc.behaviour.adder.SellerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.BuyerBehaviour;
import games.stendhal.server.entity.npc.behaviour.impl.SellerBehaviour;

public class WeaponDealerNPC implements ZoneConfigurator  {


	@Override
	public void configureZone(StendhalRPZone zone, Map<String, String> attributes) {
		buildNPC(zone);
		buildSigns(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("D J Smith") {
			@Override
			public void createDialog() {
				addGreeting("Hello, and welcome to the deniran weapon shop.");
				addJob("I am the local weapons dealer.");
				addOffer("Check out the blackboards for my prices.");
				addGoodbye();
			}

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(29, 5));
				nodes.add(new Node(11, 5));
				setPath(new FixedPath(nodes, true));
			}
		};

		// buys
		final Map<String, Integer> pricesBuy = new LinkedHashMap<String, Integer>() {{
			put("ugmash", 1200);
			put("magic cloak", 12000);
		}};
		new BuyerAdder().addBuyer(npc, new BuyerBehaviour(pricesBuy), false);

		// sells
		final Map<String, Integer> pricesSell = new LinkedHashMap<String, Integer>() {{
			put("shuriken", 77);
			put("fire shuriken", 99);
			put("wooden spear", 110);
		}};
		new SellerAdder().addSeller(npc, new SellerBehaviour(pricesSell), false);

		// add shops to the general shop list
		final ShopList shops = ShopList.get();
		for (final String key: pricesBuy.keySet()) {
			shops.add("deniranequipbuy", key, pricesBuy.get(key));
		}
		for (final String key: pricesSell.keySet()) {
			shops.add("deniranequipsell", key, pricesSell.get(key));
		}

		npc.setPosition(11, 5);
		npc.setEntityClass("wellroundedguynpc");
		npc.setDescription("You see D J Smith, the weapon dealer.");
		zone.add(npc);
	}

	private void buildSigns(final StendhalRPZone zone) {
		final ShopSign buys = new ShopSign("deniranequipbuy", "D J Smith's Shop (buying)", "You can buy these things from D J Smith.", false);
		buys.setEntityClass("blackboard");
		buys.setPosition(20, 4);

		final ShopSign sells = new ShopSign("deniranequipsell", "D J Smith's Shop (selling)", "You can buy these things from D J Smith.", false);
		sells.setEntityClass("blackboard");
		sells.setPosition(21, 4);

		zone.add(buys);
		zone.add(sells);
	}
}

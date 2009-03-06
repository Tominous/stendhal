package games.stendhal.client.gui.bag;

import static pagelayout.EasyCell.eol;
import static pagelayout.EasyCell.grid;
import games.stendhal.client.gui.styled.Style;
import games.stendhal.client.gui.styled.swing.StyledJPanel;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import marauroa.common.game.RPObject;
import pagelayout.CellGrid;

public class BagPanel extends StyledJPanel {

	private static final Dimension PREFERRED_SIZE = new Dimension(40, 40);
	ItemPanel[] panels = new ItemPanel[12];
	private Map<String, ItemPanel> panelItemMap = new HashMap<String, ItemPanel>();
	public BagPanel(final Style instance) {
		super(instance);
		for (int i = 0; i < 12; i++) {
			panels[i] = new ItemPanel();
			panels[i].setPreferredSize(PREFERRED_SIZE);
		}
		
		CellGrid baggrid = 
			grid(panels[0], panels[1], panels[2], eol(),
				panels[3], panels[4], panels[5], eol(),
				panels[6], panels[7], panels[8], eol(),
				panels[9], panels[10], panels[11], eol());
		baggrid.setFixedWidth(panels, true);
		baggrid.createLayout(this);
		
	}
	
	void addItem(final RPObject object) {
		System.out.println(object.getInt("id") + ":" + object.get("class"));
		
		 try {
			ItemPanel panel = panelItemMap.get(object.get("id"));
			
			if (panel == null) {
				panel = findEmptyPanel();
				panelItemMap.put(object.get("id"), panel);
				panel.addNew(object);

			} else {
				panel.updateValues(object);
			}
			revalidate();
		} catch (Exception e) {
			System.out.println(e);
		}
		

	}

	private ItemPanel findEmptyPanel() {
		for (ItemPanel panel : panels) {
			if (panel.isEmpty()) {
				return panel;
			}
		}
		return panels[0];
	}

	public void removeItem(final RPObject object) {
		ItemPanel panel = panelItemMap.get(object.get("id"));
		panel.removeItem(object);
		
		revalidate();
	}
	
	

}

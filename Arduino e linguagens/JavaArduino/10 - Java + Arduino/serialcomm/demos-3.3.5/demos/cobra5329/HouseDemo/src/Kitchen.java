/**
 * Java file created by JControl/IDE
 *
 * Created on 24.09.06 12:08
 *
 * @author Marcus Timmermann
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.ui.wombat.Label;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.graphics.Color;
import jcontrol.ui.wombat.Button;
import jcontrol.ui.wombat.event.ActionEvent;

public class Kitchen extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 5;
	/*
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private HouseDemo m_parentApplication;
	private Label m_kitchenLabel;
	private Button m_ceilingLight;
	private Button m_oven;
	private Button m_buttonHome;
	private Label m_headline;

	/**
	 * Constructor Kitchen
	 *
	 * @param parent
	 */
	public Kitchen(HouseDemo parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
		m_kitchenLabel = new Label(((String)null), 0, 0, 85, 70, Label.STYLE_ALIGN_CENTER);
		try {
			m_kitchenLabel.setImage(new Resource("kitchen.jcif"));
		} catch(IOException ioe) {}
		m_kitchenLabel.setBackgroundColor(new Color(0, 130, 43));
		this.add(m_kitchenLabel);
		m_ceilingLight = new Button(null, 85, 85, 70, 70);
		try {
			m_ceilingLight.setImage(new Resource("ceilinglight.jcif"));
		} catch(IOException ioe) {}
		this.add(m_ceilingLight);
		m_ceilingLight.setActionListener(this);
		m_oven = new Button(null, 165, 85, 70, 70);
		try {
			m_oven.setImage(new Resource("oven.jcif"));
		} catch(IOException ioe) {}
		this.add(m_oven);
		m_oven.setActionListener(this);
		m_buttonHome = new Button(null, 5, 85, 70, 70);
		m_buttonHome.setBackgroundColor(Color.WHITE);
		try {
			m_buttonHome.setImage(new Resource("house.jcif"));
		} catch(IOException ioe) {}
		this.add(m_buttonHome);
		m_buttonHome.setActionListener(this);
		m_headline = new Label("KITCHEN", 85, 0, 235, 70, Label.STYLE_ALIGN_LEFT);
		try {
			m_headline.setFont(new Resource("SansSerif_22px.jcfd"));
		} catch(IOException ioe) {}
		m_headline.setForegroundColor(Color.WHITE);
		m_headline.setBackgroundColor(new Color(0, 130, 43));
		this.add(m_headline);
	}

	/**
	 * This method is called every time any component on this page fires an
	 * action event.
	 *
	 * @param e
	 *        The event that has been fired.
	 */
	public void onActionEvent(ActionEvent e) {
		if (e.source == m_buttonHome) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				e.source = null;
				m_parentApplication.showPage(HouseDemo.HOUSE);
			}
		} else if (e.source == m_ceilingLight) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				e.source = null;
				m_parentApplication.showPage(HouseDemo.LIGHT_PAGE);
			}
		} else if (e.source == m_oven) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				e.source = null;
				m_parentApplication.showPage(HouseDemo.OVEN_PAGE);
			}
		}
	}
}
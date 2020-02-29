
/**
 * Copyright (c) 2001-2019 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://robocode.sourceforge.io/license/epl-v10.html
 */
package org.jogamp.glg2d.ben;

import org.jogamp.glg2d.GLG2DCanvas;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Xor
 */
public final class UIDemo extends JPanel {
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.BLACK);

		g.fillRect(0, 0, 400, 400);

		g.setColor(Color.RED);

		g.fillRect(102, 102, 46, 46);

		g.setColor(Color.GREEN);

		g.drawRect(100, 100, 50, 50);
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		final JFrame frame = new JFrame("Swing Demo");

		JPopupMenu.setDefaultLightWeightPopupEnabled(false);

		JComponent demo = new UIDemo();

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		GLG2DCanvas canvas = new GLG2DCanvas(demo);
		// canvas.setGLDrawing(false);
		Component component = (Component) canvas.getGLDrawable();
		component.setEnabled(true);

		panel.add(canvas);

		frame.setContentPane(panel);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800, 600));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		component.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.pack();
			}
		});
	}
}

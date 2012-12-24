package com.lj.snapshot;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.sun.awt.AWTUtilities;

public class TranslucentFrameDemo extends JFrame {
	private static final long serialVersionUID = 3878450094604602368L;

	TranslucentFrameDemo() {
		setUndecorated(true);
		setSize(400, 400);
		AWTUtilities.setWindowOpaque(this, false);
		final JLabel label = new JLabel(new ImageIcon("d:\\clip.png"));
		label.setBackground(Color.BLACK); // actually won't work
		add(label);
	}

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TranslucentFrameDemo().setVisible(true);
			}
		});
	}
}

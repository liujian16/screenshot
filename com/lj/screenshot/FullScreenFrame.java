package com.lj.snapshot;

import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sun.awt.AWTUtilities;

public class FullScreenFrame extends JFrame {

	public FullScreenFrame() throws HeadlessException {
		super();

		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setUndecorated(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		AWTUtilities.setWindowOpaque(this, false);

		MaskPanel p = new MaskPanel();

		this.add(p);

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				FullScreenFrame f = new FullScreenFrame();
				f.setVisible(true);
			}

		});
	}

}

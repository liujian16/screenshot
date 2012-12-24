package com.lj.snapshot;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.JPanel;

/**
 * @author Administrator
 * 
 */
public class MaskPanel extends JPanel {
	private Rectangle snapshotRect = null;
	private Handler handler = new Handler();

	public MaskPanel() {
		snapshotRect = new Rectangle(200, 200, 400, 300);
		addMouseListener(handler);
		addMouseMotionListener(handler);

	}

	class Handler extends MouseAdapter {
		boolean dragStarted = false;
		Point dragStartPoint = null;
		Rectangle copyOfsnapshotRect = null;

		@Override
		public void mousePressed(MouseEvent e) {
			Rectangle moveDetectingRect = new Rectangle(snapshotRect);
			moveDetectingRect.grow(-4, -4);
			if (moveDetectingRect.contains(e.getPoint())) {
				dragStarted = true;
				dragStartPoint = e.getPoint();
				copyOfsnapshotRect = new Rectangle(snapshotRect);
			}

			System.out.printf("dragstarted %s\n", dragStarted + "");
		}

		@Override
		public void mouseReleased(MouseEvent e) {

			if (dragStarted) {
				dragStarted = false;
				dragStartPoint = null;
				copyOfsnapshotRect = null;
			}
			System.out.printf("dragstarted %s\n", dragStarted + "");
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Rectangle moveDetectingRect = new Rectangle(snapshotRect);
			moveDetectingRect.grow(-4, -4);
			if (moveDetectingRect.contains(e.getPoint())) {
				MaskPanel.this.setCursor(Cursor
						.getPredefinedCursor(Cursor.MOVE_CURSOR));
			} else {
				MaskPanel.this.setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			System.out.printf("dragstarted %s\n", dragStarted + "");

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (dragStarted) {
				System.out.printf("dragstarted %s\n", dragStarted + "");
				int dragX = e.getPoint().x - dragStartPoint.x;
				int dragY = e.getPoint().y - dragStartPoint.y;
				Rectangle newSnapshotRect = new Rectangle(copyOfsnapshotRect);
				newSnapshotRect.setLocation(newSnapshotRect.x + dragX,
						newSnapshotRect.y + dragY);
				snapshotRect = newSnapshotRect;
				MaskPanel.this.repaint();
				System.out.printf("dragX %d, dragY %d, snapshotRect %s\n",
						dragX, dragY, snapshotRect.toString());
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2) {
				Robot robot = null;
				try {
					robot = new Robot();
				} catch (AWTException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				Point originpoint = MaskPanel.this.getLocationOnScreen();
				Rectangle snapshotOnScreen = new Rectangle(snapshotRect);
				snapshotOnScreen.translate(originpoint.x, originpoint.y);

				BufferedImage snapshot = robot
						.createScreenCapture(snapshotOnScreen);
				try {
					ImageIO.write(snapshot, "PNG", new FileImageOutputStream(
							new File("c:\\snapshot.png")));
					Runtime.getRuntime().exec("mspaint c:\\snapshot.png");
					System.exit(0);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 * 
	 * |----------------------------------------| | |
	 * |-------------|----------------|---------| | |################| | | | | |
	 * |-------------|----------------|---------|
	 * 
	 */
	@Override
	public void paint(Graphics g) {
		// super.paint(g);
		AlphaComposite composite = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 0.5f);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setComposite(composite);

		g2d.fill(new Rectangle(0, 0, this.getWidth(), snapshotRect.y));
		g2d.fill(new Rectangle(0, snapshotRect.y + snapshotRect.height, this
				.getWidth(), this.getHeight() - snapshotRect.y
				- snapshotRect.height));
		g2d.fill(new Rectangle(0, snapshotRect.y, snapshotRect.x,
				snapshotRect.height));

		g2d.fill(new Rectangle(snapshotRect.x + snapshotRect.width,
				snapshotRect.y, this.getWidth() - snapshotRect.x
						- snapshotRect.width, snapshotRect.height));

		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.01f);
		g2d.setComposite(composite);
		g2d.fill(new Rectangle(snapshotRect.x, snapshotRect.y,
				snapshotRect.width, snapshotRect.height));

		g2d.setColor(Color.blue);
		composite = AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f);
		g2d.setComposite(composite);
		g2d.drawRect(snapshotRect.x, snapshotRect.y, snapshotRect.width,
				snapshotRect.height);

		drawDragHandler(g2d, snapshotRect.x, snapshotRect.y);
		drawDragHandler(g2d, snapshotRect.x + snapshotRect.width,
				snapshotRect.y);
		drawDragHandler(g2d, snapshotRect.x, snapshotRect.y
				+ snapshotRect.height);
		drawDragHandler(g2d, snapshotRect.x + snapshotRect.width,
				snapshotRect.y + snapshotRect.height);

		g2d.dispose();

	}

	private void drawDragHandler(Graphics2D g2d, int x, int y) {
		AlphaComposite composite = AlphaComposite.getInstance(
				AlphaComposite.SRC, 1.0f);
		Composite oldcomposite = g2d.getComposite();
		g2d.setComposite(composite);
		g2d.setColor(Color.blue);
		g2d.fillRect(x - 2, y - 2, 5, 5);
		g2d.setColor(Color.white);
		g2d.drawRect(x - 3, y - 3, 6, 6);
		g2d.setComposite(oldcomposite);
	}

}
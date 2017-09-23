/* 
 * (C) Copyright 2017, by Thomas Oltzen.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
*/

package de.oltzen.aps.overhead;



import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import de.oltzen.awt.TableLayout;

@SuppressWarnings("serial")
public class DrawerFrame extends JFrame {

	DrawComponent mDrawComponent;

	Class<?> cmdClass;

	final static String TITLE = "Draw Overhead (C) 2017 by Thomas Oltzen";

	DrawerFrame() {
		setTitle(TITLE);

		this.setBounds(100, 200, 640, 440);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container pane = getContentPane();

		TableLayout layout = new TableLayout(2, 7);
		layout.setColRel(0, 20);
		layout.setColRel(1, 20);
		layout.setColRel(2, 20);
		layout.setColRel(3, 20);
		layout.setColRel(4, 40);
		layout.setColRel(5, 40);
		layout.setRowRel(1, 100);

		pane.setLayout(layout);

		JButton red = createButton("1");
		red.setBackground(Color.RED);
		layout.addComponent(red, 0, 0);
		pane.add(red);

		JButton blue = createButton("2");
		blue.setBackground(Color.BLUE);
		layout.addComponent(blue, 0, 1);
		pane.add(blue);

		JButton yellow = createButton("3");
		yellow.setBackground(Color.YELLOW);
		layout.addComponent(yellow, 0, 2);
		pane.add(yellow);

		JButton green = createButton("4");
		green.setBackground(Color.GREEN);
		layout.addComponent(green, 0, 3);
		pane.add(green);

		JButton rect = createButton("5: Rect");
		layout.addComponent(rect, 0, 4);
		pane.add(rect);

		JButton line = createButton("6: Line");
		layout.addComponent(line, 0, 5);
		pane.add(line);

		JButton transperent = createButton("7: Thru");
		layout.addComponent(transperent, 0, 6);
		pane.add(transperent);


		mDrawComponent = new DrawComponent();
		layout.addComponent(mDrawComponent, 1, 0, 1, 7);
		pane.add(mDrawComponent);

		cmdClass = Line.class;

		mDrawComponent.addMouseListener(new MyMouseListener());
		mDrawComponent.addMouseMotionListener(new MyMouseMotionListener());

		mDrawComponent.addFocusListener(new MyFocusListener());
		this.addFocusListener(new MyFocusListener());

		this.addKeyListener(new MyKeyListener());

		setBackground(new Color(0, 1, 0, 1));// Not full transparent

		this.setAlwaysOnTop(true);

		this.setVisible(true);
	}

	private JButton createButton(String label) {
		JButton button = new JButton(label);
		button.setFocusable(false);
		button.addActionListener(new ButtonActionListener(label.charAt(0)));
		return button;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		new DrawerFrame();
	}

	private void doButtonAction(char ch) {

		if (ch == '1') {
			Col c = new Col();
			c.c = new Color(255, 0, 0, 150);
			mDrawComponent.newCmd(c);
		}
		if (ch == '2') {
			Col c = new Col();
			c.c = new Color(0, 0, 255, 150);
			mDrawComponent.newCmd(c);
		}
		if (ch == '3') {
			Col c = new Col();
			c.c = new Color(255, 255, 0, 150);
			mDrawComponent.newCmd(c);
		}
		if (ch == '4') {
			Col c = new Col();
			c.c = new Color(0, 255, 0, 150);
			mDrawComponent.newCmd(c);
		}
		if (ch == '5') {
			cmdClass = Rect.class;
		}
		if (ch == '6') {
			cmdClass = Line.class;
		}
		if (ch == ' ' || ch == '7') {
			// The backgound can be selected.
			setBackground(new Color(0, 1, 0, 0));
			setTitle(TITLE + ": Transparent");
		}
		if (ch == 8) {
			// Undo
			if (mDrawComponent.lastCmd != null) {
				mDrawComponent.lastCmd = null;
			} else if (mDrawComponent.cmdList.size() > 0) {
				mDrawComponent.cmdList.remove(mDrawComponent.cmdList.size() - 1);
			}
			DrawerFrame.this.invalidate();
			DrawerFrame.this.repaint();
		}
	}

	class ButtonActionListener implements ActionListener {

		private char mChar;

		public ButtonActionListener(char ch) {
			mChar = ch;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			setBackground(new Color(0, 1, 0, 1));
			setTitle(TITLE);
			doButtonAction(mChar);

		}

	}

	class MyMouseMotionListener implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			if (e.getModifiers() == InputEvent.BUTTON1_MASK) {

				if (mDrawComponent.lastCmd != null) {
					Cmd cmd = mDrawComponent.lastCmd;
					cmd.setXYTo(e.getX(), e.getY());
					DrawerFrame.this.invalidate();
					DrawerFrame.this.repaint();
				}
			}
			if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
				mDrawComponent.setArrowPos(e.getX(), e.getY());
				DrawerFrame.this.invalidate();
				DrawerFrame.this.repaint();
			} else {
				if (mDrawComponent.pos == null) {
					mDrawComponent.pos = null;
					DrawerFrame.this.invalidate();
					DrawerFrame.this.repaint();
				}

			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

	}

	class MyMouseListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getModifiers() == InputEvent.BUTTON1_MASK) {
				if (cmdClass == Line.class) {
					mDrawComponent.newCmd(new Line());
				}
				if (cmdClass == Rect.class) {
					mDrawComponent.newCmd(new Rect());
				}
				mDrawComponent.lastCmd.setXY(e.getX(), e.getY());
				mDrawComponent.lastCmd.setXYTo(e.getX(), e.getY());
				DrawerFrame.this.invalidate();
				DrawerFrame.this.repaint();
			}

		}

		public void mouseReleased(MouseEvent e) {
			mDrawComponent.pos = null;
			DrawerFrame.this.invalidate();
			DrawerFrame.this.repaint();
		}

	}

	class MyKeyListener extends KeyAdapter {

		@Override
		public void keyTyped(KeyEvent e) {
			doButtonAction(e.getKeyChar());
		}

	}

	public class DrawComponent extends JComponent {

		ArrayList<Cmd> cmdList = new ArrayList<Cmd>();

		Cmd lastCmd;

		Position pos = null;

		Color mArrColor = (new Color(255, 128, 0, 80));

		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(3));

			g.setColor(new Color(255, 0, 0, 150));

			for (Cmd cmd : cmdList) {
				cmd.paint(g2);
			}
			if (lastCmd != null) {
				lastCmd.paint(g2);
			}

			if (pos != null) {
				g2.setColor(mArrColor);
				g2.fillOval(pos.x, pos.y, 100, 100);
			}
		}

		public void setArrowPos(int x, int y) {
			if (pos == null) {
				pos = new Position(x - 50, y - 50);
			} else {
				pos.x = x - 50;
				pos.y = y - 50;
			}

		}

		public void newCmd(Cmd cmd) {
			if (lastCmd != null) {
				cmdList.add(lastCmd);
			}
			lastCmd = cmd;
		}
	}

	public abstract class Cmd {
		abstract void paint(Graphics2D g2);

		abstract void setXY(int x, int y);

		abstract void setXYTo(int w, int h);

	}

	public class Position {
		int x, y;

		Position(int xx, int yy) {
			x = xx;
			y = yy;
		}
	}

	public class Line extends Cmd {
		int x, y, y2, x2;

		@Override
		void paint(Graphics2D g2) {
			if (Math.abs(y - y2) < Math.abs(x - x2)) {
				g2.drawLine(x, y2, x2, y2);
			} else {
				g2.drawLine(x2, y, x2, y2);
			}

		}

		@Override
		void setXY(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		void setXYTo(int xx, int yy) {
			this.x2 = xx;
			this.y2 = yy;
		}

	}

	public class Rect extends Cmd {
		int x, y, h, w;

		@Override
		void paint(Graphics2D g2) {
			int xx = x;
			int yy = y;
			int ww = w;
			int hh = h;
			if (w < 0) {
				ww = -w;
				xx = x + w;
			}
			if (h < 0) {
				hh = -h;
				yy = y + h;
			}
			g2.drawRect(xx, yy, ww, hh);
		}

		@Override
		void setXY(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		void setXYTo(int x2, int y2) {
			this.w = x2 - x;
			this.h = y2 - y;
		}

	}

	public class Col extends Cmd {
		Color c;

		int thickness;

		@Override
		void paint(Graphics2D g2) {
			g2.setColor(c);

		}

		@Override
		void setXY(int x, int y) {
		}

		@Override
		void setXYTo(int w, int h) {
		}

	}

	class MyFocusListener extends FocusAdapter {

		@Override
		public void focusGained(FocusEvent e) {
			setBackground(new Color(0, 1, 0, 1));
			setTitle(TITLE);
		}

	}

}

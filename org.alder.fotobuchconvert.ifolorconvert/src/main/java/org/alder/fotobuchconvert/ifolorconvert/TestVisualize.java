package org.alder.fotobuchconvert.ifolorconvert;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TestVisualize extends JFrame {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws Exception {

		Loader loader = new Loader();
		ProjectPath path = TestData.getTestProject();
		Book book = loader.load(path);

		TestVisualize f = new TestVisualize(book);
		f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		f.setLocationByPlatform(true);
		f.pack();
		f.setVisible(true);

	}

	protected int pageId;
	private final Book book;
	private BookPage page;

	public TestVisualize(final Book book) {
		this.book = book;
		pageId = 7;
		loadPage(pageId);

		setLayout(new BorderLayout());
		add(new JLabel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(1500, 600);
			}

			@Override
			public void paint(Graphics gg) {
				Graphics2D g = (Graphics2D) gg;

				super.paint(g);

				double f = .2;
				g.scale(f, f);

				AffineTransform baseTrans = g.getTransform();

				for (BookElement el : page.pics) {
					g.setTransform(baseTrans);
					g.translate(el.left, el.top);

					int offX = el.width / 2, offY;
					switch (el.dock) {
					case top:
						offY = el.height;
						break;
					case middle:
						offY = el.height / 2;
						break;
					case bottom:
						offY = 0;
						break;
					default:
						throw new RuntimeException(
								"This command should never be called");
					}
					g.translate(offX, offY);
					double ang = el.angleDegrees / 180f * Math.PI;
					g.rotate(ang);
					g.translate(-offX, -offY);

					Image img;
					if (el instanceof BookPicture) {
						BookPicture pic = (BookPicture) el;
						img = pic.getImage(book);
						int w = img.getWidth(null);
						int h = img.getHeight(null);
						System.out.println(">" + w + " " + h + " " + pic.cropX
								+ " " + pic.cropY + " " + pic.cropW + " "
								+ pic.cropH);
						if (w > 0) {
							g.drawImage(img, 0, 0, el.width, el.height,
									r(pic.cropX * w), r(pic.cropY * h),
									r((pic.cropX + pic.cropW) * w),
									r((pic.cropY + pic.cropH) * h), null);
						} else
							img = null;
					} else if (el instanceof BookText) {
						BookText text = (BookText) el;
						String txt = text.getRtfText(book);
						if (txt != null) {
							g.setFont(g.getFont().deriveFont(60f));
							FontMetrics fm = g.getFontMetrics();
							g.drawString(txt, 0, fm.getHeight());
						}
						// int w = img.getWidth(null);
						// int h = img.getHeight(null);
						// System.out.println(">" + w + " " + h + " " +
						// text.cropX
						// + " " + text.cropY + " " + text.cropW + " "
						// + text.cropH);
						// if (w > 0) {
						// g.drawImage(img, 0, 0, el.width, el.height,
						// r(text.cropX * w), r(text.cropY * h),
						// r((text.cropX + text.cropW) * w),
						// r((text.cropY + text.cropH) * h), null);
						// } else
						img = null;
					} else {
						img = null;
					}

					if (img == null) {
						g.setColor(Color.LIGHT_GRAY);
						g.drawRect(0, 0, el.width, el.height);
						g.drawLine(0, 0, el.width, el.height);
						g.drawLine(0, el.height, el.width, 0);
					}
					// g.drawImage(img, 0, 0, pic.width, pic.height, null);
				}
				g.setTransform(baseTrans);
				pageBorders(g);
			}

			private void pageBorders(Graphics2D g) {
				g.setColor(Color.BLUE);

				// final int ox = 102;
				// final int oy = 102;
				// // <Width>7712</Width>
				// // <Height>2953</Height>
				// g.drawRect(ox + 0, oy + 0, ox + 7712, oy + 2953);
				// // <GuideLine x1="3778" y1="0" x2="3778" y2="2953" />
				// g.drawLine(ox + 3778, oy + 0, ox + 3778, oy + 2953);
				// // <GuideLine x1="3934" y1="0" x2="3934" y2="2953" />
				// g.drawLine(ox + 3934, oy + 0, ox + 3934, oy + 2953);
				// // <GuideBox left="220" top="220" width="7272" height="2513"
				// />
				// g.drawRect(ox + 220, oy + 220, ox + 7272, oy + 2513);

				final int ox = 0;
				final int oy = 0;
				// <LeftSideOffset>0</LeftSideOffset>
				// <RightSideOffset>3531</RightSideOffset>
				// <LeftPageOffset>-102</LeftPageOffset>
				// <RightPageOffset>102</RightPageOffset>
				// <Width>7062</Width>
				// <Height>2504</Height>
				g.drawRect(ox + 0, oy + 0, ox + 7062, oy + 2504);
				// <GuideLine x1="3531" y1="0" x2="3531" y2="2504" />
				g.drawLine(ox + 3531, oy + 0, ox + 3531, oy + 2504);
				// <GuideBox left="40" top="40" width="6982" height="2424" />
				g.drawRect(ox + 40, oy + 40, ox + 6982, oy + 2424);
			}

			private int r(double d) {
				return (int) d;
			}
		}, BorderLayout.CENTER);

		JPanel control = new JPanel();
		final JLabel label = new JLabel();
		control.add(label);
		JButton b = new JButton("<");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (--pageId < 0)
					pageId = 0;
				label.setText(String.valueOf(pageId));
				loadPage(pageId);
			}
		});
		control.add(b);
		b = new JButton(">");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (++pageId >= book.pages.size())
					pageId = book.pages.size() - 1;
				label.setText(String.valueOf(pageId));
				loadPage(pageId);
			}
		});
		control.add(b);
		add(control, BorderLayout.SOUTH);
	}

	protected void loadPage(int pageId) {
		page = book.pages.get(pageId);
		System.out.println("pg " + pageId + "  " + page);
		repaint(1);
	}
}

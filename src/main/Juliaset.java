package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Juliaset extends JLabel implements MouseWheelListener, KeyListener {

	private int ITERATIONS = 100;
	private float hueOffset = 0.5f; // Change to set color style
	private double animationSpeed = 1.213;// Animation speed
	private double Zoom = 6.25;
	private double yZoom = Zoom * Main.HEIGHT / Main.WIDTH;
	private double angle = 0;
	private double xOffset = 0, yOffset = 0;
	private double cReal, cImaginary;

	private boolean autopilot = false; // Switch between animation and mouse mode
	private boolean jump = false;// false
	private boolean mandelbrot = true;
	private boolean lock = false;

	private Dimension juliaFrameLocation = new Dimension((int) (Main.WIDTH - Main.WIDTH / 2), 0);

	private JFrame frame;
	private static BufferedImage buffer;

	public Juliaset() {

		frame = new JFrame("Julia set");

		buffer = new BufferedImage((int) Main.WIDTH, (int) Main.HEIGHT, BufferedImage.TYPE_INT_RGB);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize((int) Main.WIDTH, (int) Main.HEIGHT);
		frame.setLocation(juliaFrameLocation.width, juliaFrameLocation.height);
		frame.setUndecorated(true);

		frame.add(this);
		frame.addMouseWheelListener(this);
		frame.addKeyListener(this);

		frame.setVisible(true);

	}

	public void renderJuliaset() {

		if (autopilot == true && jump == false && mandelbrot == false) {

			if (lock == false) {

				cReal = (double) Math.cos(angle * animationSpeed);
				cImaginary = (double) -1 * Math.sin(angle);
				angle += 0.02;

			}

		} else if (autopilot == false && jump == false && mandelbrot == true) {

			if (lock == false) {

				cReal = map(MouseInfo.getPointerInfo().getLocation().getX(),
						(double) Mandelbrotset.mandelFrameLocation.width,
						(double) Mandelbrotset.mandelFrameLocation.width + Main.WIDTH, Mandelbrotset.getCR(0),
						Mandelbrotset.getCR(Main.WIDTH));

				cImaginary = map(MouseInfo.getPointerInfo().getLocation().getY(),
						(double) Mandelbrotset.mandelFrameLocation.height,
						(double) Mandelbrotset.mandelFrameLocation.height + Main.HEIGHT, Mandelbrotset.getCI(0),
						Mandelbrotset.getCI(Main.HEIGHT));

			}

		}

		yZoom = Zoom * Main.HEIGHT / Main.WIDTH;

		double xMin = -1 * Zoom / 2;
		double yMin = -1 * yZoom / 2;

		double xMax = xMin + Zoom;
		double yMax = yMin + yZoom;

		double deltaxPercentage = (xMax - xMin) / Main.WIDTH;
		double deltayPercentage = (yMax - yMin) / Main.HEIGHT;

		double x = xMin;

		for (int i = 0; i < Main.WIDTH; i++) {

			double y = yMin;

			for (int j = 0; j < Main.HEIGHT; j++) {

				int color;

				double zReal = x + xOffset;
				double zImaginary = y + yOffset;

				int iteration = 0;

				while (iteration < ITERATIONS && Math.sqrt(zReal * zReal + zImaginary * zImaginary) <= 2) {

					double temp = zReal * zReal - zImaginary * zImaginary + cReal;

					zImaginary = 2 * zReal * zImaginary + cImaginary;

					zReal = temp;

					iteration++;
				}

				if (iteration == ITERATIONS) {
					color = 0x00000000;
				} else {
					color = Color.HSBtoRGB(((float) iteration / (float) ITERATIONS + hueOffset) % 1f, 0.5f, 1);
				}

				buffer.setRGB(i, j, color);

				y += deltayPercentage;
			}
			x += deltaxPercentage;
		}

	}

	public double map(double input, double oldmin, double oldmax, double newmin, double newmax) {
		return (input - oldmin) * (newmax - newmin) / (oldmax - oldmin) + newmin;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_L) {
			if (lock == false) {
				lock = true;
			} else {
				lock = false;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_M) {

			mandelbrot = true;
			autopilot = false;
			jump = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_E || e.getKeyCode() == KeyEvent.VK_C) {
			System.exit(0);
		}

		if (e.getKeyCode() == KeyEvent.VK_W) {
			yOffset -= 0.1 * yZoom;
		}

		if (e.getKeyCode() == KeyEvent.VK_S) {
			yOffset += 0.1 * yZoom;
		}

		if (e.getKeyCode() == KeyEvent.VK_A) {
			xOffset -= 0.1 * Zoom;
		}

		if (e.getKeyCode() == KeyEvent.VK_D) {
			xOffset += 0.1 * Zoom;
		}

		if (e.getKeyCode() == KeyEvent.VK_MINUS) {
			ITERATIONS *= 0.9;
		}

		if (e.getKeyCode() == KeyEvent.VK_PLUS) {
			ITERATIONS /= 0.9;
		}

		if (e.getKeyCode() == KeyEvent.VK_F) {
			hueOffset = Float.parseFloat(JOptionPane.showInputDialog("Color offset?"));
		}

		if (e.getKeyCode() == KeyEvent.VK_I) {
			JOptionPane.showMessageDialog(this,
					"Iterations: " + ITERATIONS + "\n" + "hueOffset:" + hueOffset + "\n" + "Zoom: " + Zoom + "\n"
							+ "xOffset: " + xOffset + "\n" + "yOffset: " + yOffset + "\n" + "cReal: " + cReal + "\n"
							+ "cImaginary: " + cImaginary);
		}

		if (e.getKeyCode() == KeyEvent.VK_G) {

			jump = true;

			ITERATIONS = Integer.parseInt(JOptionPane.showInputDialog("Iteration?"));
			Zoom = Double.parseDouble(JOptionPane.showInputDialog("Zoom?"));
			xOffset = Double.parseDouble(JOptionPane.showInputDialog("xOffset?"));
			yOffset = Double.parseDouble(JOptionPane.showInputDialog("yOffset?"));
			cReal = Double.parseDouble(JOptionPane.showInputDialog("cReal?"));
			cImaginary = Double.parseDouble(JOptionPane.showInputDialog("cImaginary?"));
		}

		if (e.getKeyCode() == KeyEvent.VK_P) {

			autopilot = true;
			jump = false;
			mandelbrot = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_V) {
			animationSpeed = Double.parseDouble(JOptionPane.showInputDialog("Animation Speed?"));
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		if (e.getWheelRotation() < 0) {
			Zoom *= 0.8;
		} else if (e.getWheelRotation() > 0) {
			Zoom /= 0.8;
		}

	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		renderJuliaset();

		g.drawImage(buffer, 0, 0, null);
		g.setColor(Color.BLACK);

		if (cImaginary >= 0) {
			g.drawString("" + (float) cReal + " + " + (float) cImaginary + "i", 5,
					Mandelbrotset.mandelFrameLocation.height - 5);
		} else {
			g.drawString("" + (float) cReal + " - " + -1 * (float) cImaginary + "i", 5,
					Mandelbrotset.mandelFrameLocation.height - 5);
		}

		repaint();
	}

}

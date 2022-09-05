package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class Mandelbrotset extends JLabel implements MouseWheelListener, KeyListener {

	private static int ITERATIONS = 100;
	private static double middleR = -0.64;
	private static double middleI = 0.113;
	private static double rangeR = 4;
	private static double rangeI = rangeR * Main.HEIGHT / Main.WIDTH;

	private float hueOffset = 0.5f; // Change to set color style

	private JFrame frame;
	private BufferedImage buffer;

	public static Dimension mandelFrameLocation = new Dimension((int) (Main.WIDTH - Main.WIDTH / 2), (int) Main.HEIGHT);

	public Mandelbrotset() {

		frame = new JFrame("Mandelbrot set");

		buffer = new BufferedImage((int) Main.WIDTH, (int) Main.HEIGHT, BufferedImage.TYPE_INT_RGB);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize((int) Main.WIDTH, (int) Main.HEIGHT);
		frame.setLocation(mandelFrameLocation.width, mandelFrameLocation.height);
		frame.setUndecorated(true);

		frame.add(this);
		frame.addMouseWheelListener(this);
		frame.addKeyListener(this);

		frame.setVisible(true);

	}

	public static double getCR(double x) {
		return x / Main.WIDTH * rangeR + middleR - rangeR / 2;
	}

	public static double getCI(double y) {
		return -1 * (y / Main.HEIGHT * rangeI + middleI - rangeI / 2);
	}

	public void renderMandelbrotset() {

		for (int x = 0; x < Main.WIDTH; x++) {
			for (int y = 0; y < Main.HEIGHT; y++) {

				int color;

				double cReal = getCR(x);
				double cImaginary = getCI(y);

				double zReal = 0;
				double zImaginary = 0;

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

				buffer.setRGB(x, y, color);

			}
		}

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		if (e.getWheelRotation() < 0) {
			rangeR *= 0.8;
			rangeI *= 0.8;
		} else if (e.getWheelRotation() > 0) {
			rangeR /= 0.8;
			rangeI /= 0.8;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_E || e.getKeyCode() == KeyEvent.VK_C) {
			System.exit(0);
		}

		if (e.getKeyCode() == KeyEvent.VK_W) {
			middleI -= 0.1 * rangeI;
		}

		if (e.getKeyCode() == KeyEvent.VK_S) {
			middleI += 0.1 * rangeI;
		}

		if (e.getKeyCode() == KeyEvent.VK_A) {
			middleR -= 0.1 * rangeR;
		}

		if (e.getKeyCode() == KeyEvent.VK_D) {
			middleR += 0.1 * rangeR;
		}

		if (e.getKeyCode() == KeyEvent.VK_MINUS) {
			ITERATIONS *= 0.9;
		}

		if (e.getKeyCode() == KeyEvent.VK_PLUS) {
			ITERATIONS /= 0.9;
		}

		if (e.getKeyCode() == KeyEvent.VK_I) {
			JOptionPane.showMessageDialog(this,
					"Iterations: " + ITERATIONS + "\n" + "middleR: " + middleR + "\n" + "middleI:" + middleI + "\n"
							+ "rangeR: " + rangeR + "\n" + "rangeI:" + rangeI + "\n" + "hueOffset:" + hueOffset);
		}

		if (e.getKeyCode() == KeyEvent.VK_G) {
			ITERATIONS = Integer.parseInt(JOptionPane.showInputDialog("Iteration?"));
			middleR = Double.parseDouble(JOptionPane.showInputDialog("MiddleR?"));
			middleI = Double.parseDouble(JOptionPane.showInputDialog("MiddleI?"));
			rangeR = Double.parseDouble(JOptionPane.showInputDialog("RangeR?"));
			rangeI = Double.parseDouble(JOptionPane.showInputDialog("RangeI?"));
		}

		if (e.getKeyCode() == KeyEvent.VK_F) {
			hueOffset = Float.parseFloat(JOptionPane.showInputDialog("Color offset?"));
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		renderMandelbrotset();

		g.drawImage(buffer, 0, 0, null);
		repaint();
	}

}

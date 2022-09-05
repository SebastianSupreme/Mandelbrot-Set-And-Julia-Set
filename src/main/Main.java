package main;

import java.awt.Toolkit;

public class Main {

	public static double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	public static final double WIDTH = Math.floor(screenWidth / 2);
	public static final double HEIGHT = Math.floor(screenHeight / 2);

	public static void main(String[] args) {

		new Juliaset();
		new Mandelbrotset();

	}

}

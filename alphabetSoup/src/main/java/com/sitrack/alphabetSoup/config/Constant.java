package com.sitrack.alphabetSoup.config;

import java.util.Random;

public class Constant {

	
	//Game parameters
	public static final int WIDTH = 15;
	public static final int HEIGHT = 15;
	public static final boolean LEFT2RIGHT = true;
	public static final boolean RIGHT2LEFT = false;
	public static final boolean TOP2BOTTOM = true;
	public static final boolean BOTTOM2TOP = false;
	public static final boolean DIAGONAL = false;
	public static final int WORDSFORCARD = 10;
	
	//Global parameters
	public static final Random R = new Random();
	public static final int WORDMINLENGTH = 4;
	public static final int LTRWAY = 1;
	public static final int RTLWAY = 2;
	public static final int TTBWAY = 3;
	public static final int BTTWAY = 4;
	public static final int DWAY = 5;
	public static final int INIWAY = 0;
	
}

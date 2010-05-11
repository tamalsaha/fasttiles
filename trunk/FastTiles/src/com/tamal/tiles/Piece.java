package com.tamal.tiles;

import java.util.HashMap;

public abstract class Piece {
	public static final int ROTATE_0 = 0x01;
	public static final int ROTATE_90 = 0x02;
	public static final int ROTATE_180 = 0x04;
	public static final int ROTATE_270 = 0x08;
	public static final int FLIP_ROTATE_0 = 0x10;
	public static final int FLIP_ROTATE_90 = 0x20;
	public static final int FLIP_ROTATE_180 = 0x40;
	public static final int FLIP_ROTATE_270 = 0x80;

	public static final int[] ORIENTATION = new int[] { ROTATE_0, ROTATE_90,
			ROTATE_180, ROTATE_270, FLIP_ROTATE_0, FLIP_ROTATE_90,
			FLIP_ROTATE_180, FLIP_ROTATE_270 };

	public static final int[] ROTATION = new int[] { ROTATE_0, ROTATE_90,
			ROTATE_180, ROTATE_270 };

	public static final int RF_90_270 = ROTATE_90 | ROTATE_270 | FLIP_ROTATE_90
			| FLIP_ROTATE_270;

	public static final String NEWLINE = System.getProperty("line.separator");
	public static final char BLANK = ' ';

	protected int id = -1;
	protected int right = 0;
	protected int bottom = 0;

	public abstract IntChPair[][] getData();

	public abstract int width();

	public abstract int height();

	public abstract int size();

	protected int getX(Square sq, int orientation) {
		return getX(sq.x, sq.y, orientation);
	}

	protected int getX(int x, int y, int orientation) {
		switch (orientation) {
		case ROTATE_90:
			return y;
		case ROTATE_180:
			return right - x;
		case ROTATE_270:
			return bottom - y;
		case FLIP_ROTATE_0:
			return right - x;
		case FLIP_ROTATE_90:
			return y;
		case FLIP_ROTATE_180:
			return x;
		case FLIP_ROTATE_270:
			return bottom - y;
		default:
			return x;
		}
	}

	protected int getY(Square sq, int orientation) {
		return getY(sq.x, sq.y, orientation);
	}

	protected int getY(int x, int y, int orientation) {
		switch (orientation) {
		case ROTATE_90:
			return right - x;
		case ROTATE_180:
			return bottom - y;
		case ROTATE_270:
			return x;
		case FLIP_ROTATE_0:
			return y;
		case FLIP_ROTATE_90:
			return x;
		case FLIP_ROTATE_180:
			return bottom - y;
		case FLIP_ROTATE_270:
			return right - x;
		default:
			return y;
		}
	}

	public String getOrientationString(int orientation) {
		switch (orientation) {
		case ROTATE_90:
			return "ROTATE_90";
		case ROTATE_180:
			return "ROTATE_180";
		case ROTATE_270:
			return "ROTATE_270";
		case FLIP_ROTATE_0:
			return "FLIP_ROTATE_0";
		case FLIP_ROTATE_90:
			return "FLIP_ROTATE_90";
		case FLIP_ROTATE_180:
			return "FLIP_ROTATE_180";
		case FLIP_ROTATE_270:
			return "FLIP_ROTATE_270";
		default:
			return "ROTATE_0";
		}
	}

	public void print() {
		IntChPair[][] data = getData();
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				System.out.print(data[i][j]);
			}
			System.out.println();
		}
	}

	@Override
	public String toString() {
		IntChPair[][] data = getData();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				sb.append(data[i][j]);
			}
			sb.append(NEWLINE);
		}
		return sb.toString();
	}
}

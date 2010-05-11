package com.tamal.tiles;

public class Square {
	int x;
	int y;
	char ch;

	public Square(int x, int y, char ch) {
		this.x = x;
		this.y = y;
		this.ch = ch;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public char getCh() {
		return ch;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Square))
			return false;
		Square o = (Square) obj;
		return this.x == o.x && this.y == o.y && this.ch == o.ch;
	}

	@Override
	public int hashCode() {
		return x ^ y ^ ch;
	}

	@Override
	public String toString() {
		return String.format("(%s•%s:%s)", x, y, ch);
	}
}

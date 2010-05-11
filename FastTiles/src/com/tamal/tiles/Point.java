package com.tamal.tiles;

public class Point {
	int x, y;

	public Point() {
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return String.format("(%s, %s)", x, y);
	}

	@Override
	public int hashCode() {
		return x ^ y;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Point))
			return false;
		Point o = (Point) obj;
		return x == o.x && y == o.y;
	}
}

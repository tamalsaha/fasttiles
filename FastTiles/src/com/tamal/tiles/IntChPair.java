package com.tamal.tiles;

public class IntChPair {
	int a;
	char b;

	public IntChPair(int a, char b) {
		this.a = a;
		this.b = b;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public char getB() {
		return b;
	}

	public void setB(char b) {
		this.b = b;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IntChPair))
			return false;
		IntChPair o = (IntChPair) obj;
		return this.a == o.getA() && this.b == o.getB();
	}

	@Override
	public int hashCode() {
		return a ^ b;
	}

	@Override
	public String toString() {
		return String.format("(%s•%s)", a, b);
	}
}

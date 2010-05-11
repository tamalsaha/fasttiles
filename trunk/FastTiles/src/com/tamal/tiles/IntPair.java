package com.tamal.tiles;

public class IntPair {
	int a;
	int b;

	public IntPair(int a, int b) {
		this.a = a;
		this.b = b;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IntPair))
			return false;
		IntPair o = (IntPair) obj;
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

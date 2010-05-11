package com.tamal.tiles;

public class Solution extends Piece {

	private IntChPair[][] data;
	private int size;

	protected Solution(Board board, IntChPair[][] data) {
		this.right = board.right;
		this.bottom = board.bottom;

		this.data = data;
		this.size = board.size();
	}

	@Override
	public IntChPair[][] getData() {
		return data;
	}

	@Override
	public int height() {
		return bottom + 1;
	}

	@Override
	public int width() {
		return right + 1;
	}

	@Override
	public int size() {
		return size;
	}

	public boolean sameAs(Solution ref) {
		int h = ref.height();
		int w = ref.width();

		int[] oArr;
		if (h == w) {
			oArr = new int[] { ROTATE_0, ROTATE_90, ROTATE_180, ROTATE_270,
					FLIP_ROTATE_0, FLIP_ROTATE_90, FLIP_ROTATE_180,
					FLIP_ROTATE_270 };
		} else {
			oArr = new int[] { ROTATE_0, ROTATE_180, FLIP_ROTATE_0,
					FLIP_ROTATE_180 };
		}

		boolean matched = false;
		int i, j;
		for (int index = 0; index < oArr.length && !matched; index++) {
			for (j = 0; j < h && !matched; j++) {
				for (i = 0; i < w && !matched; i++) {
					if (!ref.data[j][i]
							.equals(data[getY(i, j, oArr[index])][getX(i, j,
									oArr[index])])) {
						break;
					}
				}
				if (i < w)
					break;
			}
			matched = j == h;
		}
		return matched;
	}
}

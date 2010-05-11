package com.tamal.tiles;

public class Board extends Piece {

	char[][] square;
	int[][] squareId;
	private int size;
	private int squareNotFilled = 0;

	public Board(int id, RawPiece raw) {
		this.id = id;
		this.size = raw.size();
		this.squareNotFilled = size;

		int rawLeft = raw.getLeft();
		int rawTop = raw.getTop();
		this.right = raw.getRight() - rawLeft;
		this.bottom = raw.getBottom() - rawTop;

		int h = height();
		int w = width();
		square = new char[h][w];
		squareId = new int[h][w];
		for (int j = square.length; j-- > 0;) {
			for (int i = square[j].length; i-- > 0;) {
				square[j][i] = Piece.BLANK;
				squareId[j][i] = -1;
			}
		}
		for (Square p : raw.points) {
			square[p.y - rawTop][p.x - rawLeft] = p.ch;
			squareId[p.y - rawTop][p.x - rawLeft] = id;
		}
	}

	public long addtime = 0;
	public long removetime = 0;

	public boolean putPiece(Tile tile, int row, int col) {
		long start = System.currentTimeMillis();
		boolean result = putPieceInner(tile, row, col);
		addtime += System.currentTimeMillis() - start;
		return result;
	}

	public boolean putPieceInner(Tile tile, int row, int col) {
		int height = height();
		int width = width();
		int[] pX = tile.pX[tile.curO];
		int[] pY = tile.pY[tile.curO];
		char[] pCh = tile.pCh[tile.curO];
		int x, y;
		for (int index = tile.size(); index-- > 0;) {
			x = col + pX[index];
			y = row + pY[index];
			if (y < 0 || y >= height || x < 0 || x >= width
					|| square[y][x] != pCh[index] || squareId[y][x] != id) // implicitly
				return false;
		}
		for (int index = tile.size(); index-- > 0;) {
			x = col + pX[index];
			y = row + pY[index];

			squareId[y][x] = tile.id;
		}
		squareNotFilled -= tile.size();
		return true;
	}

	public void removePiece(Tile tile, int row, int col) {
		long start = System.currentTimeMillis();
		removePieceInner(tile, row, col);
		removetime += System.currentTimeMillis() - start;
	}

	public void removePieceInner(Tile tile, int row, int col) {
		int[] pX = tile.pX[tile.curO];
		int[] pY = tile.pY[tile.curO];
		int x, y;
		for (int index = tile.size(); index-- > 0;) {
			x = col + pX[index];
			y = row + pY[index];

			squareId[y][x] = id;
		}
		squareNotFilled += tile.size();
	}

	public MergedBoard getMergedBoard() {
		int h = height();
		int w = width();
		char[][] data = new char[h][w];
		for (int j = h; j-- > 0;)
			for (int i = w; i-- > 0;)
				data[j][i] = square[j][i];

		for (int j = h; j-- > 0;)
			for (int i = w; i-- > 0;)
				if (squareId[j][i] != id)
					data[j][i] = Piece.BLANK;

		return new MergedBoard(data, false);
	}

	public Solution getSolution() {
		return new Solution(this, getData());
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

	public boolean isFull() {
		return squareNotFilled == 0;
	}

	@Override
	public IntChPair[][] getData() {
		IntChPair[][] data = new IntChPair[height()][width()];
		for (int j = square.length; j-- > 0;)
			for (int i = square[j].length; i-- > 0;)
				data[j][i] = new IntChPair(squareId[j][i], square[j][i]);
		return data;
	}
}

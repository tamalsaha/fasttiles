package com.tamal.tiles;

import java.util.ArrayList;
import java.util.Arrays;

public class MergedBoard {

	private char[][] in;
	private ArrayList<RawPiece> rawPieces = new ArrayList<RawPiece>();
	private RawPiece maxPiece = null;
	private RawPiece minPiece = null;

	public MergedBoard(char[][] in, boolean copy) {
		if (copy) {
			char[][] clone = new char[in.length][];
			for (int i = in.length; i-- > 0;) {
				clone[i] = new char[in[i].length];
				for (int j = in[i].length; j-- > 0;) {
					clone[i][j] = in[i][j];
				}
			}
			this.in = clone;
		} else
			this.in = in;
	}

	public MergedBoard(ArrayList<char[]> inList, boolean copy) {
		if (copy) {
			char[][] clone = new char[inList.size()][];
			for (int i = inList.size(); i-- > 0;) {
				clone[i] = Arrays.copyOf(inList.get(i), Integer.MAX_VALUE);
			}
			this.in = clone;
		} else {
			this.in = new char[inList.size()][];
			for (int i = inList.size(); i-- > 0;)
				in[i] = inList.get(i);
		}
	}

	public ArrayList<RawPiece> getRawPieces() {
		return rawPieces;
	}

	public RawPiece getMaxPiece() {
		return maxPiece;
	}

	public RawPiece getMinPiece() {
		return minPiece;
	}

	public void parse() {
		int i, j;
		RawPiece piece;
		for (j = 0; j < in.length; j++) {
			for (i = 0; i < in[j].length; i++) {
				if (in[j][i] != Piece.BLANK) {
					// detect & explore raw piece and clear the points using ' '
					piece = new RawPiece();
					explore(in, i, j, piece);
					rawPieces.add(piece);
					if (maxPiece == null) {
						maxPiece = piece;
						minPiece = piece;
					} else if (piece.compareTo(maxPiece) > 0) {
						maxPiece = piece;
					} else if (piece.compareTo(minPiece) < 0) {
						minPiece = piece;
					}
				}
			}
		}
	}

	private void explore(char[][] in, int x, int y, RawPiece piece) {
		piece.addPoint(x, y, in[y][x]);
		in[y][x] = Piece.BLANK;

		if (withinBound(in, x - 1, y - 1) && in[y - 1][x - 1] != Piece.BLANK) {
			explore(in, x - 1, y - 1, piece);
		}
		if (withinBound(in, x - 1, y) && in[y][x - 1] != Piece.BLANK) {
			explore(in, x - 1, y, piece);
		}
		if (withinBound(in, x - 1, y + 1) && in[y + 1][x - 1] != Piece.BLANK) {
			explore(in, x - 1, y + 1, piece);
		}

		if (withinBound(in, x, y - 1) && in[y - 1][x] != Piece.BLANK) {
			explore(in, x, y - 1, piece);
		}
		if (withinBound(in, x, y + 1) && in[y + 1][x] != Piece.BLANK) {
			explore(in, x, y + 1, piece);
		}

		if (withinBound(in, x + 1, y - 1) && in[y - 1][x + 1] != Piece.BLANK) {
			explore(in, x + 1, y - 1, piece);
		}
		if (withinBound(in, x + 1, y) && in[y][x + 1] != Piece.BLANK) {
			explore(in, x + 1, y, piece);
		}
		if (withinBound(in, x + 1, y + 1) && in[y + 1][x + 1] != Piece.BLANK) {
			explore(in, x + 1, y + 1, piece);
		}
	}

	private boolean withinBound(char[][] in, int x, int y) {
		return y >= 0 && y < in.length && x >= 0 && x < in[y].length;
	}
}

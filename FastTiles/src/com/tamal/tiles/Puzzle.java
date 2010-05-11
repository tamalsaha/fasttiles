package com.tamal.tiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class Puzzle {

	private Board board;
	private Tile[] tiles;

	private boolean allSameSizeTiles;
	private int singleTileSize;
	private int totalTileSquare;

	private Tile[] participants;

	private HashSet<IMonitor> monitors = new HashSet<IMonitor>();

	public void addMonitor(IMonitor monitor) {
		if (monitor != null)
			monitors.add(monitor);
	}

	public void removeMonitor(IMonitor monitor) {
		if (monitor != null)
			monitors.remove(monitor);
	}

	protected void OnProgress(int event) {
		for (IMonitor monitor : monitors) {
			monitor.update(event);
		}
	}

	public Tile[] getTiles() {
		return tiles;
	}

	public Board getBoard() {
		return board;
	}

	public ArrayList<Solution> getSolutions() {
		return sols;
	}

	public int getSolutionCount() {
		return sols.size();
	}

	public void parse(String path) throws IOException {
		parse(new FileInputStream(new File(path)));
	}

	public void parse(InputStream inStream) throws IOException {
		ArrayList<char[]> fullInput = new ArrayList<char[]>();

		BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
		try {
			String line = null;
			while ((line = in.readLine()) != null) {
				fullInput.add(line.toCharArray());
			}
		} finally {
			in.close();
		}

		MergedBoard merged = new MergedBoard(fullInput, false);
		merged.parse();
		ArrayList<RawPiece> rawPieces = merged.getRawPieces();
		rawPieces.remove(merged.getMaxPiece());
		board = new Board(0, merged.getMaxPiece());
		tiles = new Tile[rawPieces.size()];

		singleTileSize = rawPieces.get(0).size();
		allSameSizeTiles = true;
		int i = 0;
		totalTileSquare = 0;
		for (RawPiece rawPiece : rawPieces) {
			tiles[i] = new Tile(i + 1, rawPiece);
			totalTileSquare += tiles[i].size();
			if (singleTileSize != tiles[i].size())
				allSameSizeTiles = false;
			i++;
		}
	}

	long totalTime = 0;
	long deadEndTime = 0;

	public long getTotalTime() {
		return totalTime;
	}

	private int[] order;
	private int[] orientation;

	private ArrayList<Solution> sols = new ArrayList<Solution>();
	private boolean singleSolution;

	public void solve(boolean singleSolution, boolean useRotation,
			boolean useReflection) {
		sols.clear();
		totalTime = 0;
		this.singleSolution = singleSolution;

		setOrientationMode(useRotation, useReflection);
		for (int index = 0; index < tiles.length; index++) {
			tiles[index].process(orientation);
		}

		if (board.size() == totalTileSquare) {

			participants = new Tile[tiles.length];
			for (int index = 0; index < tiles.length; index++) {
				participants[index] = tiles[index];
			}
			solveThis();
		} else if (board.size() < totalTileSquare) {

			selectParticipants(new BitSet(tiles.length), 0, board.size());
		} else {

			totalTime = 0;
		}
	}

	private void selectParticipants(BitSet pbits, int index, int unfilled) {
		int newUnFilled;
		for (int i = 2; i-- > 0;) {
			if (i == 1) {
				pbits.set(index);
				newUnFilled = unfilled - tiles[index].size();
			} else {
				pbits.clear(index);
				newUnFilled = unfilled;
			}

			if (newUnFilled > 0) {
				if (index + 1 < tiles.length)
					selectParticipants(pbits, index + 1, newUnFilled);
			} else if (newUnFilled == 0) {
				participants = new Tile[pbits.cardinality()];
				int k = 0;
				for (int j = 0; j <= index; j++) {
					if (pbits.get(j))
						participants[k++] = tiles[j];
				}
				solveThis();
			} else
				return;

			if (singleSolution && sols.size() == 1) {
				return;
			}
		}
	}

	private void solveThis() {
		orderTiles();

		int startRow = 0;
		int startCol = 0;
		while (board.squareId[startRow][startCol] != board.id) {
			startCol++;
			if (startCol == board.width()) {
				startCol = 0;
				startRow++;
			}
		}

		this.deadEndTime = 0;
		this.board.addtime = 0;
		this.board.removetime = 0;
		long startTime = System.currentTimeMillis();

		play(startRow, startCol);

		totalTime = System.currentTimeMillis() - startTime;
		System.err.println(String.format(
				"addtime = %s ms removetime = %s ms, deadEndTime = %s ms",
				board.addtime, board.removetime, this.deadEndTime));
	}

	private void orderTiles() {
		// unchanged order
		order = new int[participants.length];
		for (int i = 0; i < participants.length; i++)
			order[i] = i;

		// // modify order based on # of possible placement positions
		// IntPair[] posCount = new IntPair[tiles.length];
		// int index, count;
		// Tile tile;
		// for (index = 0; index < tiles.length; index++) {
		// count = 0;
		// tile = tiles[index];
		// for (int o = 0; o < orientation.length; o++) {
		// if (tile.distinctOrientation(orientation[o])) {
		// tile.setOrientation(orientation[o]);
		// for (int j = 0; j < board.height(); j++) {
		// for (int i = 0; i < board.width(); i++) {
		// tile.setPos(i, j);
		// if (board.add(tiles[index])) {
		// count++;
		// board.remove(tile);
		// }
		// }
		// }
		// }
		// }
		// posCount[index] = new IntPair(index, count);
		// tile.setOrientation(Piece.ROTATE_0);
		// }
		// Arrays.sort(posCount, new Comparator<IntPair>() {
		// @Override
		// public int compare(IntPair p1, IntPair p2) {
		// return p1.getB() - p2.getB();
		// }
		// });
		// order = new int[tiles.length];
		// for (index = 0; index < tiles.length; index++) {
		// order[index] = posCount[index].getA();
		// }

		// // modify order based on symmetryCount
		// IntPair[] posCount = new IntPair[tiles.length];
		// for (int index = 0; index < tiles.length; index++) {
		// posCount[index] = new IntPair(index, tiles[index]
		// .getSymmetryCount());
		// }
		// Arrays.sort(posCount, new Comparator<IntPair>() {
		// @Override
		// public int compare(IntPair p1, IntPair p2) {
		// return p1.getB() - p2.getB();
		// }
		// });
		// order = new int[tiles.length];
		// for (int index = 0; index < tiles.length; index++) {
		// order[index] = posCount[index].getA();
		// }

		// // REVERSE order based on symmetryCount
		// IntPair[] posCount = new IntPair[tiles.length];
		// for (int index = 0; index < tiles.length; index++) {
		// posCount[index] = new IntPair(index, tiles[index]
		// .getSymmetryCount());
		// }
		// Arrays.sort(posCount, new Comparator<IntPair>() {
		// @Override
		// public int compare(IntPair p1, IntPair p2) {
		// return p2.getB() - p1.getB();
		// }
		// });
		// order = new int[tiles.length];
		// for (int index = 0; index < tiles.length; index++) {
		// order[index] = posCount[index].getA();
		// }
	}

	private void setOrientationMode(boolean useRotation, boolean useReflection) {
		if (useRotation && useReflection) {
			orientation = new int[] { Piece.ROTATE_0, Piece.ROTATE_90,
					Piece.ROTATE_180, Piece.ROTATE_270, Piece.FLIP_ROTATE_0,
					Piece.FLIP_ROTATE_90, Piece.FLIP_ROTATE_180,
					Piece.FLIP_ROTATE_270 };
		} else if (useRotation && !useReflection) {
			orientation = new int[] { Piece.ROTATE_0, Piece.ROTATE_90,
					Piece.ROTATE_180, Piece.ROTATE_270 };
		} else if (!useRotation && useReflection) {
			orientation = new int[] { Piece.ROTATE_0, Piece.ROTATE_180,
					Piece.FLIP_ROTATE_0, Piece.FLIP_ROTATE_180 };
		} else {
			orientation = new int[] { Piece.ROTATE_0 };
		}
	}

	private void play(int row, int col) {
		for (int index = 0; index < participants.length; index++) {
			if (order[index] < 0)
				continue; // tile already used

			Tile tile = participants[order[index]];
			for (tile.curO = 0; tile.curO < tile.oCount; tile.curO++) {
				if (!board.putPiece(tile, row, col))
					continue;
				order[index] -= order.length;

				if (board.isFull()) {
					Solution sol = board.getSolution();
					// if (!foundSymmetry(sol)) {
					sols.add(sol);
					OnProgress(IMonitor.NEW_SOL);
					// }
				} else if (!deadendReached()) {
					// find next empty space, going
					// left-to-right then top-to-bottom
					int nextRow = row;
					int nextCol = col;
					while (board.squareId[nextRow][nextCol] != board.id) {
						nextCol++;
						if (nextCol == board.width()) {
							nextCol = 0;
							nextRow++;
						}
					}
					play(nextRow, nextCol); // and try to complete the
				}

				board.removePiece(tile, row, col);
				order[index] += order.length;
				if (singleSolution && sols.size() == 1) {
					return;
				}
			}
		}
	}

	// private void positionTile() {
	// int c1 = 0;
	// int c2 = 0;
	//
	// // if (!board.gotoNextEmptySquare())
	// // return;
	//
	// // int curX = board.getCurX();
	// // int curY = board.getCurY();
	//
	// int curX = 0;
	// int curY = 0;
	//
	// for (int index = 0; index < order.length; index++) {
	// if (order[index] < 0)
	// continue; // tile already used
	//
	// Tile tile = tiles[order[index]];
	// tile.setPos(curX, curY);
	// for (int o = 0; o < orientation.length; o++) {
	// if (!tile.distinctOrientation(orientation[o]))
	// continue;
	// tile.setOrientation(orientation[o]);
	// c1++;
	//
	// if (board.add(tile)) {
	// c2++;
	// order[index] -= order.length;
	//
	// if (board.isFull()) {
	// Solution sol = board.getSolution();
	// // if (newSolution(sol)) {
	// sols.add(sol);
	// OnProgress(IMonitor.NEW_SOL);
	// // }
	// } else if (!deadendReached()) {
	// positionTile();
	// }
	//
	// board.remove(tile);
	// order[index] += order.length;
	//
	// if (singleSolution && sols.size() == 1) {
	// if (curX == 0 && curY == 0)
	// System.err.println(String.format(
	// "c1 = %s, c2 = %s", c1, c2));
	// return;
	// }
	// }
	// }
	// }
	// if (curX == 0 && curY == 0)
	// System.err.println(String.format("c1 = %s, c2 = %s", c1, c2));
	// }

	private boolean deadendReached() {
		long start = System.currentTimeMillis();
		boolean result = deadendReachedInner();
		deadEndTime += System.currentTimeMillis() - start;
		return result;
	}

	private boolean deadendReachedInner() {
		MergedBoard merged = board.getMergedBoard();
		merged.parse();
		if (allSameSizeTiles) {
			ArrayList<RawPiece> rawPieces = merged.getRawPieces();
			for (RawPiece raw : rawPieces) {
				if (raw.size() % singleTileSize != 0)
					return true;
			}
			return false;
		} else {
			int size;

			// min sized holes
			int minHoleSize = merged.getMinPiece().size();
			int minHoleCount = 0;
			// max sized holes
			int maxHoleSize = merged.getMaxPiece().size();
			int maxHoleCount = 0;
			for (RawPiece raw : merged.getRawPieces()) {
				size = raw.size();
				if (size == minHoleSize)
					minHoleCount++;
				if (size == maxHoleSize)
					maxHoleCount++;
			}
			int minTileSize = Integer.MAX_VALUE;
			int maxTileSize = Integer.MIN_VALUE;

			for (int i = 0; i < participants.length; i++) {
				if (order[i] < 0)
					continue;
				size = participants[order[i]].size();
				if (size < minTileSize)
					minTileSize = size;
				if (size == minHoleSize)
					minHoleCount--;

				if (size > maxTileSize)
					maxTileSize = size;
				if (size == maxHoleSize)
					maxHoleCount--;
			}
			return minTileSize > minHoleSize
					|| (minTileSize == minHoleSize && minHoleCount > 0)
					|| maxTileSize > maxHoleSize
					|| (maxTileSize == maxHoleSize && maxHoleCount < 0);
		}
	}

	public void print() {
		for (Tile tile : participants) {
			tile.print();
			System.out.println();
		}
		board.print();
		System.out.println();
		printSolutions();
	}

	public void printSolutions() {
		System.out.println("# of Solutions: " + sols.size());
		int count = 0;
		for (Solution sol : sols) {
			System.out.println("Solution #: " + count++);
			sol.print();
			System.out.println();
		}
	}

	public void printLastSolution() {
		int index = sols.size() - 1;
		if (index >= 0) {
			Solution sol = sols.get(index);
			System.out.println("Solution #: " + index);
			sol.print();
			System.out.println();
		}
	}

	public void removeSymmetric() {
		long startTime = System.currentTimeMillis();
		int index, ref;
		for (index = 0; index < sols.size();) {
			for (ref = 0; ref < index;) {
				if (sols.get(index).sameAs(sols.get(ref))) {
					sols.remove(index);
					break;
				} else
					ref++;
			}
			if (ref == index)
				index++;
		}
		totalTime += (System.currentTimeMillis() - startTime);
	}

	public boolean foundSymmetry(Solution s) {
		for (int ref = 0; ref < sols.size();) {
			if (s.sameAs(sols.get(ref))) {
				return true;
			} else
				ref++;
		}
		return false;
	}
}
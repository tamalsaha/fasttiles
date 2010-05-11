package com.tamal.tiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.LogManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.tamal.tiles.ui.MainDialog;

public class Program {

	public static final String root = "E:\\uva_study\\spring_2010\\CS 6161\\Arbitrary Puzzle Data\\";
	public static final String REPORT_ROOT = "E:\\uva_study\\spring_2010\\CS 6161\\Puzzle Report\\";

	static {
		configeLog();
	}

	public static void main5(String[] args) {
		try {
			final Puzzle puzzle = new Puzzle();

			BufferedWriter bw = new BufferedWriter(new FileWriter(REPORT_ROOT
					+ "report.csv"));

			bw.write("Name");
			bw.write(", ");
			bw.write("#(w/o rore)");
			bw.write(", ");
			bw.write("t(w/o rore)");
			bw.write(", ");
			bw.write("#(w/o rore)");
			bw.write(", ");
			bw.write("t(w/o rore)");
			bw.write(", ");
			bw.write("#(w ro w/o re)");
			bw.write(", ");
			bw.write("t(w ro w/o re)");
			bw.write(", ");
			bw.write("#(w ro w/o re)");
			bw.write(", ");
			bw.write("t(w ro w/o re)");
			bw.write(", ");
			bw.write("#(w rore)");
			bw.write(", ");
			bw.write("t(w rore)");
			bw.write(", ");
			bw.write("#(w rore)");
			bw.write(", ");
			bw.write("t(w rore)");
			bw.write(", ");
			bw.write(Piece.NEWLINE);

			File root = new File(REPORT_ROOT);
			String[] pf = root.list();
			for (String fpath : pf) {
				if (fpath.endsWith(".txt")) {
					puzzle.parse(REPORT_ROOT + fpath);
					bw.write(fpath);
					bw.write(", ");

					puzzle.solve(true, false, false);
					bw.write("" + puzzle.getSolutionCount());
					bw.write(", ");
					bw.write("" + puzzle.getTotalTime());
					bw.write(", ");

					puzzle.solve(false, false, false);
					bw.write("" + puzzle.getSolutionCount());
					bw.write(", ");
					bw.write("" + puzzle.getTotalTime());
					bw.write(", ");
					puzzle.removeSymmetric();
					bw.write("" + puzzle.getSolutionCount());
					bw.write(", ");
					bw.write("" + puzzle.getTotalTime());
					bw.write(", ");

					// ---------------------------

					puzzle.solve(true, true, false);
					bw.write("" + puzzle.getSolutionCount());
					bw.write(", ");
					bw.write("" + puzzle.getTotalTime());
					bw.write(", ");

					puzzle.solve(false, true, false);
					bw.write("" + puzzle.getSolutionCount());
					bw.write(", ");
					bw.write("" + puzzle.getTotalTime());
					bw.write(", ");
					puzzle.removeSymmetric();
					bw.write("" + puzzle.getSolutionCount());
					bw.write(", ");
					bw.write("" + puzzle.getTotalTime());
					bw.write(", ");

					// ---------------------------

					puzzle.solve(true, true, true);
					bw.write("" + puzzle.getSolutionCount());
					bw.write(", ");
					bw.write("" + puzzle.getTotalTime());
					bw.write(", ");

					puzzle.solve(false, true, true);
					bw.write("" + puzzle.getSolutionCount());
					bw.write(", ");
					bw.write("" + puzzle.getTotalTime());
					bw.write(", ");
					puzzle.removeSymmetric();
					bw.write("" + puzzle.getSolutionCount());
					bw.write(", ");
					bw.write("" + puzzle.getTotalTime());
					bw.write(", ");

					bw.write(Piece.NEWLINE);
					bw.flush();
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setMaximized(true);
		shell.setLayout(new FillLayout());

		new MainDialog(shell);
		shell.setText("Tiling Puzzle Solver");
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				Shell[] shells = display.getShells();
				for (int i = 0; i < shells.length; i++) {
					if (shells[i] != shell)
						shells[i].close();
				}
			}
		});
		shell.setSize(700, 400);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public static void main1(String[] args) {
		final Puzzle puzzle = new Puzzle();
		puzzle.addMonitor(new IMonitor() {

			@Override
			public void update(int event) {
				switch (event) {
				case IMonitor.NEW_SOL:
					puzzle.printLastSolution();
					break;
				default:
					break;
				}
			}
		});

		try {
			// puzzle.parse(root + "trivial.txt");
			// puzzle.parse(root + "pentominoes8x8_middle_missing.txt");
			puzzle.parse(root + "pentominoes8x8_middle_missing - Copy.txt");

			// int oCount = 0;
			// for (Tile tile : puzzle.getTiles()) {
			// tile.printAll();
			// oCount += tile.oCount;
			// }

			// puzzle.parse(root + "pentominoes8x8_middle_missing.txt");
			// puzzle.parse(root + "checkerboard.txt");
			// puzzle.parse(root + "IQ_creator.txt");
			// puzzle.parse(root + "lucky13.txt");
			// puzzle.parse(root + "partial_cross.txt");
			// puzzle.parse(root + "pentominoes3x20.txt");
			// puzzle.parse(root + "pentominoes4x15.txt");
			// puzzle.parse(root + "pentominoes5x12.txt");
			// puzzle.parse(root + "pentominoes6x10.txt");
			// puzzle.parse(root + "t1.txt");
			// puzzle.parse(root + "t2.txt");
			// puzzle.parse(root + "test1.txt");
			// puzzle.parse(root + "test2.txt");
			// puzzle.parse(root + "test3.txt");
			// puzzle.parse(root + "test_symmetry.txt");
			// puzzle.parse(root + "thirteen_holes.txt");

			long startTime = System.currentTimeMillis();
			puzzle.solve(false, false, true);
			long total = System.currentTimeMillis() - startTime;
			//
			// puzzle.printLastSolution();
			// System.out.println(String.format("Total time: %smin %ssec",
			// total / 60000, (total % 60000) / 1000.0));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void configeLog() {
		Properties prop = System.getProperties();
		prop.setProperty("java.util.logging.config.file", "logging.properties");

		try {
			LogManager.getLogManager().readConfiguration();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

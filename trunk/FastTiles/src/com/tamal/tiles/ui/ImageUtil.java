package com.tamal.tiles.ui;

import java.util.HashMap;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImageUtil {

	private static HashMap<String, Image> imageRegistry = new HashMap<String, Image>();

	private static final String IMAGE_Of_TILE = "TILE";
	private static final String IMAGE_Of_BOARD = "BOARD";
	private static final String IMAGE_Of_SOLUTION = "SOLUTION";

	private static final Display display;
	static {
		display = Display.getCurrent();
		display.disposeExec(new Runnable() {
			@Override
			public void run() {
				for (Image img : imageRegistry.values()) {
					img.dispose();
				}
			}
		});

		imageRegistry.put(IMAGE_Of_TILE, new Image(display,
				"icons/application_view_tile.png"));
		imageRegistry.put(IMAGE_Of_BOARD, new Image(display,
				"icons/application_view_tile.png"));
		imageRegistry.put(IMAGE_Of_SOLUTION, new Image(display,
				"icons/application_view_tile.png"));
	}

	private ImageUtil() {
	}

	public static void registryImage(String id, String urlName) {
		try {
			imageRegistry.put(id, new Image(display, urlName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Image getImage(String id) {
		return imageRegistry.get(id);
	}

	public static Image getImageOfTile() {
		return imageRegistry.get(IMAGE_Of_TILE);
	}

	public static Image getImageOfBoard() {
		return imageRegistry.get(IMAGE_Of_BOARD);
	}

	public static Image getImageOfSolution() {
		return imageRegistry.get(IMAGE_Of_SOLUTION);
	}
}
package com.tamal.tiles.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.tamal.tiles.IntChPair;
import com.tamal.tiles.Piece;

public class PieceComposite extends Composite {

	public static int RESOLUTION = 25;

	public PieceComposite(Composite parent, int style, Piece piece) {
		super(parent, style);
		createContent(piece);
	}

	private void createContent(Piece piece) {
		IntChPair[][] data = piece.getData();
		int rowCount = piece.height();
		int colCount = piece.width();

		GridLayout layout = new GridLayout();
		layout.numColumns = colCount;
		layout.marginWidth = layout.marginHeight = 5;
		layout.horizontalSpacing = layout.verticalSpacing = 0;
		this.setLayout(layout);

		Label lbl;
		GridData gd;
		Composite wrapper;
		for (int row = 0; row < rowCount; row++) {
			for (int col = 0; col < colCount; col++) {
				wrapper = new Composite(this, SWT.NULL);
				gd = new GridData(GridData.FILL, GridData.FILL, false, false);
				gd.widthHint = RESOLUTION;
				gd.heightHint = RESOLUTION;
				wrapper.setLayoutData(gd);

				layout = new GridLayout();
				layout.numColumns = 1;
				layout.marginWidth = layout.marginHeight = 0;
				layout.horizontalSpacing = layout.verticalSpacing = 0;
				wrapper.setLayout(layout);

				lbl = new Label(wrapper, SWT.NULL);
				gd = new GridData(GridData.CENTER, GridData.CENTER, true, true);
				lbl.setLayoutData(gd);

				if (data[row][col].getB() != Piece.BLANK) {
					lbl.setText("" + data[row][col].getB());
					wrapper.setBackground(MainDialog.COLORS[data[row][col]
							.getA()]);
					lbl.setBackground(MainDialog.COLORS[data[row][col].getA()]);
				}
			}
		}
	}
}

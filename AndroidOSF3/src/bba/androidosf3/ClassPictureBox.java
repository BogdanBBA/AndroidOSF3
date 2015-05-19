package bba.androidosf3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Point;

public abstract class ClassPictureBox
{
	private static final int headerH = 18, nameH = 36, profH = 26, roomH = 20, footerH = 2;
	private static final int totalH = headerH + nameH + profH + roomH + footerH;

	private static final double leftPanelWPerc = 0.09;
	private static final int strokeW = 8, yOffset = 8;

	private static final int smallFontSize = 18, nameFontSize = 32;

	public static Bitmap GeneratePixtureBoxForClass(classes.Class cls, int width, int bgCol, int timeCol, int nameCol, int profCol, int roomCol)
	{
		Bitmap bmp = Bitmap.createBitmap(width, totalH, Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);

		Paint bgP = new Paint(Paint.ANTI_ALIAS_FLAG);
		bgP.setStyle(Paint.Style.FILL);
		bgP.setColor(bgCol);

		Paint pen1P = new Paint(Paint.ANTI_ALIAS_FLAG);
		pen1P.setStyle(Paint.Style.STROKE);
		pen1P.setStrokeWidth(strokeW);
		pen1P.setColor(cls.discipline.color1);

		Paint pen2P = new Paint(pen1P);
		pen2P.setColor(cls.discipline.color2);

		Paint fontP = new Paint(Paint.ANTI_ALIAS_FLAG);
		fontP.setStyle(Paint.Style.FILL);
		fontP.setTextSize(smallFontSize);
		fontP.setTextAlign(Paint.Align.LEFT);

		int left = (int) (width * leftPanelWPerc), bottom = headerH;

		Point p1 = new Point(-left, -2 * left);
		Point p2 = new Point(2 * left, left);

		canvas.drawPaint(bgP);

		while (p1.y < canvas.getHeight())
		{
			canvas.drawLine(p1.x, p1.y, p2.x, p2.y, pen1P);
			p1.offset(0, yOffset);
			p2.offset(0, yOffset);
			canvas.drawLine(p1.x, p1.y, p2.x, p2.y, pen2P);
			p1.offset(0, yOffset);
			p2.offset(0, yOffset);
		}

		canvas.drawRect(left, 0, canvas.getWidth(), canvas.getHeight(), bgP);

		// Class type
		String text = cls.classType.name;
		fontP.setColor(cls.classType.color);
		left += strokeW;
		canvas.drawText(text, left, bottom, fontP);

		// Time interval
		text = cls.when.timeInterval.FormatInterval(true);
		fontP.setColor(timeCol);
		left = width - (int) fontP.measureText(text) - strokeW;
		canvas.drawText(text, left, bottom, fontP);

		// Class name
		text = cls.discipline.name;
		fontP.setColor(nameCol);
		fontP.setTextSize(nameFontSize);
		left = (int) (width * leftPanelWPerc) + strokeW;
		bottom += nameH;
		canvas.drawText(text, left, bottom, fontP);

		// Professor
		text = "Cu " + cls.whoWith.name;
		fontP.setColor(profCol);
		fontP.setTextSize(smallFontSize);
		bottom += profH;
		canvas.drawText(text, left, bottom, fontP);

		// Room
		text = "În " + cls.where.name;
		fontP.setColor(roomCol);
		bottom += roomH;
		canvas.drawText(text, left, bottom, fontP);

		return bmp;
	}
}

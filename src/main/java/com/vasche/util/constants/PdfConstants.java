package com.vasche.util.constants;

import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PdfConstants {
    public static final String FONTS_BAHNSCHRIFT_TTF_PATH = "fonts/bahnschrift.ttf";
    public static final String DEF_TICKET_FILENAME = "ticket";
    public static final int TABLE_WIDTH_PERCENTAGE = 100;
    public static final float PAGE_WIDTH = PageSize.A4.getWidth() - 25;
    public static final int HEAD_FONT_SIZE = 12;
    public static final int ALIGN_LEFT = Element.ALIGN_LEFT;
    public static final int ALIGN_MIDDLE = Element.ALIGN_MIDDLE;
    public static final int PADDING_VALUE = 5;
    public static final int MAXIMUM_FRACTION_DIGITS = 2;
    public static final int COLUMNS_NUMBER = 2;
    public static final int MINIMUM_FRACTION_DIGITS = 0;
}

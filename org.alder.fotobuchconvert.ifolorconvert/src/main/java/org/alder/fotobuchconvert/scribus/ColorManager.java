package org.alder.fotobuchconvert.scribus;

import java.awt.Color;
import java.util.HashMap;

import org.alder.fotobuchconvert.tools.XmlBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ColorManager {

	protected final Log log = LogFactory.getLog(ColorManager.class);

	private XmlBuilder doc;

	HashMap<String, String> colortable = new HashMap<String, String>();

	public ColorManager(XmlBuilder doc) {
		this.doc = doc;
	}

	public String getColorName(Color color) {
		String rgbCode = color2rgbStr(color);
		String key = "RGB." + rgbCode;

		String colname = colortable.get(key);
		if (colname == null)
			return addRGB(null, rgbCode);
		else
			return colname;
	}

	private String color2rgbStr(Color color) {
		String s = Integer.toHexString(color.getRGB() & 0xFFFFFF);
		while (s.length() < 6)
			s = "0" + s;
		String rgbCode = "#" + s;
		return rgbCode;
	}

	public void initialize() {
		addCMYK("Black", "#000000ff", Color.BLACK);
		addRGB("BlackRGB", "#000000");
		addCMYK("White", "#00000000", Color.WHITE);

		addRGB("Blue", "#0000ff");
		addCMYK("Cool Black", "#990000ff", null);
		addCMYK("Cyan", "#ff000000", Color.CYAN);
		addRGB("Green", "#00ff00");
		addCMYK("Magenta", "#00ff0000", null);
		addRGB("Red", "#ff0000");
		addCMYK("Rich Black", "#996666ff", null);
		addCMYK("Warm Black", "#00994cff", null);
		addCMYK("Yellow", "#0000ff00", Color.YELLOW);

	}

	private String addRGB(String name, String rgbCode) {
		// format "#0000ff"

		String key = "RGB." + rgbCode;

		{
			String savedname = colortable.get(key);
			if (savedname != null) {
				log.warn("Color " + key + " already registered with name "
						+ savedname);
				return savedname;
			}
		}

		if (name == null)
			name = "color" + colortable.size();

		colortable.put(key, name);
		doc.addAfterSimilar(C.EL_COLOR).set(C.NAME, name).set(C.RGB, rgbCode)
				.set(C.SPOT, 0).set(C.REGISTER, 0);

		return name;
	}

	private String addCMYK(String name, String cmykCode, Color rgbVariant) {
		// format "#00ff0000"

		if (rgbVariant != null) {
			String key = "RGB." + color2rgbStr(rgbVariant);
			if (!colortable.containsKey(key))
				colortable.put(key, name);
		}

		String key = "CMYK." + cmykCode;

		{
			String savedname = colortable.get(key);
			if (savedname != null) {
				log.warn("Color " + key + " already registered with name "
						+ savedname);
				return savedname;
			}
		}

		if (name == null)
			name = "color" + colortable.size();

		colortable.put(key, name);
		doc.add(C.EL_COLOR).set(C.NAME, name).set(C.CMYK, cmykCode)
				.set(C.SPOT, 0).set(C.REGISTER, 0);

		return name;
	}
}

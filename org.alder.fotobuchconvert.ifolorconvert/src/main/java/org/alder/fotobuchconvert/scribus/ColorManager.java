package org.alder.fotobuchconvert.scribus;

import java.awt.Color;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ColorManager {

	protected final Log log = LogFactory.getLog(ColorManager.class);

	private XmlBuilder doc;

	public ColorManager(XmlBuilder doc) {
		this.doc = doc;
	}

	HashMap<String, String> colortable = new HashMap<String, String>();

	public String getColorName(Color color) {
		String rgbCode = "#" + Integer.toHexString(color.getRGB()).substring(2);
		String key = "RGB." + rgbCode;

		String colname = colortable.get(key);
		if (colname == null)
			return addRGB(null, rgbCode);
		else
			return colname;
	}

	public void initialize() {
		addCMYK("Black", "#000000ff");
		addRGB("BlackRGB", "#000000");
		addCMYK("White", "#00000000");

		addRGB("Blue", "#0000ff");
		addCMYK("Cool Black", "#990000ff");
		addCMYK("Cyan", "#ff000000");
		addRGB("Green", "#00ff00");
		addCMYK("Magenta", "#00ff0000");
		addRGB("Red", "#ff0000");
		addCMYK("Rich Black", "#996666ff");
		addCMYK("Warm Black", "#00994cff");
		addCMYK("Yellow", "#0000ff00");

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
		doc.add(C.EL_COLOR).set(C.NAME, name).set(C.RGB, rgbCode)
				.set(C.SPOT, 0).set(C.REGISTER, 0);

		return name;
	}

	private String addCMYK(String name, String cmykCode) {
		// format "#00ff0000"

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

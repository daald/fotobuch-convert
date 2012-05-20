package org.alder.fotobuchconvert.scribus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashSet;

import org.alder.fotobuchconvert.tools.XmlBuilder;

public class SVGShadowManager {
	private static SVGShadowManager instance;

	private String basePath = "/tmp/";

	HashSet<File> generatedFiles = new HashSet<File>();

	public static void main(String[] args) throws FileNotFoundException {
		SVGShadowManager m = new SVGShadowManager();

		for (int i = 10; i < 1000; i += 15) {
			File file = m.get(i, 777, 60);
			System.out.println(i + "\t" + file);
		}
	}

	public File get(double width, double height, double edge)
			throws FileNotFoundException {
		int e = (int) edge;
		int w = roundSize((int) width, e);
		int h = roundSize((int) height, e);

		if (e > w / 2)
			e = w / 2;
		if (e > h / 2)
			e = h / 2;

		File file = new File(basePath, "softborder_" + w + "x" + h + "_" + e
				+ ".svg");

		if (!generatedFiles.contains(file)) {
			generateFile(file, w, h, e);
			generatedFiles.add(file);
		}

		return file;
	}

	private int roundSize(int value, int edge) {
		final double base = 1.5;

		return (int) (Math.pow(
				base,
				Math.round(Math.log((double) value / (double) edge)
						/ Math.log(base))) * (double) edge);
	}

	private XmlBuilder generateXML(int width, int height, int edge) {
		final int x1 = 0, y1 = 0;
		final int x2 = edge, y2 = edge;
		final int x4 = width, y4 = height;
		final int x3 = x4 - edge, y3 = y4 - edge;

		final int iw = x3 - x2, ih = y3 - y2;

		final double transparency = 0.6;
		// used to simulate some logarithm
		final double trans20pct = transparency * 0.90d;

		XmlBuilder svg = new XmlBuilder("svg")
				.set("xmlns:svg", "http://www.w3.org/2000/svg")
				.set("xmlns:xlink", "http://www.w3.org/1999/xlink")
				.set("xmlns", "http://www.w3.org/2000/svg").set("width", x4)
				.set("height", y4).set("version", "1.1");

		XmlBuilder defs = svg.add("defs");

		XmlBuilder gc = defs.add("linearGradient").set("id", "gradientColors");
		gc.add("stop")
				.set("style",
						"stop-color:#000000;stop-opacity:" + transparency + ";")
				.set("offset", 0);
		gc.add("stop")
				.set("style",
						"stop-color:#000000;stop-opacity:" + trans20pct + ";")
				.set("offset", 0.2);
		gc.add("stop").set("style", "stop-color:#000000;stop-opacity:0;")
				.set("offset", 1);

		gc.add("linearGradient").set("xlink:href", "#gradientColors")
				.set("id", "linearGradientT").set("x1", 0).set("y1", 1)
				.set("x2", 0).set("y2", 0);
		gc.add("linearGradient").set("xlink:href", "#gradientColors")
				.set("id", "linearGradientR").set("x1", 0).set("y1", 0)
				.set("x2", 1).set("y2", 0);
		gc.add("linearGradient").set("xlink:href", "#gradientColors")
				.set("id", "linearGradientB").set("x1", 0).set("y1", 0)
				.set("x2", 0).set("y2", 1);
		gc.add("linearGradient").set("xlink:href", "#gradientColors")
				.set("id", "linearGradientL").set("x1", 1).set("y1", 0)
				.set("x2", 0).set("y2", 0);

		gc.add("radialGradient").set("xlink:href", "#gradientColors")
				.set("id", "radialGradientTL").set("cx", 1).set("cy", 1)
				.set("r", 1).set("fx", 1).set("fy", 1);
		gc.add("radialGradient").set("xlink:href", "#gradientColors")
				.set("id", "radialGradientTR").set("cx", 0).set("cy", 1)
				.set("r", 1).set("fx", 0).set("fy", 1);
		gc.add("radialGradient").set("xlink:href", "#gradientColors")
				.set("id", "radialGradientBL").set("cx", 1).set("cy", 0)
				.set("r", 1).set("fx", 1).set("fy", 0);
		gc.add("radialGradient").set("xlink:href", "#gradientColors")
				.set("id", "radialGradientBR").set("cx", 0).set("cy", 0)
				.set("r", 1).set("fx", 0).set("fy", 0);

		// use <g> if you want grouping
		XmlBuilder g = svg;// .add("g");

		// middle
		g.add("rect").set("style", "fill:#000000;fill-opacity:" + transparency)
				.set("x", x2).set("y", y2).set("width", iw).set("height", ih);

		// edges
		g.add("rect").set("fill", "url(#linearGradientT)").set("x", x2)
				.set("y", y1).set("width", iw).set("height", edge);
		g.add("rect").set("fill", "url(#linearGradientR)").set("x", x3)
				.set("y", y2).set("width", edge).set("height", ih);
		g.add("rect").set("fill", "url(#linearGradientB)").set("x", x2)
				.set("y", y3).set("width", iw).set("height", edge);
		g.add("rect").set("fill", "url(#linearGradientL)").set("x", x1)
				.set("y", y2).set("width", edge).set("height", ih);

		// corners
		g.add("rect").set("fill", "url(#radialGradientTL)").set("x", x1)
				.set("y", y1).set("width", edge).set("height", edge);
		g.add("rect").set("fill", "url(#radialGradientTR)").set("x", x3)
				.set("y", y1).set("width", edge).set("height", edge);
		g.add("rect").set("fill", "url(#radialGradientBR)").set("x", x3)
				.set("y", y3).set("width", edge).set("height", edge);
		g.add("rect").set("fill", "url(#radialGradientBL)").set("x", x1)
				.set("y", y3).set("width", edge).set("height", edge);

		return svg;
	}

	private void generateFile(File file, int width, int height, int edge)
			throws FileNotFoundException {
		XmlBuilder xmldoc = generateXML(width, height, edge);

		PrintStream pr = null;
		try {
			pr = new PrintStream(file);
			xmldoc.output(pr);
		} finally {
			if (pr != null)
				pr.close();
		}
		xmldoc.output(pr);
	}

	public static SVGShadowManager getInstance() {
		if (instance == null)
			instance = new SVGShadowManager();

		return instance;
	}
}

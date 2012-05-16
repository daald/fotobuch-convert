package org.alder.fotobuchconvert.tools;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Vector;

public class XmlBuilder extends XmlBuilderBase {

	private final Vector<XmlBuilderBase> subElements = new Vector<XmlBuilderBase>();
	private final String name;
	private final HashMap<String, String> attributes = new LinkedHashMap<String, String>();

	public XmlBuilder(String name) {
		this.name = name;
	}

	public final void output(PrintStream out) {
		output(out, 0);
	}

	@Override
	protected void output(PrintStream out, int indent) {
		if (indent == 0)
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		for (int i = 0; i < indent; i++)
			out.print("  ");
		out.print("<" + name);
		for (Entry<String, String> kv : attributes.entrySet())
			out.print(" " + kv.getKey() + "=\""
					+ kv.getValue().replaceAll("\"", "&quot;") + "\"");
		if (subElements.isEmpty())
			out.println("/>");
		else {
			out.println(">");
			for (XmlBuilderBase el : subElements)
				el.output(out, indent + 1);
			for (int i = 0; i < indent; i++)
				out.print("  ");
			out.println("</" + name + ">");
		}
	}

	public XmlBuilder set(String key, int value) {
		return set(key, String.valueOf(value));
	}

	public XmlBuilder set(String key, double value) {
		return set(key, String.valueOf(value));
	}

	public XmlBuilder set(String key, String value) {
		if (attributes.containsKey(key))
			System.err.println("WARN: key " + key + " already used in " + name);

		attributes.put(key, value);
		return this;
	}

	public XmlBuilder add(String name) {
		XmlBuilder e = new XmlBuilder(name);
		subElements.add(e);
		return e;
	}

	/**
	 * Like {@link add}, but inserts the element and the end of the first group
	 * of elements with the same name. Use this function for definitions like
	 * PAGE, MASTERPAGE or COLOR
	 * 
	 * @param name
	 * @return
	 */
	public XmlBuilder addAfterSimilar(String name) {
		final int n = subElements.size();
		int insert = 0;
		XmlBuilderBase o;
		for (int i = 0; i < n; i++)
			if ((o = subElements.get(i)) instanceof XmlBuilder)
				if (((XmlBuilder) o).name.equals(name))
					insert = i + 1;
				else if (insert > 0)
					break;
		if (insert == 0)
			insert = n;

		XmlBuilder e = new XmlBuilder(name);
		subElements.add(insert, e);
		return e;
	}

	public void comment(String text) {
		subElements.add(new XmlComment(text));
	}

}

abstract class XmlBuilderBase {
	protected abstract void output(PrintStream out, int indent);
}

class XmlComment extends XmlBuilderBase {
	private final String text;

	public XmlComment(String text) {
		this.text = text;
	}

	protected void output(PrintStream out, int indent) {
		for (int i = 0; i < indent; i++)
			out.print("  ");
		out.println("<!--");
		out.println(text);
		out.println("-->");
	}
}
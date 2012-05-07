package org.alder.fotobuchconvert.rtf2html;

public class RtfConvertPrototype {
	static String input = "{\\rtf1\\ansi\\ansicpg1252\\deff0\\deflang2055{\\fonttbl{\\f0\\fnil\\fcharset0 Trebuchet MS;}}\n"
			+ "{\\colortbl ;\\red0\\green0\\blue0;}\n"
			+ "\\viewkind4\\uc1\\pard\\qc\\b\\f0\\fs32 Sydney City\\b0 . \\cf1\\i The big smoke\\par\n"
			+ "}";

	public static void main(String[] args) {
		System.out.println(input);

		new RtfConvertPrototype(input);
	}

	public RtfConvertPrototype(String input) {
		char[] data = input.toCharArray();

		System.out.println("---");
		parse(data, 0, data.length);
	}

	private int parse(char[] data, int i, int z) {
		System.out.println();
		System.out.println(" Sub: ");
		for (; i < z; i++) {
			switch (data[i]) {
			case '{':
				i = parse(data, i + 1, z);
				System.out.println();
				break;
			case '}':
				return i;
			case '\\':
				i++;
				int j;
				for (j = i + 1; j < z; j++)
					if (!Character.isLetter(data[j]))
						break;
				String fn = new String(data, i, j - i);
				i = j;
				for (j = i; j < z; j++)
					if (!Character.isDigit(data[j]))
						break;
				String parms = new String(data, i, j - i);
				i = j - 1;

				System.out.print(" [" + fn + ":" + parms + "] ");
				break;
			default:
				System.out.print("  '" + data[i] + "'  ");
			}
		}
		return i;
	}
}

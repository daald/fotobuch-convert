/*******************************************************************************
 * Copyright (c) 2012 Daniel Alder.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Daniel Alder - initial API and implementation
 ******************************************************************************/
package org.alder.fotobuchconvert.scribus;

public abstract class ImageCutCoords {
	public abstract ScribusPolyBuilder get(double width, double height);

	public static class OvalCoords extends ImageCutCoords {
		@Override
		public ScribusPolyBuilder get(double w, double h) {
			ScribusPolyBuilder pb = new ScribusPolyBuilder();

			for (int i = 0; i < raw.length; i += 4)
				pb.add(w * raw[i], h * raw[i + 1], w * raw[i + 2], h
						* raw[i + 3]);

			return pb;
		}

		final static float[] raw = { 1f, 0.5f, 1f, 0.776152777777778f, 0.5f,
				1f, 0.776154761904762f, 1f, 0.5f, 1f, 0.223857738095238f, 1f,
				0f, 0.5f, 0f, 0.776152777777778f, 0f, 0.5f, 0f,
				0.223857407407407f, 0.5f, 0f, 0.223857738095238f, 0f, 0.5f, 0f,
				0.776154761904762f, 0f, 1f, 0.5f, 1f, 0.223857407407407f, };
	}

	public static class HeartCoords extends ImageCutCoords {
		@Override
		public ScribusPolyBuilder get(double w, double h) {
			ScribusPolyBuilder pb = new ScribusPolyBuilder();

			for (int i = 0; i < raw.length; i += 4)
				pb.add(w * raw[i], h * raw[i + 1], w * raw[i + 2], h
						* raw[i + 3]);

			return pb;
		}

		final static float[] raw = { 0.50185f, 1f, 0.50185f, 1f, 0.4206452381f,
				0.8235037037f, 0.4734011905f, 0.9059638889f, 0.4206452381f,
				0.8235037037f, 0.4206452381f, 0.8235037037f, 0.2165172619f,
				0.5673268519f, 0.3678880952f, 0.7410444444f, 0.2165172619f,
				0.5673268519f, 0.2165172619f, 0.5673268519f, 0.079825f,
				0.4055694444f, 0.1055130952f, 0.439837963f, 0.079825f,
				0.4055694444f, 0.079825f, 0.4055694444f, 0.0187892857f,
				0.3031953704f, 0.0376079167f, 0.3498046296f, 0.0187892857f,
				0.3031953704f, 0.0187892857f, 0.3031953704f, 0f, 0.2087759259f,
				0f, 0.2565851852f, 0f, 0.2087759259f, 0f, 0.2087759259f,
				0.0678761905f, 0.0605574074f, 0f, 0.1203240741f, 0.0678761905f,
				0.0605574074f, 0.0678761905f, 0.0605574074f, 0.2357761905f,
				0.0007904333f, 0.1357821429f, 0.0007904333f, 0.2357761905f,
				0.0007904333f, 0.2357761905f, 0.0007904333f, 0.411014881f,
				0.0629541667f, 0.336710119f, 0.0007904333f, 0.411014881f,
				0.0629541667f, 0.411014881f, 0.0629541667f, 0.50185f,
				0.2007962963f, 0.4669720238f, 0.1091555556f, 0.50185f,
				0.2007962963f, 0.50185f, 0.2007962963f, 0.587164881f,
				0.0637446296f, 0.5321178571f, 0.110762963f, 0.587164881f,
				0.0637446296f, 0.587164881f, 0.0637446296f, 0.7633154762f, 0f,
				0.6633214286f, 0f, 0.7633154762f, 0f, 0.7633154762f, 0f,
				0.9311845238f, 0.0593589815f, 0.8623988095f, 0f, 0.9311845238f,
				0.0593589815f, 0.9311845238f, 0.0593589815f, 1f, 0.2007962963f,
				1f, 0.1187175926f, 1f, 0.2007962963f, 1f, 0.2007962963f,
				0.9596309524f, 0.350212963f, 1f, 0.2725212963f, 0.9596309524f,
				0.350212963f, 0.9596309524f, 0.350212963f, 0.8036845238f,
				0.553787963f, 0.9192678571f, 0.4278787037f, 0.8036845238f,
				0.553787963f, 0.8036845238f, 0.553787963f, 0.5844047619f,
				0.8247018519f, 0.6532202381f, 0.7187333333f, 0.5844047619f,
				0.8247018519f, 0.5844047619f, 0.8247018519f, 0.50185f, 1f,
				0.5302678571f, 0.9083611111f, };
	}
}

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
package org.alder.fotobuchconvert.ifolor;

import java.io.File;

import org.alder.fotobuchconvert.objects.Book;
import org.alder.fotobuchconvert.scribus.ScribusExporter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IfolorToScribusMain {

	private static final String OPT_TEST = "test";
	private static final String OPT_ABSOLUTE_PATHS = "absolute-paths";
	private static final String OPT_OUT = "out";
	private static final String OPT_IN = "in";

	protected final static Log log = LogFactory
			.getLog(IfolorToScribusMain.class);

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		// create Options object
		Options options = new Options();
		options.addOption(OptionBuilder.withLongOpt(OPT_IN).hasArg()
				.withDescription("Input file (type ifolor)").isRequired()
				.create('i'));
		options.addOption(OptionBuilder.withLongOpt(OPT_OUT).hasArg()
				.withDescription("Output file (type scibus)").isRequired()
				.create('o'));
		options.addOption(OptionBuilder
				.withLongOpt(OPT_ABSOLUTE_PATHS)
				.withDescription(
						"Convert resource paths (eg. images) to absolute paths")
				.create());
		options.addOption(OptionBuilder.withLongOpt(OPT_TEST)
				.withDescription("Test mode (only first 4 pages)").create('t'));
		CommandLineParser parser = new PosixParser();

		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println("Unexpected exception:" + e.getMessage());

			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(IfolorToScribusMain.class.getSimpleName()
					+ " --in <file.dpp> --out <file.sla>", options);
			return;
		}

		log.trace("infile: " + cmd.getOptionValue(OPT_IN));
		log.trace("outfile: " + cmd.getOptionValue(OPT_OUT));
		log.trace("absolutePaths: " + cmd.hasOption(OPT_ABSOLUTE_PATHS));
		log.trace("test: " + cmd.hasOption(OPT_TEST));

		try {
			File infile = new File(cmd.getOptionValue(OPT_IN));
			if (!infile.exists()) {
				System.err.println("input file " + infile + " does not exist");
				System.exit(1);
			}
			ProjectPath path = new ProjectPath(infile);
			File outFile = new File(cmd.getOptionValue(OPT_OUT));

			// conversion
			IfolorLoader loader = new IfolorLoader();
			Book book = loader.load(path);

			if (cmd.hasOption(OPT_TEST))
				book.reducePagesForTesting();

			ScribusExporter f = new ScribusExporter();

			f.export(outFile, book);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

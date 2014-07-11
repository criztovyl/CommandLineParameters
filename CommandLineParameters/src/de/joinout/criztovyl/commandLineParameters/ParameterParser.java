/**
    This is part of a software that parses command line super.getArguments() to parameters.
    Copyright (C) 2014 Christoph "criztovyl" Schulz

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.joinout.criztovyl.commandLineParameters;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author criztovyl
 * 
 */
public class ParameterParser extends Parameters {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5441102405597911988L;

	private final Logger logger;

	private final ArrayList<String> args;

	/**
	 * Creates a new parameter parser.
	 * 
	 * @param args
	 *            the argument array
	 */
	public ParameterParser(String[] args) {
		this(args, "");
	}

	/**
	 * Creates a new parameter parser.
	 * 
	 * @param args
	 *            the argument array
	 * @param description
	 *            the general description for the parameters
	 */
	public ParameterParser(String[] args, String description) {
		super(description);

		logger = LogManager.getLogger();

		// Set up
		this.args = new ArrayList<String>(Arrays.asList(args));

	}

	/**
	 * Parses the Argument Array
	 */
	public void parse() {
		
		//Cancel if is empty
		if(args.isEmpty())
			return;

		// First argument in list is action
		super.setAction(args.remove(0));

		// List for parameters as ||arg, arg, arg|, |arg, arg, arg||, first arg
		// is parameter, rest are arguments
		final ArrayList<ArrayList<String>> parameters = new ArrayList<>();

		if (logger.isTraceEnabled())
			logger.trace("Iterating over arguments list.");

		// Iterate over args array, fill parameters list
		for (final String item : args)

			// Check which parameter it is
			if (item.startsWith("--")) {

				// Its long, append new List and add parameter.
				parameters.add(new ArrayList<String>());
				parameters.get(parameters.size() - 1).add(item);

			} else if (item.startsWith("-"))
				// Its short, add each single character as parameter to list.
				for (final Character character : item.replace("-", "")
						.toCharArray()) {
					parameters.add(new ArrayList<String>());
					parameters.get(parameters.size() - 1).add(
							"-" + character.toString());
				}
			else // Add to last parameter if there is one, otherwise add to
					// arguments
			if (parameters.size() >= 1)
				parameters.get(parameters.size() - 1).add(item);
			else
				super.getArguments().add(item);

		if (logger.isTraceEnabled())
			logger.trace("Iterate over found parameters and include them");

		// Iterate over parameters list, fill parameters object
		for (final ArrayList<String> parameter : parameters)

			// Check if is not empty
			if (parameter.size() >= 1) {

				// Get parameter or stay null if is no parameter.
				ParameterName param = null;
				if (parameter.get(0).startsWith("--"))
					param = super.getNameByLong(parameter.get(0).replaceAll(
							"^--", ""));
				else if (parameter.get(0).startsWith("-"))
					param = super.getNameByShort(parameter.get(0).replaceAll(
							"^-", ""));

				// Add as argument if is no parameter or add to parameters if is
				// parameter
				if (param == null)
					super.getArguments().addAll(parameter);
				else {
					// Iterate over parameter arguments and add as value
					for (final String arg : parameter.subList(1,
							parameter.size()))

						// Add to normal arguments if parameter value is full
						if (!super.get(param).add(arg))
							super.getArguments().add(arg);

					// Set present
					super.get(param).setPresent(true);
				}

			} else
				continue;
		if (logger.isDebugEnabled())
			logger.debug("Parameters: {}", super.toString());
	}
}

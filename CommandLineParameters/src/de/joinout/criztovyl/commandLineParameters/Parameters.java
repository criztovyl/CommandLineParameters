/**
    This is part of a software that parses command line arguments to parameters.
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
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a map of parameters where the parameter name is the key and the
 * parameter the value.<br>
 * You can include a description which will be shown in the first lines of the {@link #getHelp()} {@link String}.
 * 
 * @author criztovyl
 * 
 */
public class Parameters extends HashMap<ParameterName, Parameter> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1764523522307086756L;

	private ArrayList<String> arguments;

	private String description, action;

	private HashMap<String, ParameterAction> actions;

	private Logger logger;

	/**
	 * Creates a new empty parameter manager.
	 */
	public Parameters() {
		this("");
	}

	/**
	 * Creates a new, empty parameter manager. Includes a description.<br>
	 * 
	 * @param description
	 *            the description as a {@link String}.
	 */
	public Parameters(String description) {

		// Setup map and required variables
		super();
		
		// Setup arguments
		arguments = new ArrayList<>();

		// Setup description
		this.description = description == null ? "" : description;

		// Setup actions and action
		this.actions = new HashMap<>();
		action = "";

		logger = LogManager.getLogger();
	}

	/**
	 * Checks if contains a parameter name.
	 * @param o the parameter name, as {@link Character}, {@link String} or {@link ParameterName}
	 * @return true if map contains mapping for the parameter as a key.
	 */
	public boolean containsKey(Object o){
		//Its a Strings, Strings are long names
		if(o instanceof String)
			return containsLong((String) o);
		//Its a Character, Characters are short names
		else if(o instanceof Character)
			return containsShort((Character) o);
		//Its a ParameterName, ParameterNames are full names
		else if(o instanceof ParameterName)
			return super.containsKey((ParameterName) o);
		//Its something different
		else
			return super.containsKey(o);
	}
	/**
	 * Checks if this contains the given long parameter
	 * 
	 * @param longN
	 *            the long parameter name
	 * @return true if there is an parameter with this long name
	 */
	public boolean containsLong(String longN) {

		return !(getNameByLong(longN) == null);
	}

	/**
	 * Checks if this contains the given short parameter
	 * 
	 * @param shortN
	 *            the short parameter name
	 * @return true if there is an parameter with this name
	 */
	public boolean containsShort(Character shortN) {

		return !(getNameByShort(shortN) == null);
	}

	/**
	 * Checks if this contains the given short parameter
	 * 
	 * @param shortN
	 *            the short parameter name
	 * @return true if there is an parameter with this name
	 */
	public boolean containsShort(String shortN) {

		return !(getNameByShort(shortN) == null);
	}

	/**
	 * Searches for a parameter with the given key. Key will be used to match
	 * against the {@link ParameterName#getShortName()}
	 * 
	 * @param key
	 *            the key
	 * @return if contains a parameter with this key the parameter, otherwise
	 *         {@code null}
	 */
	public Parameter get(Character key) {

		// pass-through
		return get(getNameByShort(key));
	}

	/**
	 * Searches for a parameter with the given key. Key will be used to match
	 * against the {@link ParameterName#getLongName()}
	 * 
	 * @param key
	 *            the key
	 * @return if contains a parameter with this key the parameter, otherwise
	 *         {@code null}
	 */
	public Parameter get(String key) {

		// pass-through
		return get(getNameByLong(key));
	}

	/**
	 * @return a map of all keys and actions
	 */
	public HashMap<String, ParameterAction> getActions() {
		return actions;
	}

	/**
	 * @return the arguments as a list of strings
	 */
	public ArrayList<String> getArguments() {
		return arguments;
	}

	/**
	 * Loads all available parameter descriptions
	 * 
	 * @return a list of all parameter descriptions as a string, one description
	 *         per line (or more if contains line breaks), new lines within help
	 *         will be prefixed by tabs.
	 */
	public String getHelp() {

		// Setup strings
		final String newline = String.format("%n");
		final String lineseparator = String
				.format("\t-----------------------------%n");
		String help = "";

		// Add description with trailing tab
		for (final String str : description.split(newline))
			help += "\t" + str + newline;

		// Separate and add title (with two newlines)
		help += lineseparator;
		help += "\tActions:" + newline + newline;

		// Add help for each action, with trailing tab(s)
		// Format: (tab)(name):(newline)
		// (description)
		for (final String name : getActions().keySet())
			help += String.format(
					"\t---------%n\t%s:%n\t\t%s%n",
					name,
					getActions().get(name).getDescription()
							.replaceAll(newline, newline + "\t\t\t"));

		// Separate and add title (with two newlines)
		help += lineseparator;
		help += "\tParameters:" + newline + newline;

		// Add help for each parameter, with trailing tab(s)
		// Format: (tab)--(long name)(/-(short name)):(newline)
		// (description)
		// Short name only if has one, when, then a slash is suffixed.
		for (final ParameterName name : keySet())
			help += String.format(
					"\t---------%n\t--%s%s:%n\t\t%s%n",
					name.getLongName(),
					name.hasShort() ? "/-" + name.getShortName() : "",
					get(name).getDescription().replaceAll(newline,
							newline + "\t\t\t"));
		return help;
	}

	/**
	 * Searches for a parameter name by a long name
	 * 
	 * @param longName
	 *            the short name
	 * @return if there is a parameter with the given long name the parameter
	 *         name , otherwise null
	 */
	public ParameterName getNameByLong(String longName) {

		// Iterate over keys and return it if there is one with the given short
		// name.
		for (final ParameterName name : keySet())
			if (name.getLongName().equals(longName))
				return name;

		// Not found, return null
		return null;
	}

	/**
	 * Searches for a parameter name by a parameter
	 * 
	 * @param param
	 *            the parameter
	 * @return if there is this parameter the parameter name, otherwise null
	 */
	public ParameterName getNameByParam(Parameter param) {

		// Iterate over keys and return it if there is one with the given short
		// name.
		for (final ParameterName name : keySet())
			if (get(name).equals(param))
				return name;

		// Not found, return null
		return null;
	}

	/**
	 * Searches for a parameter name by a short name
	 * 
	 * @param shortName
	 *            the short name
	 * @return if there is a parameter with the given short name the parameter,
	 *         otherwise null
	 */
	public ParameterName getNameByShort(Character shortName) {

		// Iterate over keys and return it if there is one with the given short
		// name.
		for (final ParameterName name : keySet())
			if (name.hasShort())
				if (name.getShortName().equals(shortName))
					return name;

		// Not found, return null
		return null;
	}

	/**
	 * Searches for a parameter name with the given short name Pass-trough
	 * string as char to {@link #getNameByShort(Character)}
	 * 
	 * @param shortName
	 *            the short name
	 * @return the parameter name or null
	 * @see #getNameByShort(Character)
	 */
	public ParameterName getNameByShort(String shortName) {
		return getNameByShort(shortName.charAt(0));
	}
	/**
	 * Checks whether a {@link String} is a valid parameter key by checking if starts with - or -- and is followed by an string without white-space charaters.
	 * @param s
	 * @return
	 */
	public boolean isParameter(String s){
		return s.matches("(-{1,2}\\^s*)");
	}
	/**
	 * Parses an argument array
	 * @param args the array
	 */
	public void parse(String[] args) {
		
		//Make list
		ArrayList<String> raw = new ArrayList<>(Arrays.asList(args));

		// Cancel if is empty
		if (raw.isEmpty())
			return;

		// First argument in list is action if is no parameter
		if(!isParameter(raw.get(0)))
			setAction(raw.remove(0));

		// List for parameters as ||arg, arg, arg|, |arg, arg, arg||, first arg
		// is parameter, rest are arguments
		final ArrayList<ArrayList<String>> parameters = new ArrayList<>();

		if (logger.isTraceEnabled())
			logger.trace("Iterating over arguments list.");

		// Iterate over args array, fill parameters list
		for (final String item : raw)

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
				getArguments().add(item);

		if (logger.isTraceEnabled())
			logger.trace("Iterate over found parameters and include them");

		// Iterate over parameters list, fill parameters object
		for (final ArrayList<String> parameter : parameters)

			// Check if is not empty
			if (parameter.size() >= 1) {

				// Get parameter or stay null if is no parameter.
				ParameterName param = null;
				if (parameter.get(0).startsWith("--"))
					param = getNameByLong(parameter.get(0).replaceAll(
							"^--", ""));
				else if (parameter.get(0).startsWith("-"))
					param = getNameByShort(parameter.get(0).replaceAll(
							"^-", ""));

				// Add as argument if is no parameter or add to parameters if is
				// parameter
				if (param == null)
					getArguments().addAll(parameter);
				else {
					// Iterate over parameter arguments and add as value
					for (final String arg : parameter.subList(1,
							parameter.size()))

						// Add to normal arguments if parameter value is full
						if (!get(param).add(arg))
							getArguments().add(arg);

					// Set present
					get(param).setPresent(true);
				}

			} else
				continue;
		
		if (logger.isDebugEnabled())
			logger.debug("Parameters: {}", toString());
	}

	/**
	 * Runs the set action
	 */
	public void runAction() {
		if (getActions().containsKey(action))
			getActions().get(action).run(this);
		else
			if(logger.isErrorEnabled())
				logger.error("No such action \"{}\"", action);
	}

	/**
	 * Sets the action key
	 * 
	 * @param key
	 *            the key
	 */
	public void setAction(String key) {
		action = key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractMap#toString()
	 */
	@Override
	public String toString() {
		return String.format("Parameters: %s, Arguments: %s", super.toString(),
				getArguments().toString());
	}
}
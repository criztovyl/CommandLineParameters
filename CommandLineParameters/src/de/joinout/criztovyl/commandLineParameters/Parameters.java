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
import java.util.HashMap;

/**
 * Represents a map of parameters where the parameter name is the key and the
 * parameter the value.<br>
 * 
 * @author criztovyl
 * 
 */
public class Parameters extends HashMap<ParameterName, Parameter> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1764523522307086756L;

	private final ArrayList<String> arguments;

	private final String description;

	private final HashMap<String, ParameterAction> actions;

	private String action;

	/**
	 * Creates a new map of parameters.
	 */
	public Parameters() {

		// Setup
		this("");
	}

	/**
	 * Creates a new map of parameters.<br>
	 * Includes a given description.
	 * 
	 * @param description
	 *            the description, will be the first line(s) of the help.
	 */
	public Parameters(String description) {

		// Setup map
		super();

		// Setup arguments
		arguments = new ArrayList<String>();

		// Setup description
		this.description = description == null ? "" : description;

		// Setup actions and action
		actions = new HashMap<>();
		action = "";

	}

	/**
	 * Adds a action to the given key
	 * 
	 * @param key
	 *            the key
	 * @param action
	 *            the action
	 */
	public void addAction(String key, ParameterAction action) {
		getActions().put(key, action);
	}

	/**
	 * Checks if this contains the given key. Key will be used to match against
	 * the {@link ParameterName#getShortName()}
	 * 
	 * @param key
	 */
	public boolean containsKey(Character key) {

		// pass-through
		return containsShort(key);
	}

	/**
	 * Checks if this contains the given key. Key will be used to match against
	 * the {@link ParameterName#getLongName()}
	 * 
	 * @param key
	 */
	public boolean containsKey(String key) {

		// pass-through
		return containsLong(key);
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
	 * @return the arguments
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
		
		//Setup strings
		final String newline = String.format("%n");
		final String lineseparator = String.format("\t-----------------------------%n");
		String help = "";
		
		//Add description with trailing tab
		for (final String str : description.split(newline))
			help += "\t" + str + newline;
		
		//Separate and add title (with two newlines)
		help += lineseparator;
		help += "\tActions:" + newline + newline;
		
		//Add help for each action, with trailing tab(s) 
		//Format: (tab)(name):(newline)
		//            (description)
		for (final String name : getActions().keySet())
			help += String.format(
					"\t---------%n\t%s:%n\t\t%s%n",
					name,
					getActions().get(name).getDescription().replaceAll(newline,
							newline + "\t\t\t"));
		
		//Separate and add title (with two newlines)
		help += lineseparator;
		help += "\tParameters:" + newline + newline;

		//Add help for each parameter, with trailing tab(s) 
		//Format: (tab)--(long name)(/-(short name)):(newline)
		//            (description)
		//Short name only if has one, when, then a slash is suffixed.
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
	 * Runs the set action
	 */
	public void runAction() {
		if (getActions().containsKey(action))
			getActions().get(action).run(this);
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
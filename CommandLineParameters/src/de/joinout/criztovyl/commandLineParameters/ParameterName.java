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

/**
 * 
 * Represents an object that store the long and short name of an parameter.
 * 
 * @author criztovyl
 * 
 */
public class ParameterName {

	private final String longName;
	private final Character shortName;

	/**
	 * Creates a new parameter name with the given long name<br>
	 * ShortName is disabled on this name.<br>
	 * Example: <br>
	 * Name: Test<br>
	 * longName : "test", will be "--test" on command line<br>
	 * 
	 * @param longName
	 *            the long name
	 */
	public ParameterName(String longName) {
		this(longName, null);
	}

	/**
	 * Creates a new parameter name with the given long and short name<br>
	 * If shortName is empty its disabled on this name.<br>
	 * Example: <br>
	 * Name: Test<br>
	 * longName : "test", will be "--test" on command line<br>
	 * shortName : "t", will be "-test" on command line<br>
	 * 
	 * @param longName
	 *            the long name
	 * @param shortName
	 *            the short name, only one character
	 */
	public ParameterName(String longName, Character shortName) {
		this.longName = longName;
		this.shortName = shortName;
	}

	/**
	 * Checks if the parameters are equal. Checked by the long name.
	 * 
	 * @param pname
	 *            the parameter name object
	 * @return true if long names are equal.
	 */
	public boolean equals(ParameterName pname) {
		return getLongName().equals(pname.getLongName());
	}

	/**
	 * 
	 * @return the long name of this parameter name
	 */
	public String getLongName() {
		return longName;
	}

	/**
	 * 
	 * @return the long name of this parameter name, can be null if have no.
	 */
	public Character getShortName() {
		return shortName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getLongName().hashCode();
	}

	/**
	 * Checks if this parameter name has a short name
	 * 
	 * @return true if has short name, otherwise false.
	 */
	public boolean hasShort() {
		return !(shortName == null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getLongName();
	}
}

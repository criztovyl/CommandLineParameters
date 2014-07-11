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

/**
 * Represents a Parameter
 * 
 * @author criztovyl
 * 
 */
public class Parameter extends ArrayList<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9142199409145022475L;
	private boolean present;
	private String description;
	private final Integer size;

	/**
	 * Creates a new Parameter which allows only a single argument
	 */
	public Parameter() {
		this(null);
	}

	/**
	 * Creates a new Parameter which can hold arguments
	 * 
	 * @param size
	 *            how much arguments this argument are allowed to store, use -1
	 *            for unlimited.
	 */
	public Parameter(Integer size) {
		super();
		present = false;
		description = "";
		this.size = size == null ? 1 : size;
	}

	@Override
	public boolean add(String e) {
		if (size() < size || size == -1) {
			super.add(e);
			return true;
		} else
			return false;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public Parameter setDescription(String description) {
		this.description = description;
		return this;
	}

	public void setPresent(boolean present) {
		this.present = present;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#toString()
	 */
	@Override
	public String toString() {
		return wasPresent() ? super.toString() : Boolean.toString(false);
	}

	public boolean wasPresent() {
		return present;
	}
}

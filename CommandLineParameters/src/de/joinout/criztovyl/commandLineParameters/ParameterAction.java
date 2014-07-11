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
 * Represents a Action
 * 
 * @author criztovyl
 * 
 */
public interface ParameterAction {

	/**
	 * Runs a specified action
	 * 
	 * @param param
	 *            all parameters needed for the environment
	 */
	public void run(Parameters param);
	
	/**
	 * 
	 * @return the description of this parameter.
	 */
	public String getDescription();
}

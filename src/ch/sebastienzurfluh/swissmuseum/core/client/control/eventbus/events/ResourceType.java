/*
 * Copyright 2012-2013 Sebastien Zurfluh
 * 
 * This file is part of "Parcours".
 * 
 * "Parcours" is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * "Parcours" is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with "Parcours".  If not, see <http://www.gnu.org/licenses/>.
 */

package ch.sebastienzurfluh.swissmuseum.core.client.control.eventbus.events;

/**
 * This is a enumeration of all possible resource types, as each resource is displayed differently.
 * @author Sebastien Zurfluh
 *
 */
public enum ResourceType {
	NONE,
	IMAGE,
	VIDEO,
	AUDIO;
	
	public static ResourceType fromString(String resourceType) {
		System.out.println("ResourceType: fromString: converting " + resourceType);
		if (resourceType.equals("img"))
			return IMAGE;
		if (resourceType.equals("vid"))
			return VIDEO;
		if (resourceType.equals("aud"))
			return AUDIO;
		return NONE;
	}
}
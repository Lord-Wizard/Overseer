/*
 * Copyright (c) 2015, Sonny Ruff
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of Sonny Ruff nor the names
 *    of its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

package system;

import java.util.ArrayList;
import java.util.HashMap;

import visualisation.mAbstractVisualisationSet;
import visualisation.mSysNetVis;

public class mSysNet
{
	public ArrayList<mSysComp> componentList = new ArrayList<mSysComp>();
	public ArrayList<mSysCon> connectionList = new ArrayList<mSysCon>();
	
	public mSysNetVis netVis;
	
	// CONSTRUCTOR
	public mSysNet(mAbstractVisualisationSet visSet)
	{
		netVis = new mSysNetVis(this, visSet);
	}
	
	//===================================================
	// METHODS
	//===================================================
	public int getConnectionCountBetweenNodes(mSysComp c1, mSysComp c2)
	{
		return c1.outgoingConnections.get(c2).size() + c1.incomingConnections.get(c2).size();
	}
	
	//===================================================
	// GETTERS & SETTERS
	//===================================================
	public void addComponent(mSysComp c)
	{
		if(componentList.add(c))
		{
			c.net = this;
			netVis.activeComponents.add(c);
		}
	}
	public void removeComponent(mSysComp c)
	{
		// Check if the requisted component could be removed
		if(componentList.remove(c))
		{
			c.net = null;
			netVis.selectedComponents.remove(c);
			
			for(ArrayList<mSysCon> list : c.outgoingConnections.values())
			{
				for(int i = 0; i < list.size(); i++)
				{
					removeConnection(list.get(i));
				}
			}
			for(ArrayList<mSysCon> list : c.incomingConnections.values())
			{
				for(int i = 0; i < list.size(); i++)
				{
					removeConnection(list.get(i));
				}
			}
		}
	}
	public void removeComponents(mSysComp[] array)
	{
		for(mSysComp c : array)
		{
			removeComponent(c);
		}
	}
	
	public void addConnection(mSysComp c1, mSysComp c2, String connectionType)
	{
		addConnection(c1, c2, connectionType, new HashMap<String, Object>());
	}
	public void addConnection(mSysComp c1, mSysComp c2,
			String connectionType, HashMap<String, Object> customData)
	{
		addConnection(new mSysCon(connectionList.size(), "con" + connectionList.size(), c1, c2, connectionType, customData));
	}
	public void addConnection(mSysCon c)
	{
		connectionList.add(c);
		
		if(c.c1.outgoingConnections.get(c.c2) == null)
			c.c1.outgoingConnections.put(c.c2, new ArrayList<mSysCon>());
		if(c.c2.incomingConnections.get(c.c1) == null)
			c.c2.incomingConnections.put(c.c1, new ArrayList<mSysCon>());
		
		c.c1.outgoingConnections.get(c.c2).add(c);
		c.c2.incomingConnections.get(c.c1).add(c);
		
		netVis.activeConnections.add(c);
	}
	public void removeConnection(mSysCon c)
	{
		if(connectionList.remove(c))
		{
			c.c1.outgoingConnections.get(c.c2).remove(c);
			c.c2.incomingConnections.get(c.c1).remove(c);
			
			netVis.selectedConnections.remove(c);
		}
	}
	public void removeConnections(mSysCon[] array)
	{
		for(mSysCon c : array)
		{
			removeConnection(c);
		}
	}
	public void removeConnectionBetween(mSysComp comp1, mSysComp comp2, mSysCon con)
	{
		for(mSysCon c : comp1.outgoingConnections.get(comp2))
		{
			// Check if the connection exists
			if(c == con)
				removeConnection(c);
		}
	}
}
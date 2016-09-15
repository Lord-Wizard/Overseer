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

package visualisation;

import java.util.ArrayList;

import math.mVector;
import processing.core.PApplet;
import system.mSysComp;
import system.mSysCon;
import system.mSysNet;

public class mSysNetVis
{
	public mSysNet net;
	public mAbstractVisualisationSet visSet;
	
	public mVector offset = new mVector();
	public double scale = 1;
	
	public int gridCellWidth = 100;
	
	public ArrayList<mSysComp> activeComponents = new ArrayList<mSysComp>();
	public ArrayList<mSysComp> selectedComponents = new ArrayList<mSysComp>();
	public ArrayList<mSysCon> activeConnections = new ArrayList<mSysCon>();
	public ArrayList<mSysCon> selectedConnections = new ArrayList<mSysCon>();
	public ArrayList<mAbstractConstrain> constrains = new ArrayList<mAbstractConstrain>();
	
	// CONSTRUCTOR
	public mSysNetVis(mSysNet net, mAbstractVisualisationSet visSet)
	{
		this.net = net;
		this.visSet = visSet;
		
		for(mSysComp c : net.componentList)
		{
			activeComponents.add(c);
		}
	}
	
	//===================================================
	// METHODS
	//===================================================
	public void arrangeComponentsInCircle(
			mVector position,
			int radius
		)
		{
			double angle = 2 * Math.PI / net.componentList.size();
			
			int i = 0;
			for(mSysComp v : net.componentList)
			{
				v.position.relocate(
						Math.cos(angle * i) * radius + position.x,
						Math.sin(angle * i) * radius + position.y
					);
				
				i++;
			}
		}
	public void arrangeComponentsInGrid(
		mVector position,
		int offset
	)
	{
		
	}
	
	public void drawOrigin(PApplet applet)
	{
		applet.stroke(200, 200, 200, 90);
		applet.strokeWeight(0.5f);
		for(int i = 0; i < applet.displayWidth / gridCellWidth; i++)
		{
			applet.line(
					(float)(gridCellWidth * i + (offset.x % gridCellWidth)), 0,
					(float)(gridCellWidth * i + (offset.x % gridCellWidth)), applet.displayHeight
				);
		}
		for(int i = 0; i < applet.displayHeight / gridCellWidth; i++)
		{
			applet.line(
					0,						(float)(gridCellWidth * i + (offset.y % gridCellWidth)),
					applet.displayWidth,	(float)(gridCellWidth * i + (offset.y % gridCellWidth))
				);
		}
		applet.stroke(200, 200, 200, 30);
		for(int i = 0; i < applet.displayWidth / gridCellWidth; i++)
		{
			applet.line(
					(float)(gridCellWidth * i + (offset.x % gridCellWidth) - gridCellWidth / 2), 0,
					(float)(gridCellWidth * i + (offset.x % gridCellWidth) - gridCellWidth / 2), applet.displayHeight
				);
		}
		for(int i = 0; i < applet.displayHeight / gridCellWidth; i++)
		{
			applet.line(
					0,						(float)(gridCellWidth * i + (offset.y % gridCellWidth) - gridCellWidth / 2),
					applet.displayWidth,	(float)(gridCellWidth * i + (offset.y % gridCellWidth) - gridCellWidth / 2)
				);
		}
		
		applet.strokeWeight(5);
		applet.pushMatrix();
		applet.translate((float)offset.x, (float)offset.y);
		applet.line(-10, 0, 10, 0);
		applet.line(0, -10, 0, 10);
		applet.popMatrix();
	}
	public void drawComponents(PApplet applet)
	{
		applet.pushMatrix();
		applet.translate((float)offset.x, (float)offset.y);
		for(mSysComp c : activeComponents)
		{
			visSet.getComponentOfType(c.type).draw(applet, c);
		}
		for(mSysComp c : selectedComponents)
		{
			visSet.getComponentOfType(c.type).drawSelected(applet, c);
		}
		applet.popMatrix();
	}
	public void drawConnections(PApplet applet)
	{
		applet.pushMatrix();
		applet.translate((float)offset.x, (float)offset.y);
		for(mSysCon c : activeConnections)
		{
			visSet.getConnectionOfType(c.type).draw(applet, c);
		}
		for(mSysCon c : selectedConnections)
		{
			visSet.getConnectionOfType(c.type).drawSelected(applet, c);
		}
		applet.popMatrix();
	}
	public void drawConstrains(PApplet applet)
	{
		applet.pushMatrix();
		applet.translate((float)offset.x, (float)offset.y);
		for(mAbstractConstrain c : constrains)
		{
			c.draw(applet, 
				(int)(c.position.x),
				(int)(c.position.y));
		}
		applet.popMatrix();
	}
	//===================================================
	// GETTERS & SETTERS
	//===================================================
	public void enable(mSysComp c)
	{
		if(net.componentList.contains(c))
		{
			activeComponents.add(c);
			for(ArrayList<mSysCon> array : c.outgoingConnections.values())
			{
				for(mSysCon con : array)
				{
					activeConnections.add(con);
				}
			}
			for(ArrayList<mSysCon> array : c.incomingConnections.values())
			{
				for(mSysCon con : array)
				{
					activeConnections.add(con);
				}
			}
		}
	}
	public void enable(mSysCon c)
	{
		
	}
	public void disable(mSysComp c)
	{
		if(!net.componentList.contains(c))
		{
			activeComponents.remove(c);
			for(ArrayList<mSysCon> array : c.outgoingConnections.values())
			{
				for(mSysCon con : array)
				{
					activeConnections.remove(con);
				}
			}
			for(ArrayList<mSysCon> array : c.incomingConnections.values())
			{
				for(mSysCon con : array)
				{
					activeConnections.remove(con);
				}
			}
		}
	}
	public void disable(mSysCon c)
	{
		
	}
	
	public mSysComp getComponentHoveredOverByMouse(PApplet applet)
	{
		for(mSysComp c : activeComponents)
		{
			if(visSet.getComponentOfType(c.type).isMouseOver(c.position, new mVector(applet.mouseX, applet.mouseY).substract(offset)))
			{
				return c;
			}
		}
		for(mSysComp c : selectedComponents)
		{
			if(visSet.getComponentOfType(c.type).isMouseOver(c.position, new mVector(applet.mouseX, applet.mouseY).substract(offset)))
			{
				return c;
			}
		}
		return null;
	}
	public mSysCon getConnectionHoveredOverByMouse(PApplet applet)
	{
		for(mSysCon c : activeConnections)
		{
			if(visSet.getConnectionOfType(c.type).isMouseOver(c.c1.position, c.c2.position, new mVector(applet.mouseX, applet.mouseY).substract(offset)))
			{
				return c;
			}
		}
		for(mSysCon c : selectedConnections)
		{
			if(visSet.getConnectionOfType(c.type).isMouseOver(c.c1.position, c.c2.position, new mVector(applet.mouseX, applet.mouseY).substract(offset)))
			{
				return c;
			}
		}
		return null;
	}
	public mAbstractConstrain getConstrainHoveredOverByMouse(PApplet applet)
	{
		for(mAbstractConstrain c : constrains)
		{
			if(c.isMouseOver(new mVector(applet.mouseX, applet.mouseY).substract(offset)))
			{
				return c;
			}
		}
		return null;
	}
}
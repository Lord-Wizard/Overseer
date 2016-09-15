/*
 * Copyright (c) 2016, Sonny Ruff
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

import math.mVector;
import processing.core.PApplet;
import system.mSysComp;

public class CircleConstrain extends mAbstractConstrain
{
	public int radius;
	public int subdivision;
	
	public int margin = 15;
	
	{
		buttons = new mVector[1];
	}
	
	// CONSTRUCTOR
	public CircleConstrain(mVector position, int radius)
	{
		super(position);
		
		this.radius = radius;
	}
	
	public boolean isMouseOver(mVector mouse)
	{
		if(radius + margin > position.getDistanceTo(mouse) && position.getDistanceTo(mouse) > radius - margin)
		{
			return true;
		}
		return false;
	}
	public void attachComponent(mSysComp comp)
	{
		attachedComponents.add(comp);
		reorderComponents();
	}
	
	public void reorderComponents()
	{
		double theta = Math.PI * 2 / attachedComponents.size();
		
		for(int i = 0; i < attachedComponents.size(); i++)
		{
			attachedComponents.get(i).position.relocate(
					position.x + radius * Math.cos(theta * i),
					position.y + radius * Math.sin(theta * i)
				);
		}
	}
	
	public void draw(PApplet applet, int x, int y)
	{
		applet.stroke(200, 200, 200);
		applet.strokeWeight(3);
		applet.fill(100, 100, 100);
		applet.ellipse(
				x, y,
				16, 16);
		applet.strokeWeight(2);
		applet.noFill();
		applet.ellipse(
				x, y,
				radius * 2, radius * 2);
		applet.strokeWeight(margin * 2);
		applet.stroke(100, 100, 100, 50);
		applet.ellipse(
				x, y,
				radius * 2, radius * 2);
	}
}
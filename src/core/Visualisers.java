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

package core;

import math.mVector;
import processing.core.PApplet;
import system.mSysComp;
import system.mSysCon;
import visualisation.mAbstractVisualisationSet;
import visualisation.mSysCompVis;
import visualisation.mSysConVis;

public class Visualisers extends mAbstractVisualisationSet
{
	{
		compMap.put("DEFAULT", new mSysCompVis()
		{
			int radius = 30;
			
			@Override
			public boolean isMouseOver(mVector position, mVector mouse)
			{
				if(position.getDistanceTo(mouse) < radius)
				{
					return true;
				}
				return false;
			}

			double timer = 0;
			double dotSpeed = 0.1;
			
			public void update()
			{
				timer += dotSpeed;
				timer %= Math.PI * 2;
			}
			@Override
			public void draw(PApplet applet, mSysComp c)
			{
				applet.strokeWeight(3);
				applet.stroke(200, 200, 200);
				applet.fill(100, 100, 100);
				applet.ellipse(
						(float)c.position.x, (float)c.position.y,
						radius * 2, radius * 2);
				applet.fill(200, 200, 200);
				applet.textSize(15);
				applet.text(c.name, (float)c.position.x - applet.textWidth(c.name) / 2, (float)c.position.y);
				applet.textSize(8);
				applet.text(c.type, (float)c.position.x - applet.textWidth(c.type) / 2, (float)c.position.y + 10);
			}
			@Override
			public void drawSelected(PApplet applet, mSysComp c)
			{
				applet.strokeWeight(2);
				applet.stroke(255, 255, 255, 30);
				applet.noFill();
				applet.ellipse(
						(float)c.position.x, (float)c.position.y,
						radius * 2, radius * 2
					);
				applet.strokeWeight(5);
				applet.stroke(100, 100, 100);
				applet.arc(
						(float)c.position.x, (float)c.position.y,
						radius * 2 + 20, radius * 2 + 20, (float) timer, (float) (timer + Math.PI)
					);
				applet.arc(
						(float)c.position.x, (float)c.position.y,
						radius * 2 + 40, radius * 2 + 40, (float) -timer, (float) (-timer + Math.PI)
					);
				
				applet.translate(0, 0, 50);
				
				draw(applet, c);
				
				applet.translate(0, 0, -50);
			}
		});
		conMap.put("DEFAULT", new mSysConVis()
		{
			int margin = 10;
			
			@Override
			public boolean isMouseOver(mVector v1, mVector v2, mVector mouse)
			{
				// TODO Super lazy algorithm
				if(v1.getDistanceTo(mouse) + v2.getDistanceTo(mouse) < v1.getDistanceTo(v2) + margin)
				{
					return true;
				}
				return false;
			}
			
			float offset = 40;
			
			public void update()
			{
				
			}
			@Override
			public void draw(PApplet applet, mSysCon c)
			{
				float theta = PApplet.atan2((float)(c.c1.position.x - c.c2.position.x), (float)(c.c2.position.y - c.c1.position.y));
				
				float x1 = (float) (c.c1.position.x - (float)Math.sin(theta) * offset);
				float y1 = (float) (c.c1.position.y + (float)Math.cos(theta) * offset);
				float x2 = (float) (c.c2.position.x + (float)Math.sin(theta) * offset);
				float y2 = (float) (c.c2.position.y - (float)Math.cos(theta) * offset);
				
				applet.strokeWeight(2);
				applet.stroke(200, 200, 200, 50);
				
				applet.line(x1, y1, x2, y2);
				
				applet.pushMatrix();
				applet.translate((x1 + x2) / 2, (y1 + y2) / 2);
				if(theta < 0)
				{
					applet.rotate((float) (theta + Math.PI / 2));
				} else {
					applet.rotate((float) (theta - Math.PI / 2));
				}
				applet.textSize(10);
				applet.text(c.name, -applet.textWidth(c.name) / 2, 10);
				applet.popMatrix();
				
				applet.pushMatrix();
				applet.translate(x2, y2);
				applet.rotate(theta);
				applet.line(0, 0, -10, -10);
				applet.line(0, 0, 10, -10);
				applet.popMatrix();
			}
			@Override
			public void drawSelected(PApplet applet, mSysCon c)
			{
				float theta = PApplet.atan2((float)(c.c1.position.x - c.c2.position.x), (float)(c.c2.position.y - c.c1.position.y));
				
				float x1 = (float) (c.c1.position.x - (float)Math.sin(theta) * offset);
				float y1 = (float) (c.c1.position.y + (float)Math.cos(theta) * offset);
				float x2 = (float) (c.c2.position.x + (float)Math.sin(theta) * offset);
				float y2 = (float) (c.c2.position.y - (float)Math.cos(theta) * offset);
				
				applet.strokeWeight(2);
				applet.stroke(200, 200, 200);
				
				applet.line(x1, y1, x2, y2);
				
				applet.pushMatrix();
				applet.translate((x1 + x2) / 2, (y1 + y2) / 2);
				if(theta < 0)
				{
					applet.rotate((float) (theta + Math.PI / 2));
				} else {
					applet.rotate((float) (theta - Math.PI / 2));
				}
				applet.textSize(10);
				applet.text(c.name, -applet.textWidth(c.name) / 2, 10);
				applet.popMatrix();
				
				applet.pushMatrix();
				applet.translate(x2, y2);
				applet.rotate(theta);
				applet.line(0, 0, -10, -10);
				applet.line(0, 0, 10, -10);
				applet.popMatrix();
			}
		});
		conMap.put("INVERTING", new mSysConVis()
		{
			int margin = 10;
			
			@Override
			public boolean isMouseOver(mVector v1, mVector v2, mVector mouse)
			{
				// TODO Super lazy algorithm
				if(v1.getDistanceTo(mouse) + v2.getDistanceTo(mouse) < v1.getDistanceTo(v2) + margin)
				{
					return true;
				}
				return false;
			}
			
			float offset = 40;

			public void update()
			{
				
			}
			@Override
			public void draw(PApplet applet, mSysCon c)
			{
				float theta = PApplet.atan2((float)(c.c1.position.x - c.c2.position.x), (float)(c.c2.position.y - c.c1.position.y));
				
				float x1 = (float) (c.c1.position.x - (float)Math.sin(theta) * offset);
				float y1 = (float) (c.c1.position.y + (float)Math.cos(theta) * offset);
				float x2 = (float) (c.c2.position.x + (float)Math.sin(theta) * offset);
				float y2 = (float) (c.c2.position.y - (float)Math.cos(theta) * offset);
				
				applet.strokeWeight(2);
				applet.stroke(200, 200, 200, 50);
				
				applet.line(x1, y1, x2, y2);
				
				applet.pushMatrix();
				applet.translate((x1 + x2) / 2, (y1 + y2) / 2);
				if(theta < 0)
				{
					applet.rotate((float) (theta + Math.PI / 2));
				} else {
					applet.rotate((float) (theta - Math.PI / 2));
				}
				applet.textSize(10);
				applet.text(c.name, -applet.textWidth(c.name) / 2, 10);
				applet.popMatrix();
				
				applet.pushMatrix();
				applet.translate(x2, y2);
				applet.rotate(theta);
				applet.line(0, 0, -10, -10);
				applet.line(0, 0, 10, -10);
				applet.line(-10, -20, 10, -20);
				applet.popMatrix();
			}
			@Override
			public void drawSelected(PApplet applet, mSysCon c)
			{
				float theta = PApplet.atan2((float)(c.c1.position.x - c.c2.position.x), (float)(c.c2.position.y - c.c1.position.y));
				
				float x1 = (float) (c.c1.position.x - (float)Math.sin(theta) * offset);
				float y1 = (float) (c.c1.position.y + (float)Math.cos(theta) * offset);
				float x2 = (float) (c.c2.position.x + (float)Math.sin(theta) * offset);
				float y2 = (float) (c.c2.position.y - (float)Math.cos(theta) * offset);
				
				applet.strokeWeight(2);
				applet.stroke(200, 200, 200);
				
				applet.line(x1, y1, x2, y2);
				
				applet.pushMatrix();
				applet.translate((x1 + x2) / 2, (y1 + y2) / 2);
				if(theta < 0)
				{
					applet.rotate((float) (theta + Math.PI / 2));
				} else {
					applet.rotate((float) (theta - Math.PI / 2));
				}
				applet.textSize(10);
				applet.text(c.name, -applet.textWidth(c.name) / 2, 10);
				applet.popMatrix();
				
				applet.pushMatrix();
				applet.translate(x2, y2);
				applet.rotate(theta);
				applet.line(0, 0, -10, -10);
				applet.line(0, 0, 10, -10);
				applet.line(-10, -20, 10, -20);
				applet.popMatrix();
			}
		});
		conMap.put("DOTTED", new mSysConVis()
		{
			int margin = 10;
			
			@Override
			public boolean isMouseOver(mVector v1, mVector v2, mVector mouse)
			{
				// TODO Super lazy algorithm
				if(v1.getDistanceTo(mouse) + v2.getDistanceTo(mouse) < v1.getDistanceTo(v2) + margin)
				{
					return true;
				}
				return false;
			}
			
			float offset = 40;
			
			int timer = 0;
			int dotOffset = 20;
			int dotSpeed = 3;

			public void update()
			{
				timer += dotSpeed;
				timer %= dotOffset;
			}
			@Override
			public void draw(PApplet applet, mSysCon c)
			{
				float theta = PApplet.atan2((float)(c.c1.position.x - c.c2.position.x), (float)(c.c2.position.y - c.c1.position.y));
				
				float x1 = (float) (c.c1.position.x - (float)Math.sin(theta) * offset);
				float y1 = (float) (c.c1.position.y + (float)Math.cos(theta) * offset);
				float x2 = (float) (c.c2.position.x + (float)Math.sin(theta) * offset);
				float y2 = (float) (c.c2.position.y - (float)Math.cos(theta) * offset);
				
				applet.strokeWeight(2);
				applet.stroke(200, 200, 200, 30);
				
				applet.line(x1, y1, x2, y2);
				
				applet.pushMatrix();
				applet.translate((x1 + x2) / 2, (y1 + y2) / 2);
				if(theta < 0)
				{
					applet.rotate((float) (theta + Math.PI / 2));
				} else {
					applet.rotate((float) (theta - Math.PI / 2));
				}
				applet.textSize(10);
				applet.text(c.name, -applet.textWidth(c.name) / 2, 10);
				applet.popMatrix();
				
				
				applet.fill(200, 200, 200, 60);
				
				double length = Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
				
				applet.pushMatrix();
				applet.translate(x1, y1);
				applet.rotate(theta);
				for(int i = 0; i < length; i++)
				{
					if((i - timer) % dotOffset == 0)
					{
						applet.ellipse(0, i, 5, 5);
					}
				}
				applet.popMatrix();
			}
			@Override
			public void drawSelected(PApplet applet, mSysCon c)
			{
				float theta = PApplet.atan2((float)(c.c1.position.x - c.c2.position.x), (float)(c.c2.position.y - c.c1.position.y));
				
				float x1 = (float) (c.c1.position.x - (float)Math.sin(theta) * offset);
				float y1 = (float) (c.c1.position.y + (float)Math.cos(theta) * offset);
				float x2 = (float) (c.c2.position.x + (float)Math.sin(theta) * offset);
				float y2 = (float) (c.c2.position.y - (float)Math.cos(theta) * offset);
				
				applet.strokeWeight(2);
				applet.stroke(200, 200, 200, 30);
				
				applet.line(x1, y1, x2, y2);
				
				applet.pushMatrix();
				applet.translate((x1 + x2) / 2, (y1 + y2) / 2);
				if(theta < 0)
				{
					applet.rotate((float) (theta + Math.PI / 2));
				} else {
					applet.rotate((float) (theta - Math.PI / 2));
				}
				applet.textSize(10);
				applet.text(c.name, -applet.textWidth(c.name) / 2, 10);
				applet.popMatrix();
				
				
				applet.fill(200, 200, 200);
				
				double length = Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
				
				applet.pushMatrix();
				applet.translate(x1, y1);
				applet.rotate(theta);
				for(int i = 0; i < length; i++)
				{
					if((i - timer) % dotOffset == 0)
					{
						applet.ellipse(0, i, 5, 5);
					}
				}
				applet.popMatrix();
			}
		});
	}
}
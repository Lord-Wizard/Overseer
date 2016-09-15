/*
 * Copyright (c) 2014, Sonny Ruff
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Sonny Ruff
 * @version v3_0
 */
public class mGrid
{
	// CONSTRUCTOR
	public mGrid()
	{
		
	}
	
	/**
	 * 
	 * @param g - Graphics
	 * @param x - x coordinate of grid
	 * @param y - y coordinate of grid
	 * @param columnCount - Number of columns
	 * @param rowCount - Number of rows
	 * @param cellSizeX - Width of the cells
	 * @param cellSizeY - Height of the cells
	 */
	public void draw(Graphics g, int x, int y, int columnCount, int rowCount, int cellSizeX, int cellSizeY, Color color)
	{
		g.setColor(color);
		
		for(int i = 0; i < columnCount + 1; i++)
		{
			g.drawLine(
					x + i * cellSizeX,
					y,
					x + i * cellSizeX,
					y + rowCount * cellSizeY
				);
		}
		for(int i = 0; i < rowCount + 1; i++)
		{
			g.drawLine(
					x,
					y + i * cellSizeY,
					x + columnCount * cellSizeX,
					y + i * cellSizeY
				);
		}
	}
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("GridTest");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setSize(800, 650);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		
		final mGrid mGrid = new mGrid();
		final JPanel panel = new JPanel()
		{
			private static final long serialVersionUID = 2748795033706493681L;

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				setBackground(Color.BLACK);
				
				mGrid.draw(g, 10, 10, 70, 40, 10, 10, Color.WHITE);
			}
		};
		
		frame.add(panel);
		
		frame.setVisible(true); // Moet nadat alles aan window is toegevoegd !!!
		
		new Thread(new Runnable()
		{
			public void run()
			{
				while(true)
				{
					panel.repaint();
			        
					try {
						Thread.sleep(1000 / 30);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
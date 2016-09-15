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

package core;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.io.FilenameUtils;

import interpretation.mAbstractInterpretatorSet;
import math.mVector;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;
import system.mSysComp;
import system.mSysCon;
import system.mSysNet;
import system.control.PAppletListener;
import visualisation.CircleConstrain;
import visualisation.mAbstractConstrain;
import visualisation.mAbstractVisualisationSet;
import visualisation.mSysNetVis;

/**
 * A little program that will hopefully give some overview on things.
 * @author Sonny Ruff
 * @version 0.1
 */
public class Overseer extends PApplet
{
	private mAbstractInterpretatorSet interpretators = new Interpretators();
	private mAbstractVisualisationSet visSet = new Visualisers();
	public mSysNet net = new mSysNet(visSet);
	
	private CompPropertiesWindow compPropWindow = new CompPropertiesWindow();
	private ConPropertiesWindow conPropWindow = new ConPropertiesWindow();
	
	private ArrayList<mMenu> menus = new ArrayList<mMenu>();
	private mMenu clickMenu = new mMenu("clickMenu", new mButton[]
		{
			new mButton("New Component")
			{
				@Override
				public void onPressed()
				{
					mSysComp c = new mSysComp(net.componentList.size(), "comp" + net.componentList.size(), "DEFAULT");
					c.position.relocate(clickMenu.position.x - net.netVis.offset.x, clickMenu.position.y - net.netVis.offset.y);
					net.addComponent(c);
					clickMenu.visible = false;
				}
				@Override
				public void onHovered()
				{
					
				}
			},
			new mButton("New Constrain")
			{
				@Override
				public void onPressed()
				{
					net.netVis.constrains.add(
						new CircleConstrain(
							new mVector(clickMenu.position.x - net.netVis.offset.x, clickMenu.position.y - net.netVis.offset.y),
							200
						)
					);
					clickMenu.visible = false;
				}
				@Override
				public void onHovered()
				{
					
				}
			},
			new mButton("New Anchor")
			{
				@Override
				public void onPressed()
				{
					
				}
				@Override
				public void onHovered()
				{
					
				}
			}
		}
	);
	private mMenu compMenu = new mMenu("compMenu", new mButton[]
		{
			new mButton("Edit")
			{
				@Override
				public void onPressed()
				{
					SwingUtilities.invokeLater(new Runnable() {
			            public void run() {
			                //Turn off metal's use of bold fonts
			            UIManager.put("swing.boldMetal", Boolean.FALSE);
			                compPropWindow.createAndShowGUI();
			            }
			        });
					
					compMenu.visible = false;
				}
				@Override
				public void onHovered()
				{
					
				}
			},
			new mButton("Delete")
			{
				@Override
				public void onPressed()
				{
					deleteSelectedObjects();
					compMenu.visible = false;
				}
				@Override
				public void onHovered()
				{
					
				}
			}
		}
	);
	private mMenu conMenu = new mMenu("conMenu", new mButton[]
			{
				new mButton("Edit")
				{
					@Override
					public void onPressed()
					{
						SwingUtilities.invokeLater(new Runnable() {
				            public void run() {
				                //Turn off metal's use of bold fonts
				            UIManager.put("swing.boldMetal", Boolean.FALSE);
				                conPropWindow.createAndShowGUI();
				            }
				        });
						
						conMenu.visible = false;
					}
					@Override
					public void onHovered()
					{
						
					}
				},
				new mButton("Delete")
				{
					@Override
					public void onPressed()
					{
						deleteSelectedObjects();
						conMenu.visible = false;
					}
					@Override
					public void onHovered()
					{
						
					}
				}
			}
		);
	private mMenu sideMenu = new mMenu("sideMenu", new mButton[]
		{
			new mButton("Clear all")
			{
				@Override
				public void onPressed()
				{
					net.componentList.clear();
					net.connectionList.clear();
					net.netVis.activeComponents.clear();
					net.netVis.selectedComponents.clear();
					net.netVis.activeConnections.clear();
					net.netVis.selectedConnections.clear();
				}
				@Override
				public void onHovered()
				{
					
				}
			},
			new mButton("Save")
			{
				@Override
				public void onPressed()
				{
					String fileName = JOptionPane.showInputDialog(null, "Enter file name");
					
					if(fileName != null && fileName.length() > 0)
					{
						save(fileName);
					}
				}
				@Override
				public void onHovered()
				{
					
				}
			},
			new mButton("Load")
			{
				@Override
				public void onPressed()
				{
					JFileChooser fc = new JFileChooser("saves/");
					
					int returnVal = fc.showOpenDialog(null);
					
			        if (returnVal == JFileChooser.APPROVE_OPTION &&
			        	FilenameUtils.getExtension(fc.getSelectedFile().getAbsolutePath()).equals("json"))
			        {
			        	load(fc.getSelectedFile().toPath());
			        }
				}
				@Override
				public void onHovered()
				{
					
				}
			}
		}
	);
	
	public enum States
	{
		DEFAULT,
		MOVING_COMPONENTS,
		DRAWING_CONNECTION
	}
	public States currentState = States.DEFAULT;
	
	private ArrayList<PAppletListener> eavesdroppers = new ArrayList<PAppletListener>();
	
	public void settings()
	{
		size(1280, 720, P3D);
		smooth(3);
	}
	public void setup()
	{
		frameRate(30);
		
//		interpretators.map.get("java").load(Paths.get("saves/HelloWorld.java"));
//		System.exit(0);
		
		menus.add(clickMenu);
		menus.add(compMenu);
		menus.add(conMenu);
		menus.add(sideMenu);
		
		sideMenu.position.relocate(1170, 10);
		sideMenu.visible = true;
	}
	public void draw()
	{
		background(20);
		
		visSet.update();
		
		net.netVis.drawOrigin(this);
		
		net.netVis.drawConstrains(this);
		net.netVis.drawComponents(this);
		net.netVis.drawConnections(this);
		
		for(mMenu m : menus)
		{
			if(m.visible)
				m.draw(this);
		}
		
		switch(currentState)
		{
			case DEFAULT:
				break;
			case MOVING_COMPONENTS:
				break;
			case DRAWING_CONNECTION:
				if(!net.netVis.selectedComponents.isEmpty())
				{
					for(mSysComp c : net.netVis.selectedComponents)
					{
						stroke(200, 200, 200);
						line(
							(float)(net.netVis.offset.x + c.position.x),
							(float)(net.netVis.offset.y + c.position.y),
							mouseX,
							mouseY
						);
					}
				}
				break;
		}
		
		// TEXT ====================================================================
		fill(200, 200, 200);
		textSize(10);
		int cursor = 0;
		text("Offset : (" + net.netVis.offset.x + ", " + net.netVis.offset.y + ")", 10, cursor += 20);
		text("State : " + currentState.toString(), 10, cursor += 20);
		text("ScreenComponentType : " + getHoveredScreenComponentType()[0], 10, cursor += 20);
		
		cursor += 20;
		for(int i = 0; i < net.netVis.selectedComponents.size(); i++)
		{
			text(net.netVis.selectedComponents.get(i).name, 10, cursor += 10);
		}
		
		cursor += 20;
		for(int i = 0; i < net.netVis.selectedConnections.size(); i++)
		{
			text(net.netVis.selectedConnections.get(i).name, 10, cursor += 10);
		}
	}
	
	//===================================================
	// METHODS
	//===================================================
	public void save(String fileName)
	{
		System.out.println("Saving...");
		JSONObject json = new JSONObject();
		
		JSONArray components = new JSONArray();
		for(mSysComp c : net.componentList)
		{
			components.append(c.toJSON());
		}
		json.setJSONArray("components", components);
		JSONArray connections = new JSONArray();
		for(mSysCon c : net.connectionList)
		{
			connections.append(c.toJSON());
		}
		json.setJSONArray("connections", connections);
		
		saveJSONObject(json, "saves/" + fileName + ".json");
	}
	public void load(Path file)
	{
		switch(FilenameUtils.getExtension(file.toString()))
		{
			case "json":
				net = new mSysNet(visSet);
				
				JSONObject json = loadJSONObject(file.toString());
				
				JSONArray components = json.getJSONArray("components");
				for(int i = 0; i < components.size(); i++)
				{
					mSysComp comp = new mSysComp(components.getJSONObject(i));
					net.addComponent(comp);
				}
				JSONArray connections = json.getJSONArray("connections");
				for(int i = 0; i < connections.size(); i++)
				{
					mSysCon con = new mSysCon(connections.getJSONObject(i), net);
					net.addConnection(con);
				}
				break;
			case "java":
				break;
		}
	}
	
	//===================================================
	// INPUT
	//===================================================
	private boolean shiftPressed;
	private boolean ctrlPressed;
	
	/* Default layout
	switch(currentState)
	{
		case DEFAULT:
			switch(getHoveredScreenComponentType())
			{
				case CANVAS:
					switch(mouseButton)
					{
						case LEFT:
							break;
						case RIGHT:
							break;
					}
					break;
				case COMPONENT:
					switch(mouseButton)
					{
						case LEFT:
							break;
						case RIGHT:
							break;
					}
					break;
				case CONNECTION:
					switch(mouseButton)
					{
						case LEFT:
							break;
						case RIGHT:
							break;
					}
					break;
				case CONSTRAIN:
					switch(mouseButton)
					{
						case LEFT:
							break;
						case RIGHT:
							break;
					}
					break;
				case UI:
					switch(mouseButton)
					{
						case LEFT:
							break;
						case RIGHT:
							break;
					}
					break;
			}
		case DRAWING_CONNECTION:
			break;
		case MOVING_COMPONENTS:
			break;
	}
	 */
	// Mouse
	public void mouseClicked()
	{
		mSysNetVis nv = net.netVis;
		
		switch(currentState)
		{
			case DEFAULT:
				switch(getHoveredScreenComponentType()[0])
				{
					case CANVAS:
						switch(mouseButton)
						{
							case LEFT:
								deselectAll();
								break;
							case RIGHT:
								clickMenu.position.relocate(mouseX, mouseY);
								clickMenu.visible = true;
								break;
						}
						break;
					case COMPONENT:
						switch(mouseButton)
						{
							case LEFT:
								break;
							case RIGHT:
								compMenu.position.relocate(
									nv.getComponentHoveredOverByMouse(this).position.x + nv.offset.x,
									nv.getComponentHoveredOverByMouse(this).position.y + nv.offset.y);
								compMenu.visible = true;
								break;
						}
						break;
					case CONNECTION:
						switch(mouseButton)
						{
							case LEFT:
								break;
							case RIGHT:
								conMenu.position.relocate(mouseX, mouseY);
								conMenu.visible = true;
								break;
						}
						break;
					case CONSTRAIN:
						break;
					case UI:
						switch(mouseButton)
						{
							case LEFT:
								this.getButtonHoveredOverByMouse().onPressed();
								break;
							case RIGHT:
								break;
						}
						break;
				}
			case DRAWING_CONNECTION:
				break;
			case MOVING_COMPONENTS:
				break;
		}
		
		for(int i = 0; i < eavesdroppers.size(); i++)
		{
			eavesdroppers.get(i).mouseClicked();
		}
	}
	public void mousePressed()
	{
		mSysNetVis nv = net.netVis;
		
		switch(currentState)
		{
			case DEFAULT:
				switch(getHoveredScreenComponentType()[0])
				{
					case CANVAS:
						switch(mouseButton)
						{
							case LEFT:
								clickMenu.visible = false;
								compMenu.visible = false;
								conMenu.visible = false;
								break;
							case RIGHT:
								break;
						}
						break;
					case COMPONENT:
						switch(mouseButton)
						{
							case LEFT:
								selectComp(nv.getComponentHoveredOverByMouse(this));
								break;
							case RIGHT:
								selectComp(nv.getComponentHoveredOverByMouse(this));
								break;
						}
						break;
					case CONNECTION:
						switch(mouseButton)
						{
							case LEFT:
								selectCon(nv.getConnectionHoveredOverByMouse(this));
								break;
							case RIGHT:
								selectCon(nv.getConnectionHoveredOverByMouse(this));
								break;
						}
						break;
					case CONSTRAIN:
						break;
					case UI:
						break;
				}
			case DRAWING_CONNECTION:
				break;
			case MOVING_COMPONENTS:
				break;
		}
		
		for(int i = 0; i < eavesdroppers.size(); i++)
		{
			eavesdroppers.get(i).mousePressed();
		}
	}
	public void mouseReleased()
	{
		mSysNetVis nv = net.netVis;
		
		switch(currentState)
		{
			case DEFAULT:
				break;
			case MOVING_COMPONENTS:
				if(getHoveredScreenComponentType().length >= 2 && getHoveredScreenComponentType()[1] == ScreenObjectType.CONSTRAIN)
				{
					mAbstractConstrain constrain = net.netVis.getConstrainHoveredOverByMouse(this);
					
					for(mSysComp c : net.netVis.selectedComponents)
					{
						if(!constrain.attachedComponents.contains(c))
						{
							constrain.attachComponent(c);
						}
					}
				}
				currentState = States.DEFAULT;
				break;
			case DRAWING_CONNECTION:
				switch(getHoveredScreenComponentType()[0])
				{
					case CANVAS:
						currentState = States.DEFAULT;
						break;
					case COMPONENT:
						for(mSysComp c : net.netVis.selectedComponents)
						{
							net.addConnection(c, nv.getComponentHoveredOverByMouse(this), "");
						}
						currentState = States.DEFAULT;
						break;
					case CONNECTION:
						currentState = States.DEFAULT;
						break;
					case CONSTRAIN:
						currentState = States.DEFAULT;
						break;
					case UI:
						currentState = States.DEFAULT;
						break;
				}
				break;
		}
		
		for(int i = 0; i < eavesdroppers.size(); i++)
		{
			eavesdroppers.get(i).mouseReleased();
		}
	}
	public void mouseMoved()
	{
		for(int i = 0; i < eavesdroppers.size(); i++)
		{
			eavesdroppers.get(i).mouseMoved();
		}
	}
	public void mouseDragged()
	{
		mSysNetVis nv = net.netVis;
		
		switch(currentState)
		{
			case DEFAULT:
				switch(getHoveredScreenComponentType()[0])
				{
					case CANVAS:
						switch(mouseButton)
						{
							case LEFT:
								nv.offset.transform(
										mouseX - pmouseX,
										mouseY - pmouseY
									);
								break;
							case RIGHT:
								break;
						}
						break;
					case COMPONENT:
						switch(mouseButton)
						{
							case LEFT:
								currentState = States.MOVING_COMPONENTS;
								break;
							case RIGHT:
								currentState = States.DRAWING_CONNECTION;
								break;
						}
						break;
					case CONNECTION:
						break;
					case CONSTRAIN:
						break;
					case UI:
						break;
				}
			case DRAWING_CONNECTION:
				break;
			case MOVING_COMPONENTS:
				for(mSysComp c : net.netVis.selectedComponents)
				{
					if(shiftPressed)
					{
						c.position.relocate(
								mouseX - (mouseX - net.netVis.offset.x) % (net.netVis.gridCellWidth / 2) - net.netVis.offset.x,
								mouseY - (mouseY - net.netVis.offset.y) % (net.netVis.gridCellWidth / 2) - net.netVis.offset.y
							);
					} else {
						c.position.transform(
								mouseX - pmouseX,
								mouseY - pmouseY
							);
					}
				}
				break;
		}
		
		for(int i = 0; i < eavesdroppers.size(); i++)
		{
			eavesdroppers.get(i).mouseDragged();
		}
	}
	public void mouseWheel()
	{
		for(int i = 0; i < eavesdroppers.size(); i++)
		{
			eavesdroppers.get(i).mouseWheel();
		}
	}
	// Keyboard
	public void keyPressed()
	{
		if(keyCode == SHIFT)
		{
			shiftPressed = true;
		}
		if(keyCode == CONTROL)
		{
			ctrlPressed = true;
		}
		
		for(int i = 0; i < eavesdroppers.size(); i++)
		{
			eavesdroppers.get(i).keyPressed();
		}
	}
	public void keyReleased()
	{
		if(keyCode == SHIFT)
		{
			shiftPressed = false;
		}
		if(keyCode == CONTROL)
		{
			ctrlPressed = false;
		}
		
		for(int i = 0; i < eavesdroppers.size(); i++)
		{
			eavesdroppers.get(i).keyReleased();
		}
	}
	
	// Selecting --------------------------
	private void selectComp(mSysComp c)
	{
		if(!net.netVis.selectedComponents.contains(c))
		{
			if(!ctrlPressed)
			{
				deselectAll();
			}
			moveCompToSelectedCompList(c);
		} else {
			if(ctrlPressed)
			{
				moveCompToActiveCompList(c);
			}
		}
	}
	private void moveCompToSelectedCompList(mSysComp c)
	{
		for(mAbstractConstrain constrain : net.netVis.constrains)
		{
			if(constrain.attachedComponents.contains(c))
			{
				constrain.attachedComponents.remove(c);
			}
		}
		net.netVis.activeComponents.remove(c);
		net.netVis.selectedComponents.add(c);
	}
	private void moveCompToActiveCompList(mSysComp c)
	{
		net.netVis.activeComponents.add(c);
		net.netVis.selectedComponents.remove(c);
	}
	
	private void selectCon(mSysCon c)
	{
		if(!net.netVis.selectedConnections.contains(c))
		{
			if(!ctrlPressed)
			{
				deselectAll();
			}
			moveConToSelectedConList(c);
		} else {
			if(ctrlPressed)
			{
				moveConToActiveConList(c);
			}
		}
	}
	private void moveConToSelectedConList(mSysCon c)
	{
		net.netVis.activeConnections.remove(c);
		net.netVis.selectedConnections.add(c);
	}
	private void moveConToActiveConList(mSysCon c)
	{
		net.netVis.activeConnections.add(c);
		net.netVis.selectedConnections.remove(c);
	}
	
	private void deselectAll()
	{
		while(net.netVis.selectedComponents.iterator().hasNext())
		{
			moveCompToActiveCompList(net.netVis.selectedComponents.iterator().next());
		}
		while(net.netVis.selectedConnections.iterator().hasNext())
		{
			moveConToActiveConList(net.netVis.selectedConnections.iterator().next());
		}
	}
	
	// Deleting ---------------------------
	private void deleteSelectedObjects()
	{
		net.removeComponents(net.netVis.selectedComponents.toArray(new mSysComp[net.netVis.selectedComponents.size()]));
		net.removeConnections(net.netVis.selectedConnections.toArray(new mSysCon[net.netVis.selectedConnections.size()]));
	}
	
	// Test location on screen ------------
	private enum ScreenObjectType
	{
		UI,
		COMPONENT,
		CONNECTION,
		CONSTRAIN,
		CANVAS
	}
	private ScreenObjectType[] getHoveredScreenComponentType()
	{
		int i = 1;
		
		boolean onUI = false;
		boolean onComponent = false;
		boolean onConnection = false;
		boolean onConstrain = false;
		
		// UI
		if(getButtonHoveredOverByMouse() != null)
		{
			onUI = true;
			i++;
		}
		// Component
		if(net.netVis.getComponentHoveredOverByMouse(this) != null)
		{
			onComponent = true;
			i++;
		}
		// Connection
		if(net.netVis.getConnectionHoveredOverByMouse(this) != null)
		{
			onConnection = true;
			i++;
		}
		// Constrain
		if(net.netVis.getConstrainHoveredOverByMouse(this) != null)
		{
			onConstrain = true;
			i++;
		}
		
		// Instantiate array
		ScreenObjectType[] array = new ScreenObjectType[i];
		
		// Add stuff to the array
		int j = 0;
		
		if(onUI)
		{
			array[j] = ScreenObjectType.UI;
			j++;
		}
		if(onComponent)
		{
			array[j] = ScreenObjectType.COMPONENT;
			j++;
		}
		if(onConnection)
		{
			array[j] = ScreenObjectType.CONNECTION;
			j++;
		}
		if(onConstrain)
		{
			array[j] = ScreenObjectType.CONSTRAIN;
			j++;
		}
		array[j] = ScreenObjectType.CANVAS; // Canvas is always at the end
		
		return array;
	}
	
	//===================================================
	// NESTED CLASSES
	//===================================================
	public final mVector buttonDimensions = new mVector(100, 20);
	
	private class mMenu
	{
		public String name;
		
		public mVector position = new mVector();
		public boolean visible;
		
		public mButton[] buttons;
		
		// CONSTRUCTOR
		public mMenu(String name, mButton[] buttons)
		{
			this.name = name;
			this.buttons = buttons;
		}
		
		public void draw(PApplet applet)
		{
			for(int i = 0; i < buttons.length; i++)
			{
				buttons[i].draw(
					(int)position.x,
					(int)(position.y + i * buttonDimensions.y));
			}
		}
		
		public mButton getButtonOverWhichMouseHovers()
		{
			if(visible)
			{
				for(int i = 0; i < buttons.length; i++)
				{
					if(mouseX > position.x
					&& mouseX < position.x + buttonDimensions.x
					&& mouseY > position.y + i * buttonDimensions.y
					&& mouseY < position.y + (i + 1) * buttonDimensions.y)
						return buttons[i];
				}
			}
			return null;
		}
	}
	private abstract class mButton
	{
		public String id;
		
		private int textSize = 10;
		
		// CONSTRUCTOR
		public mButton(String id)
		{
			this.id = id;
		}
		
		public void draw(int x, int y)
		{
			stroke(20, 20, 20);
			strokeWeight(3);
			fill(200, 200, 200);
			rect(x, y, (int)buttonDimensions.x, (int)buttonDimensions.y);
			fill(100, 100, 100);
			textSize(textSize);
			text(id, (float)(x + (buttonDimensions.y - textSize) / 2), (float)(y + (buttonDimensions.y + textSize) / 2));
		}
		
		public abstract void onPressed();
		public abstract void onHovered();
	}
	
	public mButton getButtonHoveredOverByMouse()
	{
		for(mMenu m : menus)
		{
			if(m.getButtonOverWhichMouseHovers() != null && m.visible)
			{
				return m.getButtonOverWhichMouseHovers();
			}
		}
		return null;
	}
	
	
	private class CompPropertiesWindow extends JPanel implements ActionListener
	{
		private static final long serialVersionUID = -3000699855915730820L;
		
		private JLabel idLabel;
		private JLabel typeLabel;
		
		private String idString = "ID : ";
		private String typeString = "Type : ";
		
		private JTextField idField;
		private JComboBox<String> typeField;
		
		// CONSTRUCTOR
		public CompPropertiesWindow()
		{
			super(new BorderLayout());
			
			idLabel = new JLabel(idString);
			typeLabel = new JLabel(typeString);
			
			idField = new JTextField();
			idField.setColumns(10);
			idField.addActionListener(this);
			
			typeField = new JComboBox<String>(visSet.compMap.keySet().toArray(new String[visSet.compMap.keySet().size()]));
			typeField.setSelectedIndex(0);
			typeField.addActionListener(this);
			
			
			idLabel.setLabelFor(idField);
			typeLabel.setLabelFor(typeField);
			
			
			JPanel labelPane = new JPanel(new GridLayout(0, 1));
			labelPane.add(idLabel);
			labelPane.add(typeLabel);
			
			JPanel fieldPane = new JPanel(new GridLayout(0, 1));
			fieldPane.add(idField);
			fieldPane.add(typeField);
			
			
	        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	        add(labelPane, BorderLayout.CENTER);
	        add(fieldPane, BorderLayout.LINE_END);
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
	        Object source = e.getSource();
        	ArrayList<mSysComp> comps = net.netVis.selectedComponents;
	        if (source == idField)
	        {
	        	if(comps.size() == 1)
				{
					comps.get(0).name = idField.getText();
				} else {
					for(mSysComp s : comps)
					{
						s.name = idField.getText();
					}
				}
	        }
	        else if (source == typeField)
	        {
	        	if(comps.size() == 1)
				{
					comps.get(0).type = (String) typeField.getSelectedItem();
				} else {
					for(mSysComp s : comps)
					{
						s.type = (String) typeField.getSelectedItem();
					}
				}
	        }
		}
		
		public void createAndShowGUI()
		{
			ArrayList<mSysComp> comps = net.netVis.selectedComponents;
			if(comps.size() == 1)
			{
				idField.setText(comps.get(0).name);
			} else {
				idField.setText("~");
			}
			if(comps.size() == 1)
			{
				String[] array = visSet.compMap.keySet().toArray(new String[visSet.compMap.keySet().size()]);
				for(int i = 0; i < array.length; i++)
				{
					if(array[i].equals(comps.get(0).name))
					{
						typeField.setSelectedIndex(i);
						break;
					}
				}
			} else {
				typeField.setSelectedIndex(-1);
			}
			
	        //Create and set up the window.
	        JFrame frame = new JFrame("Component Properties");
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        
	        //Add contents to the window.
	        frame.setSize(200, 100);
	        frame.setResizable(false);
	        frame.setLocationRelativeTo(null);
	        frame.add(this);
	        
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	    }
	}
	private class ConPropertiesWindow extends JPanel implements ActionListener
	{
		private static final long serialVersionUID = 5748297920020254499L;
		
		private JLabel idLabel;
		private JLabel typeLabel;
		
		private String idString = "ID : ";
		private String typeString = "Type : ";
		
		private JTextField idField;
		private JComboBox<String> typeField;
		
		// CONSTRUCTOR
		public ConPropertiesWindow()
		{
			super(new BorderLayout());
			
			idLabel = new JLabel(idString);
			typeLabel = new JLabel(typeString);
			
			idField = new JTextField();
			idField.setColumns(10);
			idField.addActionListener(this);
			
			typeField = new JComboBox<String>(visSet.conMap.keySet().toArray(new String[visSet.conMap.keySet().size()]));
			typeField.setSelectedIndex(0);
			typeField.addActionListener(this);
			
			
			idLabel.setLabelFor(idField);
			typeLabel.setLabelFor(typeField);
			
			
			JPanel labelPane = new JPanel(new GridLayout(0, 1));
			labelPane.add(idLabel);
			labelPane.add(typeLabel);
			
			JPanel fieldPane = new JPanel(new GridLayout(0, 1));
			fieldPane.add(idField);
			fieldPane.add(typeField);
			
			
	        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	        add(labelPane, BorderLayout.CENTER);
	        add(fieldPane, BorderLayout.LINE_END);
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
	        Object source = e.getSource();
        	ArrayList<mSysCon> cons = net.netVis.selectedConnections;
	        if (source == idField)
	        {
	        	if(cons.size() == 1)
				{
					cons.get(0).name = idField.getText();
				} else {
					for(mSysCon s : cons)
					{
						s.name = idField.getText();
					}
				}
	        }
	        else if (source == typeField)
	        {
	        	if(cons.size() == 1)
				{
					cons.get(0).type = (String) typeField.getSelectedItem();
				} else {
					for(mSysCon s : cons)
					{
						s.type = (String) typeField.getSelectedItem();
					}
				}
	        }
		}
		
		public void createAndShowGUI()
		{
			ArrayList<mSysCon> cons = net.netVis.selectedConnections;
			if(cons.size() == 1)
			{
				idField.setText(cons.get(0).name);
			} else {
				idField.setText("~");
			}
			if(cons.size() == 1)
			{
				String[] array = visSet.conMap.keySet().toArray(new String[visSet.conMap.keySet().size()]);
				for(int i = 0; i < array.length; i++)
				{
					if(array[i].equals(cons.get(0).name))
					{
						typeField.setSelectedIndex(i);
						break;
					}
				}
			} else {
				typeField.setSelectedIndex(-1);
			}
			
	        //Create and set up the window.
	        JFrame frame = new JFrame("Connection Properties");
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        
	        //Add contents to the window.
	        frame.setSize(200, 100);
	        frame.setResizable(false);
	        frame.setLocationRelativeTo(null);
	        frame.add(this);
	        
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	    }
	}
	private class DebugWindow extends JFrame
	{
		
	}
	//===================================================||======
	// MAIN
	//===================================================||======
	public static void main(String[] args)
	{
		PApplet.main(new String[] {Overseer.class.getName()});
	}
}
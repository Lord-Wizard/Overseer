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
import java.util.Map.Entry;

import math.mVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class mSysComp
{
	public int id;
	public String name;
	public String type;
	public HashMap<String, Object> properties = new HashMap<String, Object>();
	protected mSysNet net;
	
	public HashMap<mSysComp, ArrayList<mSysCon>> outgoingConnections = new HashMap<mSysComp, ArrayList<mSysCon>>();
	public HashMap<mSysComp, ArrayList<mSysCon>> incomingConnections = new HashMap<mSysComp, ArrayList<mSysCon>>();
	
	// Visualisation
	public mVector position = new mVector();
	public mVector velocity = new mVector();
	
	// CONSTRUCTOR
	public mSysComp(int id)
	{
		this(id, "", "DEFAULT");
	}
	public mSysComp(int id, String name, String type)
	{
		this.id = id;
		this.name = name;
		this.type = type;
	}
	public mSysComp(JSONObject obj)
	{
		id = obj.getInt("componentID");
		name = obj.getString("componentName");
		type = obj.getString("componentType");
		for(int i = 0; i < obj.getJSONArray("componentProperties").size(); i++)
		{
			JSONObject o = obj.getJSONArray("componentProperties").getJSONObject(i);
			
			if(o.getString("valueType").equals("String"))
			{
				properties.put(
						o.getString("key"),
						o.getString("value")
					);
			}
			if(o.getString("valueType").equals("Integer"))
			{
				properties.put(
						o.getString("key"),
						o.getInt("value")
					);
			}
		}
		position.x = obj.getJSONObject("position").getDouble("x");
		position.y = obj.getJSONObject("position").getDouble("y");
	}
	
	public JSONObject toJSON()
	{
		JSONObject object = new JSONObject();

		object.setInt("componentID", id);
		object.setString("componentName", name);
		object.setString("componentType", type);
		
		JSONArray _properties = new JSONArray();
		for(Entry<String, Object> e : properties.entrySet())
		{
			JSONObject property = new JSONObject();
			property.setString("key", e.getKey());
			if(e.getValue() instanceof String)
			{
				property.setString("value", (String) e.getValue());
				property.setString("valueType", "String");
			}
			if(e.getValue() instanceof Integer)
			{
				property.setInt("value", (int) e.getValue());
				property.setString("valueType", "Integer");
			}
			
			_properties.append(property);
		}
		object.setJSONArray("componentProperties", _properties);
		
		JSONArray _outgoingConnections = new JSONArray();
		for(Entry<mSysComp, ArrayList<mSysCon>> e : outgoingConnections.entrySet())
		{
			JSONObject connectionSet = new JSONObject();
			connectionSet.setInt("componentID", e.getKey().id);
			
			JSONArray connections = new JSONArray();
			for(mSysCon c : e.getValue())
			{
				JSONObject connection = new JSONObject();
				connection.setInt("connectionID", c.id);
			}
			connectionSet.setJSONArray("connections", connections);
			
			_outgoingConnections.append(connectionSet);
		}
		object.setJSONArray("outgoingConnections", _outgoingConnections);
		
		JSONArray _incomingConnections = new JSONArray();
		for(Entry<mSysComp, ArrayList<mSysCon>> e : incomingConnections.entrySet())
		{
			JSONObject connectionSet = new JSONObject();
			connectionSet.setInt("componentID", e.getKey().id);
			
			JSONArray connections = new JSONArray();
			for(mSysCon c : e.getValue())
			{
				JSONObject connection = new JSONObject();
				connection.setInt("connectionID", c.id);
			}
			connectionSet.setJSONArray("connections", connections);
			
			_incomingConnections.append(connectionSet);
		}
		object.setJSONArray("incomingConnections", _incomingConnections);
		
		JSONObject _position = new JSONObject();
		_position.setDouble("x", position.x);
		_position.setDouble("y", position.y);
		object.setJSONObject("position", _position);
		
		return object;
	}
	
	//===================================================
	// GETTERS & SETTERS
	//===================================================
	public mSysNet getNet()
	{
		return net;
	}
}
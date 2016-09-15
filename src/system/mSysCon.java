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

package system;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.EqualsBuilder;

import processing.data.JSONArray;
import processing.data.JSONObject;

public class mSysCon
{
	public int id;
	public String name;
	public mSysComp c1;
	public mSysComp c2;
	public String type; // Information that always has to be provided, because I want it :|
	public HashMap<String, Object> properties = new HashMap<String, Object>();
	
	// CONSTRUCTOR
	public mSysCon(int id, String name, mSysComp c1, mSysComp c2, String type, HashMap<String, Object> properties)
	{
		this.id = id;
		this.name = name;
		this.c1 = c1;
		this.c2 = c2;
		this.type = type;
		this.properties = properties;
	}
	public mSysCon(JSONObject obj, mSysNet net)
	{
		id = obj.getInt("connectionID");
		name = obj.getString("connectionName");
		for(mSysComp c : net.componentList)
		{
			if(c.id == obj.getInt("c1"))
				c1 = c;
			if(c.id == obj.getInt("c2"))
				c2 = c;
		}
		type = obj.getString("connectionType");
		for(int i = 0; i < obj.getJSONArray("connectionProperties").size(); i++)
		{
			JSONObject o = obj.getJSONArray("connectionProperties").getJSONObject(i);
			
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
		
		net.addConnection(this);
	}
	
	@Override
    public boolean equals(Object obj) {
       if (!(obj instanceof mSysCon))
            return false;
        if (obj == this)
            return true;

        mSysCon rhs = (mSysCon) obj;
        return new EqualsBuilder().
            // if deriving: appendSuper(super.equals(obj)).
        	append(c1, rhs.c1).
        	append(c2, rhs.c2).
        	append(type, rhs.type).
        	append(properties, rhs.properties).
            isEquals();
    }

	public JSONObject toJSON()
	{
		JSONObject object = new JSONObject();

		object.setInt("connectionID", id);
		object.setString("connectionName", name);
		object.setInt("c1", c1.id);
		object.setInt("c2", c2.id);
		object.setString("connectionType", type);
		
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
		object.setJSONArray("connectionProperties", _properties);
		
		return object;
	}
}
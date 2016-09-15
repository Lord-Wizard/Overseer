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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import interpretation.mAbstractInterpretator;
import interpretation.mAbstractInterpretatorSet;
import system.mSysNet;

public class Interpretators extends mAbstractInterpretatorSet
{
	{
		map.put("java", new mAbstractInterpretator()
		{
			@Override
			public mSysNet load(Path file)
			{
				mSysNet net = new mSysNet(new Visualisers());
				
				try {
					BufferedReader reader = Files.newBufferedReader(file);
					String line = null;
					while((line = reader.readLine()) != null)
					{
						String[] wordArray = line.split(" ");
						for(String word : wordArray)
						{
							checkWord(word);
						}
					}
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				
				return net;
			}
			public File save()
			{
				return null;
			}
			
			boolean insideMethod;
			
			public void checkWord(String word)
			{
				switch(word)
				{
					case "public":
						if(!insideMethod)
						{
							
						} else {
							
						}
						break;
					case "private":
					case "protected":
					case "{":
					case "}":
				}
			}
		});
	}
}
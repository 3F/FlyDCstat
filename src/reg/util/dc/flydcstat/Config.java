/*
    * Copyright (C) 2013 Developed by reg <entry.reg@gmail.com>
    *
    * Licensed under the Apache License, Version 2.0 (the "License");
    * you may not use this file except in compliance with the License.
    * You may obtain a copy of the License at
    *
    *      http://www.apache.org/licenses/LICENSE-2.0
    *
    * Unless required by applicable law or agreed to in writing, software
    * distributed under the License is distributed on an "AS IS" BASIS,
    * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    * See the License for the specific language governing permissions and
    * limitations under the License.
 */

package reg.util.dc.flydcstat;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * https://bitbucket.org/3F/vssolutionbuildevent/src/e0867b0793bc556d0e03e263001ea32b9d9e3531/vsSolutionBuildEvent/Config.cs
 */
public class Config
{
    public static Settings data = null;
    private static String _path = "config";

    public static void load()
    {
        try
        (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(_path), "UTF-8")))
        {            
            String xml = "";
            String line;
            while((line = reader.readLine()) != null){
                xml += line;
            }
            
            XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library
            data = (Settings)xstream.fromXML(xml);
        }
        catch(Exception e){
            data = new Settings();
        }
    }
    
    public static void load(String path)
    {
        _path = path;
        load();
    }
    
    public static void save(String path) throws IOException
    {
        _path = path;
        save();
    }

    public static void save() throws IOException
    {
        BufferedWriter writer = null;
        try
        {
            if(data == null){
                data = new Settings();
            }
            XStream xstream = new XStream(new DomDriver());
            
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(_path), "UTF-8"));
            writer.write(xstream.toXML(data));
        }
        finally
        {
            if(writer != null){
                writer.close();
            }
        }
    }

    protected Config(){}
}
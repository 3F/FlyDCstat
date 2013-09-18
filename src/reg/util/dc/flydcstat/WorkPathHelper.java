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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class WorkPathHelper
{
    public String path;
    
    public WorkPathHelper(String path)
    {
        this.path = path;
    }
    
    public String getFavoriteFile(String name) throws IOException
    {
        String ret;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path + "/" + name), "UTF-8"))) {
            ret = "";
            String line;
            while((line = reader.readLine()) != null){
                ret += line + System.getProperty("line.separator");
            }
        }
        return ret;
    }
    
    public void saveFavoriteFileLocal(String data, String name) throws IOException
    {
        saveFavoriteFile(data, path + "/" + name);
    }
    
    public void saveFavoriteFile(String data, String name) throws IOException
    {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name), "UTF-8"))) {
            out.write(data);
        }
    }
}
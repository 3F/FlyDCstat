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
import java.util.regex.Pattern;

public class Export
{    
    public static boolean favoriteXmlActivate(TFavoriteValues[] favorites)
    {
        try{
            String data = _getFavoriteFile();
            for(TFavoriteValues fav : favorites){
                Pattern pat = Pattern.compile("(\\sconnect\\s*=\\s*)\"\\d\"([\\s\\S]+?Server[\\s\\S]+?"+fav.name+")", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
//                data = pat.matcher(data).replaceFirst("$1\\Q0\\E$2");
//                data = pat.matcher(data).replaceFirst("$1" + Matcher.quoteReplacement((fav.autoload)?"1":"0") + "$2");
                data = pat.matcher(data).replaceFirst("$1\" " + ((fav.autoload)?"1":"0") + "\" $2");//TODO: I don't know how to Quotation
            }
            data = data.replace("=\" ", "=\""); //TODO
            _saveFavoriteFile(data, Config.getFavoriteXml(true));
            return true;
        }
        catch(IOException e){
//            logger.log(Level.SEVERE, "fail getting", e);
        }
        return false;
    }
    
    public static boolean csv()
    {
        return false;
    }
    
    private static String _getFavoriteFile() throws IOException
    {
        String ret;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(Config.getFavoriteXml(false)), "UTF-8"))) {
            ret = "";
            String line;
            while((line = reader.readLine()) != null){
                ret += line + System.getProperty("line.separator");
            }
        }
        return ret;
    }
    
    private static void _saveFavoriteFile(String data, String name) throws IOException
    {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name), "UTF-8"))) {
            out.write(data);
        }        
    }
}

class TFavoriteValues
{
    public String name;
    public boolean autoload;

    public TFavoriteValues(String name, boolean activate)
    {
        this.name = name;
        autoload  = activate;
    }
}
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

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FavoriteXml
{
    protected String data;
    
    public static class TShortValues
    {
        public String name;
        public boolean autoload;

        public TShortValues(String name, boolean activate)
        {
            this.name = name;
            autoload  = activate;
        }
    }
    
    public FavoriteXml(String data)
    {
        this.data = data;
    }
    
    public String getData()
    {
        return data;
    }
    
    public boolean replaceParamConnect(TShortValues[] favorites)
    {
        boolean flagMinFind = false;
        for(FavoriteXml.TShortValues fav : favorites){
            //TODO: жадность - "(\\sconnect\\s*=\\s*)\"\\d\"(.+?Server.+?"+fav.name+")" либо:
            String r = "(\\sConnect\\s*=\\s*\")\\d(\"\\s*Description\\s*=\\s*\"[^\"]*\"\\s*Nick\\s*=\\s*\"[^\"]*\"\\s*Password\\s*=\\s*\"[^\"]*\"\\s*Server\\s*=\\s*\"[A-z\\s:/\\\\]*"+fav.name.trim()+")";
            Pattern pat = Pattern.compile(r, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Matcher m = pat.matcher(data);
//            if(!m.find()){ // maybe deleted
//                FlyDCstat.logger.log(Level.SEVERE, String.format("not match: %s", r));
//                return false;
//            }
            if(m.find()){
                flagMinFind = true;
            }
            //"$1\\Q0\\E$2"
            data = m.replaceFirst("$1" + Matcher.quoteReplacement((fav.autoload)?"1":"0") + "$2");
        }
        return flagMinFind;
    }
    
    public boolean isAutoloadHub(String name)
    {
        //TODO: up^ replaceParamConnect
        String r = "Connect\\s*=\\s*\\\"(\\d)\\\"\\s*Description\\s*=\\s*\\\"[^\\\"]*\\\"\\s*Nick\\s*=\\s*\\\"[^\\\"]*\\\"\\s*Password\\s*=\\s*\\\"[^\\\"]*\\\"\\s*Server\\s*=\\s*\\\"[A-z\\s:/\\\\]*" + name.trim();
        Pattern pat = Pattern.compile(r, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = pat.matcher(data);
        if(!m.find()){
            return false; // has already been removed from favoriteXml
        }
        if(m.group(1).contentEquals("1")){
            return true;
        }
        return false;
    }
    
    public boolean isExist(String name)
    {
        Pattern pat = Pattern.compile("Server\\s*=\\s*\\\"[A-z\\s:/\\\\]*" + name.trim(), Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        return pat.matcher(data).find();
    }
}
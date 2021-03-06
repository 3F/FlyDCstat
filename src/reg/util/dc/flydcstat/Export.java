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

import java.io.IOException;

public class Export
{
    public static boolean favoriteXmlActivate(FavoriteXml.TShortValues[] favorites)
    {
        try{
            WorkPathHelper w = new WorkPathHelper(Config.getWorkPath());
            FavoriteXml fav = new FavoriteXml(w.getFavoriteFile(Config.data.favoriteXml));
            if(!fav.replaceParamConnect(favorites)){
                return false;
            }
            w.saveFavoriteFile(fav.getData(), Config.getFavoriteXml(true));
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
}
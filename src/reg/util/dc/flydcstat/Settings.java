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

public class Settings
{
    /** full path to the database */
    public String dbPath        = "Settings/FlylinkDC.sqlite";
    public String favoriteXml   = "Favorites.xml";
    
    /** UI Language */
    public TLangUI lang = TLangUI.RU;
    public enum TLangUI{
        RU, EN
    };
    
    /**
     * actualisation data
     */    
    public TActual actual = TActual.FavoriteUse;
    public enum TActual
    {
        AllPeriods,
        FavoriteUse
    }    
}
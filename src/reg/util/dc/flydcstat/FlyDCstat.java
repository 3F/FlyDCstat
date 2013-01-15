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
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.Locale;
import java.util.ResourceBundle;
import reg.util.dc.flydcstat.exceptions.IllegalOperationException;

public class FlyDCstat
{
    private final static Logger logger = Logger.getLogger(FlyDCstat.class.getPackage().getName());
    
    public static void main(String[] args)
    {
        try{
            LogManager logMan = LogManager.getLogManager();
            logMan.readConfiguration(FlyDCstat.class.getResourceAsStream("logging.properties"));
        }
        catch(IOException e){
            System.err.println("cannot read configuration(logging.properties) from resource");
            e.printStackTrace();
            System.exit(IllegalOperationException.EXIT_CODE_CONFIG_READ_ERROR);
        }
        
        String dbName = (args.length > 0)? args[0].trim() : null;
        run(dbName, new Locale("ru", "RU")); //TODO: switch locale
    }
    
    private static void run(String dbName, Locale locale)
    {
        Dbases db       = new Dbases(dbName);
        FrontForm frm   = new FrontForm();
        try{
            new StatController(db, frm, ResourceBundle.getBundle("reg/util/dc/flydcstat/i18n/MessagesGUI", locale));
        }
        catch(IllegalOperationException e){
            logger.log(Level.SEVERE, "cannot start application", e);
            System.exit(e.getExitCode());
        }
        catch(Exception e){
            logger.log(Level.SEVERE, "cannot load ui messages", e);
            System.exit(IllegalOperationException.EXIT_CODE_UIMSG_READ_ERROR);
        }
    }
}
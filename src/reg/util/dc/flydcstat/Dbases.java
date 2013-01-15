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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.logging.*;
import java.util.ArrayList;
import org.sqlite.SQLiteConfig;

/**
 * Base model of statistics.
 * @author reg
 */
public class Dbases
{
    /**
     * Connecting to a database
     */
    private Connection db   = null;
    /**
     * The full path to the database in fs
     */
    private String dbName   = null;
    /**
     * return instance Logger
     */
    private final static Logger logger = Logger.getLogger(FlyDCstat.class.getPackage().getName());
    /**
     * Statistics group
     */
    protected enum TGroupStatistic
    {
        Download,   //Download statistics
        Upload      //Uploads statistics
    }
    /**
     * to sort the result-set in a SQL.
     * ascending / descending 
     */
    protected enum TSorted
    {
        ASC, DESC
    }
    /**
     * Path of default
     */
    private final static String dbDefault = "Settings/FlylinkDC.sqlite";
    
    /**
     * @param name DB name
     */
    public Dbases(String name)
    {
        dbName = (name == null)? dbDefault : name;
    }
    
    /**
     * Connects to the database, if this has not been done before.
     * @return false - if already connected; true - if new connect to DB.
     * @throws SQLException communication errors
     */
    protected boolean connect() throws SQLException
    {
        if(db != null && !db.isClosed()){
            return false;
        }
        
        try{
            //Class loading and initializing in the DriverManager
            Class.forName("org.sqlite.JDBC");
            
            SQLiteConfig config = new SQLiteConfig();
            config.setReadOnly(true);
            db = DriverManager.getConnection("jdbc:sqlite:" + dbName, config.toProperties());
            return true;
        }
        catch(Exception e){
            throw new SQLException("is not connect to DB", e);
        }
    }
    
    /**
     * Retrieving all rows by SELECT queries.
     * @param sql query
     * @return result-set rows or empty ArrayList (if data do not exist).
     * @throws SQLSyntaxErrorException Restriction on the type of query.
     * @throws SQLException
     */
    protected ArrayList<ArrayList<Object>> getRows(String sql) throws SQLSyntaxErrorException, SQLException
    {
        if(!sql.substring(0, 6).toLowerCase().contentEquals("select")){
            throw new SQLSyntaxErrorException("is not 'SELECT' query. Must be a start 'SELECT...': " + sql);
        }
        
        ArrayList<ArrayList<Object>> rows   = new ArrayList<>();
        Statement stmt                      = db.createStatement();
        ResultSet res                       = stmt.executeQuery(sql);
        int columns                         = res.getMetaData().getColumnCount();
        while(res.next()){
            ArrayList<Object> col = new ArrayList<>();
            for(int i = 1; i <= columns; i++){
                col.add(res.getObject(i));
            }
            rows.add(col);
        }
        return rows;
    }
    
    
    /**
     * Wrapper getting data from db
     * @param sql 
     * @return
     */
    protected ArrayList<ArrayList<Object>> wrapperGettingInfo(String sql)
    {
        try{
            connect(); //if needed
            return getRows(sql);
        }
        catch(Exception e){
            logger.log(Level.SEVERE, "cannot getting rows", e);
            return new ArrayList<ArrayList<Object>>();
        }
        finally{
//            close(); 
        }
    }
    
    /**
     * Formating size. IEC 60027
     * @param size Size of bytes
     * @return string with the prefix size
     */
    protected String formatSize(double size)
    {
        String[] text = new String[]{"bytes","KiB","MiB","GiB","TiB","PiB","EiB","ZiB","YiB"};
        int i = 0;
        while(i < text.length - 1 && (int)(size / 1024) > 0){
            size /= 1024;
            ++i;
        }
        if(i < 1){
            return (int)size + " " + text[i];
        }
        return String.format("%.3f", size) + " " + text[i];
    }
    
    /**
     * Viewing size in cell
     * @param size
     * @return 
     */
    protected String cellPrintSize(long size)
    {
        String formated = formatSize((double)size);
        if(size < 1024){
            return formated;
        }
        return formated + "   (" + size + " bytes)";
    }
    
    
    /**
     * Field in the tables on which produced statistic.
     * @param group Group enumeration
     * @return 
     */
    public String fieldStatistic(TGroupStatistic group)
    {
        switch(group){
            case Download:{
                return "download";
            }
            case Upload:{
                return "upload";
            }
            default:{
                throw new IllegalArgumentException("incorrect type: " + group);
            }
        }
    }
    
    
    /**
     * Traffic statistics by hub
     * @param group
     * @param order
     * @return 
     */
    public ArrayList<ArrayList<Object>> statTrafficByHub(TGroupStatistic group, TSorted order)
    {
        String field = fieldStatistic(group);
        String sql   = "SELECT hub, SUM("+ field +") AS "+ field +" FROM v_fly_ratio_all GROUP BY hub ORDER BY "+ field +" ";
        ArrayList<ArrayList<Object>> rows = wrapperGettingInfo(sql + order);
        
        for(ArrayList<Object> row: rows){
            row.set(0, row.get(0).toString().toLowerCase().replace("dchub://", ""));
            row.set(1, cellPrintSize(Long.valueOf(row.get(1).toString())));
        }
        return rows;
    }
    
    
    /**
     * Traffic statistics by nick
     * @param group
     * @param order
     * @return 
     */
    public ArrayList<ArrayList<Object>> statTrafficByNick(TGroupStatistic group, TSorted order)
    {
        String field = fieldStatistic(group);
        String sql   = "SELECT fly_dic.name AS nick, SUM("+ field +") AS "+ field +" "
                + "FROM fly_ratio INNER JOIN fly_dic ON dic_nick = fly_dic.id "
                + "WHERE "+ field +" > 0 GROUP BY nick ORDER BY "+ field +" ";
        ArrayList<ArrayList<Object>> rows = wrapperGettingInfo(sql + order);
        
        for(ArrayList<Object> row: rows){
            row.set(1, cellPrintSize(Long.valueOf(row.get(1).toString())));
        }
        return rows;
    }
    
    /**
     * DHT Traffic statistics
     * @param group
     * @param order
     * @return 
     */
    public ArrayList<ArrayList<Object>> statDhtTraffic(TGroupStatistic group, TSorted order)
    {
        String field = fieldStatistic(group);
        String sql   = "SELECT nick.name AS nickname, SUM("+ field +") AS "+ field +", ip.name AS ip "
                + "FROM fly_ratio "
                + "LEFT JOIN fly_dic AS nick ON dic_nick = nick.id "
                + "LEFT JOIN fly_dic AS hub ON dic_hub = hub.id "
                + "INNER JOIN fly_dic AS ip ON dic_ip = ip.id "
                + "WHERE hub.id IS NULL AND nick.id IS NOT NULL AND "+ field +" > 0 "
                + "GROUP BY nickname ORDER BY "+ field +" ";
        ArrayList<ArrayList<Object>> rows = wrapperGettingInfo(sql + order);
        
        for(ArrayList<Object> row: rows){
            row.set(1, cellPrintSize(Long.valueOf(row.get(1).toString())));
        }
        return rows;
    }
    
    /**
     * Total all upload/download
     * @return 
     */
    public ArrayList<ArrayList<Object>> statRatingAll()
    {
        String sql = "SELECT SUM(upload), SUM(download) FROM fly_ratio LIMIT 1";
        ArrayList<ArrayList<Object>> rows = wrapperGettingInfo(sql);
        
        for(ArrayList<Object> row: rows){
            double upload   = Double.valueOf(row.get(0).toString());
            double download = Double.valueOf(row.get(1).toString());
            row.set(0, cellPrintSize((long)upload));
            row.set(1, cellPrintSize((long)download));
            row.add(String.format("%.2f", upload / download));
            break;
        }
        return rows;
    }
    
    /**
     * Close the active connection
     * @return true - successfully closed; false - the connection was closed previously or an error has occurred
     */
    protected boolean close()
    {
        if(db != null){
            try{
                db.close();
                db = null;
                return true;
            }
            catch(SQLException e){
                logger.log(Level.WARNING, "problem close connection", e);
            }
        }
        return false;
    }
    
    public String getDbName()
    {
        return dbName;
    }    
    
    /**
     * Checking connectivity
     * @return 
     */
    public boolean tryToConnect()
    {
        try{
            connect();
            return true;
        }
        catch(Exception e){
            logger.log(Level.WARNING, "cannot connect to '" + dbName + "'", e);
        }
        finally{
//            close();
        }
        return false;
    }
}

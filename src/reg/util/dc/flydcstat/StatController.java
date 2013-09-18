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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import reg.util.dc.flydcstat.exceptions.IllegalOperationException;

/**
 * Controller of Model(Dbases) & View(FrontForm)
 * @author reg
 */
public class StatController
{
    public enum TStat
    {
        DownloadHub,
        DownloadNick,
        UploadHub,
        UploadNick,
        DhtDownload,
        DhtUpload,
        RatingAll,
        VisualData
    }
    
    private Dbases dBases   = null;
    private FrontForm view  = null;
    /**
     * UI messages
     */
    private ResourceBundle uimsg;
    
    public static TStat lastHandledStat;
    
    public StatController(Dbases db, FrontForm frm, ResourceBundle messages) throws IllegalOperationException
    {
        this.dBases     = db;
        this.view       = frm;
        this.uimsg      = messages;
        
        init();
        setHandlers();
    }

    private void init() throws IllegalOperationException
    {
        String path = dBases.getDbName();
        if(!dBases.tryToConnect()){
            JOptionPane.showMessageDialog(null, 
                                            uimsg.getString("db_notaccess").replace("%path%", path),
                                            uimsg.getString("db_notaccess_title"),
                                            JOptionPane.ERROR_MESSAGE);
            
            throw new IllegalOperationException("["+ path +"] no read access", IllegalOperationException.EXIT_CODE_DB_NOT_ACCESS);
        }
        
        view.i18n(uimsg);
        view.setLabelDbPath((new java.io.File(path)).getAbsolutePath(), uimsg.getString("menu_label_path"));
        view.setVisible(true);
    }
    
    /**
     * Wrapper events handler
     */
    private void warpperActionPerformed(TStat tstat, String[] columns, ArrayList<ArrayList<Object>> rows)
    {
        lastHandledStat = tstat;
        view.setTitle(_getTitleByStat(tstat));
        
        if(tstat == TStat.DownloadHub || tstat == TStat.UploadHub){
            view.tblInfo.setModel(view.tableModelBolleanFirst());
        }
        else{
            view.tblInfo.setModel(view.tableModelObjectAll());
        }

        DefaultTableModel tblModel = (DefaultTableModel)view.tblInfo.getModel();
        //erase all columns
        tblModel.setColumnCount(0);
        //erase all rows
        tblModel.setRowCount(0);
        
        //place new
        if(tstat == TStat.DownloadHub || tstat == TStat.UploadHub){
            tblModel.addColumn(""); //for checkbox, see model of tableModelBolleanFirst
            view.tblInfo.getColumnModel().getColumn(0).setMaxWidth(24);
        }
        for(String column: columns){
            tblModel.addColumn(column);
        }
        if(tstat == TStat.DownloadHub || tstat == TStat.UploadHub){
            view.tblInfo.getColumnModel().getColumn(0).setMaxWidth(24);
        }        
        
        if(rows.size() < 1){
            JOptionPane.showMessageDialog(view,
                                            uimsg.getString("stat_norecord"),
                                            uimsg.getString("stat_norecord_title"),
                                            JOptionPane.WARNING_MESSAGE);
            return;
        }
        for(ArrayList<Object> row: rows){
            tblModel.addRow(row.toArray());
        }
    }
    
    private String _getTitleByStat(TStat tstat)
    {
        switch(tstat)
        {
            case DhtDownload:{
                return uimsg.getString("stat_download_dht_wtitle");
            }
            case DhtUpload:{
                return uimsg.getString("stat_upload_dht_wtitle");
            }
            case DownloadHub:{
                return uimsg.getString("stat_download_hub_wtitle");
            }
            case DownloadNick:{
                return uimsg.getString("stat_download_nick_wtitle");
            }
            case RatingAll:{
                return uimsg.getString("stat_rating_total");
            }
            case UploadHub:{
                return uimsg.getString("stat_upload_hub_wtitle");
            }
            case UploadNick:{
                return uimsg.getString("stat_upload_nick_wtitle");
            }
            case VisualData:{
                break;
            }
        }
        return "";
    }
    
    /**
     * Events handler for menu - DownloadHub
     */
    private class actionDownloadHub implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            ArrayList<ArrayList<Object>> rows = dBases.statTrafficByHub(Dbases.TGroupStatistic.Download, Dbases.TSorted.DESC);
            String[] columns                  = new String[]{
                uimsg.getString("column_hub"),
                uimsg.getString("column_download")
            };
            warpperActionPerformed(TStat.DownloadHub, columns, rows);
        }
    }
    
    /**
     * Events handler for menu - UploadHub
     */    
    private class actionUploadHub implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            ArrayList<ArrayList<Object>> rows = dBases.statTrafficByHub(Dbases.TGroupStatistic.Upload, Dbases.TSorted.DESC);
            String[] columns                  = new String[]{
                uimsg.getString("column_hub"),
                uimsg.getString("column_upload")
            };
            warpperActionPerformed(TStat.UploadHub, columns, rows);
        }
    }    
    
    /**
     * Events handler for menu - DownloadNick
     */    
    private class actionDownloadNick implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            ArrayList<ArrayList<Object>> rows = dBases.statTrafficByNick(Dbases.TGroupStatistic.Download, Dbases.TSorted.DESC);
            String[] columns                  = new String[]{
                uimsg.getString("column_nick"),
                uimsg.getString("column_download")
            };
            warpperActionPerformed(TStat.DownloadNick, columns, rows);
        }
    }
    
    /**
     * Events handler for menu - UploadNick
     */    
    private class actionUploadNick implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            ArrayList<ArrayList<Object>> rows = dBases.statTrafficByNick(Dbases.TGroupStatistic.Upload, Dbases.TSorted.DESC);
            String[] columns                  = new String[]{
                uimsg.getString("column_nick"),
                uimsg.getString("column_upload")
            };
            warpperActionPerformed(TStat.UploadNick, columns, rows);
        }
    }
    
    /**
     * Events handler for menu - DhtDownload
     */    
    private class actionDhtDownload implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            ArrayList<ArrayList<Object>> rows = dBases.statDhtTraffic(Dbases.TGroupStatistic.Download, Dbases.TSorted.DESC);
            String[] columns                  = new String[]{
                uimsg.getString("column_nick"),
                uimsg.getString("column_download"),
                uimsg.getString("column_ip")
            };
            warpperActionPerformed(TStat.DhtDownload, columns, rows);
        }
    }
    
    /**
     * Events handler for menu - DhtUpload
     */    
    private class actionDhtUpload implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            ArrayList<ArrayList<Object>> rows = dBases.statDhtTraffic(Dbases.TGroupStatistic.Upload, Dbases.TSorted.DESC);
            String[] columns                  = new String[]{
                uimsg.getString("column_nick"),
                uimsg.getString("column_upload"),
                uimsg.getString("column_ip")
            };
            warpperActionPerformed(TStat.DhtUpload, columns, rows);
        }
    }    

    /**
     * Events handler for menu - RatingAll
     */    
    private class actionRatingAll implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            ArrayList<ArrayList<Object>> rows = dBases.statRatingAll();
            String[] columns                  = new String[]{
                uimsg.getString("column_upload"),
                uimsg.getString("column_download"),
                uimsg.getString("column_rating")
            };
            warpperActionPerformed(TStat.RatingAll, columns, rows);
        }
    }
       
    private class visualDataFocus implements ChangeListener
    {
        @Override
        public void stateChanged(ChangeEvent e)
        {
            lastHandledStat = TStat.VisualData;
            if(e.getSource() instanceof JTabbedPane){
                JTabbedPane pane = (JTabbedPane)e.getSource();
                if(pane.getSelectedIndex() == 0){ // also getSelectedComponent
                    return;
                }
                
                ((JpanelGraphics)view.jPanelDia).effectiveValueD = fillData(
                                                                        dBases.statTrafficByHubPairRaw(
                                                                                    Dbases.TGroupStatistic.Download, 
                                                                                    Dbases.TSorted.DESC), 
                                                                        0.04f, 
                                                                        new Color(122, 139, 51, 150));
                
                ((JpanelGraphics)view.jPanelDia).effectiveValueU = fillData(
                                                                        dBases.statTrafficByHubPairRaw(
                                                                                    Dbases.TGroupStatistic.Upload, 
                                                                                    Dbases.TSorted.DESC), 
                                                                        0.01f, 
                                                                        new Color(116, 56, 62, 150));
            }
        }
        
        protected PieValues[] fillData(ArrayList<Dbases.TPairDataRaw> data, float range, Color cMain)
        {
            if(data == null){
                return null;
            }
            
            ArrayList<PieValues> effValues = new ArrayList<>();
            long sum = 0;
            for(Dbases.TPairDataRaw value: data){
                sum += value.sum;
            }

            effValues.add(null);
            long sumDefect = 0;
            for(Dbases.TPairDataRaw value: data){
                float percent = (float)((double)value.sum / (double)sum);
                if(value.sum > 0 && percent < range){
                    sumDefect += value.sum;

                    effValues.add(new PieValues((percent * 100f), Dbases.formatSize(Long.valueOf(value.sum)) + " :: " + value.text));
                }
            }
            effValues.set(0, (new PieValues(((float)((1 - (double)sumDefect / (double)sum)) * 100f), "Main", cMain)));
            return effValues.toArray(new PieValues[effValues.size()]);
        }
    }    
    
    private void setHandlers()
    {
        view.listnerDownloadHub(new actionDownloadHub());
        view.listnerDownloadNick(new actionDownloadNick());
        view.listnerUploadHub(new actionUploadHub());
        view.listnerUploadNick(new actionUploadNick());
        view.listnerDhtDownload(new actionDhtDownload());
        view.listnerDhtUpload(new actionDhtUpload());
        view.listnerRatingAll(new actionRatingAll());
        view.listnerVisualData(new visualDataFocus());
    }    
}

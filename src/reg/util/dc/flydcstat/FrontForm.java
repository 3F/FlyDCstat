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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;

class JpanelGraphics extends JPanel
{
    //TODO: listner callable
    public PieValues[] effectiveValueD = null;
    public PieValues[] effectiveValueU = null;
    
    @Override
    public void paint(Graphics g)
    {
        //TODO:
        paintEffective(g);
    }

    public void setHandler(/* interface callable */)
    {
        //call method...
    }
    
    protected void paintEffective(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setPaint(new Color(184, 198, 183));
        g2d.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));

        int limit = 15;
        if(effectiveValueD != null && effectiveValueD.length > 0){
            PieShape pie = new PieShape(34, 14, 100, 12);
            Charts.pie3DpartEnhance(g2d, pie, effectiveValueD, new PieParts(30, 4, -90), limit);
            Charts.linesSmoothing(g2d, 340, 10, 340, 180, new Color(24, 76, 123), 6);
            Charts.legendPrint(g2d, effectiveValueD, 350, 10, limit);
        }
        
        if(effectiveValueU != null && effectiveValueU.length > 0){
            PieShape pie = new PieShape(34, 230, 100, 12);
            Charts.pie3DpartEnhance(g2d, pie, effectiveValueU, new PieParts(30, 4, -90), limit);
            Charts.linesSmoothing(g2d, 340, 240, 340, 400, new Color(24, 76, 123), 6);
            Charts.legendPrint(g2d, effectiveValueU, 350, 240, limit);
        }        
    }
}

public class FrontForm extends javax.swing.JFrame
{    
    public FrontForm()
    {
        setLookAndFeel();
        initComponents();
        tblInfo.setComponentPopupMenu(jPopupMenu);
        this.setLocationRelativeTo(null); //to center screen
    }
    
    /**
     * If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
     * details: http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
     */
    private void setLookAndFeel()
    {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch(Exception e){}
    }
    
    /**
     * Set handler for component jMenuDownloadHub
     * @param l 
     */
    protected void listnerDownloadHub(ActionListener l)
    {
        jMenuDownloadHub.addActionListener(l);
    }
    
    /**
     * Set handler for component jMenuDownloadNick
     * @param l 
     */    
    protected void listnerDownloadNick(ActionListener l)
    {
        jMenuDownloadNick.addActionListener(l);
    }
    
    /**
     * Set handler for component jMenuUploadHub
     * @param l 
     */    
    protected void listnerUploadHub(ActionListener l)
    {
        jMenuUploadHub.addActionListener(l);
    }
    
    /**
     * Set handler for component jMenuUploadNick
     * @param l 
     */    
    protected void listnerUploadNick(ActionListener l)
    {
        jMenuUploadNick.addActionListener(l);
    }
    
    /**
     * Set handler for component jMenuDhtDownload
     * @param l 
     */    
    protected void listnerDhtDownload(ActionListener l)
    {
        jMenuDhtDownload.addActionListener(l);
    }
    
    /**
     * Set handler for component jMenuDhtUpload
     * @param l 
     */    
    protected void listnerDhtUpload(ActionListener l)
    {
        jMenuDhtUpload.addActionListener(l);
    }
    
    /**
     * Set handler for component jMenuUploadNick
     * @param l 
     */    
    protected void listnerRatingAll(ActionListener l)
    {
        jMenuRatingAll.addActionListener(l);
    }
    
    protected void listnerVisualData(ChangeListener l)
    {
        jTabbedPane.addChangeListener(l);
    }    
    
    /**
     * label of path DB
     * @param path
     * @param toolTip 
     */
    protected void setLabelDbPath(String path, String toolTip)
    {
        jMenuLabelPath.setText("| " + path);
        jMenuLabelPath.setToolTipText(toolTip + " " + path);
    }
    
    protected TableModel tableModelBolleanFirst()
    {
        return new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", "", ""
            }
        ){
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        };
    }
    
    protected TableModel tableModelObjectAll()
    {
        return new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", ""
            }
        ){
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        };
    }
    
    /**
     * rendering of internationalization
     * @param uimsg 
     */
    public void i18n()
    {
        jMenuSelectType.setText(Config.uimsg.getString("menu_type"));
        jMenuSettings.setText(Config.uimsg.getString("menu_settings"));
        jMenuDownloadHub.setText(Config.uimsg.getString("menu_download_hub"));
        jMenuUploadHub.setText(Config.uimsg.getString("menu_upload_hub"));
        jMenuDownloadNick.setText(Config.uimsg.getString("menu_download_nick"));
        jMenuUploadNick.setText(Config.uimsg.getString("menu_upload_nick"));
        jMenuDhtDownload.setText(Config.uimsg.getString("menu_dht_download"));
        jMenuDhtUpload.setText(Config.uimsg.getString("menu_dht_upload"));
        jMenuRatingAll.setText(Config.uimsg.getString("menu_total_rating"));        
        jMenuAnalysis.setText(Config.uimsg.getString("menu_analisis"));
        jMenuItemCompire.setText(Config.uimsg.getString("menu_analisis_eff"));
        jMenuItemDynamics.setText(Config.uimsg.getString("menu_analisis_dynamic"));
        jMenuItemTrafficDia.setText(Config.uimsg.getString("menu_analisis_traffic"));
        jMenuItemExportCsv.setText(Config.uimsg.getString("menu_analisis_csv"));        
        jMenuSettings.setText(Config.uimsg.getString("menu_service"));
        jMenuItemMainSettings.setText(Config.uimsg.getString("menu_service_param"));        
        jMenuInfo.setText(Config.uimsg.getString("menu_info"));
        jMenuItemSelectAll.setText(Config.uimsg.getString("popmenu_selectall"));
        jMenuItemCopy.setText(Config.uimsg.getString("popmenu_copy"));        
        jMenuItemSaveFav.setText(Config.uimsg.getString("popmenu_save_fav")); 
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jDialog1 = new javax.swing.JDialog();
        jPopupMenu = new javax.swing.JPopupMenu();
        jMenuItemSelectAll = new javax.swing.JMenuItem();
        jMenuItemCopy = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSaveFav = new javax.swing.JMenuItem();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelList = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblInfo = new javax.swing.JTable();
        jPanelVisual = new javax.swing.JPanel();
        jPanelDia = new JpanelGraphics();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuSelectType = new javax.swing.JMenu();
        jMenuDownloadHub = new javax.swing.JMenuItem();
        jMenuDownloadNick = new javax.swing.JMenuItem();
        jMenuUploadHub = new javax.swing.JMenuItem();
        jMenuUploadNick = new javax.swing.JMenuItem();
        jMenuDhtDownload = new javax.swing.JMenuItem();
        jMenuDhtUpload = new javax.swing.JMenuItem();
        jMenuRatingAll = new javax.swing.JMenuItem();
        jMenuAnalysis = new javax.swing.JMenu();
        jMenuItemCompire = new javax.swing.JMenuItem();
        jMenuItemDynamics = new javax.swing.JMenuItem();
        jMenuItemTrafficDia = new javax.swing.JMenuItem();
        jMenuItemExportCsv = new javax.swing.JMenuItem();
        jMenuSettings = new javax.swing.JMenu();
        jMenuItemMainSettings = new javax.swing.JMenuItem();
        jMenuInfo = new javax.swing.JMenu();
        jMenuItemAbout = new javax.swing.JMenuItem();
        jMenuLabelPath = new javax.swing.JMenu();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jPopupMenu.addPopupMenuListener(new javax.swing.event.PopupMenuListener()
        {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt)
            {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt)
            {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt)
            {
                jPopupMenuPopupMenuWillBecomeVisible(evt);
            }
        });

        jMenuItemSelectAll.setText("Выделить все");
        jMenuItemSelectAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemSelectAllActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemSelectAll);

        jMenuItemCopy.setText("Копировать");
        jMenuItemCopy.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemCopyActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemCopy);
        jPopupMenu.add(jSeparator1);

        jMenuItemSaveFav.setLabel("Запись Favorite.xml");
        jMenuItemSaveFav.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemSaveFavActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemSaveFav);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        jScrollPane2.setViewportView(tblInfo);

        javax.swing.GroupLayout jPanelListLayout = new javax.swing.GroupLayout(jPanelList);
        jPanelList.setLayout(jPanelListLayout);
        jPanelListLayout.setHorizontalGroup(
            jPanelListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 741, Short.MAX_VALUE)
        );
        jPanelListLayout.setVerticalGroup(
            jPanelListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
        );

        jTabbedPane.addTab("Список", jPanelList);

        javax.swing.GroupLayout jPanelDiaLayout = new javax.swing.GroupLayout(jPanelDia);
        jPanelDia.setLayout(jPanelDiaLayout);
        jPanelDiaLayout.setHorizontalGroup(
            jPanelDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 741, Short.MAX_VALUE)
        );
        jPanelDiaLayout.setVerticalGroup(
            jPanelDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 439, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelVisualLayout = new javax.swing.GroupLayout(jPanelVisual);
        jPanelVisual.setLayout(jPanelVisualLayout);
        jPanelVisualLayout.setHorizontalGroup(
            jPanelVisualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelVisualLayout.setVerticalGroup(
            jPanelVisualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane.addTab("Визулизация", jPanelVisual);

        jMenuSelectType.setText("Выбрать тип");

        jMenuDownloadHub.setText("Скачиваний по хабам");
        jMenuDownloadHub.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuDownloadHubActionPerformed(evt);
            }
        });
        jMenuSelectType.add(jMenuDownloadHub);

        jMenuDownloadNick.setText("Скачиваний по никам");
        jMenuDownloadNick.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuDownloadNickActionPerformed(evt);
            }
        });
        jMenuSelectType.add(jMenuDownloadNick);

        jMenuUploadHub.setText("Отдача по хабам");
        jMenuUploadHub.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuUploadHubActionPerformed(evt);
            }
        });
        jMenuSelectType.add(jMenuUploadHub);

        jMenuUploadNick.setText("Отдача по никам");
        jMenuUploadNick.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuUploadNickActionPerformed(evt);
            }
        });
        jMenuSelectType.add(jMenuUploadNick);

        jMenuDhtDownload.setText("DHT Скачиваний");
        jMenuDhtDownload.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuDhtDownloadActionPerformed(evt);
            }
        });
        jMenuSelectType.add(jMenuDhtDownload);

        jMenuDhtUpload.setText("DHT Отдачи");
        jMenuDhtUpload.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuDhtUploadActionPerformed(evt);
            }
        });
        jMenuSelectType.add(jMenuDhtUpload);

        jMenuRatingAll.setText("Общий рейтинг");
        jMenuRatingAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuRatingAllActionPerformed(evt);
            }
        });
        jMenuSelectType.add(jMenuRatingAll);

        jMenuBar1.add(jMenuSelectType);

        jMenuAnalysis.setText("Анализ данных");

        jMenuItemCompire.setText("Эффективность");
        jMenuItemCompire.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemCompireActionPerformed(evt);
            }
        });
        jMenuAnalysis.add(jMenuItemCompire);

        jMenuItemDynamics.setText("Динамика");
        jMenuAnalysis.add(jMenuItemDynamics);

        jMenuItemTrafficDia.setText("Диаграмма трафика");
        jMenuAnalysis.add(jMenuItemTrafficDia);

        jMenuItemExportCsv.setLabel("Экспорт в CSV");
        jMenuAnalysis.add(jMenuItemExportCsv);

        jMenuBar1.add(jMenuAnalysis);

        jMenuSettings.setText("Сервис");

        jMenuItemMainSettings.setText("Параметры");
        jMenuSettings.add(jMenuItemMainSettings);

        jMenuBar1.add(jMenuSettings);

        jMenuInfo.setText("Информация");

        jMenuItemAbout.setText("?");
        jMenuItemAbout.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemAboutActionPerformed(evt);
            }
        });
        jMenuInfo.add(jMenuItemAbout);

        jMenuBar1.add(jMenuInfo);

        jMenuLabelPath.setText("/");
        jMenuLabelPath.setToolTipText("Текущий путь к папке FlylinkDC++");
        jMenuLabelPath.setDelay(0);
        jMenuLabelPath.setEnabled(false);
        jMenuBar1.add(jMenuLabelPath);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemAboutActionPerformed
    {//GEN-HEADEREND:event_jMenuItemAboutActionPerformed
        String version = "";
        try{
            InputStream stream      = getClass().getResourceAsStream("/META-INF/MANIFEST.MF");
            Manifest manifest       = new Manifest(stream);
            Attributes attributes   = manifest.getMainAttributes();
            String specVersion      = attributes.getValue("Specification-Version");
            if(specVersion != null){
                version = String.format("v%s-%s", specVersion, attributes.getValue("Implementation-Version"));
            }
        }
        catch(Exception e){}
        JOptionPane.showMessageDialog(null, 
                        "entry.reg@gmail.com\nsee detail on:\nhttps://bitbucket.org/3F\n" + 
                        "\nThis product includes SQLiteJDBC (xerial.org)",
                        version,
                        JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItemAboutActionPerformed

    private void jMenuItemSelectAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemSelectAllActionPerformed
    {//GEN-HEADEREND:event_jMenuItemSelectAllActionPerformed
        tblInfo.selectAll();
    }//GEN-LAST:event_jMenuItemSelectAllActionPerformed

    private void jMenuItemCopyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemCopyActionPerformed
    {//GEN-HEADEREND:event_jMenuItemCopyActionPerformed
        ActionEvent evtGen = new ActionEvent(tblInfo, ActionEvent.ACTION_PERFORMED, "copy");
        tblInfo.getActionMap().get(evtGen.getActionCommand()).actionPerformed(evtGen);
    }//GEN-LAST:event_jMenuItemCopyActionPerformed

    private void jMenuItemCompireActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemCompireActionPerformed
    {//GEN-HEADEREND:event_jMenuItemCompireActionPerformed
        jTabbedPane.setSelectedComponent(jPanelVisual);
    }//GEN-LAST:event_jMenuItemCompireActionPerformed

    private void jMenuRatingAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuRatingAllActionPerformed
    {//GEN-HEADEREND:event_jMenuRatingAllActionPerformed
        jTabbedPane.setSelectedComponent(jPanelList);
    }//GEN-LAST:event_jMenuRatingAllActionPerformed

    private void jMenuDownloadHubActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuDownloadHubActionPerformed
    {//GEN-HEADEREND:event_jMenuDownloadHubActionPerformed
        jTabbedPane.setSelectedComponent(jPanelList);
    }//GEN-LAST:event_jMenuDownloadHubActionPerformed

    private void jMenuDownloadNickActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuDownloadNickActionPerformed
    {//GEN-HEADEREND:event_jMenuDownloadNickActionPerformed
        jTabbedPane.setSelectedComponent(jPanelList);
    }//GEN-LAST:event_jMenuDownloadNickActionPerformed

    private void jMenuUploadHubActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuUploadHubActionPerformed
    {//GEN-HEADEREND:event_jMenuUploadHubActionPerformed
        jTabbedPane.setSelectedComponent(jPanelList);
    }//GEN-LAST:event_jMenuUploadHubActionPerformed

    private void jMenuUploadNickActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuUploadNickActionPerformed
    {//GEN-HEADEREND:event_jMenuUploadNickActionPerformed
        jTabbedPane.setSelectedComponent(jPanelList);
    }//GEN-LAST:event_jMenuUploadNickActionPerformed

    private void jMenuDhtDownloadActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuDhtDownloadActionPerformed
    {//GEN-HEADEREND:event_jMenuDhtDownloadActionPerformed
        jTabbedPane.setSelectedComponent(jPanelList);
    }//GEN-LAST:event_jMenuDhtDownloadActionPerformed

    private void jMenuDhtUploadActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuDhtUploadActionPerformed
    {//GEN-HEADEREND:event_jMenuDhtUploadActionPerformed
        jTabbedPane.setSelectedComponent(jPanelList);
    }//GEN-LAST:event_jMenuDhtUploadActionPerformed

    private void jMenuItemSaveFavActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemSaveFavActionPerformed
    {//GEN-HEADEREND:event_jMenuItemSaveFavActionPerformed
        ArrayList<FavoriteXml.TShortValues> data = new ArrayList<>();
        for(int i = 0, n = tblInfo.getRowCount(); i < n; ++i){
            data.add(new FavoriteXml.TShortValues(tblInfo.getValueAt(i, 1).toString(), (boolean)tblInfo.getValueAt(i, 0)));
        }
        if(Export.favoriteXmlActivate(data.toArray(new FavoriteXml.TShortValues[data.size()]))){
            JOptionPane.showMessageDialog(null, 
                                            String.format(Config.uimsg.getString("alert_favsave_success"), Config.getWorkPath() + "/"),
                                            Config.uimsg.getString("alert_favsave_title"),
                                            JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null, 
                                Config.uimsg.getString("alert_favsave_fail"),
                                Config.uimsg.getString("alert_favsave_title"),
                                JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItemSaveFavActionPerformed

    private void jPopupMenuPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt)//GEN-FIRST:event_jPopupMenuPopupMenuWillBecomeVisible
    {//GEN-HEADEREND:event_jPopupMenuPopupMenuWillBecomeVisible
        if(StatController.lastHandledStat != StatController.TStat.DownloadHub && 
           StatController.lastHandledStat != StatController.TStat.UploadHub)
        {
            jMenuItemSaveFav.setEnabled(false);
        }
        else{
            jMenuItemSaveFav.setEnabled(true);
        }
    }//GEN-LAST:event_jPopupMenuPopupMenuWillBecomeVisible
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog jDialog1;
    private javax.swing.JMenu jMenuAnalysis;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuDhtDownload;
    private javax.swing.JMenuItem jMenuDhtUpload;
    private javax.swing.JMenuItem jMenuDownloadHub;
    private javax.swing.JMenuItem jMenuDownloadNick;
    private javax.swing.JMenu jMenuInfo;
    private javax.swing.JMenuItem jMenuItemAbout;
    private javax.swing.JMenuItem jMenuItemCompire;
    private javax.swing.JMenuItem jMenuItemCopy;
    private javax.swing.JMenuItem jMenuItemDynamics;
    private javax.swing.JMenuItem jMenuItemExportCsv;
    private javax.swing.JMenuItem jMenuItemMainSettings;
    private javax.swing.JMenuItem jMenuItemSaveFav;
    private javax.swing.JMenuItem jMenuItemSelectAll;
    private javax.swing.JMenuItem jMenuItemTrafficDia;
    private javax.swing.JMenu jMenuLabelPath;
    private javax.swing.JMenuItem jMenuRatingAll;
    private javax.swing.JMenu jMenuSelectType;
    private javax.swing.JMenu jMenuSettings;
    private javax.swing.JMenuItem jMenuUploadHub;
    private javax.swing.JMenuItem jMenuUploadNick;
    protected javax.swing.JPanel jPanelDia;
    private javax.swing.JPanel jPanelList;
    private javax.swing.JPanel jPanelVisual;
    private javax.swing.JPopupMenu jPopupMenu;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane;
    protected javax.swing.JTable tblInfo;
    // End of variables declaration//GEN-END:variables
}

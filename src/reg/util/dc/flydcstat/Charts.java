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
import java.awt.Graphics2D;
import java.util.Random;

/**
 * Values of Pie-Parts
 * @author reg
 */
class PieValues
{
    public float percent;
    public Color color;
    public String text;
    
    public PieValues(float percent, String text, Color color)
    {
        this.percent = percent;
        this.color   = color;
        this.text    = text;
    }
    
    public PieValues(float percent, String text)
    {
        Random gen = new Random();
        int r = gen.nextInt(255);
        int g = gen.nextInt(255);
        int b = gen.nextInt(255);
        
        this.percent = percent;
        this.color   = new Color(r, g, b, 150);
        this.text    = text;
    }
}

/**
 * Params of Pie-Parts
 * @author reg
 */
class PieParts
{
    public int offsetX = 90;
    public int offsetY = 20;
    public int reach;
    public int space;
    public int initAngle;
    
    public PieParts(int reach, int space, int initAngle)
    {
        this.reach      = reach;
        this.space      = space;
        this.initAngle  = initAngle;
    }
}

/**
 * Wrapper for rendering operation with primitives
 * @author reg
 */
public class Charts extends Rendering
{
    /**
     * Variation display
     */
    public static boolean pie3DpartEnhance(Graphics2D g, PieShape pie, PieValues[] values, PieParts parts, int limit)
    {
        if(values.length < 2){
            return false;
        }
        pie3D(g, pie, 10, angleByPercent(values[0].percent), values[0].color);
        text(g, pie.x + pie.width / 2 - 12, pie.y + pie.height / 2, String.format("%.2f%%", values[0].percent), 12, new Color(249, 249, 238));
        
        pie.x += parts.offsetX;
        pie.y += parts.offsetY;
        
        //half Enhance (repartition for detail) - with degree limit
        float sum = 0;
        for(int idx = 1, n = Math.min(limit + 1, values.length); idx < n; ++idx){
            sum += values[idx].percent;
        }
        int previous = 0;
        for(int idx = 1, n = Math.min(limit + 1, values.length); idx < n; ++idx){
            int angle = angleByPercent((values[idx].percent / sum) * parts.reach);
            pie3D(g, pie, parts.initAngle + previous + parts.space, angle, values[idx].color);
            previous = previous + parts.space + angle;
        }
        
        //draw precent
//        pie.x += 48;
//        pie.y += 50;
//        int a = pie.width / 2;
//        int b = pie.height / 2;        
//        for(int idx = 1, n = Math.min(limit + 1, values.length); idx < n; ++idx){
//            //TODO:
//            double t = 2 * Math.PI * (idx - (1 + Math.floor((values.length - 1) / 4))) / (values.length * 2f);
//            int x = (int)Math.round(a + a * Math.cos(t));
//            int y = (int)Math.round(b + b * Math.sin(t));
//            
//            text(g, pie.x + (x - b), pie.y + (y - b), values[values.length - idx].percent + "%", 11, new Color(249, 249, 238));
//        }
        return true;
    }
    
    public static void pie3D(Graphics2D g, PieShape pie, PieValues[] values, int initAngle, int space)
    {
        int previous = 0;
        for(PieValues value : values){
            int angle = angleByPercent(value.percent);
            pie3D(g, pie, initAngle + previous + space, angle, value.color);
            previous = previous + space + angle;
        }
    }
    
    public static void legendPrint(Graphics2D g, PieValues[] values, int x, int y, int limit)
    {
        int iy = y;
        for(int idx = 1, n = Math.min(limit + 1, values.length); idx < n; ++idx){
            g.setColor(values[idx].color);
            rectangle3D(g, x, iy, 20, 10, true);
            text(g, x + 25, iy + 10, String.format("%.2f%% - %s", values[idx].percent, values[idx].text), 11, new Color(249, 249, 238));
            iy += 12;
        }
    }
    
    protected static int angleByPercent(float percent)
    {
        return (int)Math.round(360 * (percent / 100));
    }
}
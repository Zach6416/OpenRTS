/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.editor.tools;

import java.util.ArrayList;
import java.util.List;

import math.MyRandom;
import model.battlefield.map.Tile;
import model.editor.ToolManager;
import model.editor.Pencil;
import tools.LogUtil;

/**
 *
 * @author Benoît
 */
public class HeightTool extends Tool {
    private static final String RAISE_LOW_OP = "raise/low";
    private static final String NOISE_SMOOTH_OP = "noise/smooth";
    private static final String UNIFOMR_RESET_OP = "uniform/reset";
    
    double amplitude = 0.5;
    double maintainedElevation;
    
    public HeightTool(ToolManager manager) {
        super(manager, RAISE_LOW_OP, NOISE_SMOOTH_OP, UNIFOMR_RESET_OP);
    }
    
    @Override
    protected void createPencil() {
        pencil = new Pencil(manager.battlefield.map);
        pencil.size = 4;
        pencil.sizeIncrement = 1;
        pencil.strength = 0.5;
        pencil.strengthIncrement = 0.01;
    }
    

    @Override
    public void primaryAction() {
        List<Tile> group = pencil.getNodes();
        switch (actualOp){
            case RAISE_LOW_OP : raise(group); break;
            case NOISE_SMOOTH_OP : noise(group); break;
            case UNIFOMR_RESET_OP : uniform(group); break;
        }
        manager.updateParcels(group);
    }

    @Override
    public void secondaryAction() {
        List<Tile> group = pencil.getNodes();
        switch (actualOp){
            case RAISE_LOW_OP : low(group); break;
            case NOISE_SMOOTH_OP : smooth(group); break;
            case UNIFOMR_RESET_OP : reset(group); break;
        }
        manager.updateParcels(group);
    }
    
    private void raise(List<Tile> tiles){
        for(Tile t : tiles)
            t.elevation += amplitude*pencil.strength*pencil.getApplicationRatio(t.getCoord());
    }
    
    private void low(List<Tile> tiles){
        for(Tile t : tiles)
            t.elevation -= amplitude*pencil.strength*pencil.getApplicationRatio(t.getCoord());
    }
    
    private void uniform(List<Tile> tiles){
        if(!pencil.maintained){
            pencil.maintain();
            maintainedElevation = manager.battlefield.map.getGroundAltitude(pencil.getCoord());
        }
        for(Tile t : tiles){
            double diff = maintainedElevation-t.elevation;
            double attenuatedAmplitude = amplitude*pencil.strength*pencil.getApplicationRatio(t.getCoord());
            if(diff > 0)
                t.elevation += Math.min(diff, attenuatedAmplitude);
            else if(diff < 0)
                t.elevation += Math.max(diff, -attenuatedAmplitude);
        }
    }
    private void noise(List<Tile> tiles){
        for(Tile t : tiles){
            t.elevation += amplitude*pencil.strength*MyRandom.between(-1.0, 1.0)*pencil.getApplicationRatio(t.getCoord());
        }
    }

    private void smooth(List<Tile> tiles){
        for(Tile t : tiles){
            double average = 0;
            for(Tile n : t.get4Neighbors())
                average += n.elevation;
            average /= t.get4Neighbors().size();
            
            double diff = average-t.elevation;
            double attenuatedAmplitude = amplitude*pencil.strength*pencil.getApplicationRatio(t.getCoord());
            if(diff > 0)
                t.elevation += Math.min(diff, attenuatedAmplitude);
            else if(diff < 0)
                t.elevation += Math.max(diff, -attenuatedAmplitude);
        }
    }
    
    private void reset(List<Tile> tiles){
        for(Tile t : tiles)
            t.elevation = 0;
    }
}
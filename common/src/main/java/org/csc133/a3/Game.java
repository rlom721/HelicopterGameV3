package org.csc133.a3;

import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.UITimer;
import org.csc133.a3.commands.*;
import org.csc133.a3.views.ControlCluster;
import org.csc133.a3.views.GlassCockpit;
import org.csc133.a3.views.MapView;

// ----------------------------------------------------------------------------
// Initializes game world. Source of graphics context each object uses to draw.
//
public class Game extends Form implements Runnable {
    final private GameWorld gw;
    private MapView mapView;
    private GlassCockpit glassCockpit;
    private ControlCluster controlCluster;

    public final static int DISP_W = Display.getInstance().getDisplayWidth();
    public final static int DISP_H = Display.getInstance().getDisplayHeight();

    public static int getSmallDim() {
        return Math.min(DISP_W, DISP_H);
    }

    public static int getLargeDim() {
        return Math.max(DISP_W, DISP_H);
    }

    public Game() {
        gw = GameWorld.getInstance();
        mapView = new MapView(gw);
        glassCockpit = new GlassCockpit(gw);
        controlCluster = new ControlCluster(gw);

        controlCluster.setSize(new Dimension(Game.DISP_W, Game.DISP_H/5));

        this.setLayout(new BorderLayout());
        this.add(BorderLayout.CENTER, mapView);
        this.add(BorderLayout.NORTH, glassCockpit);
        this.add(BorderLayout.SOUTH, controlCluster);

        // key listeners to control user input
        //
        addKeyListener(-93, new TurnLeftCommand(gw));
        addKeyListener(-94, new TurnRightCommand(gw));
        addKeyListener(-91, new AccelerateCommand(gw));
        addKeyListener(-92, new BrakeCommand(gw));
        addKeyListener('f', new FightCommand(gw));
        addKeyListener('d', new DrinkCommand(gw));
        addKeyListener('Q', new ExitCommand(gw));

        UITimer timer = new UITimer(this);
        timer.schedule(60, true, this);
        this.show();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    public void run() {
        gw.tick();
        glassCockpit.update();
        mapView.updateLocalTransforms();
        mapView.repaint();
        repaint();
    }
}

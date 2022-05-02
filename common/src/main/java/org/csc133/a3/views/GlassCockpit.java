package org.csc133.a3.views;

import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.GridLayout;
import org.csc133.a3.GameWorld;

public class GlassCockpit extends Container {
    GameWorld gw;
    Label heading;
    Label speed;
    Label fuel;
    Label fires;
    Label fireSize;
    Label damage;
    Label loss;

    public GlassCockpit(GameWorld gw){
        this.gw = gw;
        this.setLayout(new GridLayout(2, 7));
        heading = new Label("0");
        speed = new Label("0");
        fuel = new Label("0");
        fires = new Label("0");
        fireSize = new Label("0");
        damage = new Label("0");
        loss = new Label("0");

        this.add("HEADING")
            .add("SPEED")
            .add("FUEL")
            .add("FIRES")
            .add("FIRE SIZE")
            .add("DAMAGE")
            .add("LOSS");
        this.add(heading)
            .add(speed)
            .add(fuel)
            .add(fires)
            .add(fireSize)
            .add(damage)
            .add(loss);
    }

    public void update(){
        heading.setText(gw.getHeading());
        speed.setText(gw.getSpeed());
        fuel.setText(gw.getFuel());
        fires.setText(gw.getNumberOfFires());
        fireSize.setText(gw.getTotalFireSize());
        damage.setText(gw.getTotalDamage());
        loss.setText(gw.getFinancialLoss());
    }
}

package org.csc133.a3.views;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.*;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Font;
import com.codename1.ui.layouts.BorderLayout;
import org.csc133.a3.GameWorld;
import org.csc133.a3.commands.*;

public class ControlCluster extends Container {
    GameWorld gw;

    public ControlCluster(GameWorld gw){
        this.gw = gw;
        this.setLayout(new BorderLayout());

        Button accelerate = buttonMaker(new AccelerateCommand(gw), "Accel");
        Button brake = buttonMaker(new BrakeCommand(gw), "Brake");
        Button drink = buttonMaker(new DrinkCommand(gw), "Drink");
        Button exit = buttonMaker(new ExitCommand(gw), "Exit");
        Button fight = buttonMaker(new FightCommand(gw), "Fight");
        Button left = buttonMaker(new TurnLeftCommand(gw), "Left");
        Button right = buttonMaker(new TurnRightCommand(gw), "Right");

        Container westCommands = new Container(new BorderLayout());
        westCommands.add(BorderLayout.WEST, left);
        westCommands.add(BorderLayout.CENTER, right);
        westCommands.add(BorderLayout.EAST, fight);
        this.add(BorderLayout.WEST, westCommands);

        this.add(BorderLayout.CENTER, exit);

        Container eastCommands = new Container(new BorderLayout());
        eastCommands.add(BorderLayout.WEST, drink);
        eastCommands.add(BorderLayout.CENTER, brake);
        eastCommands.add(BorderLayout.EAST, accelerate);
        this.add(BorderLayout.EAST, eastCommands);
    }

    private Button buttonMaker(Command command, String text){
        Button button = new Button(text);
        button.setCommand(command);
        button.getAllStyles().setFont(Font.createSystemFont(CN.FACE_SYSTEM,
                CN.STYLE_BOLD, CN.SIZE_LARGE));
        button.getAllStyles().setBgColor(ColorUtil.LTGRAY);
        button.getAllStyles().setBgTransparency(255);
        return button;
    }
}

package exnihilocreatio.rotationalPower;

import java.util.ArrayList;
import java.util.List;

public class RotationalPowerLineManager {
    public List<IRotationalPowerMember> lineMembers = new ArrayList<>();

    public RotationalPowerLineManager(){

    }

    public void addToPowerLine(IRotationalPowerMember iRotationalPowerMember){
        lineMembers.add(iRotationalPowerMember);
    }

    public void resetLine(){
        lineMembers.clear();
    }

    public void addSpeedToAll(float rotationalSpeed){
        for (IRotationalPowerMember lineMember : lineMembers) {
            lineMember.addEffectiveRotation(rotationalSpeed);
        }
    }



}

package beatoraja.ir.chickir.model;

import bms.player.beatoraja.ir.IRCourseData;
import bms.player.beatoraja.ir.IRScoreData;

public class ChickIRCoursePlayData {
    private IRCourseData irCourseData;
    private IRScoreData irScoreData;

    public ChickIRCoursePlayData() {

    }

    public ChickIRCoursePlayData(IRCourseData irCourseData, IRScoreData irScoreData) {
        this.irCourseData = irCourseData;
        this.irScoreData = irScoreData;
    }

    public IRCourseData getIrCourseData() {
        return irCourseData;
    }

    public void setIrCourseData(IRCourseData irCourseData) {
        this.irCourseData = irCourseData;
    }

    public IRScoreData getIrScoreData() {
        return irScoreData;
    }

    public void setIrScoreData(IRScoreData irScoreData) {
        this.irScoreData = irScoreData;
    }
}

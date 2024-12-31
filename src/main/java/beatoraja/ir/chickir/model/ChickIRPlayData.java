package beatoraja.ir.chickir.model;

import bms.player.beatoraja.MainController;
import bms.player.beatoraja.ir.IRChartData;
import bms.player.beatoraja.ir.IRScoreData;

public class ChickIRPlayData {
    private IRChartData chartData;
    private IRScoreData scoreData;
    private String client;

    public ChickIRPlayData() {

    }

    public ChickIRPlayData(IRChartData chartData, IRScoreData scoreData) {
        this.chartData = chartData;
        this.scoreData = scoreData;
        this.client = MainController.getVersion();
    }

    public IRChartData getChartData() {
        return chartData;
    }

    public void setChartData(IRChartData chartData) {
        this.chartData = chartData;
    }

    public IRScoreData getScoreData() {
        return scoreData;
    }

    public void setScoreData(IRScoreData scoreData) {
        this.scoreData = scoreData;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}

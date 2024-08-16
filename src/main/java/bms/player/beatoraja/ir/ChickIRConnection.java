package bms.player.beatoraja.ir;

import beatoraja.ir.chickir.api.response.ChickIRResponse;
import beatoraja.ir.chickir.model.ChickIRCoursePlayData;
import beatoraja.ir.chickir.model.ChickIRPlayData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;


public class ChickIRConnection implements IRConnection {

    public static final String NAME = "ChickIR";
    public static final String HOME = "http://localhost:3000";
    public static final String VERSION = "0.0.1";

    private final Gson gson;
    private final OkHttpClient client;

    public ChickIRConnection() {
        this.gson = new Gson();
        this.client = new OkHttpClient();
    }

    public ChickIRConnection(OkHttpClient mockHttpClient, Gson gson) {
        this.gson = gson;
        this.client = mockHttpClient;
    }


    @Override
    public IRResponse<IRPlayerData> register(IRAccount irAccount) {
        return null;
    }

    @Override
    public IRResponse<IRPlayerData> login(IRAccount irAccount) {
        String json = gson.toJson(irAccount);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(HOME + "/ir/api/login")
                .post(body)
                .build();
        return sendRequest(request,IRPlayerData.class);
    }

    @Override
    public IRResponse<IRPlayerData[]> getRivals() {
        // GET 请求
        Request request = new Request.Builder()
                .url(HOME + "/ir/api/getRivals")
                .get()
                .build();
        return sendRequest(request,IRPlayerData[].class);
    }

    @Override
    public IRResponse<IRTableData[]> getTableDatas() {
        // GET 请求
        Request request = new Request.Builder()
                .url(HOME + "/ir/api/getTableDatas")
                .get()
                .build();
        return sendRequest(request,IRTableData[].class);
    }

    @Override
    public IRResponse<IRScoreData[]> getPlayData(IRPlayerData irPlayerData, IRChartData irChartData) {
        // 创建请求体，包含玩家数据和图表数据
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("playerData", gson.toJsonTree(irPlayerData));
        jsonObject.add("chartData", gson.toJsonTree(irChartData));

        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(HOME + "/ir/api/getPlayData")
                .post(body)
                .build();
        return sendRequest(request,IRScoreData[].class);
    }

    @Override
    public IRResponse<IRScoreData[]> getCoursePlayData(IRPlayerData irPlayerData, IRCourseData irCourseData) {
        // 创建请求体，包含玩家数据和图表数据
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("playerData", gson.toJsonTree(irPlayerData));
        jsonObject.add("courseData", gson.toJsonTree(irCourseData));

        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(HOME + "/ir/api/getCoursePlayData")
                .post(body)
                .build();
        return sendRequest(request, IRScoreData[].class);
    }

    @Override
    public IRResponse<Object> sendPlayData(IRChartData irChartData, IRScoreData irScoreData) {
        ChickIRPlayData chickIRPlayData = new ChickIRPlayData(irChartData,irScoreData);
        String json = gson.toJson(chickIRPlayData);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(HOME + "/ir/api/sendPlayData")
                .post(body)
                .build();
        // 获取响应
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // 解析响应体为 IRPlayerData 对象
                ChickIRPlayData playData = gson.fromJson(response.body().string(), ChickIRPlayData.class);
                // 返回成功的响应
                return new ChickIRResponse<>(true, "发送成绩成功", playData);
            } else {
                // 返回失败的响应
                return new ChickIRResponse<>(false, "请求失败: " + response.code(), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 返回错误的响应
            return new ChickIRResponse<>(false, "内部错误", null);
        }
    }

    @Override
    public IRResponse<Object> sendCoursePlayData(IRCourseData irCourseData, IRScoreData irScoreData) {
        ChickIRCoursePlayData chickIRCoursePlayData = new ChickIRCoursePlayData(irCourseData,irScoreData);
        String json = gson.toJson(chickIRCoursePlayData);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(HOME + "/ir/api/sendCourseData")
                .post(body)
                .build();
        // 获取响应
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // 解析响应体为 IRPlayerData 对象
                ChickIRCoursePlayData playData = gson.fromJson(response.body().string(), ChickIRCoursePlayData.class);
                // 返回成功的响应
                return new ChickIRResponse<>(true, "发送段位成绩成功", playData);
            } else {
                // 返回失败的响应
                return new ChickIRResponse<>(false, "请求失败: " + response.code(), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 返回错误的响应
            return new ChickIRResponse<>(false, "内部错误", null);
        }
    }

    @Override
    public String getSongURL(IRChartData irChartData) {
        return "";
    }

    @Override
    public String getCourseURL(IRCourseData irCourseData) {
        return "";
    }

    @Override    public String getPlayerURL(IRPlayerData irPlayerData) {
        return "";
    }

    private <T> ChickIRResponse<T> sendRequest(Request request, Class<T> responseType) {
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                T responseData = gson.fromJson(response.body().string(), responseType);
                return new ChickIRResponse<>(true, "请求成功", responseData);
            } else {
                return new ChickIRResponse<>(false, "请求失败: " + response.code(), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ChickIRResponse<>(false, "内部错误: " + e.getMessage(), null);
        }
    }
}
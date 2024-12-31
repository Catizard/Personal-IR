package bms.player.beatoraja.ir;

import beatoraja.ir.chickir.api.response.ChickIRResponse;
import beatoraja.ir.chickir.model.ChickIRCoursePlayData;
import beatoraja.ir.chickir.model.ChickIRPlayData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;

import java.io.IOException;


public class ChickIRConnection implements IRConnection {

    public static final String NAME = "ChickIR";
    public static final String HOME = "http://120.27.244.52:17739";
    public static final String VERSION = "0.0.1";

    private final ObjectMapper om;
    private final OkHttpClient client;

    public ChickIRConnection() {
        this.om = new ObjectMapper();
        om.addMixIn(IRPlayerData.class, MixIn.class);
        this.client = new OkHttpClient();
    }

    public ChickIRConnection(OkHttpClient mockHttpClient, ObjectMapper om) {
        this.om = om;
        this.om.addMixIn(IRPlayerData.class, MixIn.class);
        this.client = mockHttpClient;
    }


    @Override
    public IRResponse<IRPlayerData> register(IRAccount irAccount) {
        return null;
    }

    @Override
    public IRResponse<IRPlayerData> login(IRAccount irAccount) {
        String json = null;
        try {
            json = om.writeValueAsString(irAccount);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(HOME + "/api/ir/login")
                .post(body)
                .build();
        return sendRequest(request, IRPlayerData.class);
    }

    @Override
    public IRResponse<IRPlayerData[]> getRivals() {
        // GET 请求
        Request request = new Request.Builder()
                .url(HOME + "/api/ir/getRivals")
                .get()
                .build();
        return sendRequest(request, IRPlayerData[].class);
    }

    @Override
    public IRResponse<IRTableData[]> getTableDatas() {
        // GET 请求
        Request request = new Request.Builder()
                .url(HOME + "/api/ir/getTableDatas")
                .get()
                .build();
        return sendRequest(request, IRTableData[].class);
    }

    @Override
    public IRResponse<IRScoreData[]> getPlayData(IRPlayerData irPlayerData, IRChartData irChartData) {
        // 创建请求体，包含玩家数据和图表数据
        ObjectNode objectNode = om.createObjectNode();
        try {
            objectNode.put("playerData", om.writeValueAsString(irPlayerData));
            objectNode.put("chartData", om.writeValueAsString(irChartData));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), objectNode.toString());
        Request request = new Request.Builder()
                .url(HOME + "/api/ir/getPlayData")
                .post(body)
                .build();
        return sendRequest(request, IRScoreData[].class);
    }

    @Override
    public IRResponse<IRScoreData[]> getCoursePlayData(IRPlayerData irPlayerData, IRCourseData irCourseData) {
        // 创建请求体，包含玩家数据和图表数据
        ObjectNode objectNode = om.createObjectNode();
        try {
            objectNode.put("playerData", om.writeValueAsString(irPlayerData));
            objectNode.put("courseData", om.writeValueAsString(irCourseData));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), objectNode.toString());
        Request request = new Request.Builder()
                .url(HOME + "/api/ir/getCoursePlayData")
                .post(body)
                .build();
        return sendRequest(request, IRScoreData[].class);
    }

    @Override
    public IRResponse<Object> sendPlayData(IRChartData irChartData, IRScoreData irScoreData) {
        ChickIRPlayData chickIRPlayData = new ChickIRPlayData(irChartData, irScoreData);
        String json = null;
        try {
            json = om.writeValueAsString(chickIRPlayData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(HOME + "/api/ir/sendPlayData")
                .post(body)
                .build();
        // 获取响应
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // 解析响应体为 IRPlayerData 对象
                ChickIRPlayData playData = om.readValue(response.body().string(), ChickIRPlayData.class);
                // 返回成功的响应
                return new ChickIRResponse<>(true, "发送成绩成功", playData);
            } else {
                // 返回失败的响应
                return new ChickIRResponse<>(false, "请求失败: " + response.code(), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
        // 返回错误的响应
        return new ChickIRResponse<>(false, "内部错误", null);
    }

    @Override
    public IRResponse<Object> sendCoursePlayData(IRCourseData irCourseData, IRScoreData irScoreData) {
        ChickIRCoursePlayData chickIRCoursePlayData = new ChickIRCoursePlayData(irCourseData, irScoreData);
        String json = null;
        try {
            json = om.writeValueAsString(chickIRCoursePlayData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(HOME + "/api/ir/sendCourseData")
                .post(body)
                .build();
        // 获取响应
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // 解析响应体为 IRPlayerData 对象
                ChickIRCoursePlayData playData = om.readValue(response.body().string(), ChickIRCoursePlayData.class);
                // 返回成功的响应
                return new ChickIRResponse<>(true, "发送段位成绩成功", playData);
            } else {
                // 返回失败的响应
                return new ChickIRResponse<>(false, "请求失败: " + response.code(), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }
        // 返回错误的响应
        return new ChickIRResponse<>(false, "内部错误", null);
    }

    @Override
    public String getSongURL(IRChartData irChartData) {
        return "";
    }

    @Override
    public String getCourseURL(IRCourseData irCourseData) {
        return "";
    }

    @Override
    public String getPlayerURL(IRPlayerData irPlayerData) {
        return "";
    }

    private <T> ChickIRResponse<T> sendRequest(Request request, Class<T> responseType) {
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                T responseData = om.readValue(response.body().string(), responseType);
                return new ChickIRResponse<>(true, "请求成功", responseData);
            } else {
                return new ChickIRResponse<>(false, "请求失败: " + response.code() + " address: " + request.url(), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }
        return new ChickIRResponse<>(false, "内部错误", null);
    }
}
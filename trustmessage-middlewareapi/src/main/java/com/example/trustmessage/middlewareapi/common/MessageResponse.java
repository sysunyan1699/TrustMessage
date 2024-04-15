package com.example.trustmessage.middlewareapi.common;

public class MessageResponse {

    int code;
    String message;


    Data data;

    public MessageResponse() {
    }

    public MessageResponse(int code, String message, Data data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        boolean result;

        public Data(boolean result) {
            this.result = result;
        }

        public Data() {
        }
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

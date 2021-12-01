package tc.web.evaluaciones.api;

public class ResponseAPI<R> {
    private int code;
    private R response;
    private String message;
    private boolean error;

    public ResponseAPI(){
        this.code = 200;
        this.message = "";
        this.error = false;
    }

    public ResponseAPI(R response){
        this.code = 200;
        this.message = "";
        this.error = false;
        this.response = response;
    }

    public ResponseAPI(R response, String message, int code){
        this.code = code;
        this.message = message;
        this.error = false;
        this.response = response;
    }

    public void setCode(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public void setResponse(R response){
        this.response = response;
    }

    public R getResponse(){
        return response;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setError(boolean error){
        this.error = error;
    }

    public boolean getError(){
        return error;
    }
}

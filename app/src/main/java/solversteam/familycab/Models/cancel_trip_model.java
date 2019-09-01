package solversteam.familycab.Models;

public class cancel_trip_model {
    private String id,content;
    public cancel_trip_model(String id,String content){
        this.id=id;
        this.content=content;

    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }
}

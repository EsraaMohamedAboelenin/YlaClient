package solversteam.familycab.Models;

/**
 * Created by ahmed ezz on 10/17/2017.
 */

public class Issues {
    private String date ,header,messege,id;
    private Boolean isRead;

    public Issues(String date, String header, String messege, String id, Boolean isRead) {
        this.date = date;
        this.header = header;
        this.messege = messege;
        this.id = id;
        this.isRead=isRead;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setMessege(String messege) {
        this.messege = messege;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getDate() {
        return date;
    }

    public String getHeader() {
        return header;
    }

    public String getMessege() {
        return messege;
    }

    public String getId() {
        return id;
    }


}

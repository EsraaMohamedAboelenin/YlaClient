package solversteam.familycab.Models;

/**
 * Created by mosta on 5/11/2017.
 */

public class History_model {
    private String date,cost,here_add,here_add_details,where_add,where_add_details,order_id;
    private Boolean canceled;
    public History_model(String date, String here_add, String cost,
                         String here_add_details, String where_add_details, String where_add,String order_id,Boolean canceled) {
        this.date = date;
        this.here_add = here_add;
        this.cost = cost;
        this.here_add_details = here_add_details;
        this.where_add_details = where_add_details;
        this.where_add = where_add;
        this.canceled=canceled;
        this.order_id=order_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public Boolean getCanceled() {
        return canceled;
    }

    public void setcanceled(Boolean canceled) {
        this.canceled = canceled;
    }

    public Boolean getcanceled() {
        return canceled;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getHere_add() {
        return here_add;
    }

    public void setHere_add(String here_add) {
        this.here_add = here_add;
    }

    public String getHere_add_details() {
        return here_add_details;
    }

    public void setHere_add_details(String here_add_details) {
        this.here_add_details = here_add_details;
    }

    public String getWhere_add() {
        return where_add;
    }

    public void setWhere_add(String where_add) {
        this.where_add = where_add;
    }

    public String getWhere_add_details() {
        return where_add_details;
    }

    public void setWhere_add_details(String where_add_details) {
        this.where_add_details = where_add_details;
    }
}

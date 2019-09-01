package solversteam.familycab.Models;

/**
 * Created by mosta on 5/1/2017.
 */

public class Cars_items {

    private String intial_factor,car_Cost_per_minute,car_cost_per_mile,moving_min_speed,moving_max_speed,rushhour_rate,mincharge,
    waiting_factor_per_minute,waiting_min_speed,waiting_max_speed,peek_amount_time,cancelation_charge,airport_factor,air_port_amount,
    min_cancel_time,max_cancel_time;
    private String img,booking_type_number,name ,id,booking_type_id;

    public Cars_items(String name, String id, String img) {
        this.name = name;
        this.id = id;
        this.img = img;
    }


    public Cars_items(String intial_factor, String car_Cost_per_minute, String car_cost_per_mile,
                      String moving_min_speed, String moving_max_speed, String rushhour_rate,
                      String waiting_factor_per_minute, String mincharge, String waiting_min_speed,
                      String waiting_max_speed, String peek_amount_time, String airport_factor,
                      String cancelation_charge, String air_port_amount, String min_cancel_time,
                      String max_cancel_time, String booking_type_number, String booking_type_id, String id, String name, String imag) {

        this.intial_factor = intial_factor;
        this.car_Cost_per_minute = car_Cost_per_minute;
        this.car_cost_per_mile = car_cost_per_mile;
        this.moving_min_speed = moving_min_speed;
        this.moving_max_speed = moving_max_speed;
        this.rushhour_rate = rushhour_rate;
        this.waiting_factor_per_minute = waiting_factor_per_minute;
        this.mincharge = mincharge;
        this.waiting_min_speed = waiting_min_speed;
        this.waiting_max_speed = waiting_max_speed;
        this.peek_amount_time = peek_amount_time;
        this.airport_factor = airport_factor;
        this.cancelation_charge = cancelation_charge;
        this.air_port_amount = air_port_amount;
        this.min_cancel_time = min_cancel_time;
        this.max_cancel_time = max_cancel_time;
        this.booking_type_number = booking_type_number;
        this.id = id;
        this.img=imag;
        this.name=name;
        this.booking_type_id=booking_type_id;
    }

    public String getBooking_type_id() {
        return booking_type_id;
    }

    public void setBooking_type_id(String booking_type_id) {
        this.booking_type_id = booking_type_id;
    }

    public String getBooking_type_number() {
        return booking_type_number;
    }

    public void setBooking_type_number(String booking_type_number) {
        this.booking_type_number = booking_type_number;
    }

    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getmoving_max_speed() {
        return moving_max_speed;
    }

    public void setmoving_max_speed(String moving_max_speed) {
        this.moving_max_speed = moving_max_speed;
    }

    public String getmoving_min_speed() {
        return moving_min_speed;
    }

    public void setmoving_min_speed(String moving_min_speed) {
        this.moving_min_speed = moving_min_speed;
    }

    public String getCar_cost_per_mile() {
        return car_cost_per_mile;
    }

    public void setCar_cost_per_mile(String car_cost_per_mile) {
        this.car_cost_per_mile = car_cost_per_mile;
    }

    public String getCar_Cost_per_minute() {
        return car_Cost_per_minute;
    }

    public void setCar_Cost_per_minute(String car_Cost_per_minute) {
        this.car_Cost_per_minute = car_Cost_per_minute;
    }

    public String getintial_factor() {
        return intial_factor;
    }

    public void setintial_factor(String intial_factor) {
        this.intial_factor = intial_factor;
    }

    public String getIntial_factor() {
        return intial_factor;
    }

    public void setIntial_factor(String intial_factor) {
        this.intial_factor = intial_factor;
    }

    public String getMoving_min_speed() {
        return moving_min_speed;
    }

    public void setMoving_min_speed(String moving_min_speed) {
        this.moving_min_speed = moving_min_speed;
    }

    public String getMoving_max_speed() {
        return moving_max_speed;
    }

    public void setMoving_max_speed(String moving_max_speed) {
        this.moving_max_speed = moving_max_speed;
    }

    public String getRushhour_rate() {
        return rushhour_rate;
    }

    public void setRushhour_rate(String rushhour_rate) {
        this.rushhour_rate = rushhour_rate;
    }

    public String getMincharge() {
        return mincharge;
    }

    public void setMincharge(String mincharge) {
        this.mincharge = mincharge;
    }

    public String getWaiting_min_speed() {
        return waiting_min_speed;
    }

    public void setWaiting_min_speed(String waiting_min_speed) {
        this.waiting_min_speed = waiting_min_speed;
    }

    public String getWaiting_factor_per_minute() {
        return waiting_factor_per_minute;
    }

    public void setWaiting_factor_per_minute(String waiting_factor_per_minute) {
        this.waiting_factor_per_minute = waiting_factor_per_minute;
    }

    public String getWaiting_max_speed() {
        return waiting_max_speed;
    }

    public void setWaiting_max_speed(String waiting_max_speed) {
        this.waiting_max_speed = waiting_max_speed;
    }

    public String getCancelation_charge() {
        return cancelation_charge;
    }

    public void setCancelation_charge(String cancelation_charge) {
        this.cancelation_charge = cancelation_charge;
    }

    public String getPeek_amount_time() {
        return peek_amount_time;
    }

    public void setPeek_amount_time(String peek_amount_time) {
        this.peek_amount_time = peek_amount_time;
    }

    public String getAirport_factor() {
        return airport_factor;
    }

    public void setAirport_factor(String airport_factor) {
        this.airport_factor = airport_factor;
    }

    public String getAir_port_amount() {
        return air_port_amount;
    }

    public void setAir_port_amount(String air_port_amount) {
        this.air_port_amount = air_port_amount;
    }

    public String getMin_cancel_time() {
        return min_cancel_time;
    }

    public void setMin_cancel_time(String min_cancel_time) {
        this.min_cancel_time = min_cancel_time;
    }

    public String getMax_cancel_time() {
        return max_cancel_time;
    }

    public void setMax_cancel_time(String max_cancel_time) {
        this.max_cancel_time = max_cancel_time;
    }
}

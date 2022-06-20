package com.krystal.staybooking.model;
//存被reserve的日期 用户搜索的时间和是否被reserve过
//搜索时作为filter用的
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "stay_reserved_date")
public class StayReservedDate implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private StayReservedDateKey id;

    @ManyToOne
    @MapsId("stay_id")
    // column stay_id also is a foreign key of table stay
    private Stay stay;

    public StayReservedDate() {}

    public StayReservedDate(StayReservedDateKey id, Stay stay) {
        this.id = id;
        this.stay = stay;
    }

    public StayReservedDateKey getId() {
        return id;
    }

    public Stay getStay() {
        return stay;
    }
}

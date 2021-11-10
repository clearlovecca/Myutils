package com.bawei.shutype.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Food {
    @Id(autoincrement = true)
    Long id;
    String name;
    String img;
    int price;
    boolean checked;
    int num;
    @Generated(hash = 1481759931)
    public Food(Long id, String name, String img, int price, boolean checked,
            int num) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.price = price;
        this.checked = checked;
        this.num = num;
    }
    @Generated(hash = 866324199)
    public Food() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImg() {
        return this.img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public int getPrice() {
        return this.price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public boolean getChecked() {
        return this.checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public int getNum() {
        return this.num;
    }
    public void setNum(int num) {
        this.num = num;
    }

}

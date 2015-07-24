package com.yuzhou.l2.prework;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by yuzhou on 2015/07/23.
 */
@Table(name = "USER")
public class User extends Model implements Serializable
{
    @Column(name = "NAME")
    @Getter
    @Setter
    private String name;

    public User()
    {
    }

    public User(String name)
    {
        this.name = name;
    }
}

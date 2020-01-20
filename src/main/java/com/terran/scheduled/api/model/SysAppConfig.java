package com.terran.scheduled.api.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "sys_app_config")
@Data
public class SysAppConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fd_key")
    private String fdKey;

    @Column(name = "fd_column1")
    private String fdColumn1;

    @Column(name = "fd_column2")
    private String fdColumn2;

    @Column(name = "fd_column3")
    private String fdColumn3;
}

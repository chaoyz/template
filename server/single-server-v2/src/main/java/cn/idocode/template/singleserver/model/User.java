package cn.idocode.template.singleserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user", indexes = {@Index(name = "idx_name", columnList = "name", unique = false)})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String name;

    @Column
    private Integer age;

    @Column
    private String tel;


    private String address;
}

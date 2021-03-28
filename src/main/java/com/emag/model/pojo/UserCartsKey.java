package com.emag.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UserCartsKey implements Serializable{
    @Column(name = "user_id")
    int userId;
    @Column(name = "product_id")
    int productId;
}

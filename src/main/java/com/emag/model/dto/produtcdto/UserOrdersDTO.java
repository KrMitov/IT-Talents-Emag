package com.emag.model.dto.produtcdto;

import com.emag.model.dto.orderdto.OrderDTO;
import com.emag.model.pojo.Order;
import com.emag.model.pojo.OrderedProduct;
import com.emag.model.pojo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserOrdersDTO {
   private List<OrderDTO> orders;

   public UserOrdersDTO(User user){
       this.orders = new ArrayList<>();
       for (Order order : user.getOrders()) {
           OrderDTO dto = new OrderDTO(order);
           orders.add(dto);
       }
   }
}

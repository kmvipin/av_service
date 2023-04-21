package com.avvsion.service.service;

import com.avvsion.service.model.Orders;
import com.avvsion.service.model.Payments;
import com.avvsion.service.repository.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    public int placeOrder(Payments payment){
        return orderDao.placeOrder(payment);
    }

    public List<Orders> getOrdersByServiceId(int service_id){
        return orderDao.getOrdersByServiceId(service_id);
    }
}

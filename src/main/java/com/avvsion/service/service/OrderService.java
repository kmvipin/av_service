package com.avvsion.service.service;

import com.avvsion.service.model.Orders;
import com.avvsion.service.model.Payments;
import com.avvsion.service.model.SellerPay;
import com.avvsion.service.repository.OrderDao;
import com.avvsion.service.repository.ServiceDao;
import com.razorpay.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private ServiceDao serviceDao;

    public int placeOrder(Payments payment){
        orderDao.placeOrder(payment);
        int seller_id = serviceDao.getSellerIdByServiceId(payment.getOrder().getService_id());
        SellerPay sellerPay = new SellerPay();
        sellerPay.setOrder(payment.getOrder());
        sellerPay.setSeller_id(seller_id);
        return sellerService.saveSellerPay(sellerPay);
    }

    public List<Orders> getOrdersByServiceId(int service_id){
        return orderDao.getOrdersByServiceId(service_id);
    }
    public int updateStatus(int order_id){
        return orderDao.updateStatus(order_id);
    }

    public Orders getOrderById(int id){
        return orderDao.getOrderById(id);
    }
}

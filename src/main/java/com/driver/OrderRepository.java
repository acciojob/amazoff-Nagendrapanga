package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Repository
public class OrderRepository {
    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> deliveryPartnerMap;

    private HashMap<String, List<String>> orderToDeliveryPartnerMap;
    private HashMap<String, List<String>> partnerIdTimeMap;

    public OrderRepository() {
    }

    public OrderRepository(HashMap<String, Order> orderMap, HashMap<String, DeliveryPartner> deliveryPartnerMap, HashMap<String, List<String>> orderToDeliveryPartner, HashMap<String, List<String>> partnerIdTimeMap) {
        this.orderMap = orderMap;
        this.deliveryPartnerMap = deliveryPartnerMap;
        this.orderToDeliveryPartnerMap = orderToDeliveryPartner;
        this.partnerIdTimeMap = partnerIdTimeMap;
    }
    public void addOrder(Order order)
    {
        orderMap.put(order.getId(),order);
    }

    public void addPartner(String partnerId)
    {
        DeliveryPartner deliverypartner = new DeliveryPartner();
        deliverypartner.setId(partnerId);
        deliverypartner.setNumberOfOrders(0);
        deliveryPartnerMap.put(partnerId,deliverypartner);
    }

    public void addOrderPartnerId(String orderId, String partnerId)
    {
        List<String> orderIdList = new ArrayList<>();
        List<String> timeList = new ArrayList<>();

        if(orderMap.containsKey(orderId) && deliveryPartnerMap.containsKey(partnerId)) {
            if (orderToDeliveryPartnerMap.containsKey(partnerId)) {
                orderIdList = orderToDeliveryPartnerMap.get(partnerId);
            }

            orderIdList.add(orderId);

            orderToDeliveryPartnerMap.put(partnerId, orderIdList);

            Order order= orderMap.get(orderId);
            String time= String.valueOf(order.getDeliveryTime());

            if(partnerIdTimeMap.containsKey(partnerId)){
                timeList=partnerIdTimeMap.get(partnerId);
            }

            timeList.add(time);

            partnerIdTimeMap.put(time, timeList);
        }
    }

    public Order getOrderById(String orderId)
    {
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId)
    {
        return deliveryPartnerMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId)
    {
        List<String> orderList = new ArrayList<>();
        int count=0;
        if(orderToDeliveryPartnerMap.containsKey(partnerId)){
            orderList = orderToDeliveryPartnerMap.get(partnerId);
            count =  orderList.size();
        }
        return count;

    }

    public List<String> getOrdersByPartnerId(String partnerId)
    {
        List<String> orderList = new ArrayList<>();

        if(orderToDeliveryPartnerMap.containsKey(partnerId)){
            orderList = orderToDeliveryPartnerMap.get(partnerId);
        }
        return orderList;
    }

    public List<String> getAllOrders()
    {
        return new ArrayList<>(orderMap.keySet());
    }

    public Integer getCountOfUnassignedOrders()
    {
        int totalOrders = orderMap.size();

        for(String orders: orderMap.keySet()){
            for(String partner: orderToDeliveryPartnerMap.keySet()){
                List<String> orderList = new ArrayList<>();
                orderList = orderToDeliveryPartnerMap.get(partner);
                for(String order: orderList){
                    if(orders.equals(order)){
                        totalOrders--;
                        break;
                    }
                }
            }
        }
        return totalOrders;
    }

    public Integer getOrdersleftAfterGivenTimeByPartnerId(String time, String partnerId)
    {
        Order order = new Order();

        int convertedTime = order.convertTimeToInteger(time);
        int count = 0;
        if(partnerIdTimeMap.containsKey(partnerId)){
            List<String> timeList = new ArrayList<>();
            timeList = partnerIdTimeMap.get(partnerId);
            for(String times: timeList){
                int newTime  = order.convertTimeToInteger(times);
                if(newTime>convertedTime){
                    count++;
                }
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId)
    {
        int maximumTime = 0;
        String time ="";
        Order order = new Order();
        List<String> list = new ArrayList<>();
        if(partnerIdTimeMap.containsKey(partnerId)){
            list=  partnerIdTimeMap.get(partnerId);
            for(String times: list){
                int newTime = order.convertTimeToInteger(times);
                if(newTime>maximumTime){
                    maximumTime = newTime;
                }
            }
        }
        time=Integer.toString(maximumTime);
        return time;
    }

    public void deletePartnerId(String partnerId)
    {
        if(deliveryPartnerMap.containsKey(partnerId)){
            deliveryPartnerMap.remove(partnerId);
        }
        if(orderToDeliveryPartnerMap.containsKey(partnerId)){
            orderToDeliveryPartnerMap.remove(partnerId);
        }
        if(partnerIdTimeMap.containsKey(partnerId)){
            partnerIdTimeMap.remove(partnerId);
        }
    }

    public void deleteOrderById(String orderId)
    {
        if(orderMap.containsKey(orderId)){
            orderMap.remove(orderId);
        }
        List<String> orderList = new ArrayList<>();
        for(String partnerId: orderToDeliveryPartnerMap.keySet()){
            orderList = orderToDeliveryPartnerMap.get(partnerId);
            for(String orders: orderList){
                if(orders.equals(orderId)){
                    orderList.remove(orders);
                }
            }
            orderToDeliveryPartnerMap.put(partnerId,orderList);
        }
        Order order = new Order();
        String time= String.valueOf(orderMap.get(orderId));

        List<String> timeList = new ArrayList<>();
        for(String partner: partnerIdTimeMap.keySet()){
            timeList=partnerIdTimeMap.get(partner);
            for(String times: timeList){
                if(times.equals(time)){
                    timeList.remove(times);
                }
            }
            partnerIdTimeMap.put(partner,timeList);
        }
    }
}

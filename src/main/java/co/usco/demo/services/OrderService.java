package co.usco.demo.services;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import java.util.List;
import co.usco.demo.models.Constants.OrderType;
import co.usco.demo.models.Constants;
import co.usco.demo.models.OrderModel;
import co.usco.demo.models.UserModel;
import co.usco.demo.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    public void addUserOrders(Model model) {
        UserModel user = userService.getSessionUser();
        model.addAttribute("pendingOrders", getPendingOrders(user));
        model.addAttribute("authorizedOrders", getAuthorizedOrders(user));
        model.addAttribute("completedOrders", getCompletedOrders(user));
        model.addAttribute("requestingOrders", getRequestingOrders(user));
    }

    public String getRedirectionPathBasedOnOrderType(String orderType) {
        if ("MEDICATION".equals(orderType)) {
            return "redirect:/patient/medicines";
        } else if ("SPECIALIST_APPOINTMENT".equals(orderType) || "LAB_APPOINTMENT".equals(orderType)) {
            return "/patient/schedule-appointment/select-type";
        } else {
            return "redirect:/patient/appointments"; 
        }
    }

    public void markOrderAsCompleted(OrderModel order) {
        order.setStatus(Constants.OrderStatus.COMPLETED);
        orderRepository.save(order);
    }

    public void markOrderAsAuthorized(OrderModel order) {
        order.setStatus(Constants.OrderStatus.AUTHORIZED);
        orderRepository.save(order);
    }

    public void markOrderAsPending(OrderModel order) {
        order.setStatus(Constants.OrderStatus.PENDING);
        orderRepository.save(order);
    }

    public void markOrderAsInDelivery(Long orderId) {
        OrderModel order = getOrderById(orderId);
        order.setStatus(Constants.OrderStatus.IN_DELIVERY);
        orderRepository.save(order);
    }

    public void addMedicationOrders(Model model) {
        UserModel user = userService.getSessionUser();
        Constants.OrderType orderType = Constants.OrderType.MEDICATION;
        model.addAttribute("authorizedOrders", getOrdersByTypeAndPatientAndAuthorized(orderType, user));
        model.addAttribute("inDeliveryOrders", getOrdersByTypeAndPatientAndInDelivery(orderType, user));
        model.addAttribute("completedOrders", getOrdersByTypeAndPatientAndCompleted(orderType, user));
    }
    

    // Auxiliary services

    public List<OrderModel> getRequestingOrders(UserModel user) {
        return orderRepository.findByPatientAndStatus(user, Constants.OrderStatus.REQUESTING, 
                Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    
    public List<OrderModel> getPendingOrders(UserModel user) {
        return orderRepository.findByPatientAndStatus(user, Constants.OrderStatus.PENDING, 
                Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    
    public List<OrderModel> getAuthorizedOrders(UserModel user) {
        return orderRepository.findByPatientAndStatus(user, Constants.OrderStatus.AUTHORIZED, 
                Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    
    public List<OrderModel> getCompletedOrders(UserModel user) {
        return orderRepository.findByPatientAndStatus(user, Constants.OrderStatus.COMPLETED, 
                Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    
    public List<OrderModel> getOrdersByTypeAndPatientAndAuthorized(OrderType orderType, UserModel user) {
        return orderRepository.findByPatientAndOrderTypeAndStatus(user, orderType, Constants.OrderStatus.AUTHORIZED,
                Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    
    public List<OrderModel> getOrdersByTypeAndPatientAndInDelivery(OrderType orderType, UserModel user) {
        return orderRepository.findByPatientAndOrderTypeAndStatus(user, orderType, Constants.OrderStatus.IN_DELIVERY,
                Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    
    public List<OrderModel> getOrdersByTypeAndPatientAndCompleted(OrderType orderType, UserModel user) {
        return orderRepository.findByPatientAndOrderTypeAndStatus(user, orderType, Constants.OrderStatus.COMPLETED,
                Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    

    public OrderModel getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

}

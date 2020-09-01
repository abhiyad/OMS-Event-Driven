package com.store.orders;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    private InventoryService inventoryService;

    Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService(final InventoryService inventoryService, final OrderRepository orderRepository){
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
    }



    public synchronized Boolean save(CatalogueOrder order){
        logger.info("ORDER_SERVICE : Attempting to place the order : " + order.toString());
        try {
            Book responseBook = inventoryService.searchInventory(order.getIsbn());
            if (responseBook.getCopies() >= order.getCopies()) {
                logger.info(responseBook.toString() + " ==================== <<<<<<<<<<<<<<");
                orderRepository.save(order);
                inventoryService.blockInventory(order);
                logger.info("ORDER_SERVICE : Successfully placed the order : " + order.toString());
                return true;
            }
        } catch (Exception e) {
            logger.info("ORDER_SERVICE : Couldn't place the order : " + order.toString());
            return false;
        }
        return false;
    }

    public List<CatalogueOrder> findOrderForUser(String username){
        logger.info("ORDER_SERVICE : Finding Order for  User : " + username);
        return orderRepository.findAllOrderForUser(username);
    }

    public synchronized Boolean update(CatalogueOrder order){
        logger.info("ORDER_SERVICE : Attempting to update the order : " + order.toString());
        if(orderRepository.existsById(order.getId())) {
            try {
                CatalogueOrder previousOrder = orderRepository.findById(order.getId()).orElse(null);
                logger.info("ORDER_SERVICE : Found previous order : " + previousOrder.toString());
                Book responseBook = inventoryService.searchInventory(order.getIsbn());
                if(order.getCopies()<=responseBook.getCopies()){
                    inventoryService.blockInventory(order);
                    inventoryService.blockInventory(previousOrder);
                    orderRepository.save(order);
                    logger.info("ORDER_SERVICE : Successfully updated the order : " + order.toString());
                    return true;
                }
            } catch (Exception e) {
                logger.info("ORDER_SERVICE : Failed to updated the order : " + order.toString());
                return false;
            }
        }
        else
            logger.info("ORDER_SERVICE : Couldn't find previous order for this ID : " + order.toString());

        return false;
    }

    public boolean sendOrder(CatalogueOrder order) {
        logger.info("ORDER_SERVICE : Attempting to send the order : " + order.toString());
        if(orderRepository.existsById(order.getId())) {
            CatalogueOrder previousOrder = orderRepository.findById(order.getId()).orElse(null);
            if(previousOrder.toString().equals(order.toString())){
                order.setSent(true);
                orderRepository.save(order);
                logger.info("ORDER_SERVICE : Successfully sent the order : " + order.toString());
                return true;
            }
            else
                logger.info("ORDER_SERVICE : Validation failed with previous order .. PREVIOUS : " + previousOrder.toString() + " THIS : " + order.toString());
        }
        else
            logger.info("ORDER_SERVICE : Couldn't find order for this ID : " + order.toString());
        return false;
    }
}

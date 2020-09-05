package com.store.orders;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryService inventoryService;

    Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService(final InventoryService inventoryService, final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
    }

    public CatalogueOrder findById(Long Id) {
        return orderRepository.findById(Id).orElseThrow(OrderNotFoundException::new);
    }


    public synchronized void save(CatalogueOrder order) {
        try {
            inventoryService.blockInventory(order);
        } catch (Exception e) {
            logger.info("ORDER_SERVICE : Cannot place of the order " + order.toString());
            throw new OrderNotPlacedException();
        }
        logger.info("ORDER_SERVICE : Order Placed " + order.toString());
        orderRepository.save(order);
    }

    public List<CatalogueOrder> findOrderForUser(String username) {
        logger.info("ORDER_SERVICE : Finding Order for  User : " + username);
        return orderRepository.findAllOrderForUser(username);
    }

    public synchronized void update(CatalogueOrder order) {
        try {
            CatalogueOrder previousOrder = findById(order.getId());
            Book responseBook = inventoryService.searchInventory(order.getIsbn());
            inventoryService.blockInventory(order);
            inventoryService.rollBackInventory(previousOrder);
            orderRepository.save(order);
        } catch (OrderNotFoundException e) {
            logger.info("ORDER_SERVICE : Cannot find Order with this ID : " + order.toString());
            throw new OrderNotPlacedException();
        } catch (NotFoundException e){
            logger.info("ORDER_SERVICE : Cannot find the book requested " + order.toString());
            throw new OrderNotPlacedException();
        } catch (Exception e){
            logger.info("ORDER_SERVICE : Insufficient copies in Inventory " + order.toString());
            throw new OrderNotPlacedException();
        }
    }

    public void sendOrder(CatalogueOrder order) {
        try {
            CatalogueOrder previousOrder = findById(order.getId());
            if(!previousOrder.equalOrder(order))
                throw new OrderNotUpdatedException();
            order.setSent(true);
            orderRepository.save(order);
            logger.info("ORDER_SERVICE : Successfully sent the order : " + order.toString());
        } catch (Exception e) {
            logger.info("ORDER_SERVICE : Cannot find the previous Order: " + order.toString());
            throw new OrderNotUpdatedException();
        }
    }

}




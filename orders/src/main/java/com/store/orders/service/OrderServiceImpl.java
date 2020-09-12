package com.store.orders.service;
import com.store.orders.domain.Book;
import com.store.orders.domain.CatalogueOrder;
import com.store.orders.exceptions.OrderNotFoundException;
import com.store.orders.exceptions.OrderNotPlacedException;
import com.store.orders.exceptions.OrderNotUpdatedException;
import com.store.orders.repository.OrderRepository;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@EnableFeignClients
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryClient inventoryClient;

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(final InventoryClient inventoryClient, final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
    }

    @Override
    public CatalogueOrder findById(Long Id) {
        return orderRepository.findById(Id).orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public List<Book> getAllBooks() {
        return inventoryClient.getAll();
    }

    @Override
    public synchronized void save(CatalogueOrder order) {
        try {
            inventoryClient.blockInventory(order);
        } catch (Exception e) {
            logger.info("ORDER_SERVICE : Cannot place of the order " + order.toString());
            e.printStackTrace();
            throw new OrderNotPlacedException();
        }
        logger.info("ORDER_SERVICE : Order Placed " + order.toString());
        orderRepository.save(order);
    }

    @Override
    public List<CatalogueOrder> findOrderForUser(String username) {
        logger.info("ORDER_SERVICE : Finding Order for  User : " + username);
        return orderRepository.findAllOrderForUser(username);
    }

    @Override
    public synchronized void update(CatalogueOrder order) {
        try {
            CatalogueOrder previousOrder = findById(order.getId());
            Book responseBook = inventoryClient.searchInventory(order.getIsbn());
            inventoryClient.blockInventory(order);
            inventoryClient.rollBackInventory(previousOrder);
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

    @Override
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




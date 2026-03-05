package com.TrungTinhBackend.barbershop_backend.Service.Order;

import com.TrungTinhBackend.barbershop_backend.DTO.OrderDTO;
import com.TrungTinhBackend.barbershop_backend.Entity.*;
import com.TrungTinhBackend.barbershop_backend.Enum.OrderStatus;
import com.TrungTinhBackend.barbershop_backend.Exception.NotFoundException;
import com.TrungTinhBackend.barbershop_backend.Repository.*;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Search.Specification.OrderSpecification;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CartsRepository cartsRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ShopsRepository shopsRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Override
    @Transactional
    public APIResponse addOrder(OrderDTO orderDTO) throws BadRequestException {
        APIResponse apiResponse = new APIResponse();

        Users customer = usersRepository.findById(orderDTO.getCustomerId()).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        Shops shop = shopsRepository.findById(orderDTO.getShopId()).orElseThrow(
                () -> new NotFoundException("Shop not found")
        );

        Orders orders = new Orders();
        orders.setCustomer(customer);
        orders.setShop(shop);
        orders.setStatus(OrderStatus.CREATED);
        orders.setCreatedAt(LocalDateTime.now());

        List<OrderItems> orderItemsList = new ArrayList<>();
        double total = 0;

        Carts cart = cartsRepository.findById(orderDTO.getCartId())
                .orElseThrow();
        List<CartItems> avaCartItem = cart.getCartItems().stream().filter(ci -> orderDTO.getCartItemId().contains(ci.getId())).collect(Collectors.toList());

        // check stock trước
        for (CartItems cartItem : avaCartItem) {
            Products product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException(product.getName() + " out of stock");
            }
        }

        for (CartItems cartItem : avaCartItem) {
            OrderItems item = new OrderItems();
            item.setOrder(orders);
            item.setProduct(cartItem.getProduct());
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(cartItem.getProduct().getPrice());

            total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
            orderItemsList.add(item);
        }

        orders.setTotalAmount(total);

        orders.setOrderItems(orderItemsList);
        orderRepository.save(orders);

        cartItemRepository.deleteAll(avaCartItem);

        List<Products> productsToUpdate = new ArrayList<>();

        avaCartItem.forEach(ava -> {
            Products product = ava.getProduct();
            product.setStock(product.getStock() - ava.getQuantity());
            productsToUpdate.add(product);
        });

        productsRepository.saveAll(productsToUpdate);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add order success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getOrderByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Orders> ordersPage = orderRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get order by page "+page+" size = "+size+" success");
        apiResponse.setData(ordersPage);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getOrderByCustomerId(Long customerId, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size,Sort.by("createdAt").descending());
        Page<Orders> ordersList = orderRepository.findByCustomer_Id(customerId,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get order by customerId = "+customerId+" success");
        apiResponse.setData(ordersList);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getOrderByShopId(Long shopId, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size,Sort.by("createdAt").descending());

        Page<Orders> ordersList = orderRepository.findByShop_Id(shopId,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get order by shopId = "+shopId+" success");
        apiResponse.setData(ordersList);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse searchOrder(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size,Sort.by("createdAt").descending());
        Specification<Orders> specification = OrderSpecification.searchByKeyword(keyword);
        Page<Orders> ordersList = orderRepository.findAll(specification,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search order by keyword = "+keyword+" success");
        apiResponse.setData(ordersList);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}

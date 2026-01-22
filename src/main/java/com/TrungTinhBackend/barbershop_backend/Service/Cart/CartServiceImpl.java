package com.TrungTinhBackend.barbershop_backend.Service.Cart;

import com.TrungTinhBackend.barbershop_backend.Entity.CartItems;
import com.TrungTinhBackend.barbershop_backend.Entity.Carts;
import com.TrungTinhBackend.barbershop_backend.Entity.Products;
import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import com.TrungTinhBackend.barbershop_backend.Exception.NotFoundException;
import com.TrungTinhBackend.barbershop_backend.Repository.CartItemRepository;
import com.TrungTinhBackend.barbershop_backend.Repository.CartsRepository;
import com.TrungTinhBackend.barbershop_backend.Repository.ProductsRepository;
import com.TrungTinhBackend.barbershop_backend.Repository.UsersRepository;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartsRepository cartsRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Override
    public APIResponse addCart(Long userId) {
        APIResponse apiResponse = new APIResponse();

        Users user = usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        Carts cart = new Carts();
        cart.setUser(user);

        cartsRepository.save(cart);

        apiResponse.setStatusCode(200L);
        apiResponse.setData(cart);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse addItem(Authentication authentication,Long productId, Integer quantity) {
        APIResponse apiResponse = new APIResponse();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Users user = usersRepository.findByUsername(userDetails.getUsername());

        Carts cart = cartsRepository.findByUser(user);
        if(cart == null) {
            cart = new Carts();
            cart.setUser(user);
            cart = cartsRepository.save(cart);
        }

        Products products = productsRepository.findById(productId).orElseThrow(
                () -> new NotFoundException("Product not found")
        );

        CartItems cartItems = cartItemRepository.findByCartAndProduct(cart,products);

        if(cartItems != null) {
            cartItems.setQuantity(cartItems.getQuantity() + quantity);
        }else {
            cartItems = new CartItems();
            cartItems.setCart(cart);
            cartItems.setProduct(products);
            cartItems.setQuantity(quantity);
        }

        cartItemRepository.save(cartItems);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add product success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getCartItemByCart(Long cartId) {
        APIResponse apiResponse = new APIResponse();

        List<CartItems> cartItemsList = cartItemRepository.findByCartId(cartId);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get cart item by cart id success");
        apiResponse.setData(cartItemsList);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse updateItem(Long id, Integer quantity) {
        APIResponse apiResponse = new APIResponse();

        CartItems cartItems = cartItemRepository.findById(id).orElseThrow(
                () -> new NotFoundException("CartItem not found")
        );

        cartItems.setQuantity(quantity);
        cartItemRepository.save(cartItems);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Update quantity cart item success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse deleteItem(Long id) {
        APIResponse apiResponse = new APIResponse();

        CartItems cartItems = cartItemRepository.findById(id).orElseThrow(
                () -> new NotFoundException("CartItem not found")
        );

        cartItemRepository.delete(cartItems);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Delete cart item success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}

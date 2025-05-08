package com.yunjun.store2;

import com.yunjun.store2.services.ProductService;
import com.yunjun.store2.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Store2Application {

    public static void main(String[] args) {

        var context = SpringApplication.run(Store2Application.class, args);
        var userService = context.getBean(UserService.class);
//        userService.showTransactionalScope();
//        userService.showRelatedEntities();
//        userService.showPersistentState();
//        userService.showDeleteState();
//        userService.showDeleteChildEntityState();


        var productService = context.getBean(ProductService.class);
//        productService.showRelatedChildEntities();
//        productService.showRelatedParentEntities();
//        productService.updateProductPrices();
//        productService.fetchProducts();

//        userService.fetchUser();
//        userService.fetchUsersWithAddresses();

//        productService.fetchProductsWithPrices();

        var profileService = context.getBean(com.yunjun.store2.services.ProfileService.class);
        profileService.showProfilesGreaterThan();

        userService.fetchUsersSummaryWithLoyaltyPointsGreaterThan();
    }

}

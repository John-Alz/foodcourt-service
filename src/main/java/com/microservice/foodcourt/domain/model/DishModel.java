package com.microservice.foodcourt.domain.model;

import java.math.BigDecimal;

public class DishModel {

      private Long id;
      private String name;
      private CategoryModel category;
      private String description;
      private BigDecimal price;
      private RestaurantModel restaurant;
      private String imageUrl;
      private boolean active;

      public DishModel(Long id, String name, CategoryModel category, String description, BigDecimal price, RestaurantModel restaurant, String imageUrl, boolean active) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.description = description;
            this.price = price;
            this.restaurant = restaurant;
            this.imageUrl = imageUrl;
            this.active = active;
      }

      public DishModel() {

      }

      public Long getId() {
            return id;
      }

      public void setId(Long id) {
            this.id = id;
      }

      public String getName() {
            return name;
      }

      public void setName(String name) {
            this.name = name;
      }

      public CategoryModel getCategory() {
            return category;
      }

      public void setCategory(CategoryModel category) {
            this.category = category;
      }

      public String getDescription() {
            return description;
      }

      public void setDescription(String description) {
            this.description = description;
      }

      public BigDecimal getPrice() {
            return price;
      }

      public void setPrice(BigDecimal price) {
            this.price = price;
      }

      public RestaurantModel getRestaurant() {
            return restaurant;
      }

      public void setRestaurant(RestaurantModel restaurant) {
            this.restaurant = restaurant;
      }

      public String getImageUrl() {
            return imageUrl;
      }

      public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
      }

      public boolean isActive() {
            return active;
      }

      public void setActive(boolean active) {
            this.active = active;
      }
}

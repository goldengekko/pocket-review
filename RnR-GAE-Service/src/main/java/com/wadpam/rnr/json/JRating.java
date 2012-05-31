/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.rnr.json;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Rating;
import javax.persistence.Basic;

/**
 *
 * @author os
 */
public class JRating {
    /** Unique id for this Entity in the database */
    private String id;
    
    /** Milliseconds since 1970 when this Entity was created in the database */
    private Long createdDate;
    
    /** Milliseconds since 1970 when this Entity was last updated in the database */
    private Long updatedDate;

    /** The Many-To-One productId (unconstrained) */
    private String             productId;

    /** The Many-To-One username (unconstrained) */
    private String             username;

    /** Where was this product rated */
    private JLocation location;

    /** A user-provided integer rating for a piece of content. Normalized to a 0-100 scale. */
    private Integer rating;

    public JRating() {
    }

    public JRating(String id, Long createdDate, Long updatedDate) {
        this.id = id;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return String.format("{id:%d, productId:%s, username:%s, location:%s, rating:%s}",
                id, productId, username, location, rating);
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public JLocation getLocation() {
        return location;
    }

    public void setLocation(JLocation location) {
        this.location = location;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}

package com.wadpam.rnr.web;

import com.wadpam.open.AbstractRestTempleIntegrationTest;
import com.wadpam.rnr.json.JComment;
import com.wadpam.rnr.json.JLike;
import com.wadpam.rnr.json.JProduct;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.MalformedURLException;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Integration tests for Likes.
 * @author mattiaslevin
 */
public class LikeITest extends AbstractRestTempleIntegrationTest {
    private static final Logger LOG = LoggerFactory.getLogger(LikeITest.class);


    @Override
    protected String getBaseUrl() {
        return "http://localhost:8888/api/itest/";
    }

    @Override
    protected String getUserName() {
        return "iuser";
    }

    @Override
    protected String getPassword() {
        return "password";
    }

    @Test
    // Test create and get like
    public void testBasicLike() throws MalformedURLException {
        LOG.info("Test basic like");

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "A001");
        map.add("latitude", 11.11);
        map.add("longitude", 12.12);

        // Create and follow redirect
        ResponseEntity<JLike> entity = postAndFollowRedirect(BASE_URL + "like", map, JLike.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    // Test create and redirect to product
    public void testBasicLikeRedirectToProduct() throws MalformedURLException {
        LOG.info("Test basic like redirect to product");

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "A002");
        map.add("latitude", 11.11);
        map.add("longitude", 12.12);

        // Create and follow redirect
        ResponseEntity<JProduct> entity = postAndFollowRedirect(BASE_URL + "product/like", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Like count is correct", 1, (long)entity.getBody().getLikeCount());
    }

    @Test
    // Test delete like
    public void testDeleteLike() throws MalformedURLException {
        LOG.info("Test delete like");

        final String productId = "A003";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);

        // Create and follow redirect
        ResponseEntity<JComment> entity = postAndFollowRedirect(BASE_URL + "like", map, JComment.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        String likeId = entity.getBody().getId();
        boolean isDeleted = deleteResourceAndCheck(BASE_URL + "like/{id}", likeId);
        assertTrue("Like was deleted", isDeleted);

        ResponseEntity<JProduct> productInfo =
                restTemplate.getForEntity(BASE_URL + "product/{id}", JProduct.class, productId);
        assertEquals("Response code 200", HttpStatus.OK, productInfo.getStatusCode());
        assertEquals("Like count is 0", 0, (long)productInfo.getBody().getLikeCount());
    }

    @Test
    // Get all likes for a user
    public void testGetLikesForUsername() throws MalformedURLException {
        LOG.info("Test get all likes for user name");

        String username = "mlv";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "B001");
        map.add("username", username);
        // Create and follow redirect
        ResponseEntity<JLike> entity = postAndFollowRedirect(BASE_URL + "like", map, JLike.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "B002");
        map.add("username", username);
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "like", map, JLike.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "B003");
        map.add("username", username);
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "like", map, JLike.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        // Get all ratings
        assertEquals("Got 3 likes", 3, countResources(BASE_URL + "like?username={username}", username));
    }


    @Test
    // Check that a user can only like once per product
    public void testOnlyLikeOncePerUsername() throws MalformedURLException {
        LOG.info("Test that the same user only can like once per product");

        String username = "mlv";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "D001");
        map.add("username", username);
        // Create and follow redirect
        ResponseEntity<JProduct> entity = postAndFollowRedirect(BASE_URL + "product/like", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Like count is 1", 1, (long)entity.getBody().getLikeCount());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "D001");
        map.add("username", username);
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "product/like", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Like count is 1", 1, (long)entity.getBody().getLikeCount());
    }


    @Test
    // Get all likes for a product
    public void testGetLikesForProduct() throws MalformedURLException {
        LOG.info("Test get all likes for product id");

        String productId = "C001";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        // Create and follow redirect
        ResponseEntity<JLike> entity = postAndFollowRedirect(BASE_URL + "like", map, JLike.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "like", map, JLike.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        // Create and follow redirect
        ResponseEntity<JProduct> productSummary = postAndFollowRedirect(BASE_URL + "product/like", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, productSummary.getStatusCode());
        // Check to comments count
        assertEquals("Like count is correct", 3, (long)productSummary.getBody().getLikeCount());

        // Get and count all likes
        assertEquals("Found 3 likes", 3, countResourcesInPage(BASE_URL + "like?productId={id}", JLike.class, productId));
    }


    @Test
    // Test create and get like with info if user liked the product or not
    public void testInnerLike() throws MalformedURLException {
        LOG.info("Test inner like indication");

        String username = "UserG";
        String productId = "G001";

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        map.add("username", username);

        // Create and follow redirect
        ResponseEntity<JProduct> entity = postAndFollowRedirect("product/like", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Like count is correct", 1, (long)entity.getBody().getLikeCount());

        // Like with no user
        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);

        // Create and follow redirect
        entity = postAndFollowRedirect("product/like", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Like count is correct", 2, (long)entity.getBody().getLikeCount());

        // Check that inner like flag is set to true when user name is provided
        ResponseEntity<JProduct> product = restTemplate.getForEntity(buildUrl("product/{id}?username={username}"),
                JProduct.class, productId, username);
        assertTrue("Inner like set to true", product.getBody().getLikedByUser());

        // Check that inner like is set to false when user name is provided but has not liked
        product = restTemplate.getForEntity(buildUrl("product/{id}?username={username}"),
                JProduct.class, productId, "DummyUserName");
        assertFalse("Inner like set to false", product.getBody().getLikedByUser());

        // Check that inner like is set to null when user name is not provided
        product = restTemplate.getForEntity(buildUrl("product/{id}"), JProduct.class, productId);
        assertNull("Inner like set not set", product.getBody().getLikedByUser());
    }

}

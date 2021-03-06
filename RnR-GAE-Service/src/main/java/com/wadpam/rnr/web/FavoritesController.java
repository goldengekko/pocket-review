package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.open.analytics.google.GoogleAnalyticsTracker;
import com.wadpam.open.analytics.google.GoogleAnalyticsTrackerBuilder;
import com.wadpam.open.exceptions.NotFoundException;
import com.wadpam.open.web.AbstractRestController;
import com.wadpam.rnr.domain.DFavorites;
import com.wadpam.rnr.json.JFavorites;
import com.wadpam.rnr.service.RnrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The favorites controller implements all REST methods related to favorites.
 * @author mattiaslevin
 */
@Controller
@RequestMapping(value="{domain}")
@RestReturn(JFavorites.class)
public class FavoritesController extends AbstractRestController {
    private static final Logger LOG = LoggerFactory.getLogger(FavoritesController.class);

    private static final int ERR_BASE_FAVORITES = RnrService.ERR_BASE_FAVORITES;
    private static final int ERR_FAVORITE_NOT_FOUND = ERR_BASE_FAVORITES + 1;

    private static final Converter CONVERTER = new Converter();

    private RnrService rnrService;


    /**
     * Add a product as favorite.
     * @param username unique user name or id
     * @param productId domain-unique id for the product to add as favorites
     * @return redirect to users favorite
     */
    @RestReturn(value=JFavorites.class, entity=JFavorites.class, code={
            @RestCode(code=302, message="OK", description="Redirect to users favorites")
    })
    @RequestMapping(value="favorites/{username}", method=RequestMethod.POST)
    public RedirectView addFavorite(HttpServletRequest request,
                                    HttpServletResponse response,
                                    UriComponentsBuilder uriBuilder,
                                    @ModelAttribute("trackingCode") String trackingCode,
                                    @PathVariable String domain,
                                    @RequestParam String productId,
                                    @PathVariable String username) {

        // Create a tracker if tracking code is set
        GoogleAnalyticsTracker tracker = null;
        if (null != trackingCode) {
            LOG.debug("Create tracker with tracking code:{}", trackingCode);
            tracker = new GoogleAnalyticsTrackerBuilder()
                    .trackerConfiguration(domain, trackingCode)
                    .deviceFromRequest(request)
                    .visitor(username != null ? Integer.toString(username.hashCode()) :
                            Integer.toString("anonymous".hashCode()))
                    .build();
        }

        final DFavorites body = rnrService.addFavorite(productId, username, tracker);

        // Redirect to the favorites
        String redirectUri = uriBuilder.path("/{domain}/favorites/{username}").
                buildAndExpand(domain, username).toUriString();

        return new RedirectView(redirectUri);
    }

    /**
     * Remove a product from favorites.
     * @param username unique user name or id
     * @param productId domain-unique id for the product to add as favorites
     * @return redirect to users favorite
     */
    @RestReturn(value=JFavorites.class, entity=JFavorites.class, code={
            @RestCode(code=302, message="OK", description="Redirect to updated users favorites"),
            @RestCode(code=404, message="NOK", description="Product not a favorite for the user")
    })
    @RequestMapping(value="favorites/{username}", method= RequestMethod.DELETE)
    public RedirectView deleteFavorite(HttpServletRequest request,
                                       HttpServletResponse response,
                                       UriComponentsBuilder uriBuilder,
                                       @PathVariable String domain,
                                       @RequestParam String productId,
                                       @PathVariable String username) {

        final DFavorites body = rnrService.deleteFavorite(productId, username);
        if (null == body) {
            throw new NotFoundException(ERR_FAVORITE_NOT_FOUND,
                    String.format("Favorite for user:%s and product:%s not found", username, productId));
        }

        // Redirect to the favorites
        String redirectUri = uriBuilder.path("/{domain}/favorites/{username}").
                buildAndExpand(domain, username).toUriString();

        return new RedirectView(redirectUri);
    }

    /**
     * Get favorites for user.
     * @param username unique user name or id
     * @return users favorite products
     */
    @RestReturn(value=JFavorites.class, entity=JFavorites.class, code={
            @RestCode(code=200, message="OK", description="Favorites found for user")
    })
    @RequestMapping(value="favorites/{username}", method= RequestMethod.GET)
    public ResponseEntity<JFavorites> getFavorites(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   @PathVariable String username) {

        final DFavorites body = rnrService.getFavorites(username);
        if (null == body)
            throw new NotFoundException(ERR_FAVORITE_NOT_FOUND,
                    String.format("No favorites found for user:%s", username));

        return new ResponseEntity<JFavorites>(CONVERTER.convert(body), HttpStatus.OK);
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }

}

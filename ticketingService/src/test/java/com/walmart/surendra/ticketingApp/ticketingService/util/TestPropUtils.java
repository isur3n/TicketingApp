package com.walmart.surendra.ticketingApp.ticketingService.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Surendra Raut on 11/30/2017.
 */
public class TestPropUtils {

    @Test
    public void testValidProperty() {
        Assert.assertNotEquals(PropUtils.getAppPropertyValue(PropUtils.EMAIL_ID_REGEX, true), null);
        Assert.assertNotEquals(PropUtils.getAppPropertyValue(PropUtils.EXPIRY_MILLI_SECONDS, false), null);
    }

    @Test
    public void testInvalidProperty() {
        Assert.assertEquals(PropUtils.getAppPropertyValue("INVALID_PROPERTY", false), null);
    }
}

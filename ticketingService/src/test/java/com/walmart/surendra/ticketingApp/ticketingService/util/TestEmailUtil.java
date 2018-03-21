package com.walmart.surendra.ticketingApp.ticketingService.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Surendra Raut on 11/30/2017.
 */
public class TestEmailUtil {

    @Test
    public void TestValidEmailId() {
        Assert.assertEquals(true, EmailUtil.isEmailIdValid("firstname.lastname@company.com"));
        Assert.assertEquals(true, EmailUtil.isEmailIdValid("firstname-001@company.com"));
        Assert.assertEquals(true, EmailUtil.isEmailIdValid("firstname_lname@company.com"));
        Assert.assertEquals(true, EmailUtil.isEmailIdValid("firstnamelname@company.org"));
        Assert.assertEquals(true, EmailUtil.isEmailIdValid("ab123@company.co.us"));
    }

    @Test
    public void TestInValidEmailId() {
        Assert.assertEquals(false, EmailUtil.isEmailIdValid("firstname.lastname@company"));
        Assert.assertEquals(false, EmailUtil.isEmailIdValid("firstname.lastname@company#com"));
        Assert.assertEquals(false, EmailUtil.isEmailIdValid("firstname$lastname@company"));
    }

    @Test
    public void TestEmptyEmailId() {
        Assert.assertEquals(false, EmailUtil.isEmailIdValid(""));
        Assert.assertEquals(false, EmailUtil.isEmailIdValid(null));
    }
}

package com.syncano.android.lib;

import com.syncano.android.lib.data.Account;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    Syncano syncano;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        syncano = new Syncano("");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAccount() {
        Account a = syncano.account().getAccount();
        Log.d("test", a.getFirstName());
        Log.d("test", a.getLastName());
        Log.d("test", a.getId());
        Log.d("test", a.getEmail());

        /*Response<Account> response = new GetAccount().send(syncano);
        Account a = response.getData();

        Invitation[] invitations = syncano.account().getInvitations();

        CreateAccount createAccount = new CreateAccount("mail@o2.pl", "pass");
        createAccount.setFirstName("Klaudiusz");
        createAccount.send(syncano);*/

        /*Account newAccount = syncano.account().createAccount("test1ssasdddummy@asd.as", "testssspasdassword", "Marsacisnek", "Lipadcskai");
        Log.d("test", newAccount.getFirstName());
        Log.d("test", newAccount.getLastName());
        Log.d("test", newAccount.getId());
        Log.d("test", newAccount.getEmail());*/
    }
}
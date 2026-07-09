package com.walletsystem.swiftpay.common.service;

import com.walletsystem.swiftpay.auth.entity.User;
import com.walletsystem.swiftpay.wallet.entity.Wallet;

public interface CurrentUserService {

    User getCurrentUser();

    Wallet getCurrentUserWallet();
}

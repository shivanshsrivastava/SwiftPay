package com.walletsystem.swiftpay.wallet.service;

import com.walletsystem.swiftpay.wallet.dto.request.AddMoneyRequest;
import com.walletsystem.swiftpay.wallet.dto.response.BalanceResponse;
import com.walletsystem.swiftpay.wallet.dto.response.WalletResponse;

public interface WalletService {
    WalletResponse createWallet();
    WalletResponse getMyWallet();
    BalanceResponse getBalance();
    WalletResponse addMoney(AddMoneyRequest request);
}

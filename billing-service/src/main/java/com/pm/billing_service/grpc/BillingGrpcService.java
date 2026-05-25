package com.pm.billing_service.grpc;

import billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;                    // ✅ SLF4J Logger



@GrpcService
@Slf4j
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {

    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest, StreamObserver<billing.BillingResponse> resonseObserver){
        log.info("create billing account request received {}",billingRequest.toString());

        billing.BillingResponse billingResponse = billing.BillingResponse.newBuilder()
                .setAccountId("1234")
                .setMessage("Billing account created successfully")
                .build();
        resonseObserver.onNext(billingResponse);
        resonseObserver.onCompleted();

    }
}

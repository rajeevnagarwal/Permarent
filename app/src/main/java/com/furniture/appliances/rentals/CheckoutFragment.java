package com.furniture.appliances.rentals;

import android.widget.Toast;

import com.furniture.appliances.rentals.asyncTask.HttpCall;
import com.furniture.appliances.rentals.model.ModelOrder;
import com.razorpay.Checkout;

public class CheckoutFragment extends Checkout {

  public void setModelOrder(ModelOrder modelOrder) {
    this.modelOrder = modelOrder;
  }

  ModelOrder modelOrder = new ModelOrder();

  // override onSuccess method to capture razorpay_payment_id
  public void onSuccess(String razorpay_payment_id){
    //Toast.makeText(getActivity(), "Payment Successful: " + razorpay_payment_id, Toast.LENGTH_SHORT).show();
    modelOrder.paymentid=razorpay_payment_id;
    new HttpCall().insertOrderDetails(getActivity(), modelOrder);
  }

  //
  // onError will be invoked in following cases:
  // 1. back or close button pressed i.e. user cancels payment form (code = Activity.RESULT_CANCELED)
  // 2. network error while loading checkout form (code = 2)
  //
  // onError isn't invoked in case of payment authentication failure, rather error is displayed on checkout form and customer can reattempt payment.

  public void onError(int code, String response){
    Toast.makeText(getActivity(), "Error " + Integer.toString(code) + ": " + response, Toast.LENGTH_SHORT).show();
  }
};

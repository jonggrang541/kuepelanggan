package com.marketplace.kelompok2.kue.ui.pilihbahan;

import android.util.Log;

import com.marketplace.kelompok2.kue.base.BasePresenterNetwork;
import com.marketplace.kelompok2.kue.common.UserState;
import com.marketplace.kelompok2.kue.model.Barang;
import com.marketplace.kelompok2.kue.model.BarangTokoList;
import com.marketplace.kelompok2.kue.model.Keranjang;
import com.marketplace.kelompok2.kue.model.list.BarangList;
import com.marketplace.kelompok2.kue.model.response.DataResponse;
import com.marketplace.kelompok2.kue.model.response.ModelResponse;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PilihBahanPresenter extends BasePresenterNetwork {
    private Call<DataResponse<BarangTokoList>> result;
    private PilihBahanView view;
    private Integer kuantitas = 1;
    public PilihBahanPresenter(PilihBahanView view){
        super();
        this.view = view;
    }

    public void addToCart(ArrayList<Barang> listBarang, Float total){
        view.showLoading();
        Observable.fromIterable(listBarang)
                .flatMap(barang -> service.addToChart(UserState.getInstance().getPembeli().getIdKeranjang(), barang.getId(), kuantitas))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(modelResponses -> {
                            view.hideLoading();
                            view.actionAddToCartSuccess();
                        },
                        throwable -> {
                            Log.e("setListBarang", throwable.getMessage());
                        });
    }

//    private void setCart(Float total){
//        Call<ModelResponse<Keranjang>> result = service.setCart(UserState.getInstance().getPembeli().getId(), total.intValue());
//        result.enqueue(new Callback<ModelResponse<Keranjang>>() {
//            @Override
//            public void onResponse(Call<ModelResponse<Keranjang>> call, Response<ModelResponse<Keranjang>> response) {
//                if(response.isSuccessful()){
//                    view.actionAddToCartSuccess();
//                }
//                else {
//                    Log.e("setcartr", response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ModelResponse<Keranjang>> call, Throwable t) {
//                Log.e("setcartf", t.getMessage());
//            }
//        });
//    }


    private void showError(String error){
        Log.e("adToCart", error);
        view.actionAddToCartFailed();
    }
}
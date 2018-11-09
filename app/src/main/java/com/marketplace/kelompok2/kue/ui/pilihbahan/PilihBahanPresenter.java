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

    public PilihBahanPresenter(PilihBahanView view){
        super();
        this.view = view;
    }

    public void getListBahan(String keyword){
        result = super.service.getListBarangPenjual(keyword);

        result.enqueue(new Callback<DataResponse<BarangTokoList>>() {
            @Override
            public void onResponse(Call<DataResponse<BarangTokoList>> call, Response<DataResponse<BarangTokoList>> response) {
                ArrayList<BarangTokoList> listBarangPenjual = response.body().getListData();
                view.showListBahan(listBarangPenjual);
            }

            @Override
            public void onFailure(Call<DataResponse<BarangTokoList>> call, Throwable t) {
                Log.i("getListToko", "Failed");

            }
        });
    }

    public void addToCart(ArrayList<Barang> listBarang, Float total){
        view.showLoading();
        Observable.concat(
                Observable.fromIterable(listBarang).flatMap(barang ->
                        service.addToChart(UserState.getInstance().getPembeli().getIdKeranjang(), barang.getId())),
                service.setCart(UserState.getInstance().getIdUser(), total)
        )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(modelResponse -> {
            view.hideLoading();
            view.actionAddToCartSuccess();
        }
        ,throwable -> {
            view.hideLoading();
            Log.e("addToChart", throwable.getMessage());
            view.actionAddToCartFailed();
                });
    }

}